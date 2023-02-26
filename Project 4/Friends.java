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
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		 Queue<Person> queue = new Queue<Person>(); Person found = null;  
		ArrayList<String> tempPath = new ArrayList<String>(); ArrayList<String> finalPath = new ArrayList<String>();
		ArrayList<Person> visited = new ArrayList<Person>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		//this finds the starting point in the graph
	//	int firstPoint = 0;
		for (int i = 0; i < g.members.length; i++) {
			if (g.members[i].name.equals(p1)) {
				if (g.members[i].name.equals(p2)) {
					finalPath.add(g.members[i].name);
					return finalPath;
				} 
			
				queue.enqueue(g.members[i]);
			
				break;
			}
		}
		
		if (queue.isEmpty()) {
			return null;
		}
		//conduct bfs
		
		while (!queue.isEmpty()) {
			Person check = queue.dequeue();
		
			for (Friend cycle = check.first; cycle != null; cycle = cycle.next) {
				int v1 = 0;
				for (int j = 0; j < g.members.length; j ++) {
					if (check.name.equals(g.members[j].name)){
						v1 = j;
						break;
					}
				}
				
				if (g.members[cycle.fnum].name.equals(p2)) {
					found = g.members[cycle.fnum];
					int v2 = cycle.fnum;
					Edge newEdge = new Edge(v1, v2);
					edges.add(newEdge);
					
					break;
				}
				else if (!visited.contains(g.members[cycle.fnum])) {
					queue.enqueue(g.members[cycle.fnum]);
					visited.add(g.members[cycle.fnum]);
					int v2 = cycle.fnum;
					Edge newEdge = new Edge(v1, v2);
					edges.add(newEdge);
				}
			}
			if (found != null) {
				break;
			}
		}
		if (found == null) {
		    return null;
		}
		
		//now backtrack to find the path using edges
		boolean complete = false; 
		Edge first = edges.get(edges.size()-1);
		
		tempPath.add(g.members[first.v2].name);
		tempPath.add(g.members[first.v1].name);
		Edge check3 = first;
		
		if (g.members[check3.v1].name.equals(p1)){
			complete = true;
		}
		
		while (complete != true) {
			for (int i = 0; i < edges.size(); i++) {
				if (check3.v1 == edges.get(i).v2) {
					tempPath.add(g.members[edges.get(i).v1].name);
					check3 = edges.get(i);
					if (g.members[edges.get(i).v1].name.equals(p1)) {
						complete = true;
					}
					break;
				}
			}
		}
		//this puts the strings in the correct order 
		while (!tempPath.isEmpty()) {
			finalPath.add(tempPath.get(tempPath.size()-1));
			tempPath.remove(tempPath.size()-1);
			
		}
		
		
		return finalPath;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		ArrayList<ArrayList<String>> allCliques = new ArrayList<ArrayList<String>>();
		boolean complete = false; ArrayList<Person> visited = new ArrayList<Person>();
		
		//does bfs and only adds to an array list if the person has the same school
		
		while (complete != true) {
			
			ArrayList<String> createdCliques = new ArrayList<String>();  //what we put in larger list
			Queue<Person> traverse = new Queue<Person>(); //used in every bfs
			
			//this finds the starting point in array. If there is nothing or we found them all, loop ends.
			for (int i = 0; i < g.members.length; i++) {
				if (!visited.contains(g.members[i]) && g.members[i].student == true && g.members[i].school.equals(school)) {
					traverse.enqueue(g.members[i]);
					visited.add(g.members[i]);
					break;
				}
			}
			
			//this terminates the loop as we have found all cliques
			if (traverse.isEmpty()) {
				complete = true;
			}
			
			//this does bfs
			while (!traverse.isEmpty()) {
				Person check = traverse.dequeue();
				//visited.add(check);
				createdCliques.add(check.name);
			/*	if (check.student == true && check.school.equals(school)){  // we may have to check again for the visited array
					createdCliques.add(check.name);
				} */
				for (Friend cycle = check.first; cycle != null; cycle = cycle.next) {
					Person check2 = g.members[cycle.fnum];
					if (!visited.contains(check2) && check2.student == true && check2.school.equals(school)) {
						traverse.enqueue(check2);
						visited.add(check2);
					}
				}
			}
			if (!createdCliques.isEmpty()) {
				allCliques.add(createdCliques);
			}
		}
		
		if (allCliques.isEmpty()) {
			return null;
	    }
		return allCliques;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		
		Stack<Person> stack = new Stack<Person>(); ArrayList<Person> visited = new ArrayList<Person>();
		ArrayList<Edge> edges = new ArrayList<Edge>(); ArrayList<Person> actuallyVisited = new ArrayList<Person>();
		ArrayList<String> allConnectors = new ArrayList<String>(); 
		int[] dfs = new int[g.members.length]; int[] back = new int[g.members.length];
		int counter = 0; boolean complete = false; int startCounter = 0; int firstPoint = 0;
		
		
		//put first point in the stack, maybe we can either put in first person or first one with only one neighbor
		
		
		while (complete != true) {
			
			Person start = null; startCounter = 0;
			for (int i = 0; i < g.members.length; i++) {
				if (!visited.contains(g.members[i])){
			    	stack.push(g.members[i]);
			    	start = g.members[i];
			    	visited.add(g.members[i]);
			    	//counter++;
			    	//dfs[i] = counter; back[i] = counter;
			//    	System.out.println("start is "+start.name);
				    break;
				}
	    	}
			
			if (stack.isEmpty()) {
				complete = true;
			}
			
			boolean deadEnd = false;
			int v1 = 0; // int v2 = 0;//change here it used to be 0 no int v2
			while (!stack.isEmpty()) {
				
				//this goes down an unvisited path to give each person their initial values
				
				Person check = stack.pop();
				if (actuallyVisited.contains(check)) {
					continue;
				}
	/*			for (int o = 0; o < g.members.length; o++) { //start
					if (check.equals(g.members[o])) {
						v2 = o;
						break;
					}
				}
				if (v1 >= 0) {
					
					Edge newEdge = new Edge(v1, v2);
					edges.add(newEdge);
				}
				//end  */
				actuallyVisited.add(check);
				counter++;
				 
				
				for (int j = 0; j < g.members.length; j++) {
					if (check.equals(g.members[j])) {
						dfs[j] = counter; back[j] = counter;
	//					System.out.println("dfs and back are "+counter);
						v1 = j;
						firstPoint = j;
			//			System.out.println("firstPoint and v1 are "+g.members[j].name);
						break;
					}
				}
				
				//this adds any unvisited friends of the person into the stack
			    boolean idk = false; //boolean alreadyIn = false; 
				for (Friend cycle = check.first; cycle != null; cycle = cycle.next) {
					if (!visited.contains(g.members[cycle.fnum])) {                //change here today
						stack.push(g.members[cycle.fnum]);
						visited.add(g.members[cycle.fnum]);
					/*	if (alreadyIn == false) {   // change here
						int v2 = cycle.fnum;
						Edge newEdge = new Edge(v1, v2);
						edges.add(newEdge); 
						alreadyIn = true;
						} */
						if (!actuallyVisited.contains(g.members[cycle.fnum])) {
						   int v2 = cycle.fnum;                                 
							Edge newEdge = new Edge(v1, v2);
							edges.add(newEdge); 
							idk = true;
						}
					}
				}
		//		v1 = v2;
				// this tells us if we reached a dead end as we visited all this person's friends as we added nothing
				if (idk == false) {
					deadEnd = true;
				}
			//	System.out.println("dead end is "+deadEnd);
				
				//now we backtrack using edges to get to an unvisited person, updating previously visited people
				if (deadEnd == true) {
					boolean foundPath = false; int lastVisited = -1; int look = firstPoint;
					while (foundPath != true) {
			//			System.out.println("last visited is "+lastVisited+ " and look is " +look);
						Edge backtrack = null;
						for (int x = 0; x < edges.size(); x++) { //change here
							if (edges.get(x).v2 == look) {
								backtrack = edges.get(x);
								//edges.remove(x);
							}
						} 
						if (backtrack == null) {
							startCounter++;
			//				System.out.println("start counter is "+startCounter);
							if (startCounter >= 2) {
								if (!allConnectors.contains(start.name)) {
									allConnectors.add(start.name);
			//						System.out.println(start.name+" is a connector");
								}
							}
							foundPath = true;    //change here
							break;
						}
					//	System.out.println(" the edge is "+backtrack.v1+" and "+backtrack.v2+" from v1 to v2");
						
						//this checks if this person is a connector
						if (lastVisited >= 0 && backtrack != null) {
						//	System.out.println("current dfs is " +dfs[backtrack.v2]); System.out.println("back of last one is "+back[lastVisited]);
							if (dfs[backtrack.v2] <= back[lastVisited]) {
						/*		if (start.equals(g.members[backtrack.v2])) {
									startCounter++;
									if (startCounter >= 2) {
										if (!allConnectors.contains(g.members[backtrack.v2].name)) {
											allConnectors.add(g.members[backtrack.v2].name);
										}
									}
								} else  */if (!allConnectors.contains(g.members[backtrack.v2].name)) {
									allConnectors.add(g.members[backtrack.v2].name);
				//					System.out.println(g.members[backtrack.v2].name+" is a connector");
								}
								
					    	}
						}
						
						//this checks if dfs of crnt number is greater than back of last visited 
						if (lastVisited >= 0 && backtrack!= null) {
							if (dfs[backtrack.v2] > back[lastVisited]) {
						}
							back[backtrack.v2] = Math.min(back[backtrack.v2], back[lastVisited]);
				//			System.out.println("since dfsv > backw, backv is "+back[backtrack.v2]);
						}
						int smallest = 1000;  //change here
					
						for (Friend cycle2 = check.first; cycle2 != null; cycle2 = cycle2.next) {
							if (actuallyVisited.contains(g.members[cycle2.fnum]) && backtrack!= null) {
								int small = Math.min(back[backtrack.v2], dfs[cycle2.fnum]);
								if (small < smallest) {
									back[backtrack.v2] = small; 
								}
							}
						}
		//				System.out.println("from all visited neighbors, back v is "+ back[backtrack.v2]);
						if (backtrack != null) {
						lastVisited = backtrack.v2; look = backtrack.v1;
						}
						if (backtrack != null) {
						for (Friend cycle3 = g.members[backtrack.v2].first; cycle3 != null; cycle3 = cycle3.next) {
						if (!stack.isEmpty() && stack.peek().equals(g.members[cycle3.fnum])) {
								foundPath = true;
						} 
			/*				if (!actuallyVisited.contains(g.members[cycle3.fnum])) {      //change here 
								foundPath = true;
								stack.push(g.members[cycle3.fnum]);  
								break;
							}  */
						}
						if (foundPath != true) {    //change here
							edges.remove(backtrack);
						}
						}
					}
					deadEnd = false;
				}
			}
		}
		
		if (allConnectors.isEmpty()) {
			return null;
		}
		
		return allConnectors;
		
	}
}

