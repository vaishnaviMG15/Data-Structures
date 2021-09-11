package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		Queue<Edge> bfsTraversal = new Queue<Edge>();
		boolean[] visited = new boolean[g.members.length];
		Stack<Edge> bfs = new Stack<Edge>();
		
		int sourceIndex = g.map.get(p1);
		int destinationIndex = g.map.get(p2);
		
		//In every edge:
		//v1 = index of predecessor
		//v2 = index of current vertex
		
		//Marking the source as visited
		Edge source = new Edge(sourceIndex, sourceIndex);
		visited[sourceIndex] = true;
		bfs.push(source);
		bfsTraversal.enqueue(source);
		
		while(!bfsTraversal.isEmpty()) {
			Edge e = bfsTraversal.dequeue();
			int indexOfEdge = e.v2;
			Friend ptr = g.members[indexOfEdge].first;
			while(ptr != null) {
				int indexOfCurrentPerson = ptr.fnum;
				if(!visited[indexOfCurrentPerson]) {
					visited[indexOfCurrentPerson] = true;
					Edge n = new Edge(indexOfEdge, indexOfCurrentPerson);
					bfs.push(n);
					bfsTraversal.enqueue(n);
				}
				ptr = ptr.next;
			}
		}
		
		if (!visited[destinationIndex]) {
			return null;
		}
		
		//At this point we know that there is a path between p1 and p2
		
		Stack<Edge> finalBFS = new Stack<Edge>();
		
		int currDestinationIndex = destinationIndex;
		while(!bfs.isEmpty()) {
			Edge currEdge = bfs.peek();
			
			if(!(currEdge.v2 == currDestinationIndex)) {
				bfs.pop();
			}else {
				finalBFS.push(currEdge);
				bfs.pop();
				currDestinationIndex = currEdge.v1;
			}
			
		}
		
		ArrayList<String> result = new ArrayList<String>();
		
		while(!finalBFS.isEmpty()) {
			Edge edge = finalBFS.pop();
			String name = g.members[edge.v2].name;
			result.add(name);
		}
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return result;
	}
	
	
	
	
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		
		Person[] people = g.members;
		
		boolean[] visited = new boolean[people.length];
		
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		
		for (int i = 0; i < people.length; i++ ) {
			
			//if we already visited this person
			if(visited[i] == true) {
				continue;
			}
			
			//If the person is not from school 
			if(people[i].student == false) {
				continue;
			}
			
			if(!(people[i].school.equals(school))) {
				continue;	
			}
			
			//At this point we know that we did not visit this person and he is from school
			//A clique can be formed starting here
			ArrayList<String> subResult = new ArrayList<String>();
			
			//subResult.add(people[i].name);
			
			//visited[i] = true;
			
			//Friend ptr = people[i].first;
			
			cliquesHelper(people[i], subResult, visited, g, school);
				
			
			result.add(subResult);
			
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return result;
		
	}
	
	private static void cliquesHelper(Person p, ArrayList<String> subResult, boolean[] visited, Graph g, String school) {
		boolean check = true;
		for(int i = 0; i < subResult.size(); i++) {
			if(subResult.get(i).equals(p.name)) {
				check = false; break;
			}
		}
		
		if(check) {
			subResult.add(p.name);
			int index = g.map.get(p.name);
			visited[index] = true;
			Friend ptr = p.first;
			for (;ptr!=null; ptr = ptr.next) {
				Person curr = g.members[ptr.fnum];
				if(curr.student == false) {
					continue;
				}
				if (curr.school.equals(school)) {
					cliquesHelper(curr, subResult, visited, g, school);
				}
				

			}
			
		}
		
		
		
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		ArrayList<String> result = new ArrayList<String>();
		
		boolean[] visited = new boolean[g.members.length];
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		
		//The following is an array of size 1 that has stores the value of dfsnum.
		
		
		//The following would be the driver
		
		for (int i = 0; i < g.members.length; i++ ) {
			if (!visited[i]) {
				
				int d = 1;
				DFS(i, i, d, visited, dfsnum, back, result, g);	
				
			}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return result;
		
	}
	
	private static void DFS(int v, int start, int d, boolean[] visited, int[] dfsnum, int[] back, ArrayList<String> result, Graph g ) {
		
		
		visited[v] = true;
		dfsnum[v] = d; back[v] = d; 
		
		
		Friend ptr = g.members[v].first;
		for(; ptr!=null; ptr = ptr.next) {
			int w = ptr.fnum;
			
			if(!visited[w]) {
				
				DFS(w, start, ++d, visited, dfsnum, back, result, g);
				
				if(dfsnum[v] > back[w]) {
					back[v] = Math.min(back[v], back[w]);
				}else {
					
					if(v != start || ((dfsnum[w] - back[w]) > 1)) {
						boolean check = true;
						
						for (int j = 0; j < result.size(); j++) {
							if (result.get(j).equals(g.members[v].name)) {
								check = false; break;
							}
						}
						if(check) {
							result.add(g.members[v].name);
						}
					}									
				}				
			}else {
				back[v] = Math.min(back[v], dfsnum[w]);
				
			}
		}
				
	}
}

