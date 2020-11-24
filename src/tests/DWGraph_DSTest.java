package tests;

import api.*;
import api.edge_data;
import api.node_data;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {


    @Test
    public void getNode() {
        DWGraph_DS g = new DWGraph_DS();
        node_data n = g.new NodeData(2, 2);
        g.addNode(n);
        assertEquals(n, g.getNode(2));//checking the same reference
    }

    @Test
    //addNode, connect, getEdge
    public void connect() {
        DWGraph_DS g = new DWGraph_DS();
        node_data n = g.new NodeData(2, 2);
        node_data n1 = g.new NodeData(3, 3);
        g.addNode(n);
        g.addNode(n1);
        g.connect(2, 3, 4);
        g.connect(3, 2, 9);
        edge_data e = g.getEdge(2, 3);
        edge_data e1 = g.getEdge(3, 2);
        assertEquals(4, e.getWeight());
        assertEquals(9, e1.getWeight());
    }

    @Test
    public void getV() {
        DWGraph_DS g = new DWGraph_DS();
        node_data[] list = new DWGraph_DS.NodeData[10];
        for (int i = 0; i < 10; i++) {
            DWGraph_DS.NodeData n = g.new NodeData(i, i);
            g.addNode(n);
            list[i] = n;
        }
        Collection<node_data> pointer = g.getV();
        boolean flag = true;
        int i = 0;
        for (node_data node : pointer) {
            if (node != list[i++]) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void getE() {
        DWGraph_DS g = new DWGraph_DS();
        DWGraph_DS.NodeData n = g.new NodeData(15, 0);
        g.addNode(n);
        double[] list = new double[10];
        for (int i = 0; i <10; i++) {
            DWGraph_DS.NodeData node = g.new NodeData(i, i);
            g.addNode(node);
            g.connect(15, i, i);
            list[i] = i;
        }
       // Arrays.sort(list);
        Collection<edge_data> pointer = g.getE(15);
        boolean flag = true;
        double[] compare = new double[10];
        int i = 0;
        for (edge_data edge : pointer) {
            compare[i++] = edge.getWeight();
        }
        Arrays.sort(compare);
        for (int j = 0; j <10 ; j++) {
            if(list[j] != compare[j]){
                flag = false;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    public void remove(){
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < 100; i++) {
            node_data n = g.new NodeData(i, 0);
            g.addNode(n);
        }
        g.connect(1,10,5);
        g.connect(3,6,2);
        g.connect(5,4,23);
        g.connect(4,5,6);
        g.connect(4,6,2);
        g.connect(4,7,2);
        g.connect(4,8,2);
        assertEquals(7, g.edgeSize());
        double edge = g.removeEdge(1,10).getWeight();
        assertEquals(edge,5 );
        assertEquals(100, g.nodeSize());
        assertTrue(g.getEdge(1,10)==null);
        assertEquals(6, g.edgeSize());
        g.removeNode(4);
        assertTrue(g.getEdge(4,5)==null);
        assertTrue(g.getEdge(4,6)==null);
        assertTrue(g.getEdge(4,7)==null);
        assertEquals(1, g.edgeSize());

    }
}