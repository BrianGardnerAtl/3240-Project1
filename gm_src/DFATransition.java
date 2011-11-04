import java.util.*;


public class DFATransition {
	private String input;
	private ArrayList<String> destinationIDs;
	
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
	
	public String getInput(){
		return input;
	}
	
	public ArrayList<String> getDest(){
		return destinationIDs;
	}
	
	/*not an actual equals method, this is just for searching for a transition on an input*/
	@Override public boolean equals(Object o){
		return (input.equals(((DFATransition) o).getInput()));
	}
	
	public String toString(){
		return (input + " = " + destinationIDs);
	}
}
