import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random; //this statement allows to use the Random class from the java.util package
public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25; //the matrix unit of how big the components are going to be
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; //how many objects we can actually fit on the screen
    static int DELAY=75;
    final int x[]=new int[GAME_UNITS]; //how many GAME_UNITS snake's part will hold on the x-axis
    final int y[]=new int[GAME_UNITS]; //snake's y-axis
    int bodyParts=6;
    int applesEaten;
    int appleX; //in which x-axis the apple will be located
    int appleY; //apple's y-axis
    char direction='R'; //L(left),R(right),U(up),D(down)
    boolean running=false;
    Timer timer;
    Random random;
    GamePanel(){ //constructor
        random=new Random(); //instance of the Random class
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true); //is used to control which Swing components can receive keyboard focus which determines which components will respond to keyboard input at any given time
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running =true;
        timer=new Timer(DELAY,this); //this will dictate how fast the game is running
        timer.start(); //timer will start to work
    }
    @Override //overrides JPanel's method
    public void paintComponent(Graphics g){ //this Graphics class provides a way to draw shape,text and images onto JFrame,JPanel and other swing components
        super.paintComponent(g); //essential part of custom painting in Swing
        draw(g);
    }
    public void draw(Graphics g){
    if(running) {
        //to turn this into a matrix or grid to make things more comprehensible
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            //if we want to increase the matrix size we just have to increase the UNIT_SIZE
        }

        //to draw the apple
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        //to draw the snake
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) { //this is the head of the snake
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        //to keep the score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.CENTER_BASELINE,40));
        FontMetrics metrics=getFontMetrics(g.getFont());
        g.drawString("Score:"+applesEaten,(SCREEN_WIDTH-metrics.stringWidth("Score:"+applesEaten))/2,g.getFont().getSize());

    }
    else{
        gameOver(g);
    }


    }
    public void newApple(){
        appleX=random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE; //x coordinate of the apple which will appear randomly
        appleY=random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE; //this UNIT_SIZE division and multiplication is important for the apple to remain in an accurate random position which area is 1 UNIT_SIZE
        //(range) (600/25)=24*25=600 the apple will appear any x_axis and y_axis of the 600 matrix units
    }
    public void move(){
        for(int i=bodyParts;i>0;i--) { //to iterate all the body parts of the snake
            //shifting all the coordinates in this array over by one spot
            x[i]=x[i-1];
            y[i]=y[i-1];
            }
        switch(direction){
            case 'U':
                y[0]=y[0]-UNIT_SIZE; //the head of the snake will go up
                //this doesn't act as typical graphs so but like 1 2 3 4 5 so up means decreasing
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
        }
        }

    public void checkApple(){
        if((x[0]==appleX) && (y[0]==appleY)){
            applesEaten++;
            bodyParts++;
            DELAY=DELAY-5;
            newApple();
        }
    }
    public void checkCollisions(){
        //if the head of the snake touches its body
        for(int i=bodyParts;i>0;i--){
            if((x[0]==x[i]) && (y[0]==y[i])){
                 running=false;
            }
        }
        //if head touches left border
        if(x[0]<0){ //if head crosses over the 0 limit of the x_axis
            running=false;
        }
        //if head touches right border
        if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        //if head touches the top border
        if(y[0]<0){
            running=false;
        }
        //if head touches the bottom border
        if(y[0]>SCREEN_HEIGHT){
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics1=getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics1.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

        //score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.CENTER_BASELINE,40));
        FontMetrics metrics2=getFontMetrics(g.getFont());
        g.drawString("Score:"+applesEaten,(SCREEN_WIDTH-metrics2.stringWidth("Score:"+applesEaten))/2,g.getFont().getSize());
    }
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint(); //this will redraw everything as it was when the game isn't running anymore
    }
    public class MyKeyAdapter extends KeyAdapter{ //inner class
        //KeyAdapter extends KeyListener interface and provides default customization
        //it provides a convenient way to listen for keyboard events without having to implement all the methods of the KeyListener
        @Override
        public void keyPressed(KeyEvent e){
             switch (e.getKeyCode()){
                 case KeyEvent.VK_LEFT: //this will work as the > button of the keyboard
                     if(direction!='R'){ //to avoid 180-degree turn
                         direction='L';
                     }
                     break;
                 case KeyEvent.VK_RIGHT:
                     if(direction!='L'){
                         direction='R';
                     }
                     break;
                 case KeyEvent.VK_UP:
                     if(direction!='D'){
                         direction='U';
                     }
                     break;
                 case KeyEvent.VK_DOWN:
                     if(direction!='U'){
                         direction='D';
                     }
                     break;
             }
        }
    }
}