package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class lseApp {

	static Scanner stdin = new Scanner(System.in);
	public static void main(String[] args) {
		
		System.out.print("Enter the docs file: ");
		String docsFile = stdin.nextLine();
		System.out.print("Enter the noise file: ");
		String noiseFile = stdin.nextLine();
		
		
		
		LittleSearchEngine l = new LittleSearchEngine();
		
		try {
		l.makeIndex(docsFile, noiseFile);
		}catch(FileNotFoundException e){
			System.out.println("File not found");
			return;
		}
		/*
		ArrayList<Occurrence> al = new ArrayList<Occurrence>();
		al.add(new Occurrence("a", 12));
		al.add(new Occurrence("c", 8));
		al.add(new Occurrence("a", 7));
		al.add(new Occurrence("a", 5));
		al.add(new Occurrence("a", 3));
		al.add(new Occurrence("a", 2));
		al.add(new Occurrence("a", 4));
		
		ArrayList<Integer> r = l.insertLastOccurrence(al);
		for (int i = 0; i<al.size(); i++) {
			System.out.print(al.get(i).frequency + " " );
		}
		
		System.out.println();
		
		for (int i = 0; i<r.size(); i++) {
			System.out.print(r.get(i) + " " );
		}
		
		
		HashMap<String,Occurrence> h;
		
		try {
			h =  l.loadKeywordsFromDocument("AliceCh1.txt");
			}catch(FileNotFoundException e){
				System.out.println("File not found");
				return;
			}
		for(String e: h.keySet()) {
			System.out.println(e + " :" + h.get(e).document + " :" +h.get(e).frequency );
			
		}
		
		
		
		String w1 = l.getKeyword("say");
		String w2 = l.getKeyword("say.)");
		String w3 = l.getKeyword("say,");
		
		
		
		System.out.println(w1);
		System.out.println(w2);
		System.out.println(w3);
		
		
		*/
		
		for(String e: l.keywordsIndex.keySet()) {
			ArrayList<Occurrence> list = l.keywordsIndex.get(e);
			System.out.print(e + ": ");
			int i = 0;
			while(i<list.size()) {
				String doc = list.get(i).document;
				int freq = list.get(i).frequency;
				System.out.print("(" + doc + " , " + freq + ")" + " " );
				i++;
				
			}
			System.out.println();
		}
		
		/*
		ArrayList<String> searchFiles = l.top5search("deep", "world");
		for(int i = 0; i<searchFiles.size(); i++) {
			System.out.print(searchFiles.get(i) + "  ");
		}
		
		*/
		
	}
	
}
