package gameClient;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**This class used in order to play music while the game is running. Creates and uses a new thread in order to allow the game
 * to run along the background song.
 **/
public class SimplePlayer implements Runnable {
    /**String represents a path in the disk.*/
    private String path;

    /**Constructor.
     * @param path
     */
    public SimplePlayer(String path){
        this.path = path;
    }

    /**Uploading the song from disk and running the thread.
     * @return  flag
     */
    public boolean play(){
        boolean flag = true;
        try{
            FileInputStream fis = new FileInputStream(path);
            Player playMP3 = new Player(fis);
            playMP3.play();//Call for run method
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**Runs the thread.
     */
    @Override
    public void run() {
        play();
    }
}
