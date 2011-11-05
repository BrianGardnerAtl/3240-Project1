import java.util.*;

/** Takes in an NFAstruct and makes a DFA from it
 * ASSUMES THAT THE NFA WAS BUILT SO THAT NO NDOE HAS MULTIPLE TRANSITIONS ON THE SAME
 * INPUT
 *
 */
public class DFAStruct {
	private DFANode init;
	private DFANode current;
	public ArrayList<DFANode> table;
	//these are for minimizing
	public ArrayList<DFANode> nodesCombined;
	public DFANode combinedNode;
	
	public DFAStruct(NfaStruct nfa){
		table = new ArrayList<DFANode>();
		nodesCombined = new ArrayList<DFANode>(2);
		combinedNode = new DFANode();
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
			//check if current is final state
			for(String id: cur.getIDs()){
				if(nfa.getNode(id).isFinal()){cur.setFinal(true);}
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
	 * minimizes this DFA
	 */
	public void minimize(){
		//splits table into accepting and non-accepting
		ArrayList<DFANode> accept = new ArrayList<DFANode>();
		ArrayList<DFANode> naccept = new ArrayList<DFANode>();
		DFANode t;
		for(DFANode n : table){
			t = new DFANode();
			for(String id : n.getIDs()){
				t.addID(id);
			}
			for(DFATransition tran : n.getTransitions()){
				t.addTransition(tran.deepCopy());
			}
			t.setFinal(n.isFinal());
			if(!accept.contains(t) && t.isFinal()){accept.add(t);}
			else if(!naccept.contains(t) && !t.isFinal()){naccept.add(t);}
		}
		boolean done = false;
		while(!done){
			if(accept.size()>1){
				done = combineCheck(accept);
				if(!done){update(accept); update(naccept);}
			}
			else{done = true;}
		}
		done = false;
		while(!done){
			if(naccept.size()>1){
				done = combineCheck(naccept);
				if(!done){update(accept); update(naccept);}
			}
			else{done = true;}
		}
		
		ArrayList<DFANode> min = new ArrayList<DFANode>();
		min.addAll(accept);
		min.addAll(naccept);
		table = min;
	}
	
	/**
	 * checks if any nodes can be combined and combines them, for minimization
	 * returns true if nothing was combined
	 */
	private boolean combineCheck(ArrayList<DFANode> tbl){
		for(DFANode cur : tbl){
			for(DFANode comp : tbl){
				if(!cur.equals(comp)){
					//go through transitions to compare
					boolean tran = true;
					for(DFATransition trn: cur.getTransitions()){
						//node made from destination of this transition, for comparison (IDs)
						DFANode t = new DFANode(trn.getDest());
						if(comp.getTransitions().contains(trn) && tran){
							//they share a transition on trn's input
							//should always be true i dunno why i even check for it??
							if(!trn.equalsM(comp.getTransitions().get(comp.getTransitions().indexOf(trn)))){
								//destination isnt the same
								DFANode t2 = new DFANode(comp.getTransitions().get(comp.getTransitions().indexOf(trn)).getDest());
								if(!t.equals(cur) || !t.equals(comp)){
									//destination isnt either of these nodes, cannot be combined
									tran = false;
								}
								if(!t2.equals(cur) || !t2.equals(comp)){
									//destination isnt either of these nodes, cannot be combined
									tran = false;
								}
							}//if
						}//if
					}//for
					if(tran){
						//transitions are all the same, combine and return false(to loop through again)
						//helper stuff for updating the table
						DFANode temp = new DFANode();
						for(String id : cur.getIDs()){
							temp.addID(id);
						}
						DFANode temp2 = new DFANode();
						for(String id2 : comp.getIDs()){
							temp2.addID(id2);
						}
						nodesCombined.clear();
						nodesCombined.add(temp);
						nodesCombined.add(temp2);
						boolean updateInit = false;
						if(init.equals(cur) || init.equals(comp)){updateInit = true;}
						//combine
						cur.addIDs(comp.getIDs());
						combinedNode = cur;
						//update init if necessary, remove 2nd node and return
						if(updateInit){init=cur;}
						tbl.remove(comp);
						return false;
					}
				}//if(!cur.equals(comp))
			}//for
		}//for
		
		//nothing to be combined, this group is done
		return true;
	}
	
	/**
	 * updates transitions if nodes have been combined
	 */
	private void update(ArrayList<DFANode> group){
		//if any nodes in this list went to the newly combined node on a transition
		//their destination is updated
		ArrayList<String> d = new ArrayList<String>();
		d.add("6");d.add("2");d.add("7");d.add("3");d.add("8");d.add("10");d.add("4");
		DFANode dn = new DFANode(d);
		for(DFANode n: group){
			for(DFATransition trn : n.getTransitions()){
				DFANode t = new DFANode(trn.getDest());
				if(t.equals(nodesCombined.get(0)) || t.equals(nodesCombined.get(1))){
					trn.addDest(combinedNode.getIDs());
				}
			}
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
	
	/**
	 * 
	 * @param input
	 * @return the new current node after transition, null if there is not one
	 */
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
	
	public int size(){
		return table.size();
	}
}
