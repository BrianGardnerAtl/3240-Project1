  // package descentParser;
   
   class RE_CHAR {
        private static char[] members = {
            '!', '#', '$', '%', '&', ',', '-', '/', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '@', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '^',
            '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '{', '}', '~'
        };
        private static char[] extendedMembers = {
            ' ', '\\', '*', '+', '?', '|', '[', ']', '(', ')', '.', '\'', '\"' 
        };
        
        protected static int contains(DescentParser parser) {
            /* Last character? */
            if( parser.lastChar()) { return 0; }
        
            char member = parser.currentChar();
            
            if( member == '\\') {
                /* Escaped character?  */
                int peekNextChar = parser.peekNextChar();
                
                if( peekNextChar != -1) {
                    for( char m : extendedMembers) {
                        if( m == peekNextChar) { return 2; }
                    }
                }
                
                return 0;
            } else {
                /* Regular character */
                for( char m : members) {
                    if( m == member) { return 1; }
                }
                
                return 0;
            }
        }
    }