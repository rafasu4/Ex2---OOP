package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import api.game_service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2 implements Runnable  {
    private static MyFrame _win;
    private static Arena _ar;
    private static int game_level;
    private static dw_graph_algorithms k = new DWGraph_Algo();
    public static void main(String[] a) {
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        game_level = 0;
        game_service game = Game_Server_Ex2.getServer(game_level);
        String graph = game.getGraph();
        directed_weighted_graph gg = deserializer(graph); //game.getJava_Graph_Not_to_be_used();
        String pokemon = game.getPokemons();
        System.out.println(pokemon);
        System.out.println(game.toString());
       /**/// game.addAgent(0);
        init(game);
        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=100;
        while(game.isRunning()){
            moveAgents(game, gg);
            try {
                if(ind%1==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
        }
        //game.stopGame();

    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            whereToGo(ag, _ar.getPokemons(), game, gg);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if(dest==-1) {
                dest = nextNode(gg, src, game);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
            }
        }
    }

    private static void whereToGo(CL_Agent ag, List<CL_Pokemon> ffs, game_service game, directed_weighted_graph g) {
        k.init(g);
        Iterator<CL_Pokemon> itPoke = ffs.listIterator();
        while (itPoke.hasNext()) {
            CL_Pokemon currentPoke = itPoke.next();
            if (!currentPoke.isLockedIn()) {
                try {
                    double newPath = k.shortestPathDist(ag.getSrcNode(), currentPoke.get_edge().getDest());
                    double curPath = k.shortestPathDist(ag.getNextNode(), ag.getSrcNode());
                    if (newPath != -1 && newPath < curPath) {
                        game.chooseNextEdge(ag.getID(), currentPoke.get_edge().getDest());
                        ag.get_curr_fruit().setLockedIn(false);
                        ag.set_curr_fruit(currentPoke);
                        currentPoke.setLockedIn(true);
                    }
                } catch (NullPointerException e) {
                }
            }
        }
    }

    private static int nextNode(directed_weighted_graph gg, int src, game_service g) {
        int ans = -1;
        k.init(gg);
        Collection<edge_data> ee = gg.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = deserializer(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int numberOfAgents = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            //updating each pokemon edge location
            for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg); }
            cl_fs = Arena.json2Pokemons(game.getPokemons());
            int a = 0;
            int i = 0;
            for(;a<numberOfAgents;a++) {
                Iterator<CL_Pokemon> itPok = _ar.getPokemons().iterator();
                if (itPok.hasNext())
                    while (itPok.hasNext()) {
                        CL_Pokemon current = itPok.next();
                        if (!current.isLockedIn()) {
                            current.setLockedIn(true);
                            int startingNode;
                            if (current.getType() < 0) {
                                startingNode = Math.max(current.get_edge().getSrc(), current.get_edge().getDest());
                                game.addAgent(startingNode);
                                game.chooseNextEdge(i, Math.min(current.get_edge().getSrc(), current.get_edge().getDest()));
                                i++;
                                break;
                            }
                            else{
                                startingNode = Math.min(current.get_edge().getSrc(), current.get_edge().getDest());
                                game.addAgent(startingNode);
                                game.chooseNextEdge(i, Math.max(current.get_edge().getSrc(), current.get_edge().getDest()));
                                i++;
                                break;
                            }
                        }
                    }
            }
            while(a<numberOfAgents){
                int ind = a%cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }

    //method to creat a graph object from Json graph
    public directed_weighted_graph deserializer(String j) {
        directed_weighted_graph g = new DWGraph_DS();
        boolean flag = false;
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class, new DWGraph_DSJsonDeserializer());
            Gson gson = builder.create();
            g = gson.fromJson(j, DWGraph_DS.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag) {
            return null;
        }
        return g;
    }
}
