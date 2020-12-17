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
                newGraph.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
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
            for (edge_data thisSi : g.getE(thisNode)) {
                node_data thisSib = this.g.getNode(thisSi.getDest());
                if (thisSib.getTag() == 0 && (thisSib.getKey() != src)) {
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
        if (g.nodeSize() < 1) return true;
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
        HashMap<Integer, Double> SPD = shortestPathHash(src, dest);
        double inf = Double.POSITIVE_INFINITY;
        if (SPD.get(dest) == inf) return -1;
        return SPD.get(dest);

    }

    private HashMap<Integer, Double> shortestPathHash(int src, int dest) {
        //setting for the  algorytem
        double inf = Double.POSITIVE_INFINITY;
        PriorityQueue<DWGraph_DS.NodeForHeap> q = new PriorityQueue<>();
        Queue<Integer> quefodisk = new LinkedList<>();
        HashMap<Integer, DWGraph_DS.NodeForHeap> HashOfDis = new HashMap<>();
        HashMap<Integer, Double> toReturn = new HashMap<>();
        g.getNode(src).setTag(0);
        // all nodes init
        for (node_data thisnode : this.g.getV()) {
            if (thisnode.getKey() != src) {
                int keyNode = thisnode.getKey();
                //DWGraph_DS.NodeForHeap current = new DWGraph_DS.NodeForHeap(thisnode, inf);
                // q.add(current);
                // HashOfDis.put(keyNode, current);
                toReturn.put(keyNode, inf);
                thisnode.setTag(0);
            }
        }


        //src node init
        DWGraph_DS.NodeForHeap srcNodeHeap = new DWGraph_DS.NodeForHeap(g.getNode(src), 0.0);
        q.add(srcNodeHeap);
        HashOfDis.put(src, srcNodeHeap);
        toReturn.put(src, 0.0);
        double updatedDis = 0;
        // starting to "walk"
        while (!(q.isEmpty())) {
            DWGraph_DS.NodeForHeap peekNodeHeap = q.peek();
            DWGraph_DS.NodeForHeap currentNodeHeap = q.poll();
            if (currentNodeHeap.getNode().getTag() == 0) {
                int currentKey = currentNodeHeap.getKey();//if first round src should fall
                node_data currentNode = currentNodeHeap.getNode();
                currentNode.setTag(1);
// move over the edges
                for (edge_data currentEdge : g.getE(currentKey)) {
                    //settings
                    int currentSibkey = currentEdge.getDest();
                    node_data sibNode = g.getNode(currentSibkey);
                    //action
                    double offer = toReturn.get(currentKey) + currentEdge.getWeight();
                    updatedDis = toReturn.get(currentSibkey);
                    if (offer < updatedDis) { // if its a better way i will put it in the tag.
                        // HashOfDis.get(currentSibkey).setDis(offer);
                        updatedDis = offer;
                        toReturn.put(currentSibkey,updatedDis);
                        DWGraph_DS.NodeForHeap current = new DWGraph_DS.NodeForHeap(sibNode, updatedDis);//i update to the heap
                        q.add(current);
                    }

                }
            }
        }
        return toReturn;
    }


    @Override
    public List<node_data> shortestPath(int src, int dest) {
        double inf = Double.POSITIVE_INFINITY;
        if (shortestPathDist(src, dest) == -1) return null;// if there no way return null. inf repesents there is no way
        HashMap<Integer, Double> hashAllShortD = shortestPathHash(src, dest);
        Stack<node_data> way = new Stack<>();
        LinkedList<node_data> way2 = new LinkedList<>();

        //dw_graph_algorithms Tgraph=new DWGraph_Algo();
        directed_weighted_graph graphT = this.copyT();
        int currentkey = dest;
        way.push(g.getNode(dest));
        while (currentkey != src) {
            double myShortD = hashAllShortD.get(currentkey);
            for (edge_data sib : graphT.getE(currentkey)) {
                int sibKey = sib.getDest();
                double sibShortD = hashAllShortD.get(sibKey);
                double edgeWight = sib.getWeight();
                if (myShortD == sibShortD + edgeWight) {
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
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
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

