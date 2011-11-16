import java.util.*;

public class NfaNode{
  private boolean finalState;
  /* Start with 10, increase by 10 when limit reached */
  private Hashtable transitions = new Hashtable<String, NfaNode>(10,10);
  private ArrayList<NfaNode> epsilonTransitions = new ArrayList<NfaNode>();
  private String identifier; // TODO figure out what to use for unique identifiers
  private static String EPSILON = "epsilon";

  public NfaNode(String ident, boolean finState){
    identifier = ident;
    finalState = finState;
    epsilonTransitions.add(this);
  }

  public NfaNode(String ident){
    this(ident, false);
  }

  /* Add transitions to the hashtable */
  public boolean addTransition(String key, NfaNode next){
    if(key == null){
      return false;
    }
    else if(transitions.get(key) == null && !key.equals(EPSILON)){
      return (transitions.put(key, next) == null);
    }
    else if(key.equals(EPSILON)){
    	//epsilon transition
    	addEpsilonTransition(next);
    	return true;
    }
    else{
      /* append the next node to the nextNodes array*/
    	/*i dont think this cast works, but i also dont think its gonna come up, 
    	depending on how we make the NFAs from regex*/
      NfaNode[] nextNodes = (NfaNode[])transitions.get(key);
      NfaNode[] check = nextNodes;
      int length = nextNodes.length;
      nextNodes[length] = next;
      return (transitions.put(key, nextNodes) == check);
    }
  }

  public void addEpsilonTransition(NfaNode next){
    if (!epsilonTransitions.contains(next)){
    	epsilonTransitions.add(next);
    }
  }

  /* returns a boolean value indicating if the specified key will cause a transition from the node */
  public boolean isTransition(String key){
    return transitions.containsKey(key);
  }

  /* Returns the next node from taking the input key */
  public NfaNode getTransition(String key){
    NfaNode nextNode = (NfaNode)transitions.get(key);
    return nextNode;
  }

  public Hashtable getAllTransitions(){
    return transitions;
  }

  public ArrayList<NfaNode> getEpsilonTransition(){
    return epsilonTransitions;
  }

  /* Traverses epsilon transitions for closure-building*/
  public void traverseEpsilon(ArrayList<NfaNode> v){
	  //add this node to visited list, traverse if not visited yet
	  if(!v.contains(this)){
		  //traverse through epsilon transitions
		  for(NfaNode n: epsilonTransitions){
			  if(!this.equals(n)){n.traverseEpsilon(v);}
			  else{v.add(n);}
		  }
	  }
	  
  }
  
  /* Getter and Setter methods for needed variables */
  public void setFinal(boolean value){
    finalState = value;
  }

  public boolean isFinal(){
    return finalState;
  }

  public void setIdentifier(String ident){
    identifier = ident;
  }

  public String getIdentifier(){
    return identifier;
  }
  
  
  /* Other useful functions*/
   @Override public boolean equals(Object o){
	  return (identifier.equals(((NfaNode) o).getIdentifier()));
  }
  
  public String toString(){
	    return identifier;
	  }
}
