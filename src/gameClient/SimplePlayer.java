package gameClient;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**This class used in order to play music while the game is running**/
public class SimplePlayer implements Runnable {
    private String path;

    public SimplePlayer(String path){
        this.path = path;
    }

    public void play(){
        try{
            FileInputStream fis = new FileInputStream(path);
            Player playMP3 = new Player(fis);
            playMP3.play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        play();
    }
}
