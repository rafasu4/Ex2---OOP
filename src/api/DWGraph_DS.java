package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph{
    @Override
    public node_data getNode(int key) {
        return null;
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return null;
    }

    @Override
    public void addNode(node_data n) {

    }

    @Override
    public void connect(int src, int dest, double w) {

    }

    @Override
    public Collection<node_data> getV() {
        return null;
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return null;
    }

    @Override
    public node_data removeNode(int key) {
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }

    public class NodeData implements node_data{
        private int key;
        private double weight;
        private String info;
        private int tag;
        private HashMap<Integer, edge_data> neighbors;

        /**Constructor**/
        public NodeData(int key, double weight){
            this.key = key;
            this.weight = weight;
            neighbors = new HashMap<>();
            this.info = "";
            this.tag =0;
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

    public class EdgeData implements edge_data{
        private int src;
        private int dest;
        private double weight;
        private int tag;
        private String info;

        /**Constructor**/
        public EdgeData(int src, int dest, double weight){
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
