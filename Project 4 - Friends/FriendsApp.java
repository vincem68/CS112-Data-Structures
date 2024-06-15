package friends;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import friends.Friends;
import friends.Graph;

public class FriendsApp {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		Graph g;
		while (true) {
			System.out.print("\nEnter the friends file => ");
			String graph = sc.nextLine();
			if (graph.length() == 0) {
				break;
			} else {
				g = new Graph(new Scanner(new File(graph)));
			}
			
			
			System.out.println("Enter 1, 2, or 3 for corresponding method => ");
			String number = sc.nextLine();
			ArrayList<String> path = new ArrayList<String>();
			ArrayList<ArrayList<String>> group = new ArrayList<ArrayList<String>>();
			ArrayList<String> connect = new ArrayList<String>();
			if (number.equals("1")) {
				System.out.println("Enter starting point => ");
				String p1 = sc.nextLine();
				System.out.println("Enter ending point => ");
				String p2 = sc.nextLine();
				path = Friends.shortestChain(g, p1, p2);
				//System.out.println(path);
			}
			else if (number.equals("2")) {
				System.out.println("Enter the name of the school => ");
				String school = sc.nextLine();
				group = Friends.cliques(g, school);
				//System.out.println(group);
			}
			else if (number.equals("3")) {
				connect = Friends.connectors(g);
			}
			else {
				break;
			}
			
			if (number.equals("1")) {
				if (path == null || path.isEmpty()) {
					System.out.println("No path found");
				} else {
					System.out.println(path);
				}
			}
			else if (number.equals("2")) {
				if (group == null || group.isEmpty()) {
					System.out.println("No cliques found");
				} else {
					System.out.println(group);
				}
			}
			else if (number.equals("3")) {
				if (connect == null || connect.isEmpty()) {
					System.out.println("No connectors");
				} else {
					System.out.println(connect);
				}
			}
		}
		sc.close();
	}

}
