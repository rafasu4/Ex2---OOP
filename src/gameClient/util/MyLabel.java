package gameClient.util;

import javax.swing.*;

public class MyLabel extends JLabel {
     private  JTextField tf;



     public MyLabel(String s){
         super(s);
         tf = new JTextField();
     }

     public JTextField getJText(){
         return tf;
     }

     public class GuiLabel extends JFrame{
         JTextField ta;
         private JLabel l;

         public GuiLabel(){
             l = new JLabel("Time: ");
             l.setBounds(10,10,100,35);
             this.add(l);
             this.setSize(200,400);
             this.setLayout(null);
             this.setVisible(true);
             this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         }

     }


    public static void main(String[] args) {
         MyLabel l = new MyLabel("");
        GuiLabel lg = l.new GuiLabel();
    }

}
