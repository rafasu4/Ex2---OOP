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

    private void setAllTags(int num) {
        for (node_data thisnode : this.g.getV()) {
            thisnode.setTag(((int) num));
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
    public double shortestPathDist(int src, int dest) {
        HashMap<Integer, Double> SPD = SPD(src, dest);
        return SPD.get(dest);

    }

    private HashMap<Integer, Double> SPD(int src, int dest) {

        //setting for the  algorytem
        double inf = Double.POSITIVE_INFINITY;
        HashMap<Integer, Double> tempD = new HashMap<>();
        for (node_data thisnode : this.g.getV()) {
            tempD.put(thisnode.getKey(), inf);
        }

        node_data node1 = g.getNode(src);
        node_data node2 = g.getNode(dest);
        tempD.put(src, 0.0);
        Queue<Integer> quefodisk = new LinkedList<>();
        // starting to "walk"
        quefodisk.add(src);
        while (!quefodisk.isEmpty()) {
            int currentkey = quefodisk.poll();
            node_data currentNode = g.getNode(currentkey);
            for (edge_data currentSib : g.getE(currentkey)) {
                int currentSibkey = currentSib.getDest();
                double offer = tempD.get(currentkey) + currentSib.getWeight();// i check if i can "offer" you a better way. my ofer his from the nodes that i stand on and th wighte of this edge.
                if (offer < tempD.get(currentSib)) { // if its a better way i will put it in the tag.
                    tempD.put(currentSibkey, offer);
                    quefodisk.add(currentSibkey);
                }
            }
        }
        return tempD;
    }


    @Override
    public List<node_data> shortestPath(int src, int dest) {
        HashMap<Integer, Double> SPD = SPD(src, dest);
        Stack<node_data> way = new Stack<>();
        LinkedList<node_data> way2 = new LinkedList<>();

        //dw_graph_algorithms Tgraph=new DWGraph_Algo();
        directed_weighted_graph graphT = this.copyT();
        int currentkey = dest;
        way.push(g.getNode(dest));
        while (currentkey != src) {
            for (edge_data sib : graphT.getE(dest)) {
                int sibKey = sib.getDest();
                double check = SPD.get(sibKey) + sib.getWeight();
                if (SPD.get(currentkey) == check) {
                    currentkey = sibKey;
                    way.push(g.getNode(sibKey));
                    break;
                }
            }
        }
        way.push(g.getNode(src)); // i reverse it with LinkedList.
        while (!way.empty()) {
            way2.add(way.pop());
        }
        return way2;
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

