package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

/**
 * This class holds a collection of more complex methods for DWGraph_DS object.
 * To run a method, one must creat a DWGraph_Algo instance, and initiating it with DWGraph_DS object using init method.
 * Implements dw_graph_algorithms interface.
 */
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

    /**
     * i copy the graph by 2 steps:
     * 1.i created all the nodes .
     * 2. i connect all the edges .
     *
     * @return copiedGraph
     */
    @Override
    public directed_weighted_graph copy() {
        DWGraph_DS graph = new DWGraph_DS();//used fot initiating inner class NodeData
        directed_weighted_graph copiedGraph = new DWGraph_DS();
        Collection<node_data> pointer = this.g.getV();
        //copying all graph's vertexes and adding to new graph
        for (node_data node : pointer) {
            DWGraph_DS.NodeData copyNode = graph.new NodeData(node);
            copiedGraph.addNode(copyNode);
        }
        //building edges in the copy graph, the same way as th original
        for (node_data node : pointer) {
            for (edge_data edge : this.g.getE(node.getKey())) {
                copiedGraph.connect(node.getKey(), edge.getDest(), edge.getWeight());
            }
        }
        return copiedGraph;
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


    private void setAllTags(int num) {
        for (node_data thisnode : this.g.getV()) {
            thisnode.setTag(((int) num));
        }
    }

    /**
     * to check if the directed graph is connected I make DFS  on both , the graph and Replaced graph.
     *
     * @return 'true' if the graph is connected 'false' if isn't.
     */
    @Override
    public boolean isConnected() {
        if (g.nodeSize() < 1) return true;
        int firstNode = g.getV().iterator().next().getKey();
        directed_weighted_graph graphT = this.copyT();
        directed_weighted_graph graph = getGraph();
        boolean check = DFS(firstNode);
        init(graphT);
        boolean checkT = DFS(firstNode);
        init(graph);
        return (check && checkT);
    }

    /**
     * this Algorytem check if from current node i able to get all nodes.
     * i travel the graph from src nodes and count how much nodes i visited
     *
     * @return 'true' if i visited all the nodes ,otherwise 'false'.
     */
    private boolean DFS(int src) {
        this.setAllTags(0);// 0 means unvisited
        Queue<Integer> queneForDFS = new LinkedList<>();
        int numOfVisits = 1;
        queneForDFS.add(src);
        g.getNode(src).setTag(1);
        while (!(queneForDFS.isEmpty())) {
            Integer thisNode = queneForDFS.poll();
            for (edge_data thisSi : g.getE(thisNode)) { // i check all his siblings.
                node_data thisSib = this.g.getNode(thisSi.getDest());
                if (thisSib.getTag() == 0) { // if i visit node that i didnt visited yet.
                    thisSib.setTag(1);
                    queneForDFS.add(thisSib.getKey());
                    numOfVisits++;
                }
            }
        }
        boolean AllVisited = (numOfVisits == (this.g.nodeSize()));
        return AllVisited; // if i visited all the nodes out of the irst one it means the graph is connected.
    }

    /**
     * This Method return the list of the way.
     * i used a replaced graph to track the best way from the dest to src then i reverse it into List to get the src in the top of the list.
     * @param src - start node
     * @param dest - end (target) node
     * @return list o nodes
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {

        if (shortestPathDist(src, dest) == -1) return null;// if there isn't wayBeforeReverse.
        HashMap<Integer, Double> hashAllShortD = shortestPathHash(src);
        Stack<node_data> wayBeforeReverse = new Stack<>();
        LinkedList<node_data> wayAfterReverse = new LinkedList<>();
        directed_weighted_graph graphT = this.copyT();// i did A replaced graph to check what was the bst way that i have.
        int currentkey = dest;
        wayBeforeReverse.push(g.getNode(dest));
        while (currentkey != src) {
            // i start to travel from dest and i check what was the nodes that gave to the "destnode" this dis from src.
            double myShortD = hashAllShortD.get(currentkey);
            for (edge_data sib : graphT.getE(currentkey)) {
                int sibKey = sib.getDest();
                double sibShortD = hashAllShortD.get(sibKey);
                double edgeWight = sib.getWeight();
                if (myShortD == sibShortD + edgeWight) {
                    currentkey = sibKey;
                    wayBeforeReverse.push(g.getNode(sibKey));
                    break; // if i found the right sibling so i will check him now.
                }
            }
        }
        wayBeforeReverse.push(g.getNode(src)); // i reverse it with LinkedList.
        while (!wayBeforeReverse.empty()) {
            wayAfterReverse.add(wayBeforeReverse.pop());
        }
        return wayAfterReverse;
    }


    /**
     * this method return this dis from src to dest using the  "shortestPathHash" method.
     * @param src - start node
     * @param dest - end (target) node
     * @return Dis if -1 it means there no way from src to dest.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        HashMap<Integer, Double> allDistanceFromSrc = shortestPathHash(src);
        double inf = Double.POSITIVE_INFINITY;
        if (allDistanceFromSrc.get(dest) == inf) return -1;
        return allDistanceFromSrc.get(dest);

    }

    /**
     *
     *  this method check whats the best way from all the nodes to the srcNode and put it into a hashmap.
     *  i start travel from srcNode then i put all his sibling to a Heap THE NEXT NODE i decide to travel is the one that has the
     *  smalled distance , thats way i used Heap TO GIVE ME  the smalled dis everytime i choose the nest node to travel.
     *  i used object "NodeForHeap" that had a "dis" inside her and the MinHeap works on this Dis . and the
     * @param src
     * @return HashODis
     */
    private HashMap<Integer, Double> shortestPathHash(int src) {
        //setting for the  algorytem
        double inf = Double.POSITIVE_INFINITY;
        PriorityQueue<DWGraph_DS.NodeForHeap> MinHeapWithDisFromSrc = new PriorityQueue<>();
        HashMap<Integer, DWGraph_DS.NodeForHeap> HashOfDis = new HashMap<>();
        HashMap<Integer, Double> toReturn = new HashMap<>();

        // all nodes init
        for (node_data thisnode : this.g.getV()) {
            if (thisnode.getKey() != src) {
                int keyNode = thisnode.getKey();
                toReturn.put(keyNode, inf);

            }
        }

        //src node init
        DWGraph_DS.NodeForHeap srcNodeHeap = new DWGraph_DS.NodeForHeap(g.getNode(src), 0.0);
        MinHeapWithDisFromSrc.add(srcNodeHeap);// START travel
        HashOfDis.put(src, srcNodeHeap);
        toReturn.put(src, 0.0);
        double updatedDis = 0;
        // starting to "walk"
        while (!(MinHeapWithDisFromSrc.isEmpty())) {
            DWGraph_DS.NodeForHeap currentNodeHeap = MinHeapWithDisFromSrc.poll();
            if (currentNodeHeap.getNode().getTag() == 0) {
                int currentKey = currentNodeHeap.getKey();//if first round src should fall
                node_data currentNode = currentNodeHeap.getNode();
// move over the edges
                for (edge_data currentEdge : g.getE(currentKey)) {
                    //settings
                    int currentSibkey = currentEdge.getDest();
                    node_data sibNode = g.getNode(currentSibkey);
                    //action
                    double offer = toReturn.get(currentKey) + currentEdge.getWeight();
                    updatedDis = toReturn.get(currentSibkey);
                    if (offer < updatedDis) { // if its a better way i will put it in the tag.
                        updatedDis = offer;
                        toReturn.put(currentSibkey, updatedDis);
                        DWGraph_DS.NodeForHeap current = new DWGraph_DS.NodeForHeap(sibNode, updatedDis);//i update to the heap
                        MinHeapWithDisFromSrc.add(current);
                    }

                }
            }
        }
        return toReturn;
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

