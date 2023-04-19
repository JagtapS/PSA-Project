# PSA-Project

This repository contains Java code for implementing the Christofides algorithm to find the approximate solution for the Traveling Salesman Problem (TSP) and also contains the implementation of the 2-opt and random swapping optimization algorithms to improve the solution.
Files Included
1. App.java: This is the main class where the TSP problem is solved using the Christofides algorithm, and the optimized solution is obtained using 2-opt and random swapping.
2. Christofides.java: This class contains the implementation of the Christofides algorithm, including finding the minimum weight perfect matching and constructing the Eulerian tour.
3. Node.java: This class represents the nodes in the graph and contains the properties of the node such as its name, X and Y coordinates.
4. Edge.java: This class represents the edges in the graph and contains the properties of the edge such as its starting node, ending node, and weight.
5. Graph.java: This class represents the graph and contains methods to add nodes and edges to the graph, calculate the distance between two nodes, and get the minimum spanning tree of the graph.
How to Run:
1. Clone the repository to your local machine.
2. Open the project in an IDE (e.g. IntelliJ, Eclipse).
3. Run the App.java file.
4. The program will prompt the user to enter the number of nodes for the TSP problem.
5. The program will randomly generate nodes with X and Y coordinates and calculate the distance between each pair of nodes.
6. The Christofides algorithm will be applied to find the approximate solution to the TSP problem.
7. The 2-opt and random swapping optimization algorithms will be applied to improve the solution.
8. The program will output the final optimized Hamiltonian cycle with the total distance traveled.
