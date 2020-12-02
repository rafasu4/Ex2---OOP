package api;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        DWGraph_DS g = new DWGraph_DS();
        DWGraph_DS k = new DWGraph_DS();
        for (int i = 0; i <10 ; i++) {
            g.addNode(g.new NodeData(i,i));
            k.addNode(g.new NodeData(i,i));
        }
        g.connect(1,3,3);
        g.connect(1,4,5);
        k.connect(1,3,3);
        k.connect(1,4,5);
        System.out.println(g.equals(k
        ));
        System.out.println(g.getMC());
        System.out.println(k.getMC());
    }
}
