package com.psa;

import org.junit.Test;

import com.psa.model.Edge;
import com.psa.model.Graph;
import com.psa.model.Node;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void testGetDegree() {
        // Create a graph with two nodes and one edge connecting them
        Graph graph = new Graph();
        Node node1 = new Node("Node 1", 0, 0);
        Node node2 = new Node("Node 2", 1, 1);
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1, node2);

        // Test that the degree of each node is correct
        assertEquals(1, graph.getDegree(node1));
        assertEquals(1, graph.getDegree(node2));
    }

    @Test
    public void testAddNode() {
        // Create a graph with one node
        Graph graph = new Graph();
        Node node1 = new Node("Node 1", 0, 0);
        graph.addNode(node1);

        // Test that the node was added to the graph
        assertEquals(1, graph.nodes.size());
        assertEquals(node1, graph.nodes.get(0));
    }

    @Test
    public void testAddEdge() {
        // Create a graph with two nodes
        Graph graph = new Graph();
        Node node1 = new Node("Node 1", 0, 0);
        Node node2 = new Node("Node 2", 1, 1);
        graph.addNode(node1);
        graph.addNode(node2);

        // Add an edge between the nodes
        graph.addEdge(node1, node2);

        // Test that the edge was added to the graph
        assertEquals(1, graph.edges.size());
        assertEquals(node1, graph.edges.get(0).source);
        assertEquals(node2, graph.edges.get(0).destination);
    }

    @Test
    public void testGetDegreeWithMultipleEdges() {
        // Create a graph with three nodes and three edges connecting them
        Graph graph = new Graph();
        Node node1 = new Node("Node 1", 0, 0);
        Node node2 = new Node("Node 2", 1, 1);
        Node node3 = new Node("Node 3", 2, 2);
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addEdge(node1, node2);
        graph.addEdge(node2, node3);
        graph.addEdge(node1, node3);

        // Test that the degree of each node is correct
        assertEquals(3, graph.getDegree(node1));
        assertEquals(3, graph.getDegree(node2));
        assertEquals(3, graph.getDegree(node3));
    }

    @Test
    public void testAddDuplicateNode() {
        // Create a graph with one node
        Graph graph = new Graph();
        Node node1 = new Node("Node 1", 0, 0);
        graph.addNode(node1);

        // Try adding the same node again
        graph.addNode(node1);

        // Test that the node was not added again
        assertEquals(2, graph.nodes.size());
    }

    @Test
    public void testAddSelfLoop() {
        // Create a graph with one node
        Graph graph = new Graph();
        Node node1 = new Node("Node 1", 0, 0);
        graph.addNode(node1);

        // Try adding a self-loop edge
        graph.addEdge(node1, node1);

        // Test that the edge was not added to the graph
        assertEquals(1, graph.edges.size());
    }

    @Test
    public void testAddMultipleEdgesBetweenNodes() {
        // Create a graph with two nodes
        Graph graph = new Graph();
        Node node1 = new Node("Node 1", 0, 0);
        Node node2 = new Node("Node 2", 1, 1);
        graph.addNode(node1);
        graph.addNode(node2);

        // Add multiple edges between the nodes
        graph.addEdge(node1, node2);
        graph.addEdge(node1, node2);
        graph.addEdge(node1, node2);

        // Test edges added to the graph
        assertEquals(3, graph.edges.size());
        assertEquals(node1, graph.edges.get(0).source);
        assertEquals(node2, graph.edges.get(0).destination);
    }
}