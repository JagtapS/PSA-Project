package main.java;

import com.psa.App;

public class Ant{
	
	public Town start;
	public Tour tour;

	public Ant(){
		start = App.towns[(int)(Math.random()*App.n)];
		tour = new Tour(start);
	}

	public void visitClosest(){
		
	}

}