# Ex2

# Description
This project is devided into two: first part concern of building a weighted and directed graph, while the second part focuses on a game which uses the first part.
* [Part 1](#p1)
* [Part 2](#p2)
* [Running The Game](#run)

<a name="p1"></a>
## Part 1:
1. [ NodeData ](#nodedata)
2. [ EdgeData ](#edge)
3. [ Geo_Location](#geo)
4. [NodeData_Json](#nodejs)
5. [NodeForHeap](#nodeheap)
6. [DWGraph_DS](#graphds)
7. [DWGraph_Algo](#graphalgo)
8. [DWGraph_DSJsonDeserializer](#des)

<a name="nodedata"></a>
## 1. NodeData 
This class represents a node in a graph. Each node has its own weight, id and location in a 2 dimensional space. Inner class in DWGraph_DS.


<a name="edge"></a>
## 2. EdgeData 
This class representsan edge in a graph. Each EdgeData object has a source and destination node and weight. Inner class in DWGraph_DS.


<a name="geo"></a>
## 3. Geo_Location
A Geo_Location object has 3 parameters: x, y, and z. this parameters help locating a node in the 3 dimensional space (in this assignment, 2 dimensional space). Inner class in DWGraph_DS.


<a name="nodejs"></a>
## 4. NodeData_Json

## 5. NodeForHeap

## 6. DWGraph_DS
This class represents a directional weighted graph.
Each graph builds from nodes that are connected by edges objects. Implements directed_weighted_graph interface.
 

## 7. DWGraph_Algo
This class holds a collection of more complex methods for DWGraph_DS object.
To run a method, one must creat a DWGraph_Algo instance, and initiating it with DWGraph_DS object using init method.

## 8.DWGraph_DSJsonDeserializer
This class used in order to deserialize Json's graph object. Using this class enable performing deserialization of interface implementation classes.

<a name="p2"></a>
## Part 2:
1. [ Arena](#ar)
2. [ CL_Agent](#ag)
3. [ CL_Pokemon](#pok)
4. [Ex2](#ex)
5. [GuiFrame](#gfr)
6. [GuiPanel](#gpl)
7. [MyLabel](#lbl)

<a name="ar"></a>
## 1. Arena
This class represents a multi Agents Arena which move on a graph and grabs Pokemons. Given class from OOP course.


<a name="ag"></a>
## 2. CL_Agent
This class represents an agent that runs on the graph and catches pokemons. 


<a name="pok"></a>
## 3. CL_Pokemon
This class represents a pokemon in the game.


<a name="ex"></a>
## 4. Ex2
This class is the main class in the second part, which its main method the game starts.


<a name="gfr"></a>
## 5. GuiFrame
This class represents a specific adapted JFrame to this project.



<a name="gpl"></a>
## 6. GuiPanel
This class represents a specific adapted JPanel to this project.



<a name="lbl"></a>
## 7. MyLabel
This class represents a specific adapted JLabel to this project.


<a name="run"></a>
## Running The Game
- Go to src->gameClient.
- Run Ex2.
- In the poping window, enter your id and the wanted level.
- Press enter.
