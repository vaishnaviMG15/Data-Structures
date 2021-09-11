package friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FriendsApp {

	static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		System.out.print("Enter the docs file: ");
		String docsFile = stdin.nextLine();
		Scanner sc;
		
		try {
			sc = new Scanner(new File(docsFile));
			}catch(FileNotFoundException e){
				System.out.println("File not found");
				return;
			}
		
		Graph g = new Graph(sc);
		/*
		String p1 = "nick";
		String p2 = "samir";
		
		ArrayList<String> path = Friends.shortestChain(g, p1, p2);
		
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i) + " -> ");
		}
		System.out.println();
		
		*/
		String school = "cornell";
		ArrayList<ArrayList<String>> cliques = Friends.cliques(g, school);
		
		for(int i = 0; i < cliques.size(); i++ ) {
			ArrayList<String> smallClique = cliques.get(i);
			for (int j = 0; j < smallClique.size(); j++) {
			System.out.print(smallClique.get(j) + ",  ");	
			}
			System.out.println();
		}
		
		/*
		ArrayList<String> connectors = Friends.connectors(g);
		
		System.out.println(connectors.size());
		
		for(int i = 0; i < connectors.size(); i++) {
			System.out.print(connectors.get(i) + ",  ");
		}
		System.out.println();
		*/

	}
	
	
	
}
