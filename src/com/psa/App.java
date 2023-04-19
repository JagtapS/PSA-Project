package com.psa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import com.psa.model.Christofides;
import com.psa.model.Edge;
import com.psa.model.Graph;
import com.psa.model.Node;

import main.java.FileIO;
import main.java.Tour;
import main.java.Town;

public class App {
	public static final double BETA = 2.0;
	public static final double PO = 0.1;
	public static double Q_0;
	public static int T_MAX;
	public static int cl = 15;
	public static Town[] towns;
	public static int n;
	public static final int m = 15;
	public static double TAU_0;
	public static double THRESH = 0.000001;

	public static double[][] adjacencyMatrix;
	public static double[][] pheromone;
	public static double dist = 0;

	
	
	
	
	
	
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        createGraph();
        
        
        // Ant Colony Optimization
        
    	FileIO reader = new FileIO();
		String name = "PSADatasets.csv";
		Q_0 = 0.8;
		T_MAX = 10000;

		String[] twns = reader.load(name);

		n = twns.length;

		towns = new Town[n];

		for (int i = 0; i < n; i++) {
			String[] parts = twns[i].split(",");
			towns[i] = new Town(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]),
					Double.parseDouble(parts[3]));
		}

		adjacencyMatrix = new double[n][n];
		pheromone = new double[n][n];

		// fill the adjacency matrix
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j) {
					adjacencyMatrix[i][j] = towns[i].distanceTo(towns[j]);
				} else
					adjacencyMatrix[i][j] = 0.0;
			}
		}

		// a pretty good tour to start with
		Tour nn = Tour.nearestNeighbour();

		double shortest = nn.distance;
		double secondBest = Double.MAX_VALUE;

		Tour globalBest = nn;
		double globalShortest = shortest;

		Tour best = nn;
		Tour second = nn;
		TAU_0 = Math.pow(n * nn.distance, -1);
		Town[][] candidateLists = new Town[n][cl];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j)
					pheromone[i][j] = TAU_0;
			}
			candidateLists[i] = towns[i].candidates();
		}

		Random gen = new Random();

		for (int t = 0; t < T_MAX; t++) {
			// all my tours
			Tour[] t_k = new Tour[m];

			for (int k = 0; k < m; k++) {
				// all my ants

				// build tour Tkt by applying n-1 times the following steps
				t_k[k] = new Tour(towns[(int) (Math.random() * n)]);

				for (int i = 0; i < n - 1; i++) {

					Town[] candidates = candidateLists[t_k[k].current().id - 1];
					Town next;

					if (numUnvisited(t_k[k], candidates) > 0) {

						Town[] unvisited = getUnvisited(t_k[k], candidates);

						double q = gen.nextDouble();
						;

						try {
							if (args[3].equals("-r"))
								q = Math.random();
						} catch (Exception e) {

						}

						int index;

						if (q <= Q_0)
							index = maxArg(unvisited, t_k[k].current());
						else
							index = probability(unvisited, t_k[k].current()); // probability formula

						next = unvisited[index];

					} else {

						next = t_k[k].nextClosest();
					}

					applyPheromone(t_k[k].current(), next);
					t_k[k].visit(next);

				}

				t_k[k].opt3();

//				double dist = 0;
				// for all my ants see if they produces a better tour
//				double sum = 0;
				if (shortest - t_k[k].distance >= THRESH) {
					shortest = t_k[k].distance;
					best = t_k[k];

					System.out.println("new best \t" + shortest*1000 + "\t" + t); //distance in meters

				} else if (secondBest - t_k[k].distance >= THRESH) {
					secondBest = t_k[k].distance;
					second = t_k[k];
				}
//				} else if (t_k[k].distance > longest) {
//					longest = t_k[k].distance;
//					Tour worst = t_k[k];
//
//				}

			}
			
			
			// update the best 2 tours
			for (int i = 0; i < n; i++) {
				int from = best.visited[i].id - 1;
				int to = best.visited[i + 1].id - 1;
				double old = pheromone[from][to];
				pheromone[from][to] = (1 - PO) * old + (PO * (1.0 / shortest));
				pheromone[to][from] = pheromone[from][to];

				from = second.visited[i].id - 1;
				to = second.visited[i + 1].id - 1;
				old = pheromone[from][to];
				pheromone[from][to] = (1 - PO) * old + (PO * (1.0 / shortest));
				pheromone[to][from] = pheromone[from][to];
				
				
			} // end update
			
		} // end t max
		dist+=shortest;
		
		System.out.println("Ant Colony Optimization Execution: ");
		System.out.println("Ant Colony Optimization Tour Length: " + dist*1000);
		System.out.println("values are \n" + best + "\n"); // This is the last line
		
        
        
        
    }

    public static void createGraph() {
        Graph graph = getNodesFromCSV();
        graph.connectAllNodes();
        System.out.println("Algo Start");
        List<Edge> mst = Christofides.findMST(graph);//mst list
//        System.out.println("MST Length: " + Christofides.calculateTourLength(m));
        System.out.println("Minimum Spanning Tree Size :" + mst.size()); //printed the size of mst list
        List<Node> oddDegrNodes = Christofides.findOddDegreeVertices(graph, mst);
        List<Edge> perfectMatchingEdges = Christofides.getMinimumWeightPerfectMatching(oddDegrNodes);
        System.out.println("Perfect Matching Edges Size :" + perfectMatchingEdges.size()); ////printed the size of perfect matching edges list
        List<Node> eulerTour = Christofides.eulerTour(graph, mst, perfectMatchingEdges);
        System.out.println("Euler Tour Size :" + eulerTour.size()); //printed the size of euler tour list
        List<Node> hamiltonCycle = Christofides.generateTSPtour(eulerTour);
        System.out.println("TSP Hamilton Cycle Size :" + hamiltonCycle.size()); //printed the size of hamiltonion cycle list
        //System.out.println("TSP Hamilton Cycle Size :" + hamiltonCycle.get(0).getCrimeId()); //printed the size of hamiltonion cycle list
        //System.out.println("TSP Hamilton Cycle Size :" + hamiltonCycle.get(hamiltonCycle.size()-1).getCrimeId()); //printed the size of hamiltonion cycle list
        
        System.out.println("Hamilton Tour Length :" + Christofides.calculateTourLength(hamiltonCycle)); // Printing the tour length of hamiltoncycle
        
        //Optimizations
        //1. Tactical Optimization
	        
        //Random Swapping Optimization
	        List<Node> randomTour = Christofides.randomSwapping(hamiltonCycle);
	        System.out.println("TSP Random Swap Tour Optimisation :" + randomTour.size()); //Printing the size of random tour list
	        System.out.println("Random Tour Length :" + Christofides.calculateTourLength(randomTour));
	        
	    //Two Optimization
	        List<Node> twoOptTour = Christofides.twoOpt(randomTour);
	        System.out.println("TSP Two Opt Tour Optimisation Size: " + twoOptTour.size());
	        System.out.println("TSP 2 Opt Tour Length : " + Christofides.calculateTourLength(twoOptTour));
	    
	    //2. Strategic Optimization
	        
	    //Simulated Annealing Optimization
	        List<Node> simulatedAnnealing = Christofides.generateSimulatedAnnealingTSPtour(eulerTour);
	        System.out.println("TSP SimulatedAnnealing Size :" + simulatedAnnealing.size());
	        System.out.println("Simulated Annealing Hamilton Tour Length :" + Christofides.calculateTourLength(simulatedAnnealing));
	       
	   
	        System.out.println("Ant Colony Optimization Execution: ");
    
    }
    // Ant Colony Optimization
    	
	public static int numUnvisited(Tour tr, Town[] candidates) {
		int count = 0;
		int len = candidates.length;
		for (int i = 0; i < len; i++) {
			if (!tr.visited(candidates[i])) {
				count++;
			}
		}
		return count;
	}

	public static Town[] getUnvisited(Tour tr, Town[] candidates) {
		Town[] t = new Town[numUnvisited(tr, candidates)];
		for (int i = 0, j = 0; i < candidates.length; i++) {
			if (!tr.visited(candidates[i])) {
				t[j] = candidates[i];
				j++;
			}
		}
		return t;
	}

	public static void applyPheromone(Town from, Town to) {
		final int OFFSET = 1;
		double old = pheromone[from.id - OFFSET][to.id - OFFSET];
		double newPheromone = (1 - PO) * old + (PO * TAU_0);
		pheromone[from.id - OFFSET][to.id - OFFSET] = newPheromone;
		pheromone[to.id - OFFSET][from.id - OFFSET] = newPheromone;
	}

	public static int maxArg(Town[] candidateList, Town i) {
		int len = candidateList.length;
		double maxArg = -10.0;
		int maxIndex = -1;
		for (int j = 0; j < len; j++) {
			double arg = ph(i, candidateList[j]);
			if (arg > maxArg) {
				maxArg = arg;
				maxIndex = j;
			}
		}
		return maxIndex;
	}

	public static double summation(Town[] candidateList, Town i) {
		int len = candidateList.length;
		double sum = 0.0;
		for (int j = 0; j < len; j++) {
			sum += ph(i, candidateList[j]);
		}
		return sum;
	}

	public static double ph(Town i, Town u) {
		double pheromoneValue = pheromone[i.id - 1][u.id - 1];
		double distanceInverse = 1.0 / adjacencyMatrix[i.id - 1][u.id - 1];
		return pheromoneValue * Math.pow(distanceInverse, BETA);
	}

	private static final int POT_SIZE = 1000;

	public static int probability(Town[] candidateList, Town i) {
		int[] pot = new int[POT_SIZE];
		double summation = summation(candidateList, i);
		int start = 0;

		for (int j = 0; j < candidateList.length; j++) {
			double p = ph(i, candidateList[j]) / summation;
			int add = (int) (POT_SIZE * p);

			if (add > 0 && start < POT_SIZE) {
				for (int k = 0; k < add && start < POT_SIZE; k++) {
					pot[start] = j;
					start++;
				}
			}
		}

		int r = (int) (Math.random() * POT_SIZE);
		return pot[r];
	}
    
    
    
    

    public static Graph getNodesFromCSV() {
        Graph graph = new Graph();
        String line = "";
        String splitBy = ",";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("info6205.spring2023.teamproject.csv"));
        	//BufferedReader reader = new BufferedReader(new FileReader("CrimeSamples.csv"));
        	//BufferedReader reader = new BufferedReader(new FileReader("/Users/salonitalwar/Documents/SEMESTER2-PSA/Project/PSA-Project/PSA-Christofies-Run/CrimeDataset.csv"));
            reader.readLine();
            int count = 0;
            while ((line = reader.readLine()) != null && count < 2000) {
                count += 1;
                String[] node = line.split(splitBy);
                // System.out.println(node.length);
                if(node.length>0 && !(node[0].isEmpty() || node[1].isEmpty() || node[2].isEmpty())){
                    Node n = new Node(node[0], Double.parseDouble(node[1]), Double.parseDouble(node[2]));
                    graph.addNode(n);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return Math.sqrt(distance);
    }
}