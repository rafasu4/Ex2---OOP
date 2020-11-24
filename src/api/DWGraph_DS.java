package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> allNodes;
    private HashMap<Integer, HashMap<Integer, edge_data>> allHashes;
    private int mc, ed;


    /**
     * constrcutur
     */

    public DWGraph_DS() {
        allNodes = new HashMap<>();
        allHashes = new HashMap<>();
        mc = 0;
        ed = 0;
    }

    //Case A :the node is not exits.
    @Override
    public node_data getNode(int key) {
        boolean CaseA = !(allNodes.containsKey(key));
        if (CaseA) return null;
        return allNodes.get(key);
    }

    @Override
    // Case A the nodes not exit
    // case B the nodes are exit but the edge not
    //case C if the edge exits.
    public edge_data getEdge(int src, int dest) {
        boolean CaseA = (getNode(src) == null || getNode(dest) == null);
        boolean CaseB = !(allHashes.get(src).containsKey(dest));
        if (CaseA || CaseB) return null;
        else { //Case C
            return allHashes.get(src).get(dest); // if edges in the graph class
        }
    }

    @Override
    // case A - if he is not exit
    public void addNode(node_data n) {
        if (getNode(n.getKey()) == null) {//Case A .
            allNodes.put(n.getKey(), n);
            allHashes.put(n.getKey(), new HashMap<>());
            mc++;
        }
    }

    @Override
    //Case A - the edge not exits.
    //Case B - the edge exits but you need to update the wight.
    //Case C - the edge  exits (do nothing).
    public void connect(int src, int dest, double w) {
        // check if the nodes are exit in the graph.
        boolean CaseA = (getEdge(src, dest) == null);// make a new edge and add.

        if (CaseA) {
            mc++;
            ed++;
            edge_data newEdge = new EdgeData(src, dest, w);// make a new edge
            allHashes.get(src).put(dest, newEdge); // put it into "src" Hashmap.
        } else
            { // if there is an edge.
            boolean CaseB = (getEdge(src, dest).getWeight() != w);
            if (CaseB) {
                edge_data updateWeight = new EdgeData(src, dest, w);// make a new edge
                allHashes.get(src).put(dest, updateWeight); // put it into "src" Hashmap.
                mc++;

            }
        }

    }

    @Override
    public Collection<node_data> getV() {
        return allNodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return allHashes.get(node_id).values();
    }

    @Override
    //Case A : if the node is not exit.
    //Case B :if the node is exits.
    public node_data removeNode(int key) {
        if (!(allNodes.containsKey(key))) return null; //Case A
        //Case B
        node_data removedNode = getNode(key);
        allNodes.remove(key);
        allHashes.remove(key);
        int numofNightbors = allHashes.get(key).size();
        mc = mc - 1 - numofNightbors;
        return removedNode;
    }

    // case A if he is not exit :
    // case B if he exits.
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (!(allHashes.get(src).containsKey(dest))) return null; // Case A
        // Case B
        edge_data removedEdge = allHashes.get(src).get(dest);
        allHashes.get(src).remove(dest);
        return removedEdge;
    }

    @Override
    public int nodeSize() {
        return allNodes.size();
    }

    @Override
    public int edgeSize() {
        return ed;
    }

    @Override
    public int getMC() {
        return mc;
    }

    public class NodeData implements node_data {
        private int key;
        private double weight;
        private String info;
        private int tag;
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

        /**
         * Constructor
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
}
