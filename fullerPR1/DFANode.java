import java.util.*;

/**
 * Represents entry in DFA table
 *
 */
public class DFANode {
	private ArrayList<String> IDs; //all the IDs of this mega node
	private boolean accepting;
	private ArrayList<DFATransition> transitions;
	//THIS IS NOT NECESSARILY THE NEXT NODE ON THE DFA, IT IS THE NEXT ENTRY IN THE TABLE
	//also, probably broken after minimizing
	public DFANode next; 
	
	public DFANode(ArrayList<String> ids, boolean isFinal){
		IDs = ids;
		accepting = isFinal;
		transitions = new ArrayList<DFATransition>();
		next = null;
	}
	
	public DFANode(ArrayList<String> ids){
		this(ids, false);
	}
	
	public DFANode(){
		IDs = new ArrayList<String>();
		transitions = new ArrayList<DFATransition>();
		accepting = false;
		next = null;
	}
	
	public ArrayList<String> getIDs(){
		return IDs;
	}
	
	public boolean isFinal(){
		return accepting;
	}
	
	public void setFinal(boolean f){
		accepting = f;
	}

	public ArrayList<DFATransition> getTransitions(){
		return transitions;
	}
	
	public ArrayList<String> getTransitionOn(String in){
		DFATransition t = new DFATransition(in);
		return transitions.get(transitions.indexOf(t)).getDest();
	}
	
	/**
	 * 
	 * @param in  input to transition
	 * @return true if the input has corresponding transition
	 */
	public boolean isTransition(String in){
		DFATransition t = new DFATransition(in);		
		return (transitions.contains(t));
	}
	
	public void addTransition(DFATransition dfat){
		transitions.add(dfat);
	}
	
	/**
	 * returns true if o is a DFANode with the same ID
	 */
	@Override public boolean equals(Object o){
		DFANode o2 = (DFANode) o;
		if(IDs.size()!=o2.getIDs().size()){
			return false;
		}else{
			int length = IDs.size();
			int i = 0;
			ArrayList<String> oIDs = o2.getIDs();
			for(String s : IDs){
				if(oIDs.contains(s)){i++;}
			}
			return (i==length);
		}
	}
	
	public void addID(String id){
		if(!IDs.contains(id)){IDs.add(id);}
	}
	
	public void addIDs(ArrayList<String> ids){
		for(String id : ids){
			addID(id);
		}
	}
	
	public String toString(){
		return (IDs + " " + accepting + " Transitions: " + transitions);
	}
}
