package com.psa.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph implements Serializable {
    List<Node> nodes;
    List<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public Graph(List<Node> nodes) {
        this.nodes = nodes;
        this.edges = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Node source, Node destination) {
        double distance = calculateDistance(source, destination);
        Edge edge = new Edge(source, destination, distance);
        this.edges.add(edge);
    }

    public static double calculateDistance(Node source, Node destination) {
    	//Haversine formula
        int R = 6371; // constant value for the radius of the Earth in kilometers
        //converted from degrees to radians using the Math.toRadians() method
        double lat1 = Math.toRadians(source.getLatitude()); //source latitude
        double lat2 = Math.toRadians(destination.getLatitude()); //destination latitude
        double lon1 = Math.toRadians(source.getLongitude()); //source longitude
        double lon2 = Math.toRadians(destination.getLongitude()); //destination longitude
        double dLat = lat2 - lat1; 
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;
        return distance; //in meters
    }

    public void connectAllNodes() {
        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);
            for (int j = 0; j < nodes.size(); j++) {
                if (i != j) {
                    Node node2 = nodes.get(j);
                    this.addEdge(node1, node2);
                }

            }
        }
    }

    public List<Edge> kruskalMST() {
        List<Edge> mst = new ArrayList<>();
        //edges of the graph are sorted in increasing order of their weights
        //using the Comparator.comparingDouble() method, which sorts the edges
        //based on their distance values
        Collections.sort(edges, Comparator.comparingDouble(e -> e.distance));
        //map called parents is created, which associates each Node object in the graph
        //with its parent Node object. Initially, each node is its own parent,
        //so the map is initialized with each node as its own parent.
        Map<Node, Node> parents = new HashMap<>();
        for (Node node : this.nodes) {
            parents.put(node, node);
        }
        //the edges are processed in order of their increasing weight. 
        //For each edge, the find() method is called on its source and 
        //destination nodes using the parents map, to find their respective parent nodes.
        for (Edge edge : this.edges) {
            Node parent1 = find(edge.source, parents);
            Node parent2 = find(edge.destination, parents);
            //If the two parent nodes are not the same, 
            //it means that adding the edge to the MST would not create a cycle,
            //so the edge is added to the MST list and the parent1 node is made 
            //a child of the parent2 node in the parents map.
            if (parent1 != parent2) {
                mst.add(edge);
                parents.put(parent1, parent2);
            }
        }
        return mst; //mst list
    }

    
    
    //    
//    public List<Edge> primMST() {
//        List<Edge> mst = new ArrayList<>();
//        Set<Node> visited = new HashSet<>();
//        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distance));
//
//        Node start = this.nodes.get(0);
//        visited.add(start);
//        pq.addAll(start.edges);
//
//        while (!pq.isEmpty() && visited.size() < this.nodes.size()) {
//            Edge edge = pq.poll();
//            Node source = edge.source;
//            Node destination = edge.destination;
//
//            if (visited.contains(source) && visited.contains(destination)) {
//                continue;
//            }
//
//            mst.add(edge);
//
//            Node next = visited.contains(source) ? destination : source;
//            visited.add(next);
//            pq.addAll(next.edges);
//        }
//
//        return mst;
//    }

    //prims algo
    
//    public List<Edge> primsMST() {
//        // Create a map to keep track of the parent node of each node in the graph
//        Map<Node, Node> parents = new HashMap<>();
//        // Create a set to keep track of the nodes that have already been included in the MST
//        Set<Node> visited = new HashSet<>();
//        // Create a priority queue to store the edges in the graph based on their weights
//        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distance));
//        // Add the first node in the graph to the set of visited nodes
//        visited.add(this.nodes.get(0));
//        // Add all the edges that are connected to the first node to the priority queue
//        System.out.println("In primsMST");
//        for (Edge edge : this.edges) {
//        	System.out.println("In primsMST edge loop");
//            if (edge.source.equals(this.nodes.get(0)) || edge.destination.equals(this.nodes.get(0))) {
//                pq.offer(edge);
//            }
//        }
//        // Loop until all the nodes have been visited
//        while (visited.size() < this.nodes.size()) {
//        	System.out.println("In visited loop");
//            // Get the edge with the smallest weight from the priority queue
//            Edge edge = pq.poll();
//            // If both the source and destination nodes of the edge have already been visited,
//            // skip the edge to avoid creating a cycle
//            if (visited.contains(edge.source) && visited.contains(edge.destination)) {
//                continue;
//            }
//            // Add the edge to the list of edges in the MST
//            List<Edge> mst = new ArrayList<>();
//            mst.add(edge);
//            System.out.println("Find MST Edge" + mst);
//            // Add the source node of the edge to the set of visited nodes
//            visited.add(edge.source);
//            // Add all the edges that are connected to the source node of the edge to the priority queue
//            for (Edge e : this.edges) {
//                if (e.source.equals(edge.source) && !visited.contains(e.destination)) {
//                    pq.offer(e);
//                }
//            }
//            // Update the parent of the destination node of the edge to be the source node of the edge
//            System.out.println("Find MST Parent" + parents);
//            parents.put(edge.destination, edge.source);
//        }
//        // Create a list of edges that represents the MST by iterating through the map of parents
//        List<Edge> mst = new ArrayList<>();
//        for (Node node : parents.keySet()) {
//            if (!node.equals(parents.get(node))) {
//                mst.add(new Edge(node, parents.get(node), calculateDistance(node, parents.get(node))));
//                System.out.println("Inside Prims Algo "+node);
//            }
//        }
//        return mst;
//    }

//    public List<Edge> primsMST() {
//        List<Edge> mst = new ArrayList<>();
//        Set<Node> visited = new HashSet<>();
//        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distance));
//        // Choose any node as starting node
//        Node startNode = nodes.get(0);
//        visited.add(startNode);
//        // Add all the edges of startNode to the priority queue
//        for (Edge edge : edges) {
//            if (edge.source.equals(startNode) || edge.destination.equals(startNode)) {
//                pq.add(edge);
//            }
//        }
//        // Loop until all the nodes have been visited
//        while (visited.size() < nodes.size()) {
//            // Get the smallest edge from the priority queue
//            Edge minEdge = pq.poll();
//            Node minNode = null;
//            // Check which node of the edge is not visited
//            if (visited.contains(minEdge.source) && !visited.contains(minEdge.destination)) {
//                minNode = minEdge.destination;
//            } else if (!visited.contains(minEdge.source) && visited.contains(minEdge.destination)) {
//                minNode = minEdge.source;
//            }
//            // If the destination node is not visited, add the edge to the MST and mark the destination node as visited
//            if (minNode != null) {
//                mst.add(minEdge);
//                visited.add(minNode);
//                // Add all the edges of the destination node to the priority queue
//                for (Edge edge : edges) {
//                    if (edge.source.equals(minNode) || edge.destination.equals(minNode)) {
//                        pq.add(edge);
//                    }
//                }
//            }
//        }
//        return mst;
//    }

    // prims end
    
    
    
    public List<Edge> getMinimumWeightPerfectMatching() {
        List<Edge> result = new ArrayList<>();
        List<Node> nodes = new ArrayList<>(this.nodes);
        while (!nodes.isEmpty()) {
            Node node = nodes.remove(0);
            double minWeight = Double.MAX_VALUE;
            Node minNode = null;
            List<Node> backupNode = this.nodes;
            for (Node otherNode : nodes) {
                double weight = calculateDistance(node, otherNode);
                if (weight < minWeight) {
                    minWeight = weight;
                    minNode = otherNode;
                }
                result.add(new Edge(node, minNode, minWeight));
                backupNode.remove(minNode);
            }
            this.nodes = backupNode;
            return result;
        }
        return result;
    }

    public Map<Node, List<Node>> adjacencyMatrix() {
    	//initializing a new HashMap called "adjacencyMatrix" to store the adjacency list of each node in the graph
        Map<Node, List<Node>> adjacencyMatrix = new HashMap<>();
        //iterating through the nodes in the graph and adding each node to the "adjacencyMatrix" map with an empty ArrayList as its value
        for (Node node : this.nodes) {
            adjacencyMatrix.put(node, new ArrayList<>());
        }
        //iterating through the edges in the graph and adding the source and destination nodes of each edge to the adjacency list of each other
        for (Edge edge : this.edges) {
            adjacencyMatrix.get(edge.source).add(edge.destination);
            adjacencyMatrix.get(edge.destination).add(edge.source);
        }
        return adjacencyMatrix; //returning the "adjacencyMatrix" map containing the adjacency list of each node in the graph

    }

    private Node find(Node node, Map<Node, Node> parents) {
        if (parents.get(node) == node) {
            return node;
        }
        Node parent = find(parents.get(node), parents);
        parents.put(node, parent);
        return parent;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}






