import java.util.*;

/**
 * Represents transition from two DFA nodes
 * 
 */
public class DFATransition {
	private String input;
	private ArrayList<String> destinationIDs;  //mega destination state
	
	public DFATransition(String in, ArrayList<String> to){
		input = in;
		destinationIDs = to;
	}
	
	public DFATransition(String in){
		input = in;
		destinationIDs = new ArrayList<String>();
	}
	
	public void setInput(String newInput){
		input = newInput;
	}
	
	public void setDest(ArrayList<String> newGoal){
		destinationIDs = newGoal;
	}
	
	public void addDest(String newGoal){
		if(!destinationIDs.contains(newGoal)){destinationIDs.add(newGoal);}
	}
	
	public void addDest(ArrayList<String> newGoals){
		for(String s: newGoals){
			addDest(s);
		}
	}
	
	public String getInput(){
		return input;
	}
	
	public ArrayList<String> getDest(){
		return destinationIDs;
	}
	
	/*not an actual equals method, this is just for searching for a transition on an input
	 * withtin a node's transition list*/
	@Override public boolean equals(Object o){
		return (input.equals(((DFATransition) o).getInput()));
	}
	
	/*
	 * this one actually checks for equality, used for minimization
	 */
	public boolean equalsM(DFATransition o){
		DFANode odest = new DFANode(o.getDest());
		DFANode thisdest = new DFANode(destinationIDs);
		
		return (input.equals(o.getInput()) && odest.equals(thisdest));
	}
	
	public String toString(){
		return (input + " = " + destinationIDs);
	}
	
	public DFATransition deepCopy(){
		DFATransition t = new DFATransition(input);
		for(String dest : destinationIDs){
			t.addDest(dest);
		}
		
		return t;
	}
}
