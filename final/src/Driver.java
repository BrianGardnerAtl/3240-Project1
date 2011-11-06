//package descentParser;

import java.util.Enumeration;
import java.util.Hashtable;
import java.io.*;

class Driver {
    public void main(String[] args) {
        /*
         * Arg[0] should be the path to the lexical specification
         * Arg[1] should be the path to the target file to tokenize
         */
        BufferedReader reader;
        String currentLine = null;
        String currentIdent = "";
        int currentIdentChar = 0;
        int lineNumber = 1;
        DescentParser currentParser = null;
        DFAStruct finalDFA = null;
        NfaStruct currentNFA = null;
        Hashtable<String, NfaStruct> identNFA = new Hashtable();
        Hashtable<String, DFAStruct> identDFA = new Hashtable();

        /* Open the lexical specification file. */
        try {
            reader = new BufferedReader(new FileReader(args[0]));
        } catch(IOException e) {
            throw new Exception("Unable to open lexical specification at " + args[0]);
        }
        
        try {
            /* Read in lines while not at the end of the file */
            while( (currentLine = reader.readLine()) != null) {
                currentIdent = "";
                currentNFA = null;
                currentParser = null;
                currentIdentChar = 0;
                /* Remove extraneous whitespace. Convert to character array. */
                currentLine = currentLine.trim();
                /* Check - do we have an identifier ($IDENTIFIER)? */
                if( currentLine.length() > 0 && currentLine.charAt(0) == '$') {
                    for(char[] currentLineChar = currentLine.toCharArray(); currentIdentChar < currentLineChar.length;) {
                        if( Character.isSpaceChar(currentLineChar[currentIdentChar])) {
                            /* We've grabbed our identifier.  Set the current identifier and proceed. */
                            currentIdent = currentLine.substring(0, currentIdentChar + 1);
                            break;
                        }
                        currentIdentChar += 1;
                    }
                }
                
                if( currentIdent.length() > 0 && (currentIdentChar + 1) < currentLine.length()) {
                    /* We have a valid identifier...proceed to parse associated regex. Create NFA, DFA. */
                	currentParser = new DescentParser(currentLine.substring(currentIdentChar + 1, currentLine.length()), false);
                	currentParser.parse();
                	currentNFA = currentParser.getNFA();
                    identNFA.put(currentIdent, currentNFA);
                    identDFA.put(currentIdent, new DFAStruct(currentNFA));
                }
                
                lineNumber += 1;
            }
        } catch(IOError e) {
            throw new Exception("Read error at line " + lineNumber);
        }
       
        reader.close();
        
        /* Build the final NFA from the smaller NFAs. */
        currentNFA = new NfaStruct(new NfaNode("final"));
        
        for( Enumeration<String> ident = identNFA.keys(); ident.hasMoreElements();) {
            currentNFA.addNode(identNFA.get(ident.nextElement()).getStart(), "epsilon");
        }
        
        finalDFA = new DFAStruct(currentNFA);
        
        /* Open up the target file to tokenize. */
        try {
            reader = new BufferedReader(new FileReader(args[1]));
        } catch(IOException e) {
            throw new Exception("Unable to open target file to tokenize at " + args[0]);
        }     

       try {
            /* Read in lines while not at the end of the file */
            while( (currentLine = reader.readLine()) != null) {
                /* Remove extraneous whitespace. */
                currentLine = currentLine.trim();
                /* Check - do we have an identifier ($IDENTIFIER)? */
                if( currentLine.length() > 0) {
                    /* Match tokens. */
                	for(String token : currentLine.split(" ")) {                    	
                    	if( finalDFA.accepts(token)) { System.out.println("Accepted token: " + token); }
                	}
                }
                
                lineNumber += 1;
            }
        } catch(IOError e) {
            throw new Exception("Read error at line " + lineNumber);
        }
       
        reader.close();        
        
        System.out.println("Finished parse and tokenization!");
    }
}