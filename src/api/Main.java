package api;

public class Main {
    public static void main(String[] args) {
        DWGraph_DS g = new DWGraph_DS();

        node_data n1 = g.new NodeData(8,0);
        node_data n2 = g.new NodeData(5,0);
        g.addNode(n1);
        g.addNode(n2);
        g.connect(8, 5, 10);
        System.out.println(g.getEdge(8,5).getWeight());
    }
}
