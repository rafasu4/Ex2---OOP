package api;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        DWGraph_DS g = new DWGraph_DS();
        DWGraph_DS k = new DWGraph_DS();
        for(int i = 0; i<3 ; i++){
            node_data n = g.new NodeData(i, i);
            g.addNode(n);
        }
        for (int i = 0; i < 3 ; i++) {
            node_data n = g.new NodeData(2-i, 2-i);
            k.addNode(n);
        }
        System.out.println(g.equals(k));
//        DWGraph_DS g = new DWGraph_DS();
//        DWGraph_Algo l = new DWGraph_Algo();
//        node_data n = g.new NodeData(2, 2);
//        node_data n1 = g.new NodeData(3, 3);
//        g.addNode(n);
//        g.addNode(n1);
//        g.connect(2, 3, 4);
//        g.connect(3,2,10);
//        edge_data e =g.getEdge(2,3);
//        dw_graph_algorithms k = new DWGraph_Algo();
//        k.init(g);
//        System.out.println(k.save("file"));
//       System.out.println(l.load("file"));

    }
}
