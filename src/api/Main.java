package api;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i <= 5; i++) {
            node_data n = g.new NodeData(i, i, i, i);
            g.addNode(n);
        }
        g.connect(1, 2, 2);
        g.connect(5, 3, 7);
        g.connect(2, 3, 3);
        DWGraph_Algo k = new DWGraph_Algo();
        k.init(g);
        System.out.println(k.save("data"));
        System.out.println(k.load("data"));
        System.out.println(k.save("data2"));
    }
}
