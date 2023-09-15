import javax.swing.JFrame;
public class GameFrame extends JFrame{
    GameFrame(){ //constructor of thr frame
        this.add(new GamePanel()); //usual way : GamePanel panel=new GamePanel();    this.add(panel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); //will keep every component of the JFrame proportional to the window,leaving all empty spaces and oddness
        this.setLocationRelativeTo(null); //will put the window in the middle of the computer monitor
        this.setVisible(true);
    }
}