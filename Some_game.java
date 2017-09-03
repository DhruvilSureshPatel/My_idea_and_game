package some;

import java.awt.*;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Some_game extends JFrame implements KeyListener{
    /*
    Idea is there will be a player and a bot.
    Both will play a snake game usually.
    Rules:: 1) player should not eat bot's food and bot will not eat player's food.
                if player eats bot's food , it's body will loose 2 balls.
            2) player should not touch bot's body nor even bot should touch player's body.
                if touched then who touches others body will be out.
            3) player wins if one of them gets double size the other.
    */
    
    int height = 500 , width = 500 , fx, fy, botScore = 0 , score = 0, ten = 5 , ffx , ffy;
    ArrayList<Integer> xbody = new ArrayList<>() , ybody = new ArrayList<>() , xxbody = new ArrayList<>() , yybody = new ArrayList<>();
    char pressed = 'd', prev = 'd';
    
    void run(Some_game sg){
        sg.setSize(width , height);
        sg.setVisible(true);
        sg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sg.initialize();
        sg.addKeyListener(sg);
        sg.makeFood();
        sg.makeFoood();
    }
    
    boolean botSelfOut(int x , int y){   //self out checking
        for(int i=0 ; i<xbody.size() ; i++){
            int xx = xbody.get(i).intValue() , yy = ybody.get(i).intValue();
            if(x==xx && y==yy){
                return true;
            }
        }
      
        return false;
    }
    
    boolean botOutByTouch(int x , int y){
        for(int i=0 ; i<xxbody.size() ; i++){
            int xx = xxbody.get(i).intValue() , yy = yybody.get(i).intValue();
            if(x==xx && y==yy){
                return true;
            }
        }
      
        return false;
    }
    
    int[] next(int i , int j){
        double prevDis = dis(i , j , fx , fy);
        int decision , x = i , y = j;
        boolean[] a = new boolean[4];
        while(true){
            decision = random(0 , 3);
            int prevx = x , prevy = y;
            switch(decision){
                case 0:
                    y += ten;
                    break;
                case 1:
                    x += ten;
                    break;
                case 2:
                    y -= ten;
                    break;
                case 3:
                    x -= ten;
                    break;
            }
            
            boolean k = false;
            for(int jj=0 ; jj<4 ; jj++){
                if(a[jj]==false){
                    k = true;
                    break;
                }
            }
            
            if(!k || botOutByTouch(x , y)){
               JOptionPane.showMessageDialog(null, "You Won!!!!!");
               System.exit(0);
            }
            
            a[decision] = true;
            
            if(x<0 || x>width || y<0 || y>height || botSelfOut(x , y)){
                x = prevx;
                y = prevy;
                continue;
            }
            
            double newDis = dis(x , y , fx , fy);
            if(newDis<prevDis){
                break;
            }
            else{
                x = prevx;
                y = prevy;
            }
        }
        
        return new int[]{x , y};
    }
    
    double dis(int x , int y , int xx , int yy){
        return Math.sqrt( ((xx - x) * (xx - x)) + ( (yy - y) * (yy - y) ) );
    }
    
    public void paintComponent(){}
    
    public void paint(Graphics g){
        Dimension d = getSize();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, d.width, d.height);
        
        g.setColor(Color.BLACK);
        g.drawString("*" , fx , fy);
        g.drawString("#" , ffx , ffy);
        
        int oldx = xbody.get(0).intValue() , oldy = ybody.get(0).intValue() , x, y;
        int[] a = next(oldx , oldy);
        x = a[0];
        y = a[1];
        
        if (x > width || x < 0 || y < 0 || y > height || botSelfOut(x , y) || botOutByTouch(x , y)) {
            x = width / 2;
            y = height / 2;
            JOptionPane.showMessageDialog(null, "You Won5!!!!!!");
            System.exit(0);
        }
        
        xbody.set(0 , x);
        ybody.set(0 , y);
        
        for(int i=1 ; i<xbody.size() ; i++){
            int newx = xbody.get(i).intValue() , newy = ybody.get(i).intValue();
            xbody.set(i , oldx);
            ybody.set(i , oldy);
            oldx = newx;
            oldy = newy;
        }
        
        for(int i=0 ; i<xbody.size() ; i++){
            g.drawString("~", xbody.get(i).intValue() , ybody.get(i).intValue());
        }
        
        if (x == fx && y == fy) {
            botScore++;
            makeFood();
            g.drawString("*", fx, fy);
            xbody.add(oldx);
            ybody.add(oldy);
        }
        
        
        
        
        oldx = xxbody.get(0).intValue();
        oldy = yybody.get(0).intValue();
        x = oldx;
        y = oldy;
        if (pressed == 'd') {
            if (prev == 'a') {
                pressed = 'a';
                x -= ten;
            } else {
                x += ten;
            } 
        } else if (pressed == 'w') {
            if (prev == 's') {
                pressed = 's';
                y += ten;
            } else {
                y -= ten;
            }
        } else if (pressed == 'a') {
            if (prev == 'd') {
                pressed = 'd';
                x += ten;
            } else {
                x -= ten;
            }
        } else if (pressed == 's') {
            if (prev == 'w') {
                pressed = 'w';
                y -= ten;
            } else {
                y += ten;
            }
        }
        
        boolean outByScore = false;
        if (x >= width || x < 0 || y < 0 || y >= height || botSelfOut(x , y) || botOutByTouch(x , y) || (x==fx && y==fy) || (score>8 && score<=(botScore/2))) {
            x = width / 2;
            y = height / 2;
            pressed = 't';
            if(score>8 && score<=(botScore/2)){
                JOptionPane.showMessageDialog(null, "You lost Fucker!!! Fuck offfff!!!!\n(out by score)");
                System.exit(0);
            }
            JOptionPane.showMessageDialog(null, "You lost Fucker!!! Fuck offfff!!!!");
            System.exit(0);
        }
        
        xxbody.set(0 , x);
        yybody.set(0 , y);
        
        for(int i=1 ; i<xxbody.size() ; i++){
            int newx = xxbody.get(i).intValue() , newy = yybody.get(i).intValue();
            xxbody.set(i , oldx);
            yybody.set(i , oldy);
            oldx = newx;
            oldy = newy;
        }
        
        for(int i=0 ; i<xxbody.size() ; i++){
            g.drawString("+", xxbody.get(i).intValue() , yybody.get(i).intValue());
        }
        
        if (x == ffx && y == ffy) {
            score++;
            makeFoood();
            g.drawString("#", ffx, ffy);
            xxbody.add(oldx);
            yybody.add(oldy);
        }
        
        prev = pressed;
        
        
        
        
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Snake_without_body.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        repaint();
    }

    int random(int i, int j) {
        Random r = new Random();
        int num = r.nextInt((j - i) + 1) + i;
        return num;
    }
    
    boolean check(int x , int y){  // to prevent food location inside body
        for(int i=0 ; i<xbody.size() ; i++){
            int xx = xbody.get(i).intValue() , yy = ybody.get(i).intValue();
            if(x==xx && y==yy){
                return false;
            }
        }
        
        for(int i=0 ; i<xxbody.size() ; i++){
            int xx = xxbody.get(i).intValue() , yy = yybody.get(i).intValue();
            if(x==xx && y==yy){
                return false;
            }
        }
        
        return true;
    }

    void makeFood() {
        int x = random(0, height / ten) * ten, y = random(0, width / ten) * ten;
        while(!check(x , y)){
            x = random(0, height / ten) * ten;
            y = random(0, width / ten) * ten;
        }

        fx = x;
        fy = y;
    }
    
    void makeFoood() {
        int x = random(0, height / ten) * ten, y = random(0, width / ten) * ten;
        while(!check(x , y)){
            x = random(0, height / ten) * ten;
            y = random(0, width / ten) * ten;
        }

        ffx = x;
        ffy = y;
    }
    
    void initialize() {
        xbody.add(0);
        ybody.add(0);
        xxbody.add(100);
        yybody.add(100);
        fx = 2;
        fy = 2;
        
        ffx = 150;
        ffy = 250;
    }
    
    public void keyTyped(KeyEvent e) {
        pressed = e.getKeyChar();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }    
}
