
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class homework {
	
	public static List<List<Long>> bfs(int W, int H, int Land_x, int Land_y, 
			long max_limit, int[][] target, int[][] map_value) {
		
		List<List<Long>> path = new ArrayList<List<Long>> ();
		List<Long> tar_path;
		for (int i = 0; i < target.length; i++) {
			tar_path = new ArrayList<Long> ();
			path.add(tar_path);
		}
		
		//{M1{H1[index1,index2], H2[index1,index2, ..], H3..}}
		List<List<Long>> pred = new ArrayList<List<Long>> (W);
		List<Long> pred_node;
		for (int i = 0; i < W; i++) {
			pred_node = new ArrayList<Long> (H);
			for (int j = 0; j < H; j++) {
				pred_node.add((long)-1);
			}
			pred.add(pred_node);
		}
		
//		System.out.println(pred);
		
		long[] tar_idx = new long[target.length];
		boolean[] tar_found = new boolean[target.length];
		
		for (int i = 0; i < target.length; i++) {
			tar_idx[i] = target[i][0] * (long)H + target[i][1];
			tar_found[i] = false;
		}
		
		long index = 0;
		Queue<Long> q = new LinkedList<Long> (); // Store INDEX OF node
		boolean[][] visited = new boolean[W][H];
		long[][] dist = new long[W][H]; 
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				visited[i][j] = false;
				dist[i][j] = Integer.MAX_VALUE;
			}
		}
		
		// Save the Landing site
		long same;
		long size = W * H;
		long orig;
		long temp_idx;
		orig = (long)Land_x * (long)H + (long)Land_y;

		visited[Land_x][Land_y] = true;
		dist[Land_x][Land_y] = 0;
		q.add(orig);
		
		
		int temp_x;
		int temp_y;
		int num_in_tar;
		long pare;
		int par_x;
		int par_y;
		int count = 0;
		
		while(q.peek() != null) {
			temp_idx = q.remove();	
			temp_x = (int)(temp_idx / H);
			temp_y = (int)(temp_idx % H);
//			System.out.println(temp_x + " " + temp_y);
			// Find in target list
			num_in_tar = find_idx(tar_idx, temp_idx); // whether the index exist in target
//			System.out.println(num_in_tar);
			
			if (num_in_tar != -1) {
				tar_found[num_in_tar] = true;


				path.get(num_in_tar).add(temp_idx);
				pare = pred.get(temp_x).get(temp_y);
				
				while (pare != -1) {
					
					path.get(num_in_tar).add(pare);			
					par_x = (int) (pare / H);
					par_y = (int) (pare % H);	
					pare = pred.get(par_x).get(par_y);
//					System.out.print("Parent: ");
//					System.out.println(par_x + " " + par_y);				
//					System.out.println(path);								
				}
				count++;
				if (count == target.length) {
					break;
				}
			}
//			
//			System.out.println(q);	
			for (int z = temp_x - 1; z <= temp_x + 1; z++) {
				for (int b = temp_y - 1; b <= temp_y + 1; b++) {
					same = z * (long)H + b;
					if (0 <= z && z < W && 0 <= b && b < H && same != temp_idx &&
							max_limit >= Math.abs((long)map_value[temp_x][temp_y] 
									- (long)map_value[z][b])) {
						if (visited[z][b] == false) {
							visited[z][b] = true;
							dist[z][b] = dist[temp_x][temp_y] + 1; // Add a cost 
							pred.get(z).set(b, temp_idx);
							q.add(same);
						}
					}
				}
			}
		}
		return path; // target -> landing site
	}
	
	
	//For Priority Queue sort
	static class node {
		long id;
		long val;
		public node(long id, long val) {
			this.id = id;
			this.val = val;
		}
		public long getid() {
			return id;
		}
		public long getval() {
			return val;
		}
	}
	
	public static List<List<Long>> ucs(int W, int H, int Land_x, int Land_y, 
			long max_limit, int[][] target, int[][] map_value) {
		
		List<List<Long>> path = new ArrayList<List<Long>> ();
		List<Long> tar_path;
		for (int i = 0; i < target.length; i++) {
			tar_path = new ArrayList<Long> ();
			path.add(tar_path);
		}
		
		//{M1{H1[index1,index2], H2[index1,index2, ..], H3..}}
		List<List<Long>> pred = new ArrayList<List<Long>> (W);
		List<Long> pred_node;
		for (int i = 0; i < W; i++) {
			pred_node = new ArrayList<Long> (H);
			for (int j = 0; j < H; j++) {
				pred_node.add((long)-1);
			}
			pred.add(pred_node);
		}
		PriorityQueue<node> q;
//		System.out.println(pred);
		
		long[] tar_idx = new long[target.length];
		boolean[] tar_found = new boolean[target.length];
		
		for (int i = 0; i < target.length; i++) {
			tar_idx[i] = target[i][0] * (long)H + target[i][1];
		}
		
		long index = 0;
		
		q = new PriorityQueue<node>(new Comparator<node>() {
			
				@Override
				public int compare(node n1, node n2) {
					return (n1.getval() > n2.getval())? 1: -1;
				}
			}
		);
		
		boolean[][] visited = new boolean[W][H];
		long[][] dist = new long[W][H]; 
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				visited[i][j] = false;
				dist[i][j] = Integer.MAX_VALUE; // Initial the cost of every node
			}
		}
		
		// Save the Landing site
		long same;
		long size = W * H;
		long orig;
		
		orig = (long)Land_x * (long)H + (long)Land_y;

		visited[Land_x][Land_y] = true;
		dist[Land_x][Land_y] = 0;

		q.add(new node(orig, dist[Land_x][Land_y]));
		
		int temp_x;
		int temp_y;
		int num_in_tar;
		long pare;
		int par_x;
		int par_y;
		int count = 0;
		long temp_cost;
		long temp_idx;
		node temp_n;
		while(q.peek() != null) {
			
			temp_n = q.remove();
			temp_idx = temp_n.getid();
			temp_cost = temp_n.getval();
//			System.out.println("node: " + temp_idx + " " + temp_cost);
			
			temp_x = (int)(temp_idx / H);
			temp_y = (int)(temp_idx % H);
			dist[temp_x][temp_y] = temp_cost;
//			System.out.println(temp_x + " " + temp_y);
			// Find in target list
			num_in_tar = find_idx(tar_idx, temp_idx); // whether the index exist in target
//			System.out.println(num_in_tar);
			
			if (num_in_tar != -1) {
				tar_found[num_in_tar] = true;
//				for (int k = 0; k < target.length; k++) {
//					System.out.println(tar_found[k]);
//				}

				path.get(num_in_tar).add(temp_idx);
				pare = pred.get(temp_x).get(temp_y);
				
				while (pare != -1) {
					
					path.get(num_in_tar).add(pare);			
					par_x = (int) (pare / H);
					par_y = (int) (pare % H);	
					pare = pred.get(par_x).get(par_y);
//					System.out.print("Parent: ");
//					System.out.println(par_x + " " + par_y);				
//					System.out.println(path);								
				}
				count++;
				if (count == target.length) {
					break;
				}
			}
//			System.out.println(q);	
			for (int z = temp_x - 1; z <= temp_x + 1; z++) {
				for (int b = temp_y - 1; b <= temp_y + 1; b++) {
					same = z * (long)H + b;
					if (0 <= z && z < W && 0 <= b && b < H && same != temp_idx &&
							max_limit >= Math.abs((long)map_value[temp_x][temp_y] 
									- (long)map_value[z][b])) {
						if (visited[z][b] == false) {
							visited[z][b] = true;
							if (Math.abs(z - temp_x) + Math.abs(b - temp_y) == 2) {
								temp_cost = dist[temp_x][temp_y] + 14;
							}
							else {
								temp_cost = dist[temp_x][temp_y] + 10; // Add a cost 
							}
							pred.get(z).set(b, temp_idx);
							q.add(new node(same, temp_cost));
						}
					}
				}
			}
		}
		return path; // target -> landing site
	}
	
	
	// A* Algorithms
	
	public static List<List<Long>> astar(int W, int H, int Land_x, int Land_y, 
			long max_limit, int[][] target, int[][] map_value) {
		
		//Inital Once
		List<List<Long>> path = new ArrayList<List<Long>> ();
		List<Long> tar_path;
		for (int i = 0; i < target.length; i++) {
			tar_path = new ArrayList<Long> ();
			path.add(tar_path);
		}
		
		List<List<Long>> pred;
		List<Long> pred_node;
		
		// Initial Once
		long[] tar_idx = new long[target.length];
		boolean[] tar_found = new boolean[target.length];
		
		for (int i = 0; i < target.length; i++) {
			tar_idx[i] = target[i][0] * (long)H + target[i][1];
			tar_found[i] = false;
		}
		
		//Initial once 
		long index = 0;
		long same;
		long size = W * H;
		long orig;
		
		PriorityQueue<node> q;
		boolean[][] visited;
		long[][] dist;
		
		int temp_x;
		int temp_y;
		long target_i;
		long pare;
		int par_x;
		int par_y;
		int count = 0;
		long temp_cost;
		long temp_idx;
		node temp_n;
		node temp_r;
		int tar_x;
		int tar_y;
		long h_cost;
		
		
		// L O O P CONTENT
		for (int num = 0; num < target.length; num++) {
			target_i = tar_idx[num];
			tar_x = target[num][0];
			tar_y = target[num][1];
			
			// Initial pred store
			pred = new ArrayList<List<Long>> (W);
			for (int i = 0; i < W; i++) {
				pred_node = new ArrayList<Long> (H);
				for (int j = 0; j < H; j++) {
					pred_node.add((long)-1);
				}
				pred.add(pred_node);
			}
			
			// Initial Queue for target_i
			q = new PriorityQueue<node>(new Comparator<node>() {
					
					@Override
					public int compare(node n1, node n2) {
						return (n1.getval() > n2.getval())? 1: -1;
					}
				}
			);
			
			// Initial visited and dist
			visited = new boolean[W][H];
			dist = new long[W][H]; 
			for (int i = 0; i < W; i++) {
				for (int j = 0; j < H; j++) {
					visited[i][j] = false;
					dist[i][j] = Integer.MAX_VALUE; // Initial the cost of every node
				}
			}	
			// Save the Landing site
			orig = (long)Land_x * (long)H + (long)Land_y;

			visited[Land_x][Land_y] = true;
			dist[Land_x][Land_y] = 0;
			
			q.add(new node(orig, dist[Land_x][Land_y]));
			while (q.peek() != null) {
				temp_r = q.remove();
				q.add(temp_r);
				temp_n = q.remove();
				temp_idx = temp_n.getid();
				temp_cost = temp_n.getval();
				temp_x = (int)(temp_idx / H);
				temp_y = (int)(temp_idx % H);
				dist[temp_x][temp_y] = temp_cost;
				
				if (temp_idx == target_i) {
					tar_found[num] = true;
					
					// SAVE THE PATH
					path.get(num).add(temp_idx);
					pare = pred.get(temp_x).get(temp_y);
					
					while (pare != -1) {
						
						path.get(num).add(pare);			
						par_x = (int) (pare / H);
						par_y = (int) (pare % H);	
						pare = pred.get(par_x).get(par_y);
//						System.out.print("Parent: ");
//						System.out.println(par_x + " " + par_y);				
//						System.out.println(path);								
					}
					continue;
				}
				else {
					for (int z = temp_x - 1; z <= temp_x + 1; z++) {
						for (int b = temp_y - 1; b <= temp_y + 1; b++) {
							same = z * (long)H + b;
							if (0 <= z && z < W && 0 <= b && b < H && same != temp_idx &&
									max_limit >= Math.abs((long)map_value[temp_x][temp_y] 
											- (long)map_value[z][b])) {
								if (visited[z][b] == false) {
									visited[z][b] = true;
									if (Math.abs(z - temp_x) + Math.abs(b - temp_y) == 2) {
										temp_cost = dist[temp_x][temp_y] + 14 + Math.abs(map_value[z][b] - map_value[temp_x][temp_y]) + heur_cal(z, b, tar_x, tar_y, map_value);
									}
									else {
										temp_cost = dist[temp_x][temp_y] + 10 + Math.abs(map_value[z][b] - map_value[temp_x][temp_y]) + heur_cal(z, b, tar_x, tar_y, map_value); // Add a cost 
									}
									pred.get(z).set(b, temp_idx);
									q.add(new node(same, temp_cost));
								}
							}
						}
					}
				}
			}
		}
		return path; // target -> landing site
	}
	
	public static long heur_cal(int curr_x, int curr_y, int tar_x, int tar_y, int[][] map_value) { 
		long h_cost = (long)Math.sqrt((curr_x - tar_x) * (curr_x - tar_x) + (curr_y - tar_y) * (curr_y - tar_y)) * 10 +  Math.abs(map_value[curr_x][curr_y] - map_value[tar_x][tar_y]);
		return h_cost;
	}
					
	public static int find_idx(long[] tar_idx, long tar) {
		for (int i = 0; i < tar_idx.length; i++) {
			if (tar_idx[i] == tar) {
				return i;
			}
		}
		return -1;
	}
	
	//Print 2D array
    public static void print2D(int mat[][]) 
    { 
        // Loop through all rows 
        for (int i = 0; i < mat.length; i++) {
        	for (int j = 0; j < mat[i].length; j++) {
        		System.out.print(mat[i][j] + " ");
        	}
        	System.out.println();
        }
    }
    
	public static void main(String[] args) throws Exception {
//		System.out.println(new Date());
//		File file = new File(".");
//		for(String fileNames : file.list()) System.out.println(fileNames);
		
		File f = new File("input.txt");
		Scanner sc = new Scanner(f);
		
		// Read the Type of Search
		String Type = sc.nextLine();
//		System.out.println("Searching: " + Type);
		
		// Get W and H
		int W;
		int H;
		W = sc.nextInt();
		H = sc.nextInt();
		sc.nextLine();
//		System.out.println("W: " + W + " H: " + H);
		
		// Get Landing spot
		int Land_x;
		int Land_y;
		Land_x = sc.nextInt();
		Land_y = sc.nextInt();
		sc.nextLine();
//		System.out.println("Landing site: " + Land_x + " " + Land_y);
		
		// Get the limit difference of rover
		long max_limit;
		max_limit = sc.nextLong();
		sc.nextLine();
//		System.out.println("Max Limitation: " + max_limit);
		
		// Get the number of target spots 
		int n_tar;
		n_tar = sc.nextInt();
		sc.nextLine();
//		System.out.println("The number of targets: " + n_tar);
		
		// Get all the spots of targets
		int[][] target = new int[n_tar][2];
		for (int i = 0; i < n_tar; i++) {
			target[i][0] = sc.nextInt();
			target[i][1] = sc.nextInt();
			sc.nextLine();
		}
//		System.out.println("Target sites: ");
//		print2D(target);
		
		// Get MAP INFO		
		int[][] map_value = new int[W][H];
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++ ) {
				map_value[j][i] = sc.nextInt();
			}
			if (i < H - 1) {
				sc.nextLine();
			}
		}
//		System.out.println("Map Values: ");
//		print2D(map_value);
		sc.close();
//		
//		System.out.println(Type.length());
//		System.out.println("A*".toString().length());
		
		PrintStream output = new PrintStream(new File("output.txt"));
		System.setOut(output);
		
		if (Type.equals("BFS".toString())) {
			List<List<Long>> path_bfs = ucs(W, H, Land_x, Land_y, 
					max_limit, target, map_value);
//			System.out.println("BFS: ");
			for (int i = 0; i < path_bfs.size();i++) {
				if (path_bfs.get(i).size() > 0) {
					for (int j = path_bfs.get(i).size() - 1; j >= 0; j--) {
						System.out.print((path_bfs.get(i).get(j) / H) + "," + (path_bfs.get(i).get(j) % H));
						if (j != 0) {
							System.out.print(" ");
						}
						else {
							if (i != path_bfs.size() - 1) {
								System.out.println();
							}
						}
					}
				}
				else {
					if (i != path_bfs.size() - 1) {
						System.out.println("FAIL");
					}
					else {
						System.out.print("FAIL");
					}
				}
			}
		}

		else if (Type.equals("UCS".toString())) {
			List<List<Long>> path_ucs = ucs(W, H, Land_x, Land_y, 
					max_limit, target, map_value);
//			System.out.println("UCS: ");
			for (int i = 0; i < path_ucs.size();i++) {
				if (path_ucs.get(i).size() > 0) {
					for (int j = path_ucs.get(i).size() - 1; j >= 0; j--) {
						System.out.print((path_ucs.get(i).get(j) / H) + "," + (path_ucs.get(i).get(j) % H));
						if (j != 0) {
							System.out.print(" ");
						}
						else {
							if (i != path_ucs.size() - 1) {
								System.out.println();
							}					
						}
					}
				}
				else {
					if (i != path_ucs.size() - 1) {
						System.out.println("FAIL");
					}
					else {
						System.out.print("FAIL");
					}
					
				}

			}
		}
		else if (Type.equals("A*")) {
			List<List<Long>> path_astar = astar(W, H, Land_x, Land_y, 
					max_limit, target, map_value);
//			System.out.println("A*: ");
			for (int i = 0; i < path_astar.size();i++) {
				if (path_astar.get(i).size() > 0) {
					for (int j = path_astar.get(i).size() - 1; j >= 0; j--) {
						System.out.print((path_astar.get(i).get(j) / H) + "," + (path_astar.get(i).get(j) % H));
						if (j != 0) {
							System.out.print(" ");
						}
						else {
							if (i != path_astar.size() - 1) {
								System.out.println();
							}
						}
					}
				}
				else {
					if (i != path_astar.size() - 1) {
						System.out.println("FAIL");
					}
					else {
						System.out.print("FAIL");
					}
				}
			}
		}
		else {
			
		}
//		System.out.println();
//		System.out.println(new Date());
	}
}

