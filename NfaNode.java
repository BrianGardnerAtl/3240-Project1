import java.util.*;

public class NfaNode{
  private boolean finalState;
  /* Start with 10, increase by 10 when limit reached */
  private Hashtable transitions = new Hashtable<String, NfaNode>(10,10);
  private NfaNode[] epsilonTransitions = new NfaNode[20];
  private String identifier; // TODO figure out what to use for unique identifiers
  private static String EPSILON = "epsilon";

  public NfaNode(String ident, boolean finState){
    identifier = ident;
    finalState = finState;
    epsilonTransitions[0] = this;
  }

  public NfaNode(String ident){
    this(ident, false);
  }

  /* Add transitions to the hashtable */
  public boolean addTransition(String key, NfaNode next){
    if(key == null){
      return false;
    }
    else if(transitions.get(key) == null){
      return (transitions.put(key, next) == null);
    }
    else{
      /* append the next node to the nextNodes array */
      NfaNode[] nextNodes = (NfaNode[])transitions.get(key);
      NfaNode[] check = nextNodes;
      int length = nextNodes.length;
      nextNodes[length] = next;
      return (transitions.put(key, nextNodes) == check);
    }
  }

  public void addEpsilonTransition(NfaNode next){
    int length = epsilonTransitions.length;
    epsilonTransitions[length] = next;
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

  public NfaNode[] getEpsilonTransition(){
    return epsilonTransitions;
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
}
