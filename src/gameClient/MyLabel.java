package gameClient;

import javax.swing.*;

/**This class represents a specific adapted JLabel to this project.**/
public class MyLabel extends JLabel {
    private JTextField tf;


    /**Constructor.
     * @param s
     */
    public MyLabel(String s) {
        super(s);
        tf = new JTextField();
    }


    /**Returns the input text to this label.
     * @return
     */
    public JTextField getJText() {
        return tf;
    }

}
