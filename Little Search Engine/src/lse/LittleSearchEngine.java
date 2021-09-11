package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		HashMap<String, Occurrence> result = new HashMap<String, Occurrence>();
		
		File doc = new File(docFile);
		if(doc.exists()) {
			Scanner sc = new Scanner(doc);
			while(sc.hasNext()) {
				String word = sc.next();
				String kword = getKeyword(word);
				if(kword == null) {
					continue;
				}else {
					if(result.get(kword) == null) {
						Occurrence o = new Occurrence (docFile, 1);
						result.put(kword, o);
					}else {
						Occurrence o = result.get(kword);
						o.frequency++;
					}
				}
			}
			
		}else {
			throw new FileNotFoundException("File is not found");
		}
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return result;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		
		for(String e: kws.keySet()) {
		ArrayList<Occurrence> masterTableValue = keywordsIndex.get(e);	
		if(masterTableValue == null) {
			ArrayList<Occurrence> newKeyWord = new ArrayList<Occurrence>();
			newKeyWord.add(kws.get(e));
			keywordsIndex.put(e, newKeyWord);
		}else {
			masterTableValue.add(kws.get(e));
			
			}
		masterTableValue = keywordsIndex.get(e);
		insertLastOccurrence(masterTableValue);
		
	 }
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		int lengthOfWord = word.length();
		int j = lengthOfWord - 1;
		
		while(j>=0) {
			char c = word.charAt(j);
			if ((c == '.') || (c == ',') || (c == '?') || (c == ':') || (c == ';') || (c == '!')) {
				
				j--;	
			}else {
				break;
			}
		}
		if (j<0) {
			return null;
		}else {
			word = word.substring(0, j+1);
		}
		
		int newlengthOfWord = word.length();
		
		//to check if there are any non-alphabetical characters in the word;
		boolean check1 = true;
		
		for(int k = 0; k < newlengthOfWord; k++) {
			char currChar = word.charAt(k);
			if (! ((currChar >= 'a' && currChar <= 'z')||(currChar >= 'A' && currChar <= 'Z')) ) {
				check1 = false; break;
			}
		}
		
		if(check1 == false) {
			return null;
		}
		
		
		word = word.toLowerCase();
		
		//to check if it is a noise word
		boolean check2 = noiseWords.contains(word);
		
		if(check2 == true) {
			return null;
		}
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
	    int n = occs.size();
	    if(n == 1) {
	    	return null;
	    }
	    ArrayList<Integer> result = new ArrayList<Integer>();
	    
	    int target = occs.get(n-1).frequency;
	    int low = 0; int high = n-2;
	    int mid=0;
	    
	    while(low <= high) {
	    	mid = (low+high)/2;
	    	result.add(mid);
	    	int value = occs.get(mid).frequency;
	    	if(value == target) {
	    		//found the frequency in the arrayList
	    		break;
	    	}else if (target > value) {
	    		high = mid - 1;
	    	}else {
	    		low = mid+1;
	    	}
	    }
	    
	    //grabbing the last occurrence that I want to insert in the correct position.
	    Occurrence newOccurrence = occs.get(n-1);
	    occs.remove(n-1);
	    int indexToAdd = 0;
	    
	    if (low <= high) {
	    	//we already found the frequency that we are looking for.
	    	//I will add this occurrence right after the mid.
	    	//add right after the mid value
	    	indexToAdd = mid+1;
	    	
	    }else if( high < mid) {
	    	//add right before mid.
	    	
	   
	    		indexToAdd = mid;	
	    	
	    	
	    }else if (low > mid) {
	    	//add right after the mid value
	    	indexToAdd = mid+1;
	    }
	    
	    occs.add(indexToAdd, newOccurrence);
	    
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return result;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		
		ArrayList <String> result = new ArrayList<String>();
		
		ArrayList<Occurrence> list1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> list2 = keywordsIndex.get(kw2);
		
		if((list1 == null) && (list2 == null)) {
			return null;
		}
		int sizeofList1, sizeofList2;
		if(list1 == null) {
			sizeofList1 = 0;
		}else {
			sizeofList1 = list1.size();
		}
		
		if(list2 == null) {
			sizeofList2 = 0;
		}else {
			sizeofList2 = list2.size();
		}
		
		int count = 0; //To keep track of number of documents
		int i = 0; 	//to keep track of list1
		int j = 0;	//to keep track of list 2
		while((count < 5) && !((i >= sizeofList1) && (j>= sizeofList2))) {
			
			if(i >= sizeofList1) {
				String docc = list2.get(j).document;
				boolean check = true;
				int k = 0;
				while(k<result.size()) {
					if(result.get(k).equals(docc)) {
						check = false; break;
					}
					k++;
				}
				if (check == false) {
					j++; continue;
				}else {
					result.add(docc); j++; count++; continue;
				}
				
			}
			
			if(j >= sizeofList2) {
				String docc = list1.get(i).document;
				boolean check = true;
				int k = 0;
				while(k<result.size()) {
					if(result.get(k).equals(docc)) {
						check = false; break;
					}
					k++;
				}
				if (check == false) {
					i++; continue;
				}else {
					result.add(docc); i++; count++; continue;
				}
				
			}
			
			if(list1.get(i).frequency >= list2.get(j).frequency ) {
				String docc = list1.get(i).document;
				boolean check = true;
				int k = 0;
				while(k<result.size()) {
					if(result.get(k).equals(docc)) {
						check = false; break;
					}
					k++;
				}
				if (check == false) {
					i++; continue;
				}else {
					result.add(docc); i++; count++; continue;
				}
			}else {
				
				String docc = list2.get(j).document;
				boolean check = true;
				int k = 0;
				while(k<result.size()) {
					if(result.get(k).equals(docc)) {
						check = false; break;
					}
					k++;
				}
				if (check == false) {
					j++; continue;
				}else {
					result.add(docc); j++; count++; continue;
				}
				
			}
			
			
		}
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return result;
	
	}
}
