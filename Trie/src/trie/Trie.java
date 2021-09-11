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
		int n = allWords.length;
		TrieNode root = new TrieNode (null, null, null);
		
		for(int i = 0; i < n; i++ ) {
			
			
			// To insert the first Word
			if (i == 0) {
				Indexes substr = new Indexes(0, (short)0, (short)(allWords[0].length()-1));
				TrieNode firstWord = new TrieNode ( substr, null, null);
				root.firstChild = firstWord;
				continue;
			}
			
			int lengthOfCurrentWord = allWords[i].length(); int j = 0;
			
			if (lengthOfCurrentWord == 1) {
				Indexes substr = new Indexes(i, (short)0, (short)0);
				TrieNode oneCharacterWord = new TrieNode ( substr, null, null);
				TrieNode ptr = root.firstChild;
				while(ptr.sibling!=null) {
					ptr = ptr.sibling;
				}
				ptr.sibling = oneCharacterWord;
				continue;
			}
			
			
			TrieNode ptr = root.firstChild;
			TrieNode prev = root;
			int word = 0; int startingIndex = 0; int endingIndex = 0;
			
			while (j < lengthOfCurrentWord) {
				
			while( ptr != null) {
				word = ptr.substr.wordIndex;
				startingIndex = ptr.substr.startIndex;
				endingIndex = ptr.substr.endIndex;
				if (allWords[i].charAt(j) == allWords[word].charAt(startingIndex)) {
					break;
				}
				prev = ptr;
				ptr = ptr.sibling;
			}
			
			if(ptr == null) {
				Indexes substr = new Indexes(i, (short)j, (short)(lengthOfCurrentWord-1));
				TrieNode newWord = new TrieNode(substr, null, null);
				prev.sibling = newWord;
				break;
			}else {
				// find the common substring
				int k = 0;
				while (j+k < lengthOfCurrentWord && startingIndex+k <= endingIndex) {
					if (allWords[i].charAt(j+k) == allWords[word].charAt(startingIndex+k)) {
						k++;
					}else {
						break;
					}
				}
				
				j = j+k; short newEndingIndex = (short)(startingIndex + k - 1);
				
				//if whatever I matched with is a leaf
				if (ptr.firstChild == null) {
					ptr.substr.endIndex = newEndingIndex;
					Indexes substring1 = new Indexes(word, (short)(newEndingIndex + 1), (short)(allWords[word].length()-1));
					Indexes substring2 = new Indexes(i, (short)(j), (short)(lengthOfCurrentWord-1));
					TrieNode two = new TrieNode (substring2, null, null);
					TrieNode one = new TrieNode (substring1, null, two);
					ptr.firstChild = one;
					break;
				}
				
				// if whatever I matched with is not a leaf and only part of the substring matched
				if(newEndingIndex != endingIndex) {
					ptr.substr.endIndex = newEndingIndex;
					Indexes substring1 = new Indexes(word, (short)(newEndingIndex + 1), (short)(endingIndex));
					Indexes substring2 = new Indexes(i, (short)(j), (short)(lengthOfCurrentWord-1));
					TrieNode two = new TrieNode (substring2, null, null);
					TrieNode one = new TrieNode (substring1, ptr.firstChild, two);
					ptr.firstChild = one;
					break;
				}
				
				// if whatever I matched to is not a leaf and the whole part matches.
				prev = ptr;
				ptr = ptr.firstChild;
				
				
				
			}
			
			
		  }	
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
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
		ArrayList<TrieNode> result = new ArrayList<TrieNode>();
		
		int lengthOfPrefix = prefix.length();
		TrieNode prev = root;
		TrieNode ptr = root.firstChild;
		
		for(int i = 0; i < lengthOfPrefix; i++) {
			
			int indexOfWord = 0; int startCharacter = 0; int endCharacter = 0;
			
			while (ptr != null) {
				indexOfWord = ptr.substr.wordIndex;
				startCharacter = ptr.substr.startIndex;
				endCharacter = ptr.substr.endIndex;
				if(prefix.charAt(i) == allWords[indexOfWord].charAt(startCharacter)) {
					break;
				}
				prev = ptr;
				ptr = ptr.sibling;
			}
			
			//prefix does not match with any prefix.
			if (ptr == null) {
				return null;
			}
			
			// finding the part of the prefix that is common.
			int j = 0; 
			while(((i + j) < lengthOfPrefix) && ((startCharacter + j) <= endCharacter) ) {
				if (prefix.charAt(i + j) == allWords[indexOfWord].charAt(startCharacter + j)) {
					j++;
				}else {
					break;
				}
			}
			
			//update i and startCharacter
			i = i + j - 1;  int endOfCommon = startCharacter + j - 1;
			
			//if we are currently pointing to a leaf
			if(ptr.firstChild == null) {
				if (i == lengthOfPrefix - 1) {
					result.add(ptr); break;
				}else {
					return null;
				}
			}
			
			//if we are curretly pointing to an internal node
			if(ptr.firstChild != null) {
				if(i == lengthOfPrefix - 1) {
					//I need to add all the leaves of this pointer
					
					addAllLeaves(root, ptr, result);
					break;
				}else if(endOfCommon < endCharacter ){
					return null; 
					
				}else {
				
					prev = ptr;
					ptr = ptr.firstChild;
				}
			}
			
			
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return result;
	}
	
	private static void addAllLeaves(TrieNode root, TrieNode ptr, ArrayList<TrieNode> result) {
		TrieNode m = ptr.firstChild;
		while (m!=null) {
			if(m.firstChild == null) {
				result.add(m);
			}else {
				addAllLeaves(root, m, result);
			}
			m = m.sibling;
		}
		
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
