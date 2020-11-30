package api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Collection;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph g;

    /**Constructor**/
    public DWGraph_Algo(){
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
        for(node_data node:pointer){
            DWGraph_DS.NodeData copyNode = graph.new NodeData(node);
            newGraph.addNode(copyNode);
        }
        //building edges in the copy graph, the same way as th original
        for (node_data node: pointer){
            for (edge_data edge : this.g.getE(node.getKey())){
                newGraph.connect(node.getKey(), edge.getDest(), edge.getWeight());
            }
        }
        return newGraph;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        boolean flag = false;
        try {
            PrintWriter pw = new PrintWriter(new File(file+".json"));
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
        try{
            BufferedReader br = new BufferedReader(new FileReader(file+".json"));
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
