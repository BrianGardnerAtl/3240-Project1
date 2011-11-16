//package descentParser;

class UnitTests {
    public static void main(String args[]) {
        boolean debug = false;
        
        for( String arg : args) {
            if( arg.toLowerCase().equals("debug")) { debug = true; }
        }
    
        System.out.println("Beginning unit tests for descent parser...");
        
        System.out.println("Creating new parser with string \'.a\\.b\\??\\[[\'...");
        DescentParser testParser = new DescentParser(".a\\.b\\??\\[[", true);
        DFAStruct testDFA;
        System.out.println();
        
        do {
            System.out.print("Current character: " + testParser.currentChar());
            if( testParser.currentChar() == '\\') {
                System.out.println(" >>lookahead>> " + testParser.peekNextChar());
            } else {
                System.out.println();
            }
            
            System.out.println("   Member of RE_CHAR: " + RE_CHAR.contains(testParser));
            System.out.println("   Member of CLS_CHAR: " + CLS_CHAR.contains(testParser));

            System.out.println("   Peeking next character: " + testParser.peekNextChar());
        
            System.out.println("   Advancing to next character...");
            testParser.nextChar();
            System.out.println();
        } while( !testParser.lastChar());
        
        System.out.println("Creating new parser with string \'([A-Z])[abc]+\'...");
        testParser = new DescentParser("([A-Z])+[abc]", debug);
        System.out.println();
        
        System.out.println("Invoking parse...");
        testParser.parse();
        System.out.println("Parse successful: " + testParser.validParse());
        System.out.println("NFA: " + testParser.getNfa());
        System.out.println();
        
        System.out.println("Creating new parser with string \')[A-Z])[abc]+\'...");
        testParser = new DescentParser(")[A-Z])+[abc]", debug);
        System.out.println();
        
        System.out.println("Invoking parse...");
        testParser.parse();
        System.out.println("Parse successful: " + testParser.validParse());
        System.out.println("NFA: " + testParser.getNfa());
        System.out.println();
        
        System.out.println("Creating new parser with string \'[aabc]|[^1-9]\'...");
        testParser = new DescentParser("[aabc]|[^1-9]", debug);
        System.out.println();
        
        System.out.println("Invoking parse...");
        testParser.parse();
        System.out.println("Parse successful: " + testParser.validParse());
        System.out.println("NFA: " + testParser.getNfa());
        testDFA = new DFAStruct(testParser.getNfa());
        testDFA.minimize();
        System.out.println("DFA: " + testDFA);
        System.out.println();
        
        System.out.println("Creating new parser with string \'[^a-z] IN $LETTER\'...");
        testParser = new DescentParser("[^a-z] IN $LETTER", debug);
        System.out.println();
        
        System.out.println("Invoking parse...");
        testParser.parse();
        System.out.println("Parse successful: " + testParser.validParse());
        System.out.println("NFA: " + testParser.getNfa());
        System.out.println();
        
        System.out.println("Ending unit tests for descent parser...");
        
        System.exit(0);
    }
}
