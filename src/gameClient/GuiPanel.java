package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class GuiPanel extends JPanel {
    private final GuiFrame myFrame;


    public GuiPanel(GuiFrame myFrame){
        super();
        this.myFrame = myFrame;
        myFrame.setPanel(this);
        this.setBounds(0,0,100,100);
        //this.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        myFrame.add(this);
    }

        void updateFrame(game_service game) {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = myFrame.get_ar().getGraph();
        myFrame.set_w2f(Arena.w2f(g,frame));
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int w = dimension.width;
        int h = dimension.height;
        g.clearRect(0, 0, w, h);
        drawPokemons(g);
        drawGraph(g);
        drawAgents(g);
        drawInfo(g);
    }

    public void drawInfo(Graphics g) {
        java.util.List<String> str = myFrame.get_ar().get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }

    }
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
    public void drawPokemons(Graphics g) {
        java.util.List<CL_Pokemon> fs = myFrame.get_ar().getPokemons();
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();
            while(itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=20;
                g.setColor(Color.green);
                if(f.getType()<0) {g.setColor(Color.orange);}
                if(c!=null) {
                    geo_location fp = myFrame.get_w2f().world2frame(c);
                    String path = "resources/004.png";
                    try {
                        BufferedImage bi = ImageIO.read(new File(path));
                        g.drawImage(bi,(int)fp.x()-r, (int)fp.y()-r, null );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                 //   g.drawImage(bi,(int)fp.x()-r, (int)fp.y()-r, null );//g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }
    public void drawAgents(Graphics g) {
        List<CL_Agent> rs = myFrame.get_ar().getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r=8;
            i++;
            if(c!=null) {
                geo_location fp = myFrame.get_w2f().world2frame(c);
                g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
            }
        }
    }
    public void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = myFrame.get_w2f().world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }
    public void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = myFrame.get_ar().getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = myFrame.get_w2f().world2frame(s);
        geo_location d0 = myFrame.get_w2f().world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }


}
