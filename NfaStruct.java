public class NfaStruct{
  private NfaNode start;
  private NfaNode[] finalStates = new NfaNode[20];
  private NfaNode current;

  public NfaStruct(){
    start = null;
    current = null;
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

      }
    }
    return current;
  }
}
