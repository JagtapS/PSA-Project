package com.psa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.psa.model.AntColonyOptimization;
import com.psa.model.Christofides;
import com.psa.model.Edge;
import com.psa.model.Graph;
import com.psa.model.Node;
//import com.psa.model.Population;
//import com.psa.model.TSPGeneticAlgorithm;
import com.psa.model.Vertex;

public class App {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        createGraph();
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
	        List<Node> randomTour = Christofides.randomSwapOptimise(hamiltonCycle, 10);
	        System.out.println("TSP Random Swap Tour Optimisation :" + randomTour.size()); //Printing the size of random tour list
	        System.out.println("Random Tour Length :" + Christofides.calculateTourLength(randomTour));
	        
	    //Two Optimization
	        List<Node> twoOptTour = Christofides.twoOpt(randomTour);
	        System.out.println("TSP Two Opt Tour Optimisation Size: " + twoOptTour.size());
	        System.out.println("OTL 2 Opt Tour Length : " + Christofides.calculateTourLength(twoOptTour));
	    
	    //2. Strategic Optimization
	        
	    //Simulated Annealing Optimization
	        List<Node> simulatedAnnealing = Christofides.generateSimulatedAnnealingTSPtour(eulerTour);
	        System.out.println("TSP SimulatedAnnealing Size :" + simulatedAnnealing.size());
	        System.out.println("Simulated Annealing Hamilton Tour Length :" + Christofides.calculateTourLength(simulatedAnnealing));
	        System.out.println("TSP Genetic Opt :");
	       
	        
//	       List<Vertex> tourVertex = convertNodeListToVertexList(hamiltonCycle);
//
//	       List<String> tour = hamiltonCycle.stream().map(vertex -> vertex.getCrimeId()).collect(Collectors.toList());
//	        benchmark.startMark();
//	        List<Integer> aopIdTsp = AntColonyOptimization.optimizeWithAntColony(tour,hamiltonCycle);
//	        benchmark.endMark();
//
//	        List<Vertex> aopTsp = new ArrayList<>();
//	        double aopCost = 0;
//	        for (int i = 0; i < aopIdTsp.size() - 1; i++) {
//	            Vertex v1 = convertNodeListToVertexList(hamiltonCycle.get(aopIdTsp.get(i)));
//	            Vertex v2 = hamiltonCycle.get(aopIdTsp.get(i+1));
//	            Edge edge = new Edge(v1, v2);
//	            aopCost += edge.getWeight();
//	            aopTsp.add(v1);
//	        }
//	        aopTsp.add(tsp.get(aopIdTsp.get(aopIdTsp.size() - 1)));
//	        
//	        
//	        List<Integer> tour = convertNodeListToIndexList(hamiltonCycle);
	       
	        //System.out.println("Simulated Annealing Hamilton Tour Length :" + AntColonyOptimization.optimizeWithAntColony(tour, tourVertex)); 
	       
        System.out.println("Execution done");
        
    }
    public static List<Vertex> convertNodeListToVertexList(List<Node> nodes) {
        List<Vertex> vertices = new ArrayList<>();
        for (Node node : nodes) {
            Vertex vertex = new Vertex(node.getCrimeId(), node.getLatitude(), node.getLongitude());
            vertices.add(vertex);
        }
        return vertices;
    }

    
    public static List<Integer> convertNodeListToIndexList(List<Node> nodes) {
        List<Integer> indices = new ArrayList<>();
        for (Node node : nodes) {
            indices.add(node.getIndex());
        }
        return indices;
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