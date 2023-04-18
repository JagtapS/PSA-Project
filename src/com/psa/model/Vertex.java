package com.psa.model;


public class Vertex {

    //private final int numericId;
    private final String id;
    private final double latitude;
    private final double longitude;

    public Vertex(String id, double latitude, double longitude) {
        //this.numericId = numericId;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

//    List<Integer> tour = tsp.stream().map(vertex -> vertex.getNumericId()).collect(Collectors.toList());
//    benchmark.startMark();
//    List<Integer> aopIdTsp = AntColonyOptimization.optimizeWithAntColony(tour,tsp);
//    benchmark.endMark();
//
//    List<Vertex> aopTsp = new ArrayList<>();
//    double aopCost = 0;
//    for (int i = 0; i < aopIdTsp.size() - 1; i++) {
//        Vertex v1 = tsp.get(aopIdTsp.get(i));
//        Vertex v2 = tsp.get(aopIdTsp.get(i+1));
//        Edge edge = new Edge(v1, v2);
//        aopCost += edge.getWeight();
//        aopTsp.add(v1);
//    }
    //aopTsp.add(tsp.get(aopIdTsp.get(aopIdTsp.size() - 1)));
//    public int getNumericId() {
//        return numericId;
//    }
//
    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public double distance(Vertex v) {
        double dLat = Math.toRadians(v.latitude - latitude);
        double dLon = Math.toRadians(v.longitude - longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(v.latitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371000 * c; // Earth's radius in meters
    }

    @Override
    public String toString() {
        return id;
    }
}