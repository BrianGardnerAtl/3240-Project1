import java.util.ArrayList;


public class testNfa2 {
	
	public static void main(String args[]){
		
		/*//test for (a|b)*
		NfaNode s0 = new NfaNode("s0");
		NfaNode s1 = new NfaNode("s1");
		NfaNode s2 = new NfaNode("s2");
		NfaNode s3 = new NfaNode("s3");
		NfaNode s4 = new NfaNode("s4");
		NfaNode s5 = new NfaNode("s5");
		NfaNode s6 = new NfaNode("s6");
		NfaNode s7 = new NfaNode("s7", true);
		NfaStruct test1 = new NfaStruct(s0);
		//current is s0 at this point
		test1.addNode(s1, "epsilon");
		//current is s1 at this point
		test1.setCurrent("s1");
		test1.addNode(s2, "epsilon");
		test1.addNode(s3, "epsilon");
		test1.addNode(s6, "epsilon");
		//s2
		test1.setCurrent("s2");
		test1.addNode(s4, "a");
		//s3
		test1.setCurrent("s3");
		test1.addNode(s5, "b");
		//s4
		test1.setCurrent("s4");
		test1.addNode(s6, "epsilon");
		//s5
		test1.setCurrent("s5");
		test1.addNode(s6, "epsilon");
		//s6
		test1.setCurrent("s6");
		test1.addNode(s1, "epsilon");
		test1.addNode(s7, "epsilon");
		test1.updateEpsilonClosures();

		//print tables		
		test1.updateEpsilonClosures();
		//nfa
		System.out.println("NFA:");
		System.out.println(test1);
		//dfa
		System.out.print("\nDFA:\n");
		DFAStruct dfa1 = new DFAStruct(test1);
		System.out.println(dfa1);
		System.out.println(dfa1.accepts("abababababaaba"));*/
		
		
		/*//test nfa for (a|b)*b+ ie any string of 'a' and 'b' that ends in b
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
		DFAStruct dfa2 = new DFAStruct(test2);
		System.out.println(dfa2);
		System.out.println(dfa2.accepts("abababababaab"));*/
		
		/*//test for a
		NfaNode j0 = new NfaNode("s0");
		NfaNode j1 = new NfaNode("s1", true);
		NfaStruct test3 = new NfaStruct(j0);
		//current is s0 at this point
		test3.addNode(j1, "a");
		//print tables		
		test3.updateEpsilonClosures();
		//nfa
		System.out.println("NFA:");
		System.out.println(test3);
		//dfa
		System.out.print("\nDFA:\n");
		DFAStruct dfa3 = new DFAStruct(test3);
		System.out.println(dfa3);
		System.out.println(dfa3.accepts("a "));*/
		
		/*//test for (a|b|c)*
		NfaNode f1 = new NfaNode("f1");
		NfaNode f2 = new NfaNode("f2");
		NfaNode f3 = new NfaNode("f3");
		NfaNode f4 = new NfaNode("f4");
		NfaNode f5 = new NfaNode("f5");
		NfaNode f6 = new NfaNode("f6");
		NfaNode f7 = new NfaNode("f7");
		NfaNode f8 = new NfaNode("f8");
		NfaNode f9 = new NfaNode("f9");
		NfaNode f10 = new NfaNode("f10", true);
		NfaStruct test5 = new NfaStruct(f1);
		//current is f1 at this point
		test5.addNode(f2, "epsilon");
		//f2
		test5.setCurrent("f2");
		test5.addNode(f3,"epsilon");
		test5.addNode(f4,"epsilon");
		test5.addNode(f5,"epsilon");
		test5.addNode(f9,"epsilon");
		//f3
		test5.setCurrent("f3");
		test5.addNode(f6, "a");
		//f4
		test5.setCurrent("f4");
		test5.addNode(f7, "b");
		//f5
		test5.setCurrent("f5");
		test5.addNode(f8, "c");
		//f6
		test5.setCurrent("f6");
		test5.addNode(f9, "epsilon");
		//f7
		test5.setCurrent("f7");
		test5.addNode(f9, "epsilon");
		//f8
		test5.setCurrent("f8");
		test5.addNode(f9, "epsilon");
		//f9
		test5.setCurrent("f9");
		test5.addNode(f10, "epsilon");
		test5.addNode(f2,"epsilon");

		//print tables		
		test5.updateEpsilonClosures();
		//nfa
		System.out.println("NFA:");
		System.out.println(test5);
		//dfa
		System.out.print("\nDFA:\n");
		DFAStruct dfa5 = new DFAStruct(test5);
		System.out.println(dfa5);
		System.out.println(dfa5.accepts("abbabcbaccbabca"));
		//minimized
		System.out.print("\nminimized DFA:\n");
		dfa5.minimize();
		System.out.println(dfa5);
		System.out.println(dfa5.accepts("abbabcbaccbabca"));*/
		
		/*//(a|b)*(ab)+
		NfaNode f1 = new NfaNode("f1");
		NfaNode f2 = new NfaNode("f2");
		NfaNode f3 = new NfaNode("f3");
		NfaNode f4 = new NfaNode("f4");
		NfaNode f5 = new NfaNode("f5");
		NfaNode f6 = new NfaNode("f6");
		NfaNode f7 = new NfaNode("f7");
		NfaNode f8 = new NfaNode("f8");
		NfaNode f9 = new NfaNode("f9");
		NfaNode f10 = new NfaNode("f10");
		NfaNode f11 = new NfaNode("f11");
		NfaNode f12 = new NfaNode("f12", true);
		NfaStruct test5 = new NfaStruct(f1);
		//current is f1 at this point
		test5.addNode(f2, "epsilon");
		//f2
		test5.setCurrent("f2");
		test5.addNode(f3,"epsilon");
		test5.addNode(f4,"epsilon");
		test5.addNode(f7,"epsilon");
		//f3
		test5.setCurrent("f3");
		test5.addNode(f5, "a");
		//f4
		test5.setCurrent("f4");
		test5.addNode(f6, "b");
		//f5
		test5.setCurrent("f5");
		test5.addNode(f7, "epsilon");
		//f6
		test5.setCurrent("f6");
		test5.addNode(f7, "epsilon");
		//f7
		test5.setCurrent("f7");
		test5.addNode(f2, "epsilon");
		test5.addNode(f8, "epsilon");
		//f8
		test5.setCurrent("f8");
		test5.addNode(f9, "epsilon");
		//f9
		test5.setCurrent("f9");
		test5.addNode(f10, "a");
		//f10
		test5.setCurrent("f10");
		test5.addNode(f11, "b");
		//f11
		test5.setCurrent("f11");
		test5.addNode(f9, "epsilon");
		test5.addNode(f12, "epsilon");
		
		//print tables		
		test5.updateEpsilonClosures();
		//nfa
		System.out.println("NFA:");
		System.out.println(test5);
		//dfa
		System.out.print("\nDFA:\n");
		DFAStruct dfa5 = new DFAStruct(test5);
		System.out.println(dfa5);
		System.out.println();
		dfa5.minimize();
		System.out.println(dfa5);
		System.out.println(dfa5.accepts("abababababbaab"));*/
		
		//last test im doing like goddamn
		//(0|1)*(101)(0|1)*
		NfaNode f1 = new NfaNode("1");
		NfaNode f2 = new NfaNode("2");
		NfaNode f3 = new NfaNode("3");
		NfaNode f4 = new NfaNode("4");
		NfaNode f5 = new NfaNode("5");
		NfaNode f6 = new NfaNode("6");
		NfaNode f7 = new NfaNode("7");
		NfaNode f8 = new NfaNode("8");
		NfaNode f9 = new NfaNode("9");
		NfaNode f10 = new NfaNode("10");
		NfaNode f11 = new NfaNode("11");
		NfaNode f12 = new NfaNode("12");
		NfaNode f13 = new NfaNode("13");
		NfaNode f14 = new NfaNode("14");
		NfaNode f15 = new NfaNode("15");
		NfaNode f16 = new NfaNode("16");
		NfaNode f17 = new NfaNode("17");
		NfaNode f18 = new NfaNode("18",true);
		NfaStruct n = new NfaStruct(f1);
		String e = "epsilon";
		//1
		n.addNode(f2,e);
		//2
		n.setCurrent("2");
		n.addNode(f3, e);
		n.addNode(f4, e);
		n.addNode(f7, e);
		//3
		n.setCurrent("3");
		n.addNode(f5,"1");
		//4
		n.setCurrent("4");
		n.addNode(f6,"0");
		//5
		n.setCurrent("5");
		n.addNode(f7,e);
		//6
		n.setCurrent("6");
		n.addNode(f7,e);
		//7
		n.setCurrent("7");
		n.addNode(f2,e);
		n.addNode(f8,e);
		//8
		n.setCurrent("8");
		n.addNode(f9,"1");
		//9
		n.setCurrent("9");
		n.addNode(f10,"0");
		//10
		n.setCurrent("10");
		n.addNode(f11,"1");
		//11
		n.setCurrent("11");
		n.addNode(f12,e);
		//12
		n.setCurrent("12");
		n.addNode(f13,e);
		n.addNode(f14,e);
		n.addNode(f17,e);
		//13
		n.setCurrent("13");
		n.addNode(f15,"1");
		//14
		n.setCurrent("14");
		n.addNode(f16,"0");
		//15
		n.setCurrent("15");
		n.addNode(f17,e);
		//16
		n.setCurrent("16");
		n.addNode(f17,e);
		//17
		n.setCurrent("17");
		n.addNode(f12,e);
		n.addNode(f18,e);
		
		//print tables		
		n.updateEpsilonClosures();
		//nfa
		System.out.println("NFA:");
		System.out.println(n);
		//dfa
		System.out.print("\nDFA:\n");
		DFAStruct d = new DFAStruct(n);
		System.out.print(d);
		System.out.println("Size:" + d.size());
		System.out.println();
		
		System.out.println("Minimized:");
		d.minimize();
		System.out.print(d);
		System.out.println("Size:" + d.size());
		System.out.println(d.accepts("101"));

		
	}
}
