package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ex2Test {

    @Test
    void moveAgents() {
        int game_level=0;
        dw_graph_algorithms ga1 = new DWGraph_Algo();
        directed_weighted_graph a = GcreatorRandom(3,0);
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
        a.connect(1,0,4.0);
        a.connect(2,1,1.0);
        a.connect(3,2,1.0);
        a.connect(4,3,4.0);
        a.connect(5,4,1.0);
        a.connect(6,5,1.0);
        a.connect(7,6,4.0);
        a.connect(8,7,1.0);
        a.connect(9,8,1.0);
        a.connect(9,0,50);
        ga1.init(a);
        Ex2 t= new Ex2();
        game_service game = Game_Server_Ex2.getServer(game_level);
        Point3D p = new Point3D(5.5,5.5,0);
        CL_Pokemon POK=new CL_Pokemon(p,1,5,0,a.getEdge(5,6));
        CL_Agent AGENT = new CL_Agent(a,5);
        ArrayList<CL_Agent> agentList = new ArrayList<>();
        ArrayList<CL_Pokemon> pokemonList=new ArrayList<>();
        agentList.add(AGENT);
        pokemonList.add(POK);
        int v = t.NextNodeToGo(a,pokemonList,AGENT.getSrcNode(),AGENT);
        assertEquals(v,6);


    }

    @Test
    void edgesInPok() {
    }

    @Test
    void toWalk() {
    }

    @Test
    void pokemonOnEdge() {
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

            geo_location p= new Point3D(i,i,0);//new DWGraph_DS.Geo_Location(i,i,0);
            n.setLocation(p);
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