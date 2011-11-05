package descentParser;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
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
        String currentToken = "";
        int currentIdentChar = 0;
        int lineNumber = 1;
        Hashtable<String, NfaStruct> identNFA = new Hashtable();
        Hashtable<String, Stack<String>> identTokens = new Hashtable();
        Stack<String> currentTokens;

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
                    /* We have a valid identifier...proceed to parse associated regex. */
                    identNFA.put(currentIdent, new DescentParser(currentLine.substring(currentIdentChar + 1, currentLine.length()), false).parse());
                }
                
                lineNumber += 1;
            }
        } catch(IOError e) {
            throw new Exception("Read error at line " + lineNumber);
        }
       
        reader.close();
        
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
                }
                
                if( currentToken.length() > 0) {
                    /* We have a valid identifier...proceed to parse associated regex. */
                    if( identTokens.get(currentIdent) == null) {
                        identTokens.put(currentIdent, new Stack<String>());
                    }
                    
                    identTokens.get(currentIdent).push(currentToken);
                }
                
                lineNumber += 1;
            }
        } catch(IOError e) {
            throw new Exception("Read error at line " + lineNumber);
        }
       
        reader.close();        
        
        /* Done reading the file? Print out all matched tokens and the associated identifiers... */
        for( Enumeration<String> ident = identTokens.keys(); ident.hasMoreElements();) {
            ident.nextElement();
            System.out.println("Identifier: " + ident);
            
            if( (currentTokens = identTokens.get(ident)) != null) {
                while( !currentTokens.empty()) {
                    System.out.println("   Token: " + currentTokens.pop());
                }
            } else {
                System.out.println("   No associated tokens for this identifier.");
            }
            
            System.out.println();
        }
        
        System.out.println("Finished parse and tokenization!");
    }
}