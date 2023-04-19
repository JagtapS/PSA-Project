package main.java;

import com.psa.App;

public class Town{
	
	public int id;
	public String name;
	public double latitude;
	public double longitude;
	public Town [] candidates;
	private double longest_cl = Double.MAX_VALUE;
	private int cl_contains = 0;

	//constructor
	public Town(int id, String name, double latitude, double longitude){
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		candidates = new Town[App.cl+1];
	}

	//return the distance between two latitude/longitude points im kilometres - uses Haversine
	public double distanceTo(Town to){

		if(this == to){
			return 0;//same town
		}

		//radius of the earth = 6371km
		double fromLat = Math.toRadians(latitude);
		double fromLong = Math.toRadians(longitude);
		double toLat = Math.toRadians(to.latitude);
		double toLong = Math.toRadians(to.longitude);

		//implementation of haversin formula
		double d = (2 * 6371 * Math.asin(Math.sqrt(Math.pow(Math.sin((fromLat-toLat)/2.0),2)+(Math.cos(fromLat)*Math.cos(toLat)*Math.pow(Math.sin((toLong-fromLong)/2.0),2)))));
		
		return d;
	}

	public Town copy(){
		Town toReturn = new Town(id, name, latitude, longitude);
		return toReturn;
	}

	public String toString(){
		return (id+"");
	}

	public Town [] candidates(){

		//fill it with the first 16
		for(int i=0; i<candidates.length; i++){
			candidates[i] = App.towns[i];
		}

		for(int i=candidates.length; i<App.towns.length; i++){
			//get the current furthest town;
			Town furthest = candidates[0];
			for(int j=0; j<candidates.length; j++){
				if(isCloser(furthest, candidates[j]) == 0)
					furthest = candidates[j];
			}
			if(isCloser(App.towns[i], furthest) == 0)
				replace(furthest, App.towns[i]);
		}

		//return all but this
		Town [] rv = new Town[candidates.length-1];
		for(int i=0, added=0; i<candidates.length; i++){
			if(candidates[i].id != id){
				rv[added] = candidates[i];
				added++;
			}
		}
		return rv;

	}

	//returns 0 or one depending on the closer town
	public int isCloser(Town t0, Town t1){
		int rv = App.adjacencyMatrix[id-1][t0.id-1] < App.adjacencyMatrix[id-1][t1.id-1] ? 0 : 1;
		return rv;
	}

	private void replace(Town longest, Town closer){
		//find index
		int index = -1;
		for(int i=0; i<candidates.length; i++){
			if(candidates[i].id == longest.id){
				index = i;
				break;
			}
		}
		candidates[index] = closer;
	}

	

}