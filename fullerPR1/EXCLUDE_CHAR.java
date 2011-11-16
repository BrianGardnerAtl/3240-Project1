import java.util.ArrayList;

   //package descentParser;
   
   class EXCLUDE_CHAR {
        private static char[] members = {
            ' ', '!', '#', '$', '%', '&', ',', '-', '/', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '@', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '^',
            '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '{', '}', '~', '*', '+', '?', '|', '(', ')', '.', '\'',
            '\"', '\\', '^', '[', ']' 
        };
        
        protected static NfaStruct exclude(char first, char last, String id) {
        	NfaNode start = new NfaNode(id, false); 
        	NfaNode finish = new NfaNode(id + "_end", true);
        	NfaStruct excludeNFA = new NfaStruct(start);
        	int counter = 0;
        	
        	for(char member : members) {
        		if(((int)member<(int)first) || ((int)member>(int)last)){
	        		NfaNode startTransition, endTransition;
	        		startTransition = new NfaNode(id + "_excludeStart_" + counter);
	        		endTransition = new NfaNode(id + "_excludeEnd_" + counter);
	        		excludeNFA.addNode(startTransition, "epsilon");
	        		excludeNFA.setCurrent(id + "_excludeStart_" + counter);
	        		excludeNFA.addNode(endTransition, Character.toString(member));
	        		excludeNFA.setCurrent(id + "_excludeEnd_" + counter);
	        		excludeNFA.addNode(finish, "epsilon");
	        		excludeNFA.setCurrent(id);
	        		counter++;
        		}
        	}
        	
        	return excludeNFA;
        }
        
        protected static NfaStruct exclude(ArrayList<String> exceptions, String id) {
        	NfaNode start = new NfaNode(id, false); 
        	NfaNode finish = new NfaNode(id + "_end", true);
        	NfaStruct excludeNFA = new NfaStruct(start);
        	int counter = 0;
        	
        	for(char member : members) {
        		if(!exceptions.contains(Character.toString(member))){
	        		NfaNode startTransition, endTransition;
	        		startTransition = new NfaNode(id + "_excludeStart_" + counter);
	        		endTransition = new NfaNode(id + "_excludeEnd_" + counter);
	        		excludeNFA.addNode(startTransition, "epsilon");
	        		excludeNFA.setCurrent(id + "_excludeStart_" + counter);
	        		excludeNFA.addNode(endTransition, Character.toString(member));
	        		excludeNFA.setCurrent(id + "_excludeEnd_" + counter);
	        		excludeNFA.addNode(finish, "epsilon");
	        		excludeNFA.setCurrent(id);
	        		counter++;
        		}
        	}
        	
        	return excludeNFA;
        }
    }