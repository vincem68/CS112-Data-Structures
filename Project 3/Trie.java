package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		//this creates the empty root
		TrieNode root = new TrieNode(null, null, null);
		
		//this cycles through the array list and inserts the words into the tree 
		
		for (int i = 0; i < allWords.length; i++) {
			TrieNode crnt = root; TrieNode prev = null; 
			String crntWord = allWords[i]; int length = crntWord.length()-1; 
			boolean empty = false; boolean child = false; boolean rearrange = false;
			short change = 0;
			
		//	System.out.println("the current word is "+ crntWord);
			
			//this checks if we just have a root node with no other nodes
			if (root.firstChild == null) {
				int index = i;
				short start = 0; short end = (short)length;
				Indexes newIndex = new Indexes(index, start, end);
				TrieNode newNode = new TrieNode(newIndex, null, null);
				root.firstChild = newNode;
		//		System.out.println("the first node is "+newNode);
			} else {
				
				//starts at the first child
				crnt = crnt.firstChild; prev = root;
				while (empty == false && crnt != null && rearrange == false) {
			//		System.out.println(crnt);
					String comparedWord = allWords[crnt.substr.wordIndex]; 
			//	System.out.println("the compared word is "+comparedWord);
					//checks to see if we have any prefixes 
					if (crntWord.charAt((int)crnt.substr.startIndex) == comparedWord.charAt((int)crnt.substr.startIndex)) {
					    int go = (int)crnt.substr.startIndex;
				//	    System.out.println("go is "+go);
						while (go < comparedWord.length() && go < crntWord.length() && go <= (int)crnt.substr.endIndex && crntWord.charAt(go) == comparedWord.charAt(go)) {
							go++;
							
						}
						go -= 1;
			//			System.out.println("the number of matching characters is "+go);
						//checks if this is an internal node and we move down
					    if (go == (int)crnt.substr.endIndex) {
						    prev = crnt;
						    crnt = crnt.firstChild;
						    child = true;
						    
						    // checks if we need to make a new pair of nodes
				     	} else if (go < (int)crnt.substr.endIndex) {
				     		if (crnt.firstChild != null) {
				     			change = (short)go;
				     			child = true;
				     			rearrange = true;
				     			
				     		} else {
				     		prev = crnt;
				     		prev.substr.endIndex = (short)go;
				     		child = true;
				     		empty = true;
				     		}
				     	}
		
					} else {
						prev = crnt;
						crnt = crnt.sibling;
					}
				}
				
		//	System.out.println("change is "+change);
		//		System.out.println("child is "+child);
		//		System.out.println("empty is "+empty);
		//		System.out.println("rearrange is "+rearrange);
		//		System.out.println(crnt);
				if (rearrange == true) {
					int index = i;
					short childStart = crnt.substr.startIndex; short childEnd = change;
				//	System.out.println(change);
					crnt.substr.startIndex = (short)(change + 1);
					Indexes newFirstIndex = new Indexes(index, childStart, childEnd);
					TrieNode newFirstChild = new TrieNode(newFirstIndex, crnt, null);
					newFirstChild.firstChild = crnt;
					prev.firstChild = newFirstChild;
		//		System.out.println("new first child is "+newFirstChild);
					
					//create the new node of the word we want to insert
					Indexes siblingIndex = new Indexes(i, crnt.substr.startIndex, (short)length);
					TrieNode siblingNode = new TrieNode(siblingIndex, null, null);
					crnt.sibling = siblingNode;
	//				System.out.println("sibling node is "+siblingNode);
					
				}
				
				else if (empty == true) {
					int index = prev.substr.wordIndex;
					short newStart = prev.substr.endIndex; newStart += 1;
					short newEnd = (short)allWords[prev.substr.wordIndex].length(); newEnd -= 1; //changing something here 
					Indexes newIndex = new Indexes(index, newStart, newEnd);
				//	System.out.println("the new index when empty is true is "+newIndex);
					TrieNode newNode = new TrieNode(newIndex, null, null);
					prev.firstChild = newNode;
		//			System.out.println("new node when empty is true is "+newNode);
				 
					int otherIndex = i; short otherNewStart = newStart;
					short otherNewEnd = (short)crntWord.length(); otherNewEnd -= 1;
					Indexes otherNewIndex = new Indexes(otherIndex, otherNewStart, otherNewEnd);
			//		System.out.println("the other new index when empty is true is "+otherNewIndex);
					TrieNode otherNewNode = new TrieNode(otherNewIndex, null, null);
					newNode.sibling = otherNewNode;
			//		System.out.println("other new node when emtpy is true is "+otherNewNode);
					
					}
				//this checks if the word matches no other prefixes at all in the tree, and we just add a new node
				else {
					int index = i;
					if (child == true) {
						short childStart = prev.substr.startIndex;   //make sure this is start
						short end = (short)length;
						Indexes newIndex = new Indexes(index, childStart, end);
						TrieNode newNode = new TrieNode(newIndex, null, null);
						prev.sibling = newNode;
			//			System.out.println("a simple new sibling is "+newNode);
					} else {
						short start = 0;
						short end = (short)length;
						Indexes newIndex = new Indexes(index, start, end);
						TrieNode newNode = new TrieNode(newIndex, null, null);
						prev.sibling = newNode;
			//			System.out.println("a simple new sibling is "+newNode);
					}
				}
			}
		}
		
		
		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		 
		TrieNode crnt = root.firstChild; boolean found = false; boolean foundWithChildren = false;
		ArrayList<TrieNode> connectedWords = new ArrayList<TrieNode>(); ArrayList<TrieNode> internalNodes = new ArrayList<TrieNode>();
		while (crnt != null && found == false && foundWithChildren == false) {
			String compare = allWords[crnt.substr.wordIndex]; 
		//	System.out.println("the string at the current node is "+compare);
			if ((int)crnt.substr.startIndex < prefix.length() && compare.charAt((int)crnt.substr.startIndex) == prefix.charAt((int)crnt.substr.startIndex)) {
				int count = (int)crnt.substr.startIndex;
				while (count < compare.length() && count < prefix.length() && compare.charAt(count) == prefix.charAt(count) && count <= (int)crnt.substr.endIndex) {
					count++;
				}
				count -= 1;
		//		System.out.println("the last similar index is "+count);
				if (count <= (int)crnt.substr.endIndex && count == prefix.length()-1) { //another change here
		
					if (crnt.firstChild == null) {
					    found = true;
					} else { foundWithChildren = true; }
				}
				else if (count == (int)crnt.substr.endIndex && prefix.length() > count) { // I made a change here
					crnt = crnt.firstChild;
				}
			} else {
				crnt = crnt.sibling;
			}
		}
	//	System.out.println("found is "+found);
	//	System.out.println("found with children is "+foundWithChildren);

		if (crnt == null) {
			return null;
		}

		else if (found == true) {
			connectedWords.add(crnt);
		}
		  //I think we need a recursive method here or something 
		else if (foundWithChildren == true) {
			
			TrieNode temp = crnt.firstChild; crnt = crnt.firstChild; 
		//	System.out.println("temp starts at "+temp); System.out.println("crnt starts at "+crnt);
			while (temp != null) {
				if ( crnt.firstChild == null) {
					connectedWords.add(crnt);
			//		System.out.println("connected words is "+connectedWords);
				} else {
					if (crnt != temp) {
					internalNodes.add(crnt);
		//			System.out.println("internal nodes is "+internalNodes);
					}
				}
				crnt = crnt.sibling;
		//		System.out.println("crnt is now "+crnt);
				if (crnt == null) {
					if (temp.firstChild != null) {
					    temp = temp.firstChild; crnt = temp;
				//	    System.out.println("temp is now "+temp); System.out.println("crnt is also "+crnt);
					} else {
						if (internalNodes.size() == 0) {
							temp = temp.firstChild;
					//		System.out.println("if the size is 0, temp is "+temp);
						
						} else {
							temp = internalNodes.get(0);
				//			System.out.println("if the array list isn't empty, temp is now "+temp);
							internalNodes.remove(0);
							temp = temp.firstChild;
							crnt = temp;
					//		System.out.println("internalNodes is now "+internalNodes);
						}
					}
				}
				
			}
		
		} 
		return connectedWords;
		
		
		
		
		
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
