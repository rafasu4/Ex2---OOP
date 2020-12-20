package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

/**
 * This class is the main class in the second part, which its main method the game starts.
 */
public class Ex2 implements Runnable, ActionListener {
    private static GuiFrame _win;
    private static Arena _ar;
    private static int game_level;
    private static dw_graph_algorithms k = new DWGraph_Algo();
    private static JFrame f = new JFrame();
    private static JPanel p = new JPanel();
    private static MyLabel scene;
    private static MyLabel id;
    private static Thread client;
    private static long user_id;
    private static long start = 0;
    private static long dt1 = 0;
    private static int countMoves = 0;
    private CL_Agent Agent = null;

    public static void main(String[] a) {
        client = new Thread(new Ex2());
        startMenu();
    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(game_level);
        String graph = game.getGraph();
        //game.login(user_id);
        directed_weighted_graph gg = deserializer(graph); //game.getJava_Graph_Not_to_be_used();
        String pokemon = game.getPokemons();
        System.out.println(pokemon);
        System.out.println(game.toString());
        SimplePlayer play = new SimplePlayer("resources/Pokemon theme song.mp3");
        Thread player = new Thread(play);
        player.start();
        init(game);
        game.startGame();
        int ind = 0;
        while (game.isRunning()) {
            moveAgents(game, gg);
            game.move();
            System.out.println("time to sleep :" + dt1);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                moveAgents(game, gg);
                Thread.sleep(dt1);
                game.move();
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    private void init(game_service game) {
        String g = game.getGraph();
        directed_weighted_graph gg = deserializer(g);
        String fs = game.getPokemons();
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new GuiFrame(game);
        _win.setTitle("Ex2 - OOP: Gotta Catch 'Em All!");
        _win.setSize(1000, 700);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        _win.setLocation(dim.width / 2 - _win.getSize().width / 2, dim.height / 2 - _win.getSize().height / 2);
        _win.initPanel();
        _win.update(_ar);
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

    /**
     * i iterate my agents and give to them a misson between each move.
     * every agent gets to target the closest free pokemon.
     * each pokemon that "targeted" become to "locked him" thats mean no other agent will target him at the next move.
     *
     * @param game
     * @param gg
     */

    private void moveAgents(game_service game, directed_weighted_graph gg) {
        System.out.println("Time: " + game.timeToEnd());
        dt1 = 3000;// max of sleeping time.
        // setting list of agents and pokemons.
        String AgentsJson = game.getAgents();
        ArrayList<CL_Agent> agentList = (ArrayList<CL_Agent>) Arena.getAgents(AgentsJson, gg);
        ArrayList<CL_Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());//updates edges to pok
        this.edgesInPok(pokemonList, gg);//updating all the edges in each pok.
        _ar.setAgents(agentList);// update where the agents.
        _ar.setPokemons(pokemonList);//update where the pokemon are.
        k.init(gg);
        setAllFree(pokemonList);//after move i free all the pokemon to check who is closest to them.
        int nextNodeToGo;
        Iterator<CL_Agent> itAgent = agentList.iterator();
        while (itAgent.hasNext()) {
            CL_Agent currentAgent = itAgent.next();
            nextNodeToGo = NextNodeToGo(gg, pokemonList, currentAgent.getSrcNode(), currentAgent);
            if (nextNodeToGo != -1) {// if its -1 the agent already have a target.
                game.chooseNextEdge(currentAgent.getID(), nextNodeToGo);//now agent i does the move then i will go to next agent.
            }
        }
    }

    /**
     * this Method current agent  the closest free pokemon to him, and return
     * the node he need to go to catch him
     * dt1:how much time he need to wait to get to the nextnode/catch thepokemon.
     *
     * @param gg
     * @param pokemonList
     * @param srcAgent
     * @param currentAgent
     * @return NextNodeToGO
     */
    private int NextNodeToGo(directed_weighted_graph gg, List<CL_Pokemon> pokemonList,
                             int srcAgent, CL_Agent currentAgent) {
        //SETTINGS
        double closest = Double.POSITIVE_INFINITY;
        Long minDt = Long.MAX_VALUE;
        CL_Pokemon closestPokemon = null;
        int nextNodeTogo = -1;
        // I check what te closest free pokemon
        for (CL_Pokemon currentPok : pokemonList) {
            boolean pokemonIsFree = !currentPok.isLockedIn();
            double currentDis = k.shortestPathDist(srcAgent, currentPok.get_edge().getSrc());
            boolean thereIsWay = (currentDis != -1);
            int src_pok = currentPok.get_edge().getSrc();
            if (pokemonIsFree && thereIsWay) {

                if (closest > currentDis) { // if this pokemon is closest
                    closest = currentDis;
                    nextNodeTogo = src_pok;
                    closestPokemon = currentPok;

                    if (nextNodeTogo == srcAgent) {// if the agent on the srcPokemon node.
                        nextNodeTogo = currentPok.get_edge().getDest();
                        double w = this.pokemonOnEdge(currentPok, gg);
                        minDt = howMuchToSleep(currentAgent, gg, w);
                        if (minDt < dt1) dt1 = minDt;
                        return nextNodeTogo;
                    }
                }
            }
        }
        if (nextNodeTogo != -1) { // if the agent not on the srcPokemon node.
            nextNodeTogo = k.shortestPath(srcAgent, nextNodeTogo).get(2).getKey();// the list gives me the next node i need to go.
            edge_data e = gg.getEdge(currentAgent.getSrcNode(), nextNodeTogo);
            double w = this.toWalk(currentAgent, gg, e);
            minDt = howMuchToSleep(currentAgent, gg, w);
            if (minDt < dt1) dt1 = minDt;
            if (closestPokemon != null) closestPokemon.setLockedIn(true);
        }
        closestPokemon.setLockedIn(true);
        return nextNodeTogo;

    }

    private void edgesInPok(List<CL_Pokemon> cl_fs, directed_weighted_graph gg) {
        for (int j = 0; j < cl_fs.size(); j++) {
            Arena.updateEdge(cl_fs.get(j), gg);
        }
    }

    /**
     * in this method i check how much Wight i need to move to the next node.
     * @param agent
     * @param gg
     * @param e
     * @return wayToMove
     */

    public double toWalk(CL_Agent agent, directed_weighted_graph gg, edge_data e) {
        geo_location src = gg.getNode(e.getSrc()).getLocation();
        geo_location dest = gg.getNode(e.getDest()).getLocation();
        geo_location AgentLoc = agent.getLocation();

        double way = src.distance(dest);
        double wayAgent = AgentLoc.distance(dest);
        double percent = wayAgent / way;// how much percent of the wighte of the edge remains to walk.
        double wayToMove = percent * e.getWeight();
        return wayToMove;
    }

    /**
     * in this method i check how much Wight there is from the srcNode to the Pok itself.
     * @param currentPok
     * @param gg
     * @return wayToMove
     */

    public double pokemonOnEdge(CL_Pokemon currentPok, directed_weighted_graph gg) {
        edge_data e = currentPok.get_edge();
        double w = currentPok.get_edge().getWeight();
        geo_location src = gg.getNode(e.getSrc()).getLocation();
        geo_location dest = gg.getNode(e.getDest()).getLocation();
        geo_location PokLocation = currentPok.getLocation();
        double way = src.distance(dest);
        double wayPok = src.distance(PokLocation);
        double percent = wayPok / way;//how much percent of the wight there is from the src to the Pokemon.
        double wayToMove = percent * w;
        return wayToMove;
    }

    /**
     * after current agent get hoe much he need to walk to his nxt target this method return how much he will need to do this.
     * @param agent
     * @param gg
     * @param w
     * @return time in miliSeconds
     */
    private long howMuchToSleep(CL_Agent agent, directed_weighted_graph gg, double w) {
        double speed = agent.getSpeed();
        double timeToSleep = w / speed;
        Double time = timeToSleep * 1000;
        return time.longValue();
    }

    /**
     * this set that the all the pokemon isn't targeted by some agent.
     * @param list
     */
    private void setAllFree(List<CL_Pokemon> list) {
        Iterator<CL_Pokemon> itPok = list.iterator();
        while (itPok.hasNext()) {
            itPok.next().setLockedIn(false);
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

    public static void startMenu() {
        f.setSize(220, 200);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation(dim.width / 2 - f.getSize().width / 2, dim.height / 2 - f.getSize().height / 2);
        f.setDefaultCloseOperation(MyFrame.EXIT_ON_CLOSE);
        f.add(p);
        p.setLayout(null);
        id = new MyLabel("User ID:");
        id.setBounds(10, 20, 80, 25);
        id.getJText().setBounds(100, 20, 80, 25);
        p.add(id.getJText());
        p.add(id);

        scene = new MyLabel("Scenario:");
        scene.setBounds(10, 50, 80, 25);
        scene.getJText().setBounds(100, 50, 80, 25);
        p.add(scene);
        p.add(scene.getJText());


        JButton button = new JButton("Enter");
        button.setBounds(75, 80, 70, 25);
        button.addActionListener(new Ex2());
        p.add(button);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int scenario = Integer.parseInt(scene.getJText().getText());
            user_id = Integer.parseInt(id.getJText().getText());
            game_level = scenario;
            client.start();
        } catch (Exception er) {
            JLabel comment = new MyLabel("Invalid input!");
            comment.setBounds(60, 110, 110, 25);
            p.add(comment);
            p.repaint();
        }

    }
}
