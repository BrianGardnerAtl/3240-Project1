package descentParser;

import java.io.*;

class DescentParser {
    private String parseString = "";
    private boolean validParse = false;
    private boolean debugMode = false;
    private StringReader parseReader = null;
    private int currentChar = -1;
    private NfaStruct nfa;
    /*
     * Individual NFA nodes for each of the methods
     */
    private NfaNode rexp = new NfaNode("rexp");
    private NfaNode rexp_prime = new NfaNode("rexp_prime");
    private NfaNode rexp1 = new NfaNode("rexp1");
    private NfaNode rexp2 = new NfaNode("rexp2");
    private NfaNode rexp2_tail = new NfaNode("rexp2_tail");
    private NfaNode rexp3 = new NfaNode("rexp3");
    private NfaNode char_class = new NfaNode("char_class");
    private NfaNode char_class1 = new NfaNode("char_class1");
    private NfaNode char_set_list = new NfaNode("char_set_list");
    private NfaNode char_set = new NfaNode("char_set");
    private NfaNode char_set_tail = new NfaNode("char_set_tail");
    private NfaNode exclude_set = new NfaNode("exclude_set");
    private NfaNode exclude_set_tail = new NfaNode("exclude_set_tail");

    /* counts for all of the nfa nodes so each node has a unique identifier */
    private int rexpCnt = 0;
    private int rexpPrimeCnt = 0;
    private int rexp1Cnt = 0;
    private int rexp2Cnt = 0;
    private int rexp2TailCnt = 0;
    private int rexp3Cnt = 0;
    private int charClassCnt = 0;
    private int charClass1Cnt = 0;
    private int charSetListCnt = 0;
    private int charSetCnt = 0;
    private int charSetTailCnt = 0;
    private int excludeSetCnt = 0;
    private int excludeSetTailCnt = 0;

    public DescentParser(String parseString, boolean debugMode) {
        this.parseString = parseString;
        parseReader = new StringReader( this.parseString);
        this.debugMode = debugMode;
        NfaNode startNode = new NfaNode("start");
        nfa = new NfaStruct(startNode); //NFA is created every time Descent parser is called
    }    
    
    public char currentChar() { return (char)currentChar; }
    
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
        //Add the completed NFA to the nfa variable
        NfaNode start = nfa.getStart();
        start.addEpsilonTransition(rexp);
    
        return validParse();
    }

    public NfaStruct getNfa(){
      return nfa;
    }
    
    private boolean rexp() {
        String name = "rexp-" + rexpCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("REXP"); }
        if( rexp1()) {
            temp.addEpsilonTransition(rexp1);
            if(rexp_prime()){
              temp.addEpsilonTransition(rexp_prime);
            }
            rexp.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("REXP - true"); }
            rexpCnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("REXP - false"); }
            return false;
        }
    }
    
    private boolean rexp_prime(){
        String name = "rexp_prime-" + rexpPrimeCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("REXP_PRIME"); }
        if( match('|') && rexp1() && rexp_prime()) {
            temp.addEpsilonTransition(rexp1);
            rexp_prime.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("REXP_PRIME - true"); }
            rexpPrimeCnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("REXP_PRIME - false"); }
            return false;
        }
    }
    
    private boolean rexp1() {
        String name = "rexp1-" + rexp1Cnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("REXP1"); }
        if( rexp2()) {
            while( rexp2()) { ; }
            temp.addEpsilonTransition(rexp2);
            rexp1.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("REXP1 - true"); }
            rexp1Cnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("REXP1 - false"); }
            return false;
        }
    }
        
    private boolean rexp2() {
        String name = "rexp2-" + rexp2Cnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("REXP2"); }
        if( match('(') && rexp() && match(')') && rexp2_tail()) {
            temp.addEpsilonTransition(rexp);
            rexp2.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("REXP2 - true"); }
            rexp2Cnt += 1;
            return true;
        } else {
            int skipAhead = RE_CHAR.contains(this);
        
            if( skipAhead > 0) {
                for( int i = 0; i < skipAhead; i++) { match(currentChar()); }
                
                if( rexp2_tail()) {
                    if( debugMode) { System.out.println("REXP2 - true"); }
                    temp.addEpsilonTransition(rexp2_tail);
                    rexp2.addEpsilonTransition(temp);
                    rexp2Cnt += 1;
                    return true;
                } else {
                    if( debugMode) { System.out.println("REXP2 - false"); }
                    return false;
                }
            } else {
                if( rexp3()) {
                    if( debugMode) { System.out.println("REXP2 - true"); }
                    temp.addEpsilonTransition(rexp2_tail);
                    rexp2.addEpsilonTransition(temp);
                    rexp2Cnt += 1;
                    return true;
                } else {
                    if( debugMode) { System.out.println("REXP2 - false"); }
                    return false;
                }
            }
        }
    }
    
    private boolean rexp2_tail() {
        String name = "rexp2_tail-" + rexp2TailCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("REXP2_TAIL"); }
        if( match('*') || match('+')) {
            if( debugMode) { System.out.println("REXP2_TAIL - true"); }
            temp.addEpsilonTransition(rexp2);
            rexp2_tail.addEpsilonTransition(temp);
            rexp2TailCnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("REXP2_TAIL - epsilon"); }
            return true;
        }
    }
    
    private boolean rexp3() {
        String name = "rexp3-" + rexp3Cnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("REXP3"); }
        if( char_class()) {
            temp.addEpsilonTransition(char_class);
            rexp3.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("REXP3 - true"); }
            rexp3Cnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("REXP3 - false"); }
            return false;
        }
    }
    
    private boolean char_class() {
        String name = "char_class-" + charClassCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("CHAR_CLASS"); }
        if( match('.')) /* || DEFINED */ {
            //add a transition to temp for all characters from RE_CHAR
            NfaNode endNode = new NfaNode("Stuff");
            char[] allTrans = RE_CHAR.getMembers();
            int transLen = allTrans.length;
            for(int i=0; i<transLen; i++){
              temp.addTransition(Character.toString(allTrans[i]), endNode);
            }
            char_class.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("CHAR_CLASS - true"); }
            charClassCnt += 1;
            return true;
        } else if(match('[') && char_class1()){
            temp.addEpsilonTransition(char_class1);
            char_class.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("CHAR_CLASS - true"); }
            charClassCnt += 1;
            return true;
        }else {
            if( debugMode) { System.out.println("CHAR_CLASS - false"); }
            return false;
        }
    }
    
    private boolean char_class1() {
        String name = "char_class1-" + charClass1Cnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("CHAR_CLASS1"); }
        if( char_set_list()) {
            temp.addEpsilonTransition(char_set_list);
            char_class1.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("CHAR_CLASS1 - true"); }
            charClass1Cnt += 1;
            return true;
        }
        if(exclude_set()){
            temp.addEpsilonTransition(exclude_set);
            char_class1.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("CHAR_CLASS1 - true"); }
            charClass1Cnt += 1;
        } else {
            if( debugMode) { System.out.println("CHAR_CLASS1 - false"); }
            return false;
        }
        return false;
    }
    
    private boolean char_set_list() {
        String name = "char_set_list-" + charSetListCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("CHAR_SET_LIST"); }
        if( (char_set() && char_set_list()) || match(']')) {
            temp.addEpsilonTransition(char_set);
            temp.addEpsilonTransition(char_set_list);
            char_set_list.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("CHAR_SET_LIST - true"); }
            charSetListCnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("CHAR_SET_LIST - false"); }
            return false;
        }
    }
    
    private boolean char_set() {
        String name = "char_set-" + charSetCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("CHAR_SET"); }
        int skipAhead = CLS_CHAR.contains(this);
        
        char temp1 = '\0';
        if( skipAhead > 0) {
            if( skipAhead == 1){
              temp1 = currentChar();
            }
            for( int i = 0; i < skipAhead; i++) {
                match(currentChar());
            }
            if( char_set_tail()) {
                char temp2 = currentChar();
                //Loop through temp1-temp2 and add transitions for each
                int index = (int)temp1;
                int end = (int)temp2;
                while(index<end){
                  temp.addTransition(Character.toString((char)index), char_set_tail);
                  index += 1;
                }
                temp.addEpsilonTransition(char_set_tail);
                char_set.addEpsilonTransition(temp);
                if( debugMode) { System.out.println("CHAR_SET - true"); }
                charSetCnt += 1;
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
        String name = "char_set_tail-" + charSetTailCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("CHAR_SET_TAIL"); }
        if( match('-')) {
            int skipAhead = CLS_CHAR.contains(this);
        
            if( skipAhead > 0) {
                for( int i = 0; i < skipAhead; i++) {
                    match(currentChar());
                }
                // TODO add transition for char_set_tail
                if( debugMode) { System.out.println("CHAR_SET_TAIL - true"); }
                charSetTailCnt += 1;
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
        String name = "exclude_set-" + excludeSetCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("EXCLUDE_SET"); }
        if( match('^') && char_set() && match('I') && match('N') && exclude_set_tail()) {
            //TODO add transition to exclude_set
            if( debugMode) { System.out.println("EXCLUDE_SET - true"); }
            excludeSetCnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("EXCLUDE_SET - false"); }
            return false;
        }
    }
    
    private boolean exclude_set_tail() {
        String name = "exclude_set_tail-" + excludeSetTailCnt;
        NfaNode temp = new NfaNode(name);
        if( debugMode) { System.out.println("EXCLUDE_SET_TAIL"); }
        if( match('[') && char_set() && match(']') /* || DEFINED */) {
            temp.addEpsilonTransition(char_set);
            exclude_set_tail.addEpsilonTransition(temp);
            if( debugMode) { System.out.println("EXCLUDE_SET_TAIL - true"); }
            excludeSetTailCnt += 1;
            return true;
        } else {
            if( debugMode) { System.out.println("EXCLUDE_SET_TAIL - false"); }
            return false;
        }
    }
}
