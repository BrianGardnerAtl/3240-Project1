//package descentParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Hashtable;

class DescentParser {
    private String parseString;
    private boolean validParse;
    private boolean debugMode;
    private boolean charSetPush;
    private StringReader parseReader;
    private int currentChar;
    private Stack<NfaStruct> states;
    private Hashtable<String, NfaStruct> predefined;
    private static int nodeCount = 1;
    private static int pushCount = 0;
    
    public DescentParser(String parseString, boolean debugMode) {
        this.parseString = parseString;
        validParse = false;
        parseReader = new StringReader( this.parseString);
        this.debugMode = debugMode;
        currentChar = -1;
        states = new Stack<NfaStruct>();
        predefined = null;
        charSetPush = false;
    }
    
    public DescentParser(String parseString, boolean debugMode, Hashtable predefined) {
        this(parseString, debugMode);
        this.predefined = predefined;
      }
    
    
    public char currentChar() { return (char)currentChar; }
    
    public NfaStruct getNfa() {
        if( states.empty()) {
            return null;
        } else {
            return states.peek();
        }
    }
    
    public boolean validParse() { return validParse; }
    
    public boolean lastChar() {
        if( currentChar == -1) { return true; }
        else { return false; }
    }
    
    protected boolean nextChar() {
        int peekNextChar = -1;
    
        try {
            peekNextChar = parseReader.read();
        
            /* Skip all whitespace */
            while (peekNextChar != -1 && Character.isSpaceChar( peekNextChar)) {
                peekNextChar = parseReader.read();
            }
        } catch (IOException e) {
            System.exit(1);
        }
        
        currentChar = peekNextChar;
        
        if( peekNextChar == -1) {
            /* End of text. */
            return false;
        } else {
            /* Valid non-whitespace character */
            return true;
        }
    }
    
    public char peekNextChar() {
        int peekNextChar = -1;
    
        try {
            /* Save current position in string */
            parseReader.mark(1);
        
            peekNextChar = parseReader.read();
        
            /* Skip all whitespace */
            while (peekNextChar != -1 && Character.isSpaceChar( peekNextChar)) {
                peekNextChar = parseReader.read();
            }
            
            /* Reset stream */
            parseReader.reset();
        } catch (IOException e) {
            System.exit(1);
        }
        
        return (char)peekNextChar;
    }
    
    private boolean match(char token) {
        if( debugMode) {
            System.out.println("Matching " + token + " on " + currentChar() + "...");
        }
    
        if( currentChar == token) {
            /* Expected token matches current token
             *
             * Grab next token, acknowledge success */
            if( debugMode) { System.out.print(currentChar() + " >> "); }
            nextChar();
            if( debugMode) { System.out.println(currentChar()); }
            return true;
        } else {
            /* No match
             *
             * Don't advance to the next token */
            if( debugMode) { System.out.println("Failed to match..."); }
            return false;
        }
    }    
    
    public boolean parse() {
        nextChar();
        
        if( !lastChar()) { validParse = rexp(); }
    
        if( validParse()) {
            /* Concatenate everything on the stack */
            while(true) {
                if( !merge()) { break; }
            }
        }
    
        return validParse();
    }
    
    private boolean merge() {
        NfaStruct left = null;
        NfaStruct right = null;
        if( !states.empty()) { right = states.pop(); pushCount -= 1; }
        if( !states.empty()) { left = states.pop(); pushCount -= 1; }
        else { states.push(right); pushCount += 1; return false; }
                
        if( right != null && left != null) {
            for( NfaNode finals : left.getFinal()) {
                finals.setFinal(false);
                left.setCurrent(finals.getIdentifier());
                left.addNode(right.getStart(), "epsilon");
            }
                
            left.setFinal(right.getFinal());
                
            states.push(left);
            pushCount += 1;
        }   

        return true;
    }
    
    private boolean rexp() {
        if( debugMode) { System.out.println("REXP"); }
        if( rexp1()) {
            rexp_prime();
            if( debugMode) { System.out.println("REXP - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("REXP - false"); }
            return false;
        }
    }
    
    private boolean rexp_prime(){
        if( debugMode) { System.out.println("REXP_PRIME"); }
        if( match('|') && rexp1() && rexp_prime()) {
            NfaStruct right = states.pop();
            pushCount -= 1;
            NfaStruct left = states.pop();
            pushCount -= 1;
            ArrayList<NfaNode> combinedFinal = left.getFinal();
            combinedFinal.addAll(right.getFinal());
            NfaStruct alternate = new NfaStruct(new NfaNode("rexp_prime_" + nodeCount++, false));
            alternate.addNode(left.getStart(), "epsilon");
            alternate.addNode(right.getStart(), "epsilon");
            alternate.setFinal(combinedFinal);
            states.push(alternate);
            pushCount += 1;
        
            if( debugMode) { System.out.println("REXP_PRIME - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("REXP_PRIME - false"); }
            return false;
        }
    }
    
    private boolean rexp1() {
        if( debugMode) { System.out.println("REXP1"); }
        if( rexp2()) {
            while( rexp2()) { ; }
            if( debugMode) { System.out.println("REXP1 - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("REXP1 - false"); }
            return false;
        }
    }
        
    private boolean rexp2() {
        if( debugMode) { System.out.println("REXP2"); }
        if( match('(')) {
            int precount = pushCount;
        
            if( rexp()) {
                if( match(')')) {              
                    if( pushCount - precount > 1) {
                        for( ; pushCount > precount; pushCount--) {
                            merge();
                        }
                    }
                
                    if( rexp2_tail()) {
                        if( debugMode) { System.out.println("REXP2 - true"); }
                        return true;
                    }
                }
             }
             
             if( debugMode) { System.out.println("REXP2 - false"); }
             return false;
        } else {
            int skipAhead = RE_CHAR.contains(this);
        
            if( skipAhead > 0) {
                String builder = "";
                NfaStruct literal = new NfaStruct(new NfaNode("rexp2_start_" + nodeCount, false));
                
                for( int i = 0; i < skipAhead; i++) {
                    if( i == skipAhead - 1) { builder = new Character(currentChar()).toString(); }
                    match(currentChar());
                }
                
                literal.addNode(new NfaNode("rexp2_finish_" + nodeCount++, true), builder);
                states.push(literal);
                pushCount += 1;
                
                if( rexp2_tail()) {
                    if( debugMode) { System.out.println("REXP2 - true"); }
                    return true;
                } else {
                    if( debugMode) { System.out.println("REXP2 - false"); }
                    return false;
                }
            } else {
                if( rexp3()) {
                    if( debugMode) { System.out.println("REXP2 - true"); }
                    return true;
                } else {
                    if( debugMode) { System.out.println("REXP2 - false"); }
                    return false;
                }
            }
        }
    }
    
    private boolean rexp2_tail() {
        if( debugMode) { System.out.println("REXP2_TAIL"); }
        if( match('*')) {
            NfaStruct star = new NfaStruct(new NfaNode("rexp2_tail_start_" + nodeCount, false));
            NfaNode starEnd = new NfaNode("rexp2_tail_finish_" + nodeCount++, true);
            NfaStruct left = states.pop();
            pushCount -= 1;
            star.addNode(left.getStart(), "epsilon");
            star.addNode(starEnd, "epsilon");
            
            for(NfaNode finals : left.getFinal()) {
                finals.setFinal(false);
                star.setCurrent(finals.getIdentifier());
                star.addNode(star.getStart(), "epsilon");
            }
            
            states.push(star);
            pushCount += 1;
            
            if( debugMode) { System.out.println("REXP2_TAIL - true"); }
            return true;
        } else if( match('+')) {
            NfaStruct plus = new NfaStruct(new NfaNode("rexp2_tail_start_" + nodeCount, false));
            NfaNode plusEnd = new NfaNode("rexp2_tail_finish_" + nodeCount++, true);
            NfaStruct left = states.pop();
            pushCount -= 1;
            
            plus.addNode(left.getStart(), "epsilon");
            
            for(NfaNode finals : left.getFinal()) {
                finals.setFinal(false);
                plus.setCurrent(finals.getIdentifier());
                plus.addNode(plusEnd, "epsilon");
            }
            
            plus.setCurrent(plusEnd.getIdentifier());
            plus.addNode(plus.getStart(), "epsilon");
            states.push(plus);
            pushCount += 1;
        
            if( debugMode) { System.out.println("REXP2_TAIL - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("REXP2_TAIL - epsilon"); }
            return true;
        }
    }
    
    private boolean rexp3() {
        if( debugMode) { System.out.println("REXP3"); }
        if( char_class()) {
            if( debugMode) { System.out.println("REXP3 - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("REXP3 - false"); }
            return false;
        }
    }
    
    private boolean char_class() {
        if( debugMode) { System.out.println("CHAR_CLASS"); }
        if( match('.')) {
            NfaStruct any = EXCLUDE_CHAR.exclude(null, "any_" + nodeCount++);
            states.push(any);
            pushCount += 1;
            
            if( debugMode) { System.out.println("CHAR_CLASS - true"); }
            return true;
        } else if( match('[')) {
            char_class1();
            
            if( debugMode) { System.out.println("CHAR_CLASS - true"); }
            return true;
        } else if( defined()) {
            if( debugMode) { System.out.println("CHAR_CLASS - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("CHAR_CLASS - false"); }
            return false;
        }
    }
    
    private boolean char_class1() {
        if( debugMode) { System.out.println("CHAR_CLASS1"); }
        if( char_set_list() || exclude_set()) {
            if( debugMode) { System.out.println("CHAR_CLASS1 - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("CHAR_CLASS1 - false"); }
            return false;
        }
    }
    
    private boolean char_set_list() {
        if( debugMode) { System.out.println("CHAR_SET_LIST"); }
        
        if( charSetPush && char_set()) {
            NfaStruct right = states.pop();
            pushCount -= 1;
            NfaStruct left = states.pop();
            pushCount -= 1;
            
            for(NfaNode finals : left.getFinal()) {
                finals.setFinal(false);
                left.setCurrent(finals.getIdentifier());
                left.addNode(right.getStart(), "epsilon");
            }            
            
            left.setFinal(right.getFinal());
            states.push(left);
            pushCount += 1;
            charSetPush = false;
            
            char_set_list();
            
            if( debugMode) { System.out.println("CHAR_SET_LIST - true"); }
            return true;
        } else if ( char_set()) {
            char_set_list();
            
            if( debugMode) { System.out.println("CHAR_SET_LIST - true"); }
            return true;
        } else if( match(']')) {
            if( debugMode) { System.out.println("CHAR_SET_LIST - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("CHAR_SET_LIST - false"); }
            return false;
        }
    }
    
    private boolean char_set() {
        if( debugMode) { System.out.println("CHAR_SET"); }
        int skipAhead = CLS_CHAR.contains(this);
        
        if( skipAhead > 0) {
            String builder = "";
            NfaStruct literal = new NfaStruct(new NfaNode("char_set_start_" + nodeCount, false));
        
            for( int i = 0; i < skipAhead; i++) {
                if( i == skipAhead - 1) { builder = new Character(currentChar()).toString(); }
                match(currentChar());
            }
            
            literal.addNode(new NfaNode("char_set_finish_" + nodeCount++, true), builder);
            states.push(literal);
            pushCount += 1;
            charSetPush = true;
            
            if( char_set_tail()) {
                if( debugMode) { System.out.println("CHAR_SET - true"); }
                return true;
            } else {
                if( debugMode) { System.out.println("CHAR_SET - false"); }
                return false;
            }
        } else {
            if( debugMode) { System.out.println("CHAR_SET - false"); }
            return false;
        }
    }
    
    private boolean char_set_tail() {
        if( debugMode) { System.out.println("CHAR_SET_TAIL"); }
        if( match('-')) {
            int skipAhead = CLS_CHAR.contains(this);
        
            if( skipAhead > 0) {
                char builder = (char)-1;
                NfaStruct range;
                String first = (String)(states.pop().getStart().getAllTransitions().keys().nextElement());
                pushCount -= 1;
                
                for( int i = 0; i < skipAhead; i++) {
                    if( i == skipAhead - 1) { builder = currentChar(); }
                    match(currentChar());
                }
                
                range = EXCLUDE_CHAR.exclude(first.charAt(0), builder, "range_" + nodeCount++);
                states.push(range);
                pushCount += 1;
                
                
                if( debugMode) { System.out.println("CHAR_SET_TAIL - true"); }
                return true;
            } else {
                if( debugMode) { System.out.println("CHAR_SET_TAIL - false"); }
                return false;
            }
        } else {
            if( debugMode) { System.out.println("CHAR_SET_TAIL - epsilon"); }
            return true;
        }
    }
    
    private boolean exclude_set() {
        if( debugMode) { System.out.println("EXCLUDE_SET"); }
        if( match('^') && char_set() && match('I') && match('N') && exclude_set_tail()) {
            if( debugMode) { System.out.println("EXCLUDE_SET - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("EXCLUDE_SET - false"); }
            return false;
        }
    }
    
    private boolean exclude_set_tail() {
        if( debugMode) { System.out.println("EXCLUDE_SET_TAIL"); }
        if( (match('[') && char_set() && match(']')) || defined()) {
            if( debugMode) { System.out.println("EXCLUDE_SET_TAIL - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("EXCLUDE_SET_TAIL - false"); }
            return false;
        }
    }
    
    private boolean defined() {
    	StringBuilder buffer = new StringBuilder();
    	NfaStruct predef = null;
    	
    	if( debugMode) { System.out.println("DEFINED_CLASS"); }
    	if( match('$')) {
    		buffer.append('$');
    		while( currentChar() >= 65 && currentChar() <= 92) {
    			if( debugMode) { System.out.println("DEFINED_CLASS - identifier >> " + currentChar()); }
    			buffer.append( currentChar());
    			match( currentChar());
    		}
    		if( buffer.length() <= 1) {
    			if( debugMode) { System.out.println("DEFINED_CLASS - false"); }
    			return false;
    		} else {
    			/* do stuff with NFA */
    			predef = this.predefined.get(buffer);
    			if( predef == null) {
    				if( debugMode) { System.out.println("DEFINED_CLASS - true"); }
    				return false;
    			} else {
    				/* Hook in NFA here */
    				
        			if( debugMode) { System.out.println("DEFINED_CLASS - true"); }
        			return true;
    			}
    			
    		}
    	} else {
    		if( debugMode) { System.out.println("DEFINED_CLASS - false"); }
    		return false;
    	}
    }
}