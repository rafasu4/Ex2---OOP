package api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class represents a directional weighted graph.
 * Each graph builds from nodes that are connected by edges objects. Implements directed_weighted_graph interface.
 */
public class DWGraph_DS implements directed_weighted_graph {
    /**
     * Holds the keys of this graph's nodes and their associate node_data object
     **/
    @Expose
    private HashSet<edge_data> Edges;
    @Expose
    private HashSet<NodeData_Json> Nodes;
    private HashMap<Integer, node_data> allNodes;
    private int modeCount, edgesCount;


    /**
     * Constructor
     */
    public DWGraph_DS() {
        allNodes = new HashMap<>();
        Edges = new HashSet<>();
        Nodes = new HashSet<>();
        modeCount = 0;
        edgesCount = 0;

    }

    //Case A :the node is not exist.
    @Override
    public node_data getNode(int key) {
        boolean CaseA = !(allNodes.containsKey(key));
        if (CaseA) return null;
        return allNodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        //if one of the nodes isn't in the graph
        if (getNode(src) == null || getNode(dest) == null)
            return null;
        NodeData srcNode = (NodeData) this.getNode(src);
        //if there's no existing edge between the nodes
        if (!srcNode.neighbors.containsKey(dest)) {
            return null;
        }
        return srcNode.neighbors.get(dest);
    }

    @Override
    public void addNode(node_data n) {
        // if and only if the given node isn't exist in this graph
        if (getNode(n.getKey()) == null) {
            Nodes.add(new NodeData_Json(n));
            allNodes.put(n.getKey(), n);
            modeCount++;
        }
    }

    @Override
    //Case B - the edge exits but you need to update the wight.
    //Case C - the edge  exits (do nothing).
    public void connect(int src, int dest, double w) {
        if (!(src == dest)) {
            NodeData srcNode = (NodeData) this.getNode(src);
            NodeData destNode = (NodeData) this.getNode(dest);
            boolean CaseA = (getEdge(src, dest) == null);
            //if there's no edge between the nodes
            if (CaseA) {
                edge_data newEdge = new EdgeData(src, dest, w);// make a new edge
                Edges.add(newEdge);//add to this graph Edges list
                srcNode.neighbors.put(dest, newEdge);
                destNode.edgesFrom.put(src, newEdge);
                edgesCount++;
                modeCount++;
            }
            //if there's existing edge - update it's weight
            else {
                boolean newWeight = (getEdge(src, dest).getWeight() != w);
                //if the new weight isn't the same as current - perform an update
                if (newWeight) {
                    edge_data newEdge = new EdgeData(src, dest, w);
                    srcNode.neighbors.replace(dest, newEdge);
                    destNode.edgesFrom.put(src, newEdge);
                    modeCount++;
                }
            }
        }
    }

    @Override
    public Collection<node_data> getV() {
        return allNodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        //if the node isn't in the graph
        if (this.getNode(node_id) == null) return null;
        NodeData n = (NodeData) this.getNode(node_id);
        return n.neighbors.values();
    }

    @Override
    public node_data removeNode(int key) {
        //if the node doesn't exist
        if (!allNodes.containsKey(key)) return null;
        NodeData removedNode = (NodeData) this.getNode(key);
        Iterator<edge_data> it = removedNode.neighbors.values().iterator();
        while (it.hasNext()) { // delete from who i connect to.
            NodeData current = (NodeData) getNode(it.next().getDest());
            edge_data deleteEdge = current.edgesFrom.get(key);
            Edges.remove(deleteEdge);//remove from graph's edges list
            current.edgesFrom.remove(key);
            edgesCount--;
            modeCount++;
        }
        Iterator<edge_data> itOther = removedNode.edgesFrom.values().iterator();
        while (itOther.hasNext()) {
            NodeData current = (NodeData) getNode(itOther.next().getSrc());
            edge_data deleteEdge = current.neighbors.get(key);
            Edges.remove(deleteEdge);//remove from graph's edges list
            current.neighbors.remove(key);
            edgesCount--;
            modeCount++;
        }
         modeCount++;
        Iterator<NodeData_Json> itNodes = Nodes.iterator();
        while (itNodes.hasNext()){
            NodeData_Json current = itNodes.next();
            if(current.id == removedNode.getKey()){
                it.remove();
                Nodes.remove(current);
                break;
            }
        }
         removedNode.neighbors=new HashMap<>();//it should be empty now
         removedNode.edgesFrom=new HashMap<>();//it should be empty now
         return removedNode;
    }


    // case A if he is not exit :
    // case B if he exits.
    @Override
    public edge_data removeEdge(int src, int dest) {
        //if one of the nodes isn't in the graph or there isn't edge between them
        if (!allNodes.containsKey(src) || !allNodes.containsKey(dest) || this.getEdge(src, dest) == null) {
            return null;
        }

        NodeData srcNode = (NodeData) this.getNode(src);
        NodeData destNode = (NodeData) this.getNode(dest);

        edge_data removedEdge = srcNode.neighbors.get(dest);
        Edges.remove(removedEdge);//remove from graph's edges list
        srcNode.neighbors.remove(dest);
        destNode.edgesFrom.remove(src);
        edgesCount--;
        modeCount++;
        return removedEdge;
    }


    @Override
    /**Override Equals method. Two graphs will considered equal if they have the exact same properties.
     * @param graph
     * @return flag
     */
    public boolean equals(Object graph) {
        //making sure the input object is weighted_graph object
        if (!(graph instanceof directed_weighted_graph)) {
            return false;
        }
        directed_weighted_graph g = (DWGraph_DS) graph;
        //basic conditions to be equals
        if (this.edgeSize() != g.edgeSize() || this.nodeSize() != g.nodeSize()) {
            return false;
        }
        boolean flag = true;
        int[][] thisGraph = new int[this.nodeSize()][2]; //two dimensional array for this graph nodes' keys and weights
        int[][] gGraph = new int[g.nodeSize()][2]; //two dimensional array for g nodes' keys and weights
        int i = 0;
        //copying this graph's nodes' keys and weights
        for (node_data node : this.getV()) {
            thisGraph[i][0] = node.getKey();
            thisGraph[i][1] = (int) node.getWeight();
            i++;
        }
        i = 0;
        //copying g nodes' keys and weights
        for (node_data node : g.getV()) {
            gGraph[i][0] = node.getKey();
            gGraph[i][1] = (int) node.getWeight();
            i++;
        }

        Arrays.sort(thisGraph, Comparator.comparingInt(o -> o[0]));//sorting by first column - node key
        Arrays.sort(gGraph, Comparator.comparingInt(o -> o[0]));//sorting by first column - node key
        for (int j = 0; j < this.nodeSize(); j++) {
            if (thisGraph[j][0] != gGraph[j][0] || thisGraph[j][1] != gGraph[j][1]) {
                flag = false;
            }
        }
        //comparing each node neighbor list to its matching node from the other graph
        for (int j = 0; j <thisGraph.length ; j++) {
            if(!flag) break;
            Collection<edge_data> thisP = this.getE(thisGraph[j][0]);
            double[][] thisNodeNeighbors = new double[thisP.size()][2];
            i = 0;
            for(edge_data edge : thisP){
                thisNodeNeighbors[i][0] = edge.getSrc();
                thisNodeNeighbors[i][1] = edge.getWeight();
                i++;
            }
            Collection<edge_data> gP = g.getE(thisGraph[j][0]);
            double[][] gNodeNeighbors = new double[gP.size()][2];
            i = 0;
            for(edge_data edge : gP){
                gNodeNeighbors[i][0] = edge.getSrc();
                gNodeNeighbors[i][1] = edge.getWeight();
                i++;
            }
            Arrays.sort(thisNodeNeighbors, Comparator.comparingDouble(o -> o[0]));//sorting by first column - node key
            Arrays.sort(gNodeNeighbors, Comparator.comparingDouble(o -> o[0]));//sorting by first column - node key
            //comparing each neighbor
            for (int k = 0; k < gP.size(); k++) {
                if (thisNodeNeighbors[k][0] != gNodeNeighbors[k][0] || thisNodeNeighbors[k][1] != gNodeNeighbors[k][1]){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public int nodeSize() {
        return allNodes.size();
    }

    @Override
    public int edgeSize() {
        return edgesCount;
    }

    @Override
    public int getMC() {
        return modeCount;
    }

    /**This class represents a node in a graph. Each node has its own weight, id and location in a 2 dimensional
     * space. Inner class in DWGraph_DS. Implements node_data interface.
     */
    public class NodeData implements node_data  {
        @Expose
        private String info;
        private geo_location location;
        @Expose
        private int id;
        private double weight;
        private int tag;

        /**
         * Holds connected neighbor: key - neighbor ID, value - edge_data object that holds edge info.
         **/
        private HashMap<Integer, edge_data> neighbors;
        /**Holds all the nodes that have edges to this node: key - neighbor ID, value - edge_data object that holds edge info.**/
        private HashMap<Integer, edge_data> edgesFrom;


        /**
         * Constructor
         **/
        public NodeData(int id, double x, double y, double z) {
            this.id = id;
            this.weight = weight;
            neighbors = new HashMap<>();
            edgesFrom = new HashMap<>();
            location = new Geo_Location(x,y,z);
            this.info = location.toString();
            this.tag = 0;
        }
        public NodeData(int key, double weight) {
            this.id = key;
            this.weight = weight;
            neighbors = new HashMap<>();
            edgesFrom = new HashMap<>();
            this.info = "";
            this.tag = 0;
        }

        public NodeData(int key) {
            this.id = key;
            this.weight = 0;
            neighbors = new HashMap<>();
            edgesFrom = new HashMap<>();
            this.info = "";
            this.tag = 0;
        }

        /**
         * Copy constructor
         **/
        public NodeData(node_data n) {
            this.id = n.getKey();
            this.weight = n.getWeight();
            neighbors = new HashMap<>();
            edgesFrom=new HashMap<>();
            this.info = n.getInfo();
            this.tag = n.getTag();
        }

        @Override
        public int getKey() {
            return id;
        }

        @Override
        public geo_location getLocation() {
            return location;
        }

        @Override
        public void setLocation(geo_location p) {
            location = p;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public void setWeight(double w) {
            this.weight = w;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }



    }

    /**This class represents an edge in a graph.
    Each EdgeData object has a source and destination node and weight. Inner class in DWGraph_DS. Implements of edge_data interface.
     */
    public class EdgeData implements edge_data {
        @Expose
        private final int src;
        @Expose
        @SerializedName("w")
        private final double weight;
        @Expose
        private final int dest;
        private int tag;
        private String info;

        /**
         *Constructor
         **/
        public EdgeData(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int getSrc() {
            return src;
        }

        @Override
        public int getDest() {
            return dest;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }
    }

    /**A Geo_Location object has 3 parameters: x, y, and z. this parameters
     * help locating a node in the 3 dimensional space (in this assignment, 2 dimensional space). Inner class in DWGraph_DS.
     * Implements geo_location interface.
     */
    public class Geo_Location implements geo_location {
        private double x;
        private double y;
        private double z;


        public Geo_Location(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;

        }

        /**Copy constructor**/
        public Geo_Location(geo_location p) {
            this.x = p.x();
            this.y = p.y();
            this.z = p.z();
        }

        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public double z() {
            return z;
        }

        @Override
        public double distance(geo_location p2) {
            double dx = this.x() - p2.x();
            double dy = this.y() - p2.y();
            double dz = this.z() - p2.z();
            double t = (dx*dx+dy*dy+dz*dz);
            return Math.sqrt(t);
        }

        @Override
        public String toString(){
            String ans = "";
            ans+=this.x+","+this.y+","+this.z;
            return ans;
        }

    }
    public class NodeData_Json{
        @Expose
        private String pos;
        @Expose
        private int id;

        public NodeData_Json(node_data n){
            if (n.getLocation()!=null) pos = n.getLocation().x()+","+n.getLocation().y()+","+n.getLocation().z();
            id = n.getKey();
        }
    }
    public static class NodeForHeap implements Comparable{
        double dis;
        node_data node;

        public NodeForHeap (node_data node,double dis){
            this.node=node;
            this.dis=dis;
        }
        public double getDis (){
            return dis;
        }
        public int getKey (){
            return node.getKey();
        }
        public node_data getNode (){
            return node;
        }
        public void setDis (double updateddis){
             dis=updateddis;
        }

        @Override
        public int compareTo(@NotNull Object o) {
            NodeForHeap other =(NodeForHeap)o;
            if (dis>other.dis)return 1;
            if (dis<other.dis)return -1;
            return 0;
        }
    }
}


