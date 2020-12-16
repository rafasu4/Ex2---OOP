package gameClient.util;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MyLabel extends JLabel {
     private  JTextField tf;
     private  ImageIO im;

     public MyLabel(String s){
         super(s);
         tf = new JTextField();
     }

     public JTextField getJText(){
         return tf;
     }

    public ImageIO getIm(){
        return im;
    }

}
