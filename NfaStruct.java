import java.util.*;

public class NfaStruct{
  private NfaNode start;
  private NfaNode[] finalStates = new NfaNode[20];
  private int finalCount;
  private NfaNode current;

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
        return current.getTransition(key);
      }
    }
    return null;
  }

  public void addNode(NfaNode node, String transition){
    if(node.isFinal()){
      finalStates[finalCount] = node;
      finalCount++;
    }

    if(start==null){
      start = node;
      current = node;
    }
    else{
      current.addTransition(transition, node);
    }
  }

  public NfaNode[] getEpsilonEnclusure(NfaNode node){
    return node.getEpsilonTransition();
  }
}
