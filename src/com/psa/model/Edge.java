package com.psa.model;

import java.io.Serializable;

import com.psa.model.Node;

public class Edge implements Serializable {
    Node source;
    Node destination;
    double distance;

    public Edge(Node source, Node destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

}
