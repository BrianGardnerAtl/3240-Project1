
public class testNfa2 {
	
	public static void main(String args[]){
		/*test nfa for (a|b)*
		NfaNode s10 = new NfaNode("s0");
		NfaNode s11 = new NfaNode("s1");
		NfaNode s12 = new NfaNode("s2");
		NfaNode s13 = new NfaNode("s3");
		NfaNode s14 = new NfaNode("s4");
		NfaNode s15 = new NfaNode("s5");
		NfaNode s16 = new NfaNode("s6");
		NfaNode s17 = new NfaNode("s7", true);
		NfaStruct test1 = new NfaStruct(s10);
		//current is s0 at this point
		test1.addNode(s11, "epsilon");
		//current is s1 at this point
		test1.setCurrent("s1");
		test1.addNode(s12, "epsilon");
		test1.addNode(s13, "epsilon");
		test1.addNode(s16, "epsilon");
		//s2
		test1.setCurrent("s2");
		test1.addNode(s14, "a");
		//s3
		test1.setCurrent("s3");
		test1.addNode(s15, "b");
		//s4
		test1.setCurrent("s4");
		test1.addNode(s16, "epsilon");
		//s5
		test1.setCurrent("s5");
		test1.addNode(s16, "epsilon");
		//s6
		test1.setCurrent("s6");
		test1.addNode(s11, "epsilon");
		test1.addNode(s17, "epsilon");
		test1.updateEpsilonClosures();
		System.out.println(test1);
		System.out.print("\nTo DFA:\n");
		DFAStruct dfa1 = new DFAStruct(test1);
		System.out.println(dfa1);*/
		
		
		//test nfa for (a|b)*b+ ie any string of 'a' and 'b' that ends in b
		/*char a = 'a';
		char b = 'b';*/
		NfaNode b1 = new NfaNode("b1");
		NfaNode b2 = new NfaNode("b2");
		NfaNode b3 = new NfaNode("b3");
		NfaNode b4 = new NfaNode("b4");
		NfaNode b5 = new NfaNode("b5");
		NfaNode b6 = new NfaNode("b6");
		NfaNode b7 = new NfaNode("b7");
		NfaNode b8 = new NfaNode("b8");
		NfaNode b9 = new NfaNode("b9");
		NfaNode b10 = new NfaNode("b10");
		NfaNode b11 = new NfaNode("b11", true);
		NfaStruct test2 = new NfaStruct(b1);
		//current is b1 at this point
		test2.setCurrent("b1");
		test2.addNode(b2, "epsilon");
		//b2
		test2.setCurrent("b2");
		test2.addNode(b3, "epsilon");
		test2.addNode(b4, "epsilon");
		test2.addNode(b7, "epsilon");
		//b3
		test2.setCurrent("b3");
		test2.addNode(b5, "a");
		//b4
		test2.setCurrent("b4");
		test2.addNode(b6, "b");
		//b5
		test2.setCurrent("b5");
		test2.addNode(b7, "epsilon");
		//b6
		test2.setCurrent("b6");
		test2.addNode(b7, "epsilon");
		//b7
		test2.setCurrent("b7");
		test2.addNode(b2, "epsilon");
		test2.addNode(b8, "epsilon");
		//b8
		test2.setCurrent("b8");
		test2.addNode(b9, "epsilon");
		//b9
		test2.setCurrent("b9");
		test2.addNode(b10, "b");
		//b10
		test2.setCurrent("b10");
		test2.addNode(b9, "epsilon");
		test2.addNode(b11, "epsilon");
		
		//print tables		
		test2.updateEpsilonClosures();
		//nfa
		System.out.println("NFA:");
		System.out.println(test2);
		//dfa
		System.out.print("\nDFA:\n");
		DFAStruct dfa1 = new DFAStruct(test2);
		System.out.println(dfa1);
		System.out.println(dfa1.accepts("abababababaab"));
		
	}
}
