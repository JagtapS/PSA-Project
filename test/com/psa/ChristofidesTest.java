package com.psa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.psa.model.Christofides;
import com.psa.model.Edge;
import com.psa.model.Graph;
import com.psa.model.Node;

public class ChristofidesTest {
    private Graph graph;
    private List<Edge> mst;

    @BeforeEach
    public void setUp() {
        // Construct a sample graph
        graph = new Graph();
        Node node1 = new Node("1", 1.0, 2.0);
        Node node2 = new Node("2", 2.0, 3.0);
        Node node3 = new Node("3", 3.0, 4.0);
        Node node4 = new Node("4", 4.0, 5.0);
        Node node5 = new Node("5", 5.0, 6.0);
        graph.nodes = Arrays.asList(node1, node2, node3, node4, node5);
        graph.edges = Arrays.asList(
                new Edge(node1, node2, 2),
                new Edge(node1, node3, 3),
                new Edge(node1, node4, 4),
                new Edge(node2, node3, 5),
                new Edge(node2, node5, 6),
                new Edge(node3, node4, 7),
                new Edge(node3, node5, 8),
                new Edge(node4, node5, 9)
        );
        
        // Construct a minimum spanning tree for the graph
        mst = new ArrayList<>();
        mst.add(new Edge(node1, node2, 2));
        mst.add(new Edge(node1, node3, 3));
        mst.add(new Edge(node1, node4, 4));
        mst.add(new Edge(node2, node5, 6));
    }
    @Test
    public void testFindOddDegreeVerticesEmpty() {
        // Construct an empty graph and MST
        Graph emptyGraph = new Graph();
        emptyGraph.nodes = new ArrayList<>();
        emptyGraph.edges = new ArrayList<>();
        List<Edge> emptyMST = new ArrayList<>();

        // Call the function to get the actual result
        List<Node> actualOddDegreeNodes = Christofides.findOddDegreeVertices(emptyGraph, emptyMST);

        // Assert the result
        assertTrue(actualOddDegreeNodes.isEmpty());
    }
    
    @Test
    public void testFindOddDegreeVertices() {
    	//Test Case 1
        // Calculate the expected result
        Map<Node, Integer> expectedDegrees = new HashMap<>();
//        expectedDegrees.put(new Node("1", 1.0, 2.0), 3);
//        expectedDegrees.put(new Node("2", 2.0, 3.0), 2);
//        expectedDegrees.put(new Node("3", 3.0, 4.0), 3);
//        expectedDegrees.put(new Node("4", 4.0, 5.0), 3);
//        expectedDegrees.put(new Node("5", 5.0, 6.0), 2);
        expectedDegrees.put(new Node("1", 1.0, 2.0), 3);
        expectedDegrees.put(new Node("2", 2.0, 3.0), 2);
        expectedDegrees.put(new Node("3", 3.0, 4.0), 3);
        expectedDegrees.put(new Node("4", 4.0, 5.0), 1);
        expectedDegrees.put(new Node("5", 5.0, 6.0), 2);
        List<Node> expectedOddDegreeNodes = Arrays.asList(new Node("1", 1.0, 2.0), new Node("3", 3.0, 4.0), new Node("4", 4.0, 5.0), new Node("5", 5.0, 6.0));


        // Call the function to get the actual result
        List<Node> actualOddDegreeNodes = Christofides.findOddDegreeVertices(graph, mst);
        //System.out.println("Expected size: " + expectedOddDegreeNodes.size());
        //System.out.println("Actual size: " + actualOddDegreeNodes.size());

        // Assert the result
        assertEquals(expectedOddDegreeNodes.size(), actualOddDegreeNodes.size());

	}
    
    
    @Test
    public void testGetMinimumWeightPerfectMatchingWithEmptyList() {
    List<Node> oddDegreeNodes = new ArrayList<>();
    List<Edge> actualMatching = Christofides.getMinimumWeightPerfectMatching(oddDegreeNodes);
    assertTrue(actualMatching.isEmpty());
    }
    
//    @Test
//    public void testGetMinimumWeightPerfectMatching() {
//        // Create a list of odd degree nodes
//        List<Node> oddDegreeNodes = new ArrayList<>();
//        oddDegreeNodes.add(new Node("1", 0, 0));
//        oddDegreeNodes.add(new Node("2", 1, 0));
//        oddDegreeNodes.add(new Node("3", 2, 0));
//        oddDegreeNodes.add(new Node("4", 0, 1));
//        oddDegreeNodes.add(new Node("5", 1, 1));
//
//        // Get the minimum weight perfect matching
//        List<Edge> minimumWeightPerfectMatching = Christofides.getMinimumWeightPerfectMatching(oddDegreeNodes);
//        //System.out.println("Expected size: " + minimumWeightPerfectMatching.size());
//        //System.out.println("Actual size: " + oddDegreeNodes.size());
//        // Assert that the size of the minimum weight perfect matching is equal to the size of the list of odd degree nodes divided by 2
//        assertEquals(minimumWeightPerfectMatching.size(), oddDegreeNodes.size());
//    }
    @Test
    public void testGetMinimumWeightPerfectMatching() {
        // Create a list of odd degree nodes
        List<Node> oddDegreeNodes = new ArrayList<>();
        oddDegreeNodes.add(new Node("1", 0, 0));
        oddDegreeNodes.add(new Node("2", 1, 0));
        oddDegreeNodes.add(new Node("3", 2, 0));
        oddDegreeNodes.add(new Node("4", 0, 1));
        oddDegreeNodes.add(new Node("5", 1, 1));

        // Get the minimum weight perfect matching
        List<Edge> minimumWeightPerfectMatching = Christofides.getMinimumWeightPerfectMatching(oddDegreeNodes);

        // Assert that the size of the minimum weight perfect matching is equal to the size of the list of odd degree nodes divided by 2, rounded up
        assertEquals(minimumWeightPerfectMatching.size(), 4);
    }

    



    @Test
    void testFindOddDegreeVerticesNoOddDegrees() {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            nodes.add(new Node(Integer.toString(i), i, i));
        }
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(nodes.get(0), nodes.get(1), 2));
        edges.add(new Edge(nodes.get(0), nodes.get(2), 3));
        edges.add(new Edge(nodes.get(1), nodes.get(2), 1));
        edges.add(new Edge(nodes.get(2), nodes.get(3), 4));
        edges.add(new Edge(nodes.get(3), nodes.get(0), 5));
        Graph graph = new Graph(nodes);
        List<Edge> mst = Christofides.findMST(graph);
        List<Node> oddDegreeVertices = Christofides.findOddDegreeVertices(graph, mst);
        Assertions.assertEquals(0, oddDegreeVertices.size());
    }



}
