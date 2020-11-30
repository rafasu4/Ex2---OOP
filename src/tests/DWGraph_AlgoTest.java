package tests;

import api.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import api.DWGraph_DS;
import api.node_data;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DWGraph_AlgoTest {

    @Test
    public void copy() {
        DWGraph_Algo k = new DWGraph_Algo();
        DWGraph_DS g = new DWGraph_DS();
        int[] gKeys = new int[10];
        int[] gCopyKeys = new int[10];
        for (int i = 0; i < 10; i++) {
            DWGraph_DS.NodeData node = g.new NodeData(i, i);
            g.addNode(node);
            gKeys[i] = i;
        }
        g.connect(0, 3, 5);
        g.connect(1, 4, 5);
        g.connect(9, 8, 7);
        k.init(g);
        DWGraph_DS gCopy = (DWGraph_DS) k.copy();
        int i = 0;
        //copying all the nodes keys of the copy graph to an array
        for (node_data node : gCopy.getV()) {
            gCopyKeys[i++] = node.getKey();
        }
        Arrays.sort(gCopyKeys);
        boolean flag = true;

        for (int j = 0; j < 10; j++) {
            //checking that copy and original graphs have the same node's keys
            if (gCopyKeys[j] != gKeys[j]) {
                flag = false;
                break;
            }
            //checking that copy and original graphs don't share the same node object
            if (g.getNode(j).equals(gCopy.getNode(j))) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);
        assertEquals(5, gCopy.getEdge(0, 3).getWeight());
        assertEquals(5, gCopy.getEdge(1, 4).getWeight());
        assertEquals(7, gCopy.getEdge(9, 8).getWeight());
        //checking that copy and original graphs don't share the same edge object
        assertFalse(g.getEdge(0, 3).equals(gCopy.getEdge(0, 3)));
    }

    @Test
    public void connected() {
        //no nodes
        dw_graph_algorithms ga1 = new DWGraph_Algo();
        DWGraph_DS g = new DWGraph_DS();
        ga1.init(g);
        assertTrue(ga1.isConnected());
        //1 node
        node_data v1 = g.new NodeData(0);
        ga1.getGraph().addNode(v1);
        assertTrue(ga1.isConnected());
        // 2 nodes without edged
        node_data v2 = g.new NodeData(1);
        ga1.getGraph().addNode(v2);
        assertFalse(ga1.isConnected());
        //2 nodes with 1 edge
        ga1.getGraph().connect(0,1,6);
        assertFalse(ga1.isConnected());
        // 2 nodes with 2 edge to eachother
        ga1.getGraph().connect(1,0,5);
        assertTrue(ga1.isConnected());
        // linkin list
        DWGraph_DS g2 = (DWGraph_DS) GcreatorNotRandom(100,98);
        g2.connect(0,1,0.0);
        g2.connect(1,2,0.0);
        g2.connect(2,3,0.0);
        g2.connect(3,4,0.0);
        g2.connect(4,5,0.0);
        g2.connect(5,6,0.0);
        g2.connect(6,7,0.0);
        g2.connect(7,8,0.0);
        g2.connect(8,9,0.0);
        ga1.init(g2);
        assertFalse(ga1.isConnected());
        // 1000 nodes and 998
        directed_weighted_graph g3 = (DWGraph_DS) GcreatorRandom(1000,998);
        ga1.init(g3);
        assertFalse(ga1.isConnected());
        //3 nodes and 1 edges
        ga1.init(GcreatorRandom(3,1));
        assertFalse(ga1.isConnected());
        //3 nodes and 6 edges
        ga1.init(GcreatorRandom(3,6));
        assertTrue(ga1.isConnected());
        //
        g3=  GcreatorRandom(3,0);
        g3.connect(0,1,0.0);
        g3.connect(1,2,0.0);
        g3.connect(0,2,0.0);
        ga1.init(g3);
        assertFalse(ga1.isConnected());

    }

    public static directed_weighted_graph GcreatorNotRandom(int v_size, int e_size) {
        DWGraph_DS a = new DWGraph_DS();
        for (int i = 0; i < v_size; i++) {
            node_data n = a.new NodeData(i, i);
            a.addNode(n);
        }
        edgeNotRandom(a, e_size);
        return a;
}

    public static directed_weighted_graph GcreatorRandom(int v_size, int e_size) {
        DWGraph_DS a = new DWGraph_DS();
        for (int i = 0; i < v_size; i++) {
            node_data n = a.new NodeData(i, i);
            a.addNode(n);
        }
        edgeRandom(a, e_size);
        return a;
    }

    public static void edgeNotRandom(directed_weighted_graph g, int e_size) {
        while (g.edgeSize() < e_size) {
            for (int i = 0; i < g.nodeSize() - 10; i++) {
                for (int j = 1; j < 11; j++) {
                    g.connect(i, i + j, (double) i + j);
                }
            }
        }
    }

    public static void edgeRandom(directed_weighted_graph a, int e_size) {
        while (a.edgeSize() < e_size) {
            Random rnd = new Random();
            int Limit=a.nodeSize();
            int r1 = rnd.nextInt(Limit);
            int r2 = rnd.nextInt(Limit);
            double w = (double) r1 + r2;
            a.connect(r1, r2, w);
        }


    }
}