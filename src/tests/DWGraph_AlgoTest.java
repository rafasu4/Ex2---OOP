package tests;

import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.node_data;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    @Test
    public void copy(){
        DWGraph_Algo k = new DWGraph_Algo();
        DWGraph_DS g = new DWGraph_DS();
        int[] gKeys = new int[10];
        int[] gCopyKeys = new int[10];
        for (int i = 0; i<10; i++){
            DWGraph_DS.NodeData node = g.new NodeData(i, i);
            g.addNode(node);
            gKeys[i] = i;
        }
        g.connect(0, 3, 5);
        g.connect(1, 4, 5);
        g.connect(9, 8 ,7);
        k.init(g);
        DWGraph_DS gCopy = (DWGraph_DS) k.copy();
        int i = 0;
        //copying all the nodes keys of the copy graph to an array
        for (node_data node: gCopy.getV()) {
            gCopyKeys[i++] = node.getKey();
        }
        Arrays.sort(gCopyKeys);
        boolean flag = true;

        for (int j = 0; j <10 ; j++) {
            //checking that copy and original graphs have the same node's keys
            if(gCopyKeys[j]!=gKeys[j]){
                flag = false;
                break;
            }
            //checking that copy and original graphs don't share the same node object
            if (g.getNode(j).equals(gCopy.getNode(j))){
                flag = false;
                break;
            }
        }
        assertTrue(flag);
        assertEquals(5, gCopy.getEdge(0,3).getWeight());
        assertEquals(5, gCopy.getEdge(1,4).getWeight());
        assertEquals(7, gCopy.getEdge(9,8).getWeight());
        //checking that copy and original graphs don't share the same edge object
        assertFalse(g.getEdge(0,3).equals(gCopy.getEdge(0,3)));
    }

}