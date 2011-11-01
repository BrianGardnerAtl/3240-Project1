import java.util.*;

public class testNfa{
  
  public static void main(String args[]){
    //Create the first node and the nfa structure
    NfaNode init = new NfaNode("Initial Node");
    NfaStruct nfa = new NfaStruct(init);
    NfaNode startNode = nfa.getStart();
    System.out.println("Single node NFA");
    nfa.printNfa(startNode);    //Prints out 'Initial Node:   {}' since there are no transitions

    NfaNode temp1 = new NfaNode("second");
    NfaNode temp2 = new NfaNode("third");
    nfa.addNode(temp1, "a"); //Transition to temp1 on the character a
    nfa.addNode(temp2, "b"); //Transition to temp2 on the character b
    System.out.println("Three node NFA");
    nfa.printNfa(startNode);

    NfaNode transNode = nfa.makeTransition("a");
    if(nfa.setCurrent(transNode.getIdentifier())){
      System.out.println("Set current");
    }
    NfaNode temp3 = new NfaNode("fourth");
    NfaNode temp4 = new NfaNode("fifth");
    nfa.addNode(temp3, "a");
    nfa.addNode(temp4, "b");
    System.out.println("Test makeTransition");
    nfa.printNfa(startNode);
  }
}
