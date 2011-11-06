import java.util.*;

import org.omg.CORBA.CTX_RESTRICT_SCOPE;

public class NfaStruct{
  private NfaNode start;
  private NfaNode current;
  private NfaNode[] finalStates = new NfaNode[20];
  private int finalCount;
  private Hashtable allNodes = new Hashtable<String, NfaNode>(10,10); //Node identifier is the key, the node object is the value
  private int nodeCount;
  private ArrayList<String> alphabet;

  /* Creates an NfaStruct with the input node as the start state */
  public NfaStruct(NfaNode node){
    start = node;
    current = node;
    //start node should be in the nodes shouldnt it?
    allNodes.put(node.getIdentifier(), node);
    alphabet = new ArrayList<String>();
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
      if(!alphabet.contains(transition) && !transition.equals("epsilon")){alphabet.add(transition);}
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
  public ArrayList getEpsilonClosure(){
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
  
  /* Updates the epsilon closures of all the nodes*/
  public void updateEpsilonClosures(){
	  NfaNode cur;
	  ArrayList<NfaNode> curtrans;
	  ArrayList<NfaNode> visited = new ArrayList<NfaNode>();
	  //make enumeration of all the keys to iterate through table
	  Enumeration e = allNodes.keys();
	  //iterate through hash table by key
	  while(e.hasMoreElements()){
		  cur = (NfaNode)allNodes.get(e.nextElement());
		  curtrans = cur.getEpsilonTransition();
		  visited.clear();
		  visited.add(cur);
		  for (NfaNode n : curtrans){
			  n.traverseEpsilon(visited);
		  }
		  /*add visited nodes to epsilon transitions*/
		  for (NfaNode n: visited){
			  cur.addEpsilonTransition(n);
		  }
	  }	  
  }
  
  
  /* getters*/
  public NfaNode getNode(String key){
	  return (NfaNode)allNodes.get(key);
  }
  public NfaNode getCurrent(){
	  return (NfaNode)current;
  }
  public Enumeration getNodeIDs(){
	  return (Enumeration)allNodes.keys();
  }
  public ArrayList<String> getAlphabet(){
	  return alphabet;
  }
  
  /*current print functions doesnt take empty transitions into account*/
  public String toString(){
	  String t = "";
	  //make enumeration of all the keys to iterate through table
	  Enumeration e = allNodes.keys();
	  //iterate through hash table by key
	  while(e.hasMoreElements()){
		  NfaNode n = (NfaNode)allNodes.get(e.nextElement());
		  t += n + " " + n.getAllTransitions() + " " + n.getEpsilonTransition() + "\n";
	  }	  
	  return t;
  }
	
}
