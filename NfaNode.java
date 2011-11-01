import java.util.*;

public class NfaNode{
  private boolean finalState;
  /* Start with 10, increase by 10 when limit reached */
  private Hashtable transitions = new Hashtable<String, NfaNode>(10,10);
  private NfaNode[] epsilonTransitions = new NfaNode[20];
  static String EPSILON = "epsilon";

  public NfaNode(boolean finState){
    finalState = finState;
    epsilonTransitions[0] = this;
  }

  public NfaNode(){
    this(false);
  }

  /* Add transitions to the hashtable */
  public void addTransition(String key, NfaNode next){
    if(key == null){
      return false;
    }
    else if(transitions.get(key) == null){
      return (transitions.put(key, next) == null);
    }
    else{
      /* append the next node to the nextNodes array */
      NfaNode[] nextNodes = (NfaNode[])transitions.get(key);
      int length = nextNodes.length;
      nextNodes[length] = next;
      transitions.put(key, nextNodes);
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

  /* Returns an array of transitions that result from the specified key */
  public NfaNode getTransitions(String key){
    NfaNode nextNode = (NfaNode)transitions.get(key);
    return nextNode;
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
}
