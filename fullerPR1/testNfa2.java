import java.util.ArrayList;


/**
 * 
 * Rules for making NFAs in rules.jpg included
 *
 */
public class testNfa2 {
	
	public static void main(String args[]){
				
		//test nfa for (a|b)*b+ ie any string of 'a' and 'b' that ends in b
		System.out.println("(a|b)*b+");
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
		System.out.println("SIZE: " + dfa2.size());
		System.out.println("Accepts \"abababababaaba\"? " + dfa2.accepts("abababababaaba"));
		System.out.println("Accepts \"abababababaabb\"? " + dfa2.accepts("abababababaabb"));
		System.out.println("minimized DFA:");
		dfa2.minimize();
		System.out.println(dfa2);
		System.out.println("SIZE: " + dfa2.size());
		System.out.println("Accepts \"abababababaaba\"? " + dfa2.accepts("abababababaaba"));
		System.out.println("Accepts \"abababababaabb\"? " + dfa2.accepts("abababababaabb"));
		

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
		
		
		//(0|1)*(101)(0|1)*
		System.out.println("");
		System.out.println("----------------");
		System.out.println("(0|1)*(101)(0|1)*");
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
		System.out.println(d);
		System.out.println("SIZE: " + d.size());
		System.out.println("Accepts \"11010101001\"? " + d.accepts("11010101001"));
		System.out.println("Accepts \"11001001001\"? " + d.accepts("11001001001"));
		System.out.println("minimized DFA:");
		d.minimize();
		System.out.println(d);
		System.out.println("SIZE: " + d.size());
		System.out.println("Accepts \"11010101001\"? " + d.accepts("11010101001"));
		System.out.println("Accepts \"11001001001\"? " + d.accepts("11001001001"));
		
		
		//exclusion tests
		System.out.println("");
		System.out.println("Test excluion nfa for [^a-z]");
		NfaStruct nf = EXCLUDE_CHAR.exclude('a','z', "s");
		DFAStruct df = new DFAStruct(nf);
		System.out.println("Accepts \"#\"? "+df.accepts("#"));
		System.out.println("Accepts \"j\"? "+df.accepts("j"));
		
		System.out.println("Test excluion nfa for [^abd]");
		ArrayList<String> exclusions = new ArrayList<String>();
		exclusions.add("a");
		exclusions.add("b");
		exclusions.add("d");
		NfaStruct nf2 = EXCLUDE_CHAR.exclude(exclusions, "s");
		DFAStruct df2 = new DFAStruct(nf2);
		System.out.println("Accepts \"a\"? "+df2.accepts("a"));
		System.out.println("Accepts \"b\"? "+df2.accepts("b"));
		System.out.println("Accepts \"d\"? "+df2.accepts("d"));
		System.out.println("Accepts \"c\"? "+df2.accepts("c"));

		
	}
}
