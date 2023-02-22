import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_R;

public class panel extends JPanel implements ActionListener{

    static int width =1200;
    static int height = 600;
    static int unit = 50;
    int score;
    int fx, fy;
    int totUnit = (width*height)/unit;

    int length = 3;
    char dir = 'R';

    boolean flag = false;
    Random random;
    Timer timer;
    static int DELAY = 160;
    int[] xsnake = new int[totUnit];
    int[] ysnake = new int[totUnit];




    panel(){
        this.setPreferredSize(new DimensionUIResource(width, height));
        this.setBackground(Color.black);
        this.addKeyListener((KeyListener) new myKey());
        this.setFocusable(true);
        random = new Random();

        gamestart();
    }

    public void gamestart(){
        flag = true;
        spawnfood();
        // timer to check on the game state on each 160ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnfood(){
    
        fx = random.nextInt((int) width/unit) * unit;
        fy = random.nextInt((int) height/unit) * unit;
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }
    public void draw(Graphics graphic){
        if(flag){
            // to spawn the food particle
            graphic.setColor(Color.ORANGE);
            graphic.fillOval(fx, fy, unit, unit);

            // to spawn the snake's body
            for(int i=0; i<length; i++){
                if(i==0){
                    graphic.setColor(Color.RED);
                    graphic.fillRect(xsnake[0], ysnake[0], unit, unit);
                }else{
                    graphic.setColor(Color.GREEN);
                    graphic.fillRect(xsnake[i], ysnake[i], unit, unit);
                }
            }
            //for the score display
            graphic.setColor(Color.CYAN);
            graphic.setFont(new FontUIResource("Comic sans", Font.BOLD, 40));
            FontMetrics fme = getFontMetrics(graphic.getFont());
            graphic.drawString("Score : " +score, (width - fme.stringWidth(("Score : " +score)))/2, graphic.getFont().getSize());
            
        }
        else {
            gameover(graphic);
        }
    }
    public void gameover(Graphics graphic){
        //score display
        graphic.setColor(Color.CYAN);
        graphic.setFont(new FontUIResource("Comic sans", Font.BOLD, 40));
        FontMetrics fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Score : " +score, (width - fme.stringWidth(("Score : " +score)))/2, graphic.getFont().getSize());
        
        // Game over text
        graphic.setColor(Color.RED);
        graphic.setFont(new FontUIResource("Comic sans", Font.BOLD, 80));
        FontMetrics fme1 = getFontMetrics(graphic.getFont());
        graphic.drawString("Game Over", (width - fme1.stringWidth(("Game Over")))/2, height/2);
        
        // replay prompt display
        graphic.setColor(Color.GREEN);
        graphic.setFont(new FontUIResource("Comic sans", Font.BOLD, 40));
        FontMetrics fme2 = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to replay", (width - fme2.stringWidth(("Press R to replay")))/2, height/2 - 150);
        
    }

    public void move(){
        // for all other body part

        for(int i= length; i>0 ; i--){
            xsnake[i] = xsnake[i-1];
            ysnake[i] = ysnake[i-1];
        }
        //for updating the head
        switch(dir){
            case 'U':
                ysnake[0] = ysnake[0] - unit;
                break;
            case 'D':
                ysnake[0] = ysnake[0] + unit;
                break;
            case 'L':
                xsnake[0] = xsnake[0] - unit;
                break;
            case 'R':
                xsnake[0] = xsnake[0] + unit;
                break;
        }
    }

    void check(){
        //checking if head has hit a body

        for(int i=length; i>0; i--){
            if((xsnake[0]==xsnake[i]) && (ysnake[0]==ysnake[i])){
                flag = false;
            }
        }

        // checking hit with wall

        if(xsnake[0]<0){
            flag = false;
        }
        else if(xsnake[0]>width){
            flag = false;
        }
        else if(ysnake[0]<0){
            flag = false;
        }
        else if(ysnake[0]>height){
            flag = false;
        }

        if(flag == false){
            timer.stop();
        }
    }
    public void foodeaten(){
        if((xsnake[0]==fx)&& (ysnake[0]==fy)){
            length++;
            score++;
            spawnfood();
        }
    }
    public class myKey extends KeyAdapter{

        public void keyPressed(KeyEvent k){
            switch(k.getKeyCode()){
                case VK_UP :
                    if(dir!='D'){
                        dir = 'U';
                    }
                    break;
                case VK_DOWN :
                    if(dir!='U'){
                        dir = 'D';
                    }
                    break;
                case VK_RIGHT :
                    if(dir!='L'){
                        dir = 'R';
                    }
                    break;
                case VK_LEFT :
                    if(dir!='R'){
                        dir = 'L';
                    }
                    break;
                case VK_R :
                    if(!flag){
                        score =0;
                        length = 3;
                        dir = 'R';
                        Arrays.fill(xsnake, 0);
                        Arrays.fill(ysnake, 0);
                        gamestart();
                    }
                    break;
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(flag==true){
            move();
            foodeaten();
            check();
        }
        //explicitly calls the paint function
        repaint();

    }
}
