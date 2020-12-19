package gameClient;

import javax.swing.*;

/**This class represents a specific adapted JLabel to this project.**/
public class MyLabel extends JLabel {
    private JTextField tf;
    /**Used to set Timer thread in GuiPanel.**/
    public long t;

    /**Constructor.
     * @param s
     */
    public MyLabel(String s) {
        super(s);
        tf = new JTextField();
    }

    /**Constructor for a countdown presentation.
     * @param t
     */
    public MyLabel(long t){
        super();
        this.t = t;
    }

    /**Returns the input text to this label.
     * @return
     */
    public JTextField getJText() {
        return tf;
    }

}
