package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static int game_level;
    private static dw_graph_algorithms k = new DWGraph_Algo();
    long start = 0;
    long dt1 = 0;
    int countMoves = 0;
    CL_Agent Agent = null;

    public static void main(String[] a) {
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        game_level = 6;
        game_service game = Game_Server_Ex2.getServer(game_level);

        String graph = game.getGraph();
        directed_weighted_graph gg = deserializer(graph); //game.getJava_Graph_Not_to_be_used();
        String pokemon = game.getPokemons();
        System.out.println(pokemon);
        System.out.println(game.toString());
        /**/// game.addAgent(0);
        init(game);
        Date date;
        long l = game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());
        int ind = 0;
        long dt = 100;
//int test=0;

        while (game.isRunning()) {
            // while (test<2){
            moveAgentsyeho(game, gg);
            game.move();

            System.out.println("time to sleep :"+dt1);

            //test ++;

            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }

                Thread.sleep(dt1);

                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }


    //game.stopGame();
    private void moveAgentsyeho(game_service game, directed_weighted_graph gg) {
        dt1=3000;
        String lg = game.move();// i move from the prev standing.
        // setting list of agents and pokemons.
        String AgentsJson = game.getAgents();
        ArrayList<CL_Agent> agentList = (ArrayList<CL_Agent>) Arena.getAgents(AgentsJson, gg);
        ArrayList<CL_Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());//updates edges to pok
        this.edgesInPok(pokemonList, gg);
        _ar.setAgents(agentList);// update where the agents.
        _ar.setPokemons(pokemonList);//update where the pokemon are.
        k.init(gg);

        int nextNodeToGo = 0;

        Iterator<CL_Agent> itAgent = agentList.iterator();
        while (itAgent.hasNext()) {

            CL_Agent currentAgent = itAgent.next();
            Agent = currentAgent;
            boolean currentAgentIsWaiting = currentAgent.getNextNode() == -1;
            //if (currentAgentIsWaiting) {//if he is on node.

                nextNodeToGo = nextNodeyeho(gg, currentAgent.getSrcNode(), game, currentAgent, pokemonList);
                double w = gg.getEdge(currentAgent.getSrcNode(), nextNodeToGo).getWeight();

                if (nextNodeToGo != -1) {
                    game.chooseNextEdge(currentAgent.getID(), nextNodeToGo);//now agent i does the move then i will go to next agent.
                }
           // }


        }

    }

    public double toWalk(CL_Agent agent, directed_weighted_graph gg,edge_data e) {

        geo_location src = gg.getNode(e.getSrc()).getLocation();
        geo_location dest = gg.getNode(e.getDest()).getLocation();
        geo_location AgentLoc = agent.getLocation();
        double way = src.distance(dest);
        double wayPok = AgentLoc.distance(dest);
        double percent = wayPok / way;
        double waytoMove = percent * e.getWeight();

        return waytoMove;

    }

    public double pokemononedge(CL_Pokemon currentPok, directed_weighted_graph gg) {
        edge_data e = currentPok.get_edge();
        double w = currentPok.get_edge().getWeight();

        geo_location src = gg.getNode(e.getSrc()).getLocation();
        geo_location dest = gg.getNode(e.getDest()).getLocation();
        geo_location PokLocation = currentPok.getLocation();
        double way = src.distance(dest);
        double wayPok = src.distance(PokLocation);
        double percent = wayPok / way;
        double waytoMove = percent * e.getWeight();

        return waytoMove;
    }

    private long howMuchToSleep(CL_Agent agent, directed_weighted_graph gg, double w) {

        double speed = agent.getSpeed();
        double timeToSleep = w / speed;
        Double time = timeToSleep * 1000;
        return time.longValue();

    }

    private void moveAgents(game_service game, directed_weighted_graph gg) {
        this.dt1 = 10000000;
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
            whereToGo(ag, _ar.getPokemons(), game, gg);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(gg, src, game);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
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

    private static int MostValuePok(directed_weighted_graph gg, List<CL_Pokemon> pokemonList, int srcAgent) {
        double maxValue = 0.0;
        CL_Pokemon MostValuePokemon = null;
        int nextNodetogo = -1;
        for (CL_Pokemon currentPok : pokemonList) {
            double currentDis = k.shortestPathDist(srcAgent, currentPok.get_edge().getSrc());
            boolean pokemonIsFree = !currentPok.isLockedIn();
            boolean thereIsWay = (currentDis != -1);

            if (pokemonIsFree && thereIsWay) {//compare the best way
                int src_pok = currentPok.get_edge().getSrc();
                double pokValue = currentPok.getValue();
                if (pokValue > maxValue) {
                    maxValue = pokValue;
                    nextNodetogo = src_pok;
                    MostValuePokemon = currentPok;
                    if (nextNodetogo == srcAgent) {
                        nextNodetogo = currentPok.get_edge().getDest();
                        MostValuePokemon.setLockedIn(true);
                        return nextNodetogo;
                    }
                }

            }
        }
        nextNodetogo = k.shortestPath(srcAgent, nextNodetogo).get(2).getKey();
        if (MostValuePokemon != null) MostValuePokemon.setLockedIn(true);
        return nextNodetogo;

    }

    private int ClosestPok(directed_weighted_graph gg, List<CL_Pokemon> pokemonList, int srcAgent, CL_Agent currentAgent) {
        double closest = Double.POSITIVE_INFINITY;
        long minDt=1000000;
        CL_Pokemon closestPokemon = null;
        int nextNodetogo = -1;
        for (CL_Pokemon currentPok : pokemonList) {
            boolean pokemonIsFree = !currentPok.isLockedIn();
            double currentDis = k.shortestPathDist(srcAgent, currentPok.get_edge().getSrc());
            boolean thereIsWay = (currentDis != -1);
            if (pokemonIsFree && thereIsWay) {//compare the best way
                int src_pok = currentPok.get_edge().getSrc();
                if (closest > currentDis) {
                    closest = currentDis;
                    nextNodetogo = src_pok;
                    closestPokemon = currentPok;
                    if (nextNodetogo == srcAgent) {// if final move.
                        currentAgent.setStep(true);
                        nextNodetogo = currentPok.get_edge().getDest();
                        closestPokemon.setLockedIn(true);
                        double w = this.pokemononedge(currentPok,gg);
                        minDt =howMuchToSleep(currentAgent,gg,w);
                        if (minDt < dt1) dt1 = minDt;
                        return nextNodetogo;
                    }
                    currentAgent.setStep(false);
                }

            }
        }// if not final move . need to jump to node.
        if (nextNodetogo != -1) {
            nextNodetogo = k.shortestPath(srcAgent, nextNodetogo).get(2).getKey();

            edge_data e =gg.getEdge(currentAgent.getSrcNode(),nextNodetogo);
            double w = this.toWalk(currentAgent,gg,e);
            minDt =howMuchToSleep(currentAgent,gg,w);
            if (minDt < dt1) dt1 = minDt;
            if (closestPokemon != null) closestPokemon.setLockedIn(true);
        }



        return nextNodetogo;

}

    private int nextNodeyeho(directed_weighted_graph gg, int srcAgent, game_service g, CL_Agent currentAgent, List<CL_Pokemon> pokemonList) {
        Iterator<CL_Pokemon> itPok = pokemonList.iterator();
        int nextNodeToGo = -1;
        double w = this.pokemononedge(itPok.next(), gg);
        System.out.println("way to go for pok is :" + w);
        nextNodeToGo = ClosestPok(gg, pokemonList, srcAgent, currentAgent);

        return nextNodeToGo;
    }


    private static int nextNode(directed_weighted_graph gg, int src, game_service g) {
        int ans = -1;
        k.init(gg);
        Collection<edge_data> ee = gg.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int) (Math.random() * s);
        int i = 0;
        while (i < r) {
            itr.next();
            i++;
        }
        ans = itr.next().getDest();
        return ans;
    }

    private void edgesInPok(List<CL_Pokemon> cl_fs, directed_weighted_graph gg) {
        for (int j = 0; j < cl_fs.size(); j++) {
            Arena.updateEdge(cl_fs.get(j), gg);
        }
    }

    private void init(game_service game) {
        String g = game.getGraph();
        directed_weighted_graph gg = deserializer(g);
        dw_graph_algorithms g1 = new DWGraph_Algo();
        String fs = game.getPokemons();
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
            //  for (int j = 0; j < cl_fs.size(); j++) {
            //    Arena.updateEdge(cl_fs.get(j), gg);
            //}
            edgesInPok(cl_fs, gg);
            // ARENA completed.
            int a = 0;
            for (; a < numberOfAgents; a++) {
                Iterator<CL_Pokemon> itPok = cl_fs.iterator();
                while (itPok.hasNext()) {
                    CL_Pokemon currentPok = itPok.next();
                    if (!currentPok.isLockedIn()) {
                        edge_data e = currentPok.get_edge();
                        int startNode = e.getSrc();
                        game.addAgent(startNode);
                        //game.chooseNextEdge(a, e.getDest());
                        a++;

                    }
                }
            }


            while (a < numberOfAgents) {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }
                game.addAgent(nn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
