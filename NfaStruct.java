import java.util.*;

public class NfaStruct{
  private NfaNode start;
  private NfaNode current;
  private NfaNode[] finalStates = new NfaNode[20];
  private int finalCount;
  private Hashtable allNodes = new Hashtable<String, NfaNode>(10,10); //Node identifier is the key, the node object is the value
  private int nodeCount;

  /* Creates an NfaStruct with the input node as the start state */
  public NfaStruct(NfaNode node){
    start = node;
    current = node;
    if(node!=null){
      nodeCount = 1;
    }
    else{
      nodeCount = 0;
    }
  }

  /* Creates an empty NfaStruct */
  public NfaStruct(){
    this(null);
  }

  public NfaNode makeTransition(String key){
    if(current!=null){
      if(current.isTransition(key)){
        return current.getTransition(key);
      }
    }
    return null;
  }

  public boolean addNode(NfaNode node, String transition){
    if(node==null){
      return false;
    }
    if(node.isFinal()){
      finalStates[finalCount] = node;
      finalCount++;
    }
    allNodes.put(node.getIdentifier(), node);

    if(start==null){
      start = node;
      current = node;
      nodeCount += 1;
      return true;
    }
    else{
      nodeCount += 1;
      return current.addTransition(transition, node); //Just add a node that is connected to the current node
    }
  }

  public NfaNode getStart(){
    return start;
  }

  public NfaNode[] getFinal(){
    return finalStates;
  }

  public int getNodeCount(){
    return nodeCount;
  }

  /* Method that changes the current node, returns false if no node has the identifier */
  public boolean setCurrent(String identifier){
    if(allNodes.containsKey(identifier)){
      current = (NfaNode)allNodes.get(identifier);
      return true;
    }
    return false;
  }

  /* Gets the epsilon closures of the current node */
  public NfaNode[] getEpsilonClosure(){
    if(current!=null){
      return current.getEpsilonTransition();
    }
    else{
      return null; //null if node is empty
    }
  }

  public void printNfa(NfaNode init){
    //Print out the initial node and its transitions
    NfaNode temp = init;
    System.out.printf("%s:\t", temp.getIdentifier());
    Hashtable tempTrans = temp.getAllTransitions();
    System.out.println(tempTrans.toString());

    //Recurse through all of the transitions from the init node
    for(Enumeration<NfaNode> nodeEnum = tempTrans.elements(); nodeEnum.hasMoreElements();){
      printNfa(nodeEnum.nextElement());
    }
  }
}
