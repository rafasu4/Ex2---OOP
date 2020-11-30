package tests;

import api.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import api.DWGraph_DS;
import api.edge_data;
import api.node_data;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
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

    }

    public static directed_weighted_graph GcreatorNotRandom(int v_size, int e_size) {
        DWGraph_DS a = new DWGraph_DS();
        for (int i = 0; i < v_size; i++) {
            node_data n = a.new NodeData(i, i);
        }
        edgeNotRandom(a, e_size);
        return a;
}

    public static directed_weighted_graph GcreatorRandom(int v_size, int e_size) {
        DWGraph_DS a = new DWGraph_DS();
        for (int i = 0; i < v_size; i++) {
            node_data n = a.new NodeData(i, i);
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
            int r1 = rnd.nextInt(a.nodeSize());
            int r2 = rnd.nextInt(a.nodeSize());
            double w = (double) r1 + r2;
            a.connect(r1, r2, w);
        }


    }
}