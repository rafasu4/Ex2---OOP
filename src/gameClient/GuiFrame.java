package gameClient;

import api.game_service;
import gameClient.util.Range2Range;

import javax.swing.*;


/**This class represents a specific adapted JFrame to this project.**/
public class GuiFrame extends JFrame{
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private GuiPanel myPanel;
    private game_service game;

    /**Constructor
     * @param game - server.
     */
    public GuiFrame(game_service game) {
        super();
        this.game = game;
    }

    /**Adds a proper panel to this GuiFrame.
     */
    public void initPanel(){
        GuiPanel myPanel = new GuiPanel(this);
        this.setPanel(myPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    public void update(Arena _ar) {
        this._ar = _ar;
        this.myPanel.updateFrame();
    }

    /**Returns the info about of the agents in this level.
     * @return _ar - Arena object
     */
    public Arena get_ar() {
        return _ar;
    }

    /**Returns the game server which in play.
     * @return game - game_service obfect
     */
    public game_service getGame(){ return game;}

    public void set_w2f(Range2Range r) {
        this._w2f = r;
    }

    public Range2Range get_w2f() {
        return _w2f;
    }

    public void setPanel(GuiPanel myPanel) {
        this.myPanel = myPanel;
    }

    public GuiPanel getPanel() { return myPanel; }

}
