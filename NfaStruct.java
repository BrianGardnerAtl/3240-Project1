import java.util.*;

public class NfaStruct{
  private NfaNode start;
  private NfaNode current;
  private NfaNode[] finalStates = new NfaNode[20];
  private int finalCount;
  private Hashtable allNodes = new Hashtable<String, NfaNode>(10,10); //Node identifier is the key, the node object is the value

  public NfaStruct(){
    start = null;
    current = null;
    finalCount = 0;
  }

  public NfaNode getStart(){
    return start;
  }

  public NfaNode[] getFinal(){
    return finalStates;
  }

  public NfaNode makeTransition(String key){
    if(current!=null){
      if(current.isTransition(key)){
        return current.getTransitions(key);
      }
    }
    return null;
  }

  public boolean addNode(NfaNode node, String transition){
    if(node.isFinal()){
      finalStates[finalCount] = node;
      finalCount++;
    }

    if(start==null){
      start = node;
      current = node;
      return true;
    }
    else{
      return current.addTransition(transition, node); //Just add a node that is connected to the current node
    }
  }

  /* Method that changes the current node, returns false if no node has the identifier */
  public boolean setCurrent(String identifier){
    if(allNodes.containsKey(identifier)){
      current = (NfaNode)allNodes.get(identifier);
      return true;
    }
    return false;
  }

  public NfaNode[] getEpsilonClosure(NfaNode node){
    return node.getEpsilonTransition();
  }
}
