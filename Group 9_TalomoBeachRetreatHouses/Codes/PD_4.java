package pd5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PD_4 extends JPanel implements KeyListener {
    int tileSize = 100;
    int[][] map = {
            {6,6,6,6,6,6,6},
            {1,0,0,1,2,1,0},
            {1,1,0,0,3,0,0},
            {1,0,0,4,0,5,1},
            {1,1,0,3,0,3,1}
    };

    int playerX = 1;
    int playerY = 1;

    Image playerUp, playerDown, playerLeft, playerRight;
    Image currentPlayerSprite;
    Image floorImg, wallImg, objectImg, waterImg, correctHouseImg, wrongHouseImg;

    public PD_4() {
        waterImg = new ImageIcon("images/Watertile.png").getImage();
        floorImg = new ImageIcon("images/Rocktiles.png").getImage();
        wallImg = new ImageIcon("images/tree3.jpg").getImage();
        objectImg = new ImageIcon("images/mat.jpg").getImage();
        correctHouseImg = new ImageIcon("images/house.jpg").getImage();
        wrongHouseImg = new ImageIcon("images/house.jpg").getImage();

        playerUp    = new ImageIcon("images/player_up.png").getImage();
        playerDown  = new ImageIcon("images/player_down.png").getImage();
        playerLeft  = new ImageIcon("images/player_left.png").getImage();
        playerRight = new ImageIcon("images/player_right.png").getImage();

        currentPlayerSprite = playerDown;

        JFrame frame = new JFrame("The Forest - Find the House");
        frame.setSize(map[0].length * tileSize + 16, map.length * tileSize + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);

        setFocusable(true);
        addKeyListener(this);
        
        JOptionPane.showMessageDialog(this, "Find the right house.");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                Image tile = floorImg;
                if (map[row][col] == 1) tile = wallImg;
                else if (map[row][col] == 2) tile = correctHouseImg;
                else if (map[row][col] == 3) tile = objectImg;
                else if (map[row][col] == 6) tile = waterImg;
                else if (map[row][col] == 4 || map[row][col] == 5) tile = wrongHouseImg;
                g.drawImage(tile, col * tileSize, row * tileSize, tileSize, tileSize, this);
            }
        }
        g.drawImage(currentPlayerSprite, playerX * tileSize, playerY * tileSize, tileSize, tileSize, this);
    }

    public void movePlayer(int newX, int newY) {
        if (newX < 0 || newY < 0 || newY >= map.length || newX >= map[0].length) return;

        int tile = map[newY][newX];
        if (tile == 1 || tile == 6 || tile == 2 || tile == 4 || tile == 5) {
            if (tile == 2) {
                JOptionPane.showMessageDialog(this, "You found the correct house! Entering...");
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.dispose(); 
                new PD6(); // TRANSITION TO PD6
            } else if (tile == 4 || tile == 5) {
                JOptionPane.showMessageDialog(this, "Wrong house! Try again.");
            }
            return;
        }
        playerX = newX;
        playerY = newY;
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) { currentPlayerSprite = playerUp; movePlayer(playerX, playerY - 1); }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) { currentPlayerSprite = playerDown; movePlayer(playerX, playerY + 1); }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) { currentPlayerSprite = playerLeft; movePlayer(playerX - 1, playerY); }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { currentPlayerSprite = playerRight; movePlayer(playerX + 1, playerY); }
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) { new PD_4(); }
}