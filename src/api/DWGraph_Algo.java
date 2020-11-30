package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph g;

    /**
     * Constructor
     **/
    public DWGraph_Algo() {
        g = new DWGraph_DS();
    }

    @Override
    public void init(directed_weighted_graph g) {
        this.g = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return this.g;
    }

    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS graph = new DWGraph_DS();//used fot initiating inner class NodeData
        directed_weighted_graph newGraph = new DWGraph_DS();
        Collection<node_data> pointer = this.g.getV();
        //copying all graph's vertexes and adding to new graph
        for (node_data node : pointer) {
            DWGraph_DS.NodeData copyNode = graph.new NodeData(node);
            newGraph.addNode(copyNode);
        }
        //building edges in the copy graph, the same way as th original
        for (node_data node : pointer) {
            for (edge_data edge : this.g.getE(node.getKey())) {
                newGraph.connect(node.getKey(), edge.getDest(), edge.getWeight());
            }
        }
        return newGraph;
    }

    private void setAllTags(double num) {
        for (node_data thisnode : this.g.getV()) {
         //   thisnode.setTag(((int)num);
        }
    }

    public directed_weighted_graph copyT() {
        DWGraph_DS graph = new DWGraph_DS();//used fot initiating inner class NodeData
        directed_weighted_graph newGraph = new DWGraph_DS();
        Collection<node_data> pointer = this.g.getV();
        //copying all graph's vertexes and adding to new graph
        for (node_data node : pointer) {
            DWGraph_DS.NodeData copyNode = graph.new NodeData(node);
            newGraph.addNode(copyNode);
        }
        //building edges in the copy graph, the same way as th original
        for (node_data node : pointer) {
            for (edge_data edge : this.g.getE(node.getKey())) {
                newGraph.connect(edge.getDest(), node.getKey(), edge.getWeight());
            }
        }
        return newGraph;
    }

    private boolean BFS(int src) {
        this.setAllTags(0);// 0 means unvisited
        Queue<Integer> queforbfs = new LinkedList<>();
        int numOfVisits = 1;

        queforbfs.add(src);
        while (!(queforbfs.isEmpty())) {
            Integer thisNode = queforbfs.poll();
            for (edge_data thisSi : g.getE(src)) {
                node_data thisSib = this.g.getNode(thisSi.getDest());
                if (thisSib.getTag() == 0) {
                    thisSib.setTag(1);
                    queforbfs.add(thisSib.getKey());
                    numOfVisits++;
                }
            }
        }
        return (numOfVisits == (this.g.nodeSize())); // if i visited all the nodes out of the irst one it means the graph is connected.
    }


    @Override
    public boolean isConnected() {
        int firstNode = g.getV().iterator().next().getKey();
        directed_weighted_graph graphT = this.copyT();
        directed_weighted_graph graph = getGraph();
        boolean check = BFS(firstNode);
        init(graphT);
        boolean checkT = BFS(firstNode);
        init(graph);
        return (check && checkT);

    }

    @Override
    public double shortestPathDist(int src, int dest) {return 0;}
/*
        //setting for the  algorytem
        double inf = Double.POSITIVE_INFINITY;
        this.setAllTags((inf)); // i put in all infinity.
        node_data node1 = g.getNode(src);
        node_data node2 = g.getNode(dest);
        //node1.setTag(0);
        Queue<Integer> quefodisk = new LinkedList<>();
        // starting to "walk"
        quefodisk.add(src);
        while (!quefodisk.isEmpty()) {
            int currentkey = quefodisk.poll();
            node_data currentNode = g.getNode(currentkey);
            for (node_data currentSib : g.getV(currentkey)) {
                double offer = g.getNode(currentkey).getTag() + currentNode.getLocation().distance(currentSib.getLocation());// i check if i can "offer" you a better way. my ofer his from the nodes that i stand on and th wighte of this edge.
                if (offer < currentSib.getTag()) { // if its a better way i will put it in the tag.
                    currentSib.setTag(offer);
                    quefodisk.add(currentSib.getKey());
                }
            }
            double ans = Double.POSITIVE_INFINITY;
            if (g.getNode(dest).getTag() == ans) return -1;
            return g.getNode(dest).getTag();
        }
    }
*/
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        boolean flag = false;
        try {
            PrintWriter pw = new PrintWriter(new File(file + ".json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this.g);
            pw.write(json);
            pw.close();
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }


    @Override
    public boolean load(String file) {
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file + ".json"));
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_DSJsonDeserializer());
            Gson gson = builder.create();
            this.g = gson.fromJson(br, DWGraph_DS.class);
            flag = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }


}

