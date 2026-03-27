package Gr7Cytophobia;

import javax.swing.*;

public class Player extends Entity {
    private boolean mobile;
    private ImageIcon sprites[];
    private int dir;
    private int state;
    
    public Player(JLabel l, int xs, int ys, ImageIcon s[]) {
        super(l, 2, 2, xs, ys);
        mobile = true;
        sprites = s;
        changeDirAndState(3, 0);
    }
    
    public void changeDirAndState(int d, int s) {
        dir = d;
        state = s;
        getJLabel().setIcon(sprites[d*3+s]);
    }
    public void changeDirAndState(int d) {
        changeDirAndState(d, (state+1)%3);
    }
    
    public int getDir() {return dir;}
    public int getState() {return state;}
    
    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        if((dx - getX()) > 0) changeDirAndState(2, (state+1)%3);
        else if((dx - getX()) < 0) changeDirAndState(0, (state+1)%3);
        else if((dy - getY()) > 0) changeDirAndState(3, (state+1)%3);
        else changeDirAndState(1, (state+1)%3);
    }
    
    @Override
    public void move(int dir) {
        super.move(dir);
        changeDirAndState(dir, (state+1)%3);
    }
    
    @Override
    public boolean canMove() {
        return mobile;
    }
    
    public void setMobility(boolean m) {
        mobile = m;
    }
}
