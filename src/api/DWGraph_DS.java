package api;


import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {
    /**Holds the keys of this graph's nodes and their associate node_data object**/
    private HashMap<Integer, node_data> allNodes;
    private int modeCount, edgesCount;


    /**
     *Constructor
     */
    public DWGraph_DS() {
        allNodes = new HashMap<>();
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
        NodeData srcNode = (NodeData)this.getNode(src);
        //if there's no existing edge between the nodes
        if (!srcNode.neighbors.containsKey(dest)){
            return null;
        }
        return srcNode.neighbors.get(dest);
    }

    @Override
    public void addNode(node_data n) {
        // if and only if the given node isn't exist in this graph
        if (getNode(n.getKey()) == null) {
            allNodes.put(n.getKey(), n);
            modeCount++;
        }
    }

    @Override
    //Case B - the edge exits but you need to update the wight.
    //Case C - the edge  exits (do nothing).
    public void connect(int src, int dest, double w) {
        NodeData srcNode = (NodeData)this.getNode(src);
        boolean CaseA = (getEdge(src, dest) == null);
        //if there's no edge between the nodes
        if (CaseA) {
            edge_data newEdge = new EdgeData(src, dest, w);// make a new edge
            srcNode.neighbors.put(dest, newEdge);
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
                modeCount++;
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
        if(this.getNode(node_id)==null) return null;
        NodeData n = (NodeData) this.getNode(node_id);
        return n.neighbors.values();
    }

    @Override
    public node_data removeNode(int key) {
        //if the node doesn't exist
        if (!allNodes.containsKey(key)){
            return null;
        }
        NodeData removedNode = (NodeData)this.getNode(key);
        Iterator<edge_data> it = this.getE(key).iterator();
        //going over all the connected neighbors
        while (it.hasNext()){
            edge_data currentEdge = it.next();
            int dest = currentEdge.getDest();
            NodeData neighbor = (NodeData)this.getNode(dest);//the neighbor is the destination of the extracted edge
            //if there's an edge from neighbor to this node - remove
            if(this.getEdge(neighbor.getKey() ,key)!=null) {
                this.removeEdge(neighbor.getKey(), key);
            }
            it.remove();
            edgesCount--;
        }
        this.allNodes.remove(removedNode.getKey());//remove this node from the graph
        modeCount++;
        return removedNode;
    }

    // case A if he is not exit :
    // case B if he exits.
    @Override
    public edge_data removeEdge(int src, int dest) {
        //if one of the nodes isn't in the graph or there isn't edge between them
        if (!allNodes.containsKey(src) || !allNodes.containsKey(dest) || this.getEdge(src,dest)==null){
            return null;
        }
        NodeData srcNode = (NodeData) this.getNode(src);
        edge_data removedEdge = srcNode.neighbors.get(dest);
        srcNode.neighbors.remove(dest);
        edgesCount--;
        modeCount++;
        return removedEdge;
    }

    @Override
    /**Override Equals method. Two graphs will considered equal if they have the exact same properties.
     * @param graph
     * @return flag
     */
    public boolean equals(Object graph){
        //making sure the input object is weighted_graph object
        if(!(graph instanceof directed_weighted_graph)){ return false; }
        directed_weighted_graph g =  (DWGraph_DS) graph;
        //basic conditions to be equals
        if(this.edgeSize() != g.edgeSize() || this.nodeSize() != g.nodeSize()){ return false;}
        boolean flag = true;
        int[][] thisGraph = new int[this.nodeSize()][2]; //two dimensional array for this graph nodes' keys and weights
        int[][] gGraph = new int[g.nodeSize()][2]; //two dimensional array for g nodes' keys and weights
        int i = 0 ;
        //copying this graph's nodes' keys and weights
        for (node_data node:this.getV()) {
            thisGraph[i][0] = node.getKey();
            thisGraph[i][1] = (int)node.getWeight();
            i++;
        }
        i = 0;
        //copying g nodes' keys and weights
        for (node_data node:g.getV()) {
            gGraph[i][0] = node.getKey();
            gGraph[i][1] = (int)node.getWeight();
            i++;
        }
        Arrays.sort(thisGraph, Comparator.comparingInt(o -> o[0]));//sorting by first column
        Arrays.sort(gGraph, Comparator.comparingInt(o -> o[0]));//sorting by first column
        for (int j = 0; j <this.nodeSize() ; j++) {
            if(thisGraph[j][0]!=gGraph[j][0] ||  thisGraph[j][1]!=gGraph[j][1]){
                flag = false;
            }
        }
        //if(flag = false) return false;

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

    public class NodeData implements node_data {
        private int key;
        private double weight;
        private String info;
        private int tag;
        /**Holds connected neighbor: key - neighbor ID, value - edge_data object that holds edge info.**/
        private HashMap<Integer, edge_data> neighbors;


        /**
         * Constructor
         **/
        public NodeData(int key, double weight) {
            this.key = key;
            this.weight = weight;
            neighbors = new HashMap<>();
            this.info = "";
            this.tag = 0;
        }

        /**Copy constructor**/
        public NodeData(node_data n){
            this.key = n.getKey();
            this.weight = n.getWeight();
            neighbors = new HashMap<>();
            this.info = n.getInfo();
            this.tag = n.getTag();
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public geo_location getLocation() {
            return null;
        }

        @Override
        public void setLocation(geo_location p) {

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

    public class EdgeData implements edge_data {
        private int src;
        private int dest;
        private double weight;
        private int tag;
        private String info;

        /**Constructor**/
        public EdgeData(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        public EdgeData(edge_data edge){
            this.src = edge.getSrc();
            this.dest = edge.getDest();
            this.weight = edge.getWeight();
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
}


