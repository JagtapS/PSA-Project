package main.java;

import com.psa.App;

public class Tour{
	
	public Town [] visited; //in order
	public double distance;
	public int numVisited;
	public boolean [] vis;

	public Tour(Town start){
		visited = new Town [App.n+1];
		visited[0] = start;
		vis = new boolean[App.n+1];
		vis[start.id] = true;
		numVisited = 1;
		distance = 0.0;
	}

	public static Tour nearestNeighbour(){
		double best = Double.MAX_VALUE;
		Tour bestTour = new Tour(App.towns[0]);
		for(int t = 0; t<App.n; t++){
			Tour iter = new Tour(App.towns[t]);
			//find smallest at the current town
			int current = t;
			boolean [] v = new boolean [App.n];
			v[t] = true;

			for(int i=0; i<App.n-1; i++){

				double dist = Double.MAX_VALUE;
				int index = 0;
				
				for(int j=0; j<App.n; j++){
					if(App.adjacencyMatrix[current][j]<dist && !v[j]){
						index = j;
						dist = App.adjacencyMatrix[current][j];
					}
				}
				
				//found smallest
				v[index] = true;
				iter.visit(App.towns[index]);
				current = index;

			}
			//update
			if(iter.distance < best){
				best = iter.distance;
				bestTour = iter;
			}
		}
		return bestTour;
	}


	public void visit(Town t){
		distance += App.adjacencyMatrix[current().id-1][t.id-1];
		visited[numVisited] = t;
		vis[t.id] = true;
		numVisited++;
		if(numVisited == App.n)
			visit(visited[0]); //tie up
	}

	public boolean visited(Town t){
/*
		for(int i=0; i<numVisited; i++){
			if(visited[i].id == t.id)
				return true;
		}
		return false;
*/
		return vis[t.id];

	}

	public String toString(){
		String out = "";
		for(int i=0; i<visited.length-1; i++){
			out += visited[i].toString()+".";
		}
		out += visited[visited.length-1].toString();
		return out;
	}

	public Town current(){
		return visited[numVisited-1];
	}

	public Town nextClosest(){
		Town current = current();
		int index = -1;
		double closest = Double.MAX_VALUE;
		for(int i=0; i<App.n; i++){
			if(!visited(App.towns[i]) && App.adjacencyMatrix[current.id-1][i] < closest){
				closest =  App.adjacencyMatrix[current.id-1][i];
				index = i;
			}
		}
		return App.towns[index];
	}

	public void opt3(){
		for(int i=0; i<visited.length-4; i++){
			Town start = visited[i];
			Town end = visited[i+4];

			Town [] opt = {visited[i+1].copy(), visited[i+2].copy(), visited[i+3].copy()};

			Town [] perm1 = {opt[0], opt[1], opt[2]};
			Town [] perm2 = {opt[0], opt[2], opt[1]}; 
			Town [] perm3 = {opt[1], opt[0], opt[2]};
			Town [] perm4 = {opt[1], opt[2], opt[0]};
			Town [] perm5 = {opt[2], opt[0], opt[1]};
			Town [] perm6 = {opt[2], opt[1], opt[0]};

			Town [][] perms = {perm1, perm2, perm3, perm4, perm5, perm6};

			double best = Double.MAX_VALUE;
			int index = -1;
			double old = 0.0;

			for(int perm=0; perm < perms.length; perm++){
				double test = App.adjacencyMatrix[start.id-1][perms[perm][0].id-1];
				test += App.adjacencyMatrix[perms[perm][0].id-1][perms[perm][1].id-1];
				test += App.adjacencyMatrix[perms[perm][1].id-1][perms[perm][2].id-1];
				test += App.adjacencyMatrix[perms[perm][2].id-1][end.id-1];
				if(test < best){
					best = test;
					index = perm;
				}
				if(perm == 0)
					old = test;
			}
			if(index != 0){
				visited[i+1] = perms[index][0];
				visited[i+2] = perms[index][1];
				visited[i+3] = perms[index][2];
				//System.out.print("3 opt worked from "+distance);
				distance -= old;
				distance += best;
				//System.out.println(" to "+distance);
			}
		}
	}

}