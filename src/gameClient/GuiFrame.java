package gameClient;

import api.directed_weighted_graph;
import api.game_service;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import javax.swing.*;
import java.awt.*;



public class GuiFrame extends JFrame{
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private GuiPanel myPanel;
    private long currentTime;
    private game_service game;

    public long getCurrentTime(){return currentTime;}
    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public GuiFrame(game_service game) {
        super();
        this.game = game;
        this.setTitle("Ex2 - OOP: Gotta Catch 'Em All!");
    }

    public GuiFrame(){
        super();
    }


    public void initPanel(){
        GuiPanel myPanel = new GuiPanel(this);
        Dimension dim = this.getSize();
        this.setPanel(myPanel);
        myPanel.setSize(dim.width, dim.height);
        this.add(myPanel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }


    public void update(Arena _ar, game_service game) {
        this._ar = _ar;
        this.myPanel.updateFrame(game);
    }



//
//    public void paint(Graphics g) {
//        this.myPanel.paint(g);
//    }

    public Arena get_ar() {
        return _ar;
    }

    public void set_w2f(Range2Range r) {
        this._w2f = r;
    }

    public Range2Range get_w2f() {
        return _w2f;
    }

    public void setPanel(GuiPanel myPanel) {
        this.myPanel = myPanel;
    }

    public JPanel getPanel() {
        return myPanel;
    }

}
