package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**This class represents a specific adapted JPanel to this project.**/
public class GuiPanel extends JPanel {
    private final GuiFrame myFrame;
    private static MyLabel l;


    /**Constructor.
     * @param myFrame - every GuiFrame has an instance of GuiPanel.
     */
    public GuiPanel(GuiFrame myFrame){
        super();
        this.myFrame = myFrame;
        this.setLayout(null);
        myFrame.setPanel(this);
        myFrame.add(this);
    }

    void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = myFrame.get_ar().getGraph();
        myFrame.set_w2f(Arena.w2f(g,frame));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPokemons(g);
        drawGraph(g);
        drawAgents(g);
        drawInfo(g);
    }

    /**Starts a new thread that represents the remaining time till the end of the game
     */
    public void timer() {
        JPanel p = new JPanel();
        p.setLayout(null);
        this.myFrame.add(p);
        l = new MyLabel(myFrame.getGame().timeToEnd());
        l.setBounds(10,10,500,25);
        p.add(l);
        //creating an anonymous ActionListener
        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long seconds = l.t;
                seconds--;
                long second = TimeUnit.SECONDS.toSeconds(seconds)
                        - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
                l.setText("Time Remaining: " + second + " Second(s)");
            }
        });
        t.start();
    }

    /**
     *
     * @param g
     */
    public void drawInfo(Graphics g) {
        java.util.List<String> str = myFrame.get_ar().get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }
    }

    /**Paints a graph on GuiPanel. Given method from the OOP course.
      * @param g
     */
    public void drawGraph(Graphics g) {
        directed_weighted_graph gg = myFrame.get_ar().getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.DARK_GRAY);
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    /**Paints a pokemon on the graph in an image form. Given method from the OOP course with some changes of the look of the pokemon.
     * @param g
     */
    public void drawPokemons(Graphics g) {
        java.util.List<CL_Pokemon> fs = myFrame.get_ar().getPokemons();
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();
            while(itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=20;
                if(c!=null) {
                    geo_location fp = myFrame.get_w2f().world2frame(c);
                    String path = "resources/004.png";
                    try {
                        BufferedImage bi = ImageIO.read(new File(path));
                        g.drawImage(bi,(int)fp.x()-r, (int)fp.y()-r, null );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**Paints an agent on the graph in an image form. Given method from the OOP course with some changes of the look of the agent.
     * @param g
     */
    public void drawAgents(Graphics g) {
        List<CL_Agent> rs = myFrame.get_ar().getAgents();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r=8;
            i++;
            if(c!=null) {
                geo_location fp = myFrame.get_w2f().world2frame(c);
                String path = "resources/001.jpg";
                try {
                    BufferedImage bi = ImageIO.read(new File(path));
                    g.drawImage(bi, (int)fp.x()-r, (int)fp.y(),null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**Paints a node on the graph. Given method from the OOP course.
     * @param n - node_data object
     * @param r - a number for more accurate location
     * @param g
     */
    public void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = myFrame.get_w2f().world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }

    /**Paints a node on the graph. Given method from the OOP course.
     * @param e - edge_data object
     * @param g
     */
    public void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = myFrame.get_ar().getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = myFrame.get_w2f().world2frame(s);
        geo_location d0 = myFrame.get_w2f().world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }


}
