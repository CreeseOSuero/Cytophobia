package Gr7Cytophobia;

import java.awt.*;
import javax.swing.*;

public class Entity {
    private int posX, posY;
    private int wt, ht;
    private JLabel entmodel;
    public Entity(JLabel l, int w, int h, int xs, int ys) {
        entmodel = l;
        wt = w;
        ht = h;
        posX = xs;
        posY = ys;
    }
    public void setPos(int x, int y) {
        posX = x;
        posY = y;
    }
    public int getX() { return posX; }
    public int getY() { return posY; }
    public void move(int xstep, int ystep, int dir) {
        switch (dir) {
            case 0:
                posX -= xstep;
                break;
            case 1:
                posY -= ystep;
                break;
            case 2:
                posX += xstep;
                break;
            case 3:
                posY += ystep;
                break;
            default:
                break;
        }
    }
    public void move(int dx, int dy) {
        posX += dx;
        posY += dy;
    }
    public void move(int dir) {
        switch (dir) {
            case 0:
                posX--;
                break;
            case 1:
                posY--;
                break;
            case 2:
                posX++;
                break;
            case 3:
                posY++;
                break;
            default:
                break;
        }
    }
    public void setWH(int w, int h) {
        wt = w;
        ht = h;
    }
    public int getWidth() {return wt;}
    public int getHeight() {return ht;}
    public boolean canMove() {
        return true;
    }
    public void updatePos(JFrame frame) {
        ((GraphPaperLayout)(frame.getContentPane().getLayout())).setConstraints(entmodel, new Rectangle(posX, posY, wt, ht));
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    public JLabel getJLabel() {
        return entmodel;
    }
    public void setJLabel(JLabel lbl) {
        entmodel = lbl;
    }
}
