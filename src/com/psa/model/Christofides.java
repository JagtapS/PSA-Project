package com.psa.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.psa.util.DeepCopyUtil;


public class Christofides {

	
    public static List<Edge> findMST(Graph graph) {
        return graph.kruskalMST();
    }

    /*
     * code to compute the degree of each vertex in a graph by iterating through the edges in its minimum spanning tree 
     * returns - the list of vertices with odd degree
     */
    public static List<Node> findOddDegreeVertices(Graph graph, List<Edge> mst) {
    	//degree- to keep track of the degree of each vertex in the graph.
        Map<Node, Integer> degrees = new HashMap<>();
        for (Node node : graph.nodes) {
        	//The initial degree of each vertex is set to 0
            degrees.put(node, 0);
        }
        //method iterates through the edges in the MST and
        //increments the degree of each endpoint of the edge by 1
        for (Edge edge : mst) {
            degrees.put(edge.source, degrees.get(edge.source) + 1);
            degrees.put(edge.destination, degrees.get(edge.destination) + 1);
        }
        List<Node> oddDegreeNodes = new ArrayList<>(); //Empty arraylist to store the vertices with odd degree
        //Iterates through the entries in the "degrees" HashMap and 
        //adds any vertex with odd degree to the "oddDegreeNodes" ArrayList
        for (Map.Entry<Node, Integer> entry : degrees.entrySet()) {
            if (entry.getValue() % 2 != 0) {
                oddDegreeNodes.add(entry.getKey());
            }
        }
        return oddDegreeNodes; //returns the "oddDegreeNodes" ArrayList containing the vertices with odd degree
    }

    public static List<Edge> getMinimumWeightPerfectMatching(List<Node> oddDegreeNodes) {
    	//creates a new Graph object called "subgraph" containing only the nodes in the "oddDegreeNodes" list
        Graph subgraph = new Graph(oddDegreeNodes);
        //connects all nodes in the "subgraph" object with edges to create a complete subgraph
        subgraph.connectAllNodes();
        
        //returns the minimum weight perfect matching of the "subgraph" object
        return subgraph.getMinimumWeightPerfectMatching();
    }

    public static List<Node> eulerTour(Graph graph, List<Edge> mst, List<Edge> perfEdges) {
    	//initializing an empty ArrayList "eulerTour" to store the nodes visited during the Euler tour
        List<Node> eulerTour = new ArrayList<>();
        //creating a new ArrayList "combinEdges" to combines the edges in the MST and the minimum weight perfect matching
        List<Edge> combinEdges = new ArrayList<>(mst);
        //addAll minimum weight perfect matching edges
        combinEdges.addAll(perfEdges);
        //creating Map "adjacencyMatrix" containing the adjacency list of each node in the graph
        Map<Node, List<Node>> adjacenyMatrix = graph.adjacencyMatrix();
        //creating empty HashSet "visited" to keep track of visited nodes during the tour.
        Set<Node> visited = new HashSet<>();
        //selecting the first node in the "combinEdges" list as the starting node for the tour
        Node startNode = combinEdges.get(0).source;
        //traversing the graph and adding nodes to the "eulerTour" list
        dfs(startNode, eulerTour, combinEdges, visited, adjacenyMatrix);
        //Returning "eulerTour" list containing the nodes visited during the Euler tour
        return eulerTour;
    }


    
    public static List<Node> generateTSPtour(List<Node> eulerTour) {
    	//creating a new ArrayList "hamiltonList" to store the nodes in the Hamiltonian cycle
        List<Node> hamiltonList = new ArrayList<>();
        //creating a new HashSet "visited" to keep track of visited nodes
        Set<Node> visited = new HashSet<>();
        //iterating through each node in the "eulerTour" list
        for (Node node : eulerTour) {
        	//For each node in the "eulerTour" list, if the node has not been visited before, 
        	//it is added to the "visited" set and the "hamiltonList" ArrayList
            if (!visited.contains(node)) {
                visited.add(node);
                hamiltonList.add(node);
            }
            
        }
        
        hamiltonList.add(eulerTour.get(0));
        return hamiltonList; //returning "hamiltonList" ArrayList containing the nodes in the Hamiltonian cycle of the graph
    }
       
        
    public static List<Node> generateSimulatedAnnealingTSPtour(List<Node> eulerTour) {
        List<Node> hamiltonList = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        for (Node node : eulerTour) {
            if (!visited.contains(node)) {
                visited.add(node);
                hamiltonList.add(node);
            }
        }
//        return hamiltonList;
        return simulatedAnnealingOptimise(hamiltonList, 10000.0, 0.999, 10000);
    }
    
    public static List<Node> randomSwapping(List<Node> hamiltonCycle) {
        double originalLength = calculateTourLength(hamiltonCycle);
        List<Node> optimizedHamiltonCycle = new ArrayList<>(hamiltonCycle);

        int n = hamiltonCycle.size();
        int maxIterations = 1000;

        for (int i = 0; i < maxIterations; i++) {
            int a = (int) (Math.random() * (n - 1));
            int b = (int) (Math.random() * (n - 1));

            if (a == b) {
                continue;
            }

            if (a > b) {
                int temp = a;
                a = b;
                b = temp;
            }

            List<Node> newHamiltonCycle = new ArrayList<>(optimizedHamiltonCycle);

            // Reverse the sub-tour from index a to index b
            for (int j = a, k = b; j < k; j++, k--) {
                Node temp = newHamiltonCycle.get(j);
                newHamiltonCycle.set(j, newHamiltonCycle.get(k));
                newHamiltonCycle.set(k, temp);
            }

            double newLength = calculateTourLength(newHamiltonCycle);

            // If the new Hamilton cycle is shorter than the original Hamilton cycle,
            // then update the optimized Hamilton cycle
            if (newLength < originalLength) {
                optimizedHamiltonCycle = newHamiltonCycle;
                originalLength = newLength;
                i = 0; // Reset the iteration count
            }
        }

        return optimizedHamiltonCycle;
    }

    public static List<Node> twoOpt(List<Node> tour) {
        List<Node> newTour = new ArrayList<>(tour);
        int size = newTour.size();

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (j - i == 1) continue;
                List<Node> reversedNodes = reverseNodes(newTour, i, j);
                if (getTourLength(reversedNodes) < getTourLength(newTour)) {
                    newTour = reversedNodes;
                }
            }
        }

        return newTour;
    }

    private static List<Node> reverseNodes(List<Node> tour, int i, int j) {
        List<Node> reversedNodes = new ArrayList<>(tour.subList(0, i));
        List<Node> reversedSegment = new ArrayList<>(tour.subList(i, j + 1));
        Collections.reverse(reversedSegment);
        reversedNodes.addAll(reversedSegment);
        reversedNodes.addAll(tour.subList(j + 1, tour.size()));
        return reversedNodes;
    }

    private static double getTourLength(List<Node> tour) {
        double length = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            Node a = tour.get(i);
            Node b = tour.get(i + 1);
            length += Graph.calculateDistance(a, b);
        }
        length += Graph.calculateDistance(tour.get(tour.size() - 1), tour.get(0));
        return length;
    }





    //simulatedAnnealing Optimization
    public static List<Node> simulatedAnnealingOptimise(List<Node> tspTour, double initialTemp, double coolingRate, int stoppingCriterion) {
        List<Node> currentTour = DeepCopyUtil.deepCopy(tspTour);
        double currentTourLength = calculateTourLength(currentTour);
        double temperature = initialTemp;

        for (int i = 0; i < stoppingCriterion; i++) {
            int randomIndexOne = (int) (Math.random() * tspTour.size());
            int randomIndexTwo = (int) (Math.random() * tspTour.size());
            List<Node> newTour = DeepCopyUtil.deepCopy(currentTour);
            swap(newTour, randomIndexOne, randomIndexTwo);
            double newTourLength = calculateTourLength(newTour);
            double deltaE = newTourLength - currentTourLength;

            if (deltaE < 0) {
                currentTour = newTour;
                currentTourLength = newTourLength;
            } else {
                double acceptanceProb = Math.exp(-deltaE / temperature);
                if (Math.random() < acceptanceProb) {
                    currentTour = newTour;
                    currentTourLength = newTourLength;
                }
            }

            temperature *= coolingRate;
        }

        return currentTour;
    }
    


    
    private static void dfs(Node node, List<Node> eulerTour, List<Edge> edges, Set<Node> visited,
            Map<Node, List<Node>> adjacenyMatrix) {
    	//adding the current node to the "visited" set to mark it as visited
        visited.add(node);
        //iterating through the adjacency list of the current node to visit its neighbors
        for (Node v : adjacenyMatrix.get(node)) {
        	//For each unvisited neighbor, adding the current node and 
        	//the neighbor to the "eulerTour" list in the order they are visited
            if (!visited.contains(v)) {
                eulerTour.add(node);
                eulerTour.add(v);

                //searching for the matched edge in the "edges" list for the current and next node being visited. 
                //This is done to find the edge to be removed from the "edges" list
                Edge matchedEdge = edges.get(0);
                for (Edge edge : edges) {
                    if (edge.source.crimeId == node.crimeId && edge.destination.crimeId == v.crimeId) {
                        matchedEdge = edge;
                        break;
                    }
                }
                //removing the edge from the "edges" list
                edges.remove(matchedEdge);
                //Recursion until all nodes in the graph have been visited
                dfs(v, eulerTour, edges, visited, adjacenyMatrix);
                //Resulting "eulerTour" list contains the nodes visited during the Euler tour
            }
        }
    }

    private static void swap(List<Node> nodes, Integer i, Integer j) {
        Node nodeI = nodes.get(i);
        Node nodeJ = nodes.get(j);
        nodes.set(i, nodeJ);
        nodes.set(j, nodeI);
    }

    /*
     * Calculates the total length of a tour given a List of Nodes representing the tour. 
     * The length is the sum of the distances between consecutive nodes in the tour.
     */
    public static double calculateTourLength(List<Node> tour) {
        double length = 0;
        for (int i = 0; i < tour.size() - 2; i++) {
            Node source = tour.get(i);
            Node destination = tour.get(i + 1);
            //calculating the distance between the current node and the next node
            double l = Graph.calculateDistance(source, destination);
            //Adding the distance to length
            length += l;
        }
        
        //Adding the distance between first and last node
        Node source = tour.get(0);
        Node destination = tour.get(tour.size() - 1);
        length += Graph.calculateDistance(source, destination);
        return length;
    }
}