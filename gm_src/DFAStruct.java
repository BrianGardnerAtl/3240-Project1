import java.util.*;

/** Takes in an NFAstruct and makes a DFA from it
 * THIS ASSUMES THAT THE NFA WAS BUILT SO THAT NO NDOE HAS MULTIPLE TRANSITIONS ON THE SAME
 * INPUT
 *
 */

public class DFAStruct {
	private DFANode init;
	private DFANode current;
	private ArrayList<DFANode> table;
	
	public DFAStruct(NfaStruct nfa){
		table = new ArrayList<DFANode>();
		startDFA(nfa);
	}
	
	private void startDFA(NfaStruct nfa){
		//get nfa's initial state's epsilon closure
		ArrayList<NfaNode> start = nfa.getStart().getEpsilonTransition();
		init = new DFANode();
		//fill init's IDs from this
		for (NfaNode nf : start){
			init.addID(nf.getIdentifier());
			if(nf.isFinal()){init.setFinal(true);}
		}
		//transitions
		//get all possible inputs
		ArrayList<String> abc = nfa.getAlphabet();
		//for all possible inputs find every transition init state would make
		//includes episolon closures, oh my!
		NfaNode temp, temp2;
		DFATransition tran;
		for(String in: abc){
			tran = new DFATransition(in);
			for(String nfanode: init.getIDs()){
				temp = nfa.getNode(nfanode);
				if(temp.isTransition(in)){
					temp2 = nfa.getNode(temp.getTransition(in).getIdentifier());
					tran.addDest(temp2.getIdentifier());
					//epsilon closures as well
					for(NfaNode other: temp2.getEpsilonTransition()){
						tran.addDest(other.getIdentifier());
					}
				}
			}
			init.addTransition(tran);
		}
		//init MUST have a transition for all inputs in alphabet
		//set next entry on table
		DFANode nextNode = new DFANode(init.getTransitionOn(abc.get(0)));
		init.next = nextNode;
		
		//System.out.println(nextNode);
		//System.out.println(nfa.getNode("s2").getAllTransitions());
		//System.out.println(init);
		//build rest of table
		table.add(init);
		buildDFA(nfa, init, abc);
	}
	
	private void buildDFA(NfaStruct nfa, DFANode curr, ArrayList<String> alpha){
		DFANode cur = curr;
		while (cur.next!=null){
			cur = cur.next;
			table.add(cur);
			NfaNode temp, temp2;
			DFATransition tran;
			for(String in: alpha){
				tran = new DFATransition(in);
				for(String nfanode: cur.getIDs()){
					temp = nfa.getNode(nfanode);
					if(temp.isTransition(in)){
						temp2 = nfa.getNode(temp.getTransition(in).getIdentifier());
						tran.addDest(temp2.getIdentifier());
						//epsilon closures as well
						for(NfaNode other: temp2.getEpsilonTransition()){
							tran.addDest(other.getIdentifier());
						}
					}
				}
				cur.addTransition(tran);
			}
			//create next node for table
			//first build a list of destinations
			ArrayList<ArrayList<String>> dest = new ArrayList<ArrayList<String>>();
			for(DFANode nd : table){
				for(DFATransition p : nd.getTransitions()){
					ArrayList<String> pd = p.getDest();
					if(!dest.contains(pd)){dest.add(pd);}
				}
			}
			//if any of these destinations has not been visited, it is the next state
			DFANode nextNode = new DFANode();
			boolean found = false;
			while(!found){
				for(ArrayList<String> d : dest){
					DFANode dnode = new DFANode(d);
					if(!table.contains(dnode) && !found){
						nextNode = dnode;
						//check is dnode is final state
						for(String id: dnode.getIDs()){
							if(nfa.getNode(id).isFinal()){nextNode.setFinal(true);}
						}
						found = true;
					}
				}
				if(!found){
					found = true;
					nextNode = null;
				}
			}
			cur.next = nextNode;
		}
	}
	
	/**
	 * 
	 * @param token
	 * @return whether the token is accepted
	 */
	public boolean accepts(String input){
		current = init;		
		//break string into a bunch of smaller strings (per character)
		for(int i = 0; i<input.length(); i++){
			current = makeTransitionOn(Character.toString(input.charAt(i)));
			if (current == null){return false;}
		}
		
		return current.isFinal();
	}
	
	private DFANode makeTransitionOn(String in){
		DFANode t = null;
		if(current.isTransition(in)){
			ArrayList<String> ids = current.getTransitionOn(in);
			t = new DFANode(ids);
			t = table.get(table.indexOf(t));
		}
		
		return t;
	}
	
	public String toString(){
		String t = "";
		for (DFANode n: table){
			t+= n + "\n";
		}		
		return t;
	}
	
}
