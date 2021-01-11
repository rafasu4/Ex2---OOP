package tests;

import api.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


class DWGraph_AlgoTest {

    @Test
    public void transpose(){
        DWGraph_Algo k = new DWGraph_Algo();
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < 100; i++) {
            DWGraph_DS.NodeData node = g.new NodeData(i, i);
            g.addNode(node);
        }
        g.connect(3, 4, 1);
        g.connect(3, 7, 1);
        g.connect(7, 3, 1);
        g.connect(7, 5, 1);
        g.connect(5, 0, 1);
        g.connect(5, 6, 1);
        g.connect(4, 5, 1);
        g.connect(6, 4, 1);
        g.connect(6, 4, 1);
        g.connect(6, 0, 1);
        g.connect(6, 2, 1);
        g.connect(0, 1, 1);
        g.connect(1, 2, 1);
        g.connect(2, 0, 1);
        k.init(g);
        directed_weighted_graph g_t = k.transpose();
        System.out.println(g_t.getV().size());

    }

    @Test
    public void connectedComponents(){
        DWGraph_Algo k = new DWGraph_Algo();
        DWGraph_DS g = new DWGraph_DS();
        k.init(g);
        for (int i = 0; i < 10; i++) {
            DWGraph_DS.NodeData node = g.new NodeData(i, i);
            g.addNode(node);
        }
        g.connect(3, 4, 1);
        g.connect(3, 7, 1);
        g.connect(7, 3, 1);
        g.connect(7, 5, 1);
        g.connect(5, 0, 1);
        g.connect(5, 6, 1);
        g.connect(4, 5, 1);
        g.connect(6, 4, 1);
        g.connect(6, 4, 1);
        g.connect(6, 0, 1);
        g.connect(6, 2, 1);
        g.connect(0, 1, 1);
        g.connect(1, 2, 1);
        g.connect(2, 0, 1);
        List<Integer> l0 = k.connectedComponents(0);
        List<Integer> l1 = k.connectedComponents(1);
        List<Integer> l2 = k.connectedComponents(2);
        List<Integer> l3 = k.connectedComponents(3);
        List<Integer> l4 = k.connectedComponents(4);
        List<Integer> l5 = k.connectedComponents(5);
        List<Integer> l6 = k.connectedComponents(6);
        List<Integer>  l7 = k.connectedComponents(7);
        assertEquals(l2, l1); // first component
        assertEquals(l0, l2); // first component
        assertEquals(l7, l3);  // second component
        assertEquals(l6, l5);  // third component
        assertEquals(l4, l6);  // third component

    }


    @Test
    public void pyDfs(){
        DWGraph_Algo k = new DWGraph_Algo();
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < 100; i++) {
            DWGraph_DS.NodeData node = g.new NodeData(i, i);
            g.addNode(node);
        }
        k.init(g);
        g.connect(4, 1, 2);
        g.connect(1, 10, 2);
        g.connect(10, 1, 2);
        g.connect(9, 6, 6);
        List<Integer> temp = new ArrayList<>();
        temp.add(1);
        temp.add(10);
        assertTrue(temp.equals(k.pyDfs(1, g)));
        temp.clear();
        temp.add(1);
        temp.add(4);
        temp.add(10);
        assertTrue(temp.equals(k.pyDfs(4, g)));
        temp.clear();
        temp.add(6);
        temp.add(9);
        assertTrue(temp.equals(k.pyDfs(9, g)));
        temp.clear();
        temp.add(6);
        assertTrue(temp.equals(k.pyDfs(6, g)));
        temp.clear();
        temp.add(20);
        assertTrue(temp.equals(k.pyDfs(20, g)));
    }

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
    @Test
    public void ShortestPath() {
        dw_graph_algorithms ga1 = new DWGraph_Algo();
        // 3 nodes
        directed_weighted_graph a = GcreatorRandom(3,0);
        a.connect(0,1,4.0);
        a.connect(0,2,1.0);
        a.connect(2,1,1.0);
        ga1.init(a);
        assertEquals(ga1.shortestPathDist(0,1),2.0);
        ga1.getGraph().removeEdge(2,1);
        assertEquals(ga1.shortestPathDist(0,1),4.0);
        a=(GcreatorRandom(10,0));
        a.connect(0,1,4.0);
        a.connect(1,2,1.0);
        a.connect(2,3,1.0);
        a.connect(3,4,4.0);
        a.connect(4,5,1.0);
        a.connect(5,6,1.0);
        a.connect(6,7,4.0);
        a.connect(7,8,1.0);
        a.connect(8,9,1.0);
        a.connect(0,9,50);
        ga1.init(a);
        assertEquals(ga1.shortestPathDist(0,9),18.0);
        assertEquals(ga1.shortestPathDist(6,5),-1.0);
        assertEquals(ga1.shortestPathDist(1,0),-1.0);
        assertEquals(ga1.shortestPathDist(9,8),-1.0);
        assertEquals(ga1.shortestPathDist(7,9),2.0);
        assertEquals(ga1.shortestPathDist(2,5),6.0);
        assertEquals(ga1.shortestPathDist(8,0),-1.0);
        // 5 nodes
        a=(GcreatorRandom(5,0));
        a.connect(0,1,1.0);
        a.connect(1,2,1.0);
        a.connect(2,3,1.0);
        a.connect(3,4,1.0);
        a.connect(0,4,3.0);
        ga1.init(a);
        assertEquals(ga1.shortestPathDist(0,4),3);

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