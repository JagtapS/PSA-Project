package com.psa.model;
import java.io.*;

public class Node implements Serializable {
    String crimeId;
    private int index;	
    double longitude;
    double latitude;

    public Node(String crimeId, double longitude, double latitude) {
        this.crimeId = crimeId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
    public String getCrimeId() {
        return crimeId;
    }

    public void setCrimeId(String crimeId) {
        this.crimeId = crimeId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
