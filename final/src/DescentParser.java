//package descentParser;

import java.io.*;

class DescentParser {
    private String parseString = "";
    private boolean validParse = false;
    private boolean debugMode = false;
    private StringReader parseReader = null;
    private int currentChar = -1;

    public DescentParser(String parseString, boolean debugMode) {
        this.parseString = parseString;
        parseReader = new StringReader( this.parseString);
        this.debugMode = debugMode;
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
    
        return validParse();
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
        if( match('(') && rexp() && match(')') && rexp2_tail()) {
            if( debugMode) { System.out.println("REXP2 - true"); }
            return true;
        } else {
            int skipAhead = RE_CHAR.contains(this);
        
            if( skipAhead > 0) {
                for( int i = 0; i < skipAhead; i++) { match(currentChar()); }
                
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
        if( match('*') || match('+')) {
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
        if( match('.') || (match('[') && char_class1()) /* || DEFINED */) {
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
        if( (char_set() && char_set_list()) || match(']')) {
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
            for( int i = 0; i < skipAhead; i++) {
                match(currentChar());
            }
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
                for( int i = 0; i < skipAhead; i++) {
                    match(currentChar());
                }
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
        if( match('[') && char_set() && match(']') /* || DEFINED */) {
            if( debugMode) { System.out.println("EXCLUDE_SET_TAIL - true"); }
            return true;
        } else {
            if( debugMode) { System.out.println("EXCLUDE_SET_TAIL - false"); }
            return false;
        }
    }
}