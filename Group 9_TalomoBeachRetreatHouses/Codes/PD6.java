package pd5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Members: Baguio, Banga, Tongcua

public class PD6 extends JPanel implements KeyListener {

    int tileSize = 80;

    int[][] map1 = {
            {1,1,1,1,1,1,1,1,1,1},
            {6,3,0,0,0,0,0,5,3,1},
            {6,5,0,4,0,0,0,0,0,7},
            {1,0,4,0,0,0,0,0,0,1},
            {7,0,0,0,0,0,4,0,0,6},
            {1,1,2,2,0,0,2,2,1,1}
    };

    int[][] map2 = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,0,4,0,4,0,0,3,0,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,0,4,0,0,0,0,4,0,1},
            {1,0,0,0,4,0,0,0,4,1},
            {1,1,1,1,1,1,1,1,1,1}
    };

    int[][] currentMap = map1;

    Image floor1, wall1, window1;
    Image Fbed, Fblood, Ftable, Wblood, Wspider;
    Image playerDown, playerUp, playerLeft, playerRight;
    Image keyImage, manananggalImage;
    Image floorImg, treeImg, objectImg, waterImg;
    Image correctHouseImg, wrongHouseImg;
    
    Player player;

    boolean hasKey = false;
    boolean insideHouse = false;
    boolean showDialogue = false;

    int keyRow = 3;
    int keyCol = 1;

    int houseRow = 5;
    int houseCol = 4;

    public PD6() {

        floor1 = new ImageIcon("images/Floortile.png").getImage();
        wall1 = new ImageIcon("images/Walltile.png").getImage();
        window1 = new ImageIcon("images/Wallwithwindow.png").getImage();
        Fbed = new ImageIcon("images/Floorwithbed.png").getImage();
        Fblood = new ImageIcon("images/Floortilewithblood.png").getImage();
        Ftable = new ImageIcon("images/Floorwithtable.png").getImage();
        Wblood = new ImageIcon("images/Wallwithblood.png").getImage();
        Wspider = new ImageIcon("images/Wallwithspider.png").getImage();
        
        waterImg = new ImageIcon("images/Watertile.png").getImage();
        floorImg = new ImageIcon("images/Rocktiles.png").getImage();
        treeImg = new ImageIcon("images/tree3.jpg").getImage();
        objectImg = new ImageIcon("images/mat.jpg").getImage();
        correctHouseImg = new ImageIcon("images/house.jpg").getImage();
        wrongHouseImg = new ImageIcon("images/house.jpg").getImage();

        playerDown = new ImageIcon("images/player_down.png").getImage();
        playerUp = new ImageIcon("images/player_up.png").getImage();
        playerLeft = new ImageIcon("images/player_left.png").getImage();
        playerRight = new ImageIcon("images/player_right.png").getImage();

        keyImage = new ImageIcon("images/key.png").getImage();
        manananggalImage = new ImageIcon("images/monster.png").getImage();

        player = new Player(1,1);
        
        JFrame frame = new JFrame("PD6");
        frame.setSize(map1[0].length * tileSize + 16,
                map1.length * tileSize + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < currentMap.length; row++) {
            for (int col = 0; col < currentMap[row].length; col++) {

                Image tile = floor1;

                int value = currentMap[row][col];

                if (value == 1) tile = wall1;
                else if (value == 2) tile = window1;
                else if (value == 3) tile = Fbed;
                else if (value == 4) tile = Fblood;
                else if (value == 5) tile = Ftable;
                else if (value == 6) tile = Wblood;
                else if (value == 7) tile = Wspider;
                else if (value == 8) tile = waterImg;
                else if (value == 9) tile = floorImg;
                else if (value == 10) tile = treeImg;
                else if (value == 11) tile = objectImg;
                else if (value == 12) tile = correctHouseImg;

                g.drawImage(tile, col * tileSize, row * tileSize,
                        tileSize, tileSize, this);
            }
        }

        if (!hasKey && !insideHouse) {
            g.drawImage(keyImage,
                    keyCol * tileSize,
                    keyRow * tileSize,
                    tileSize, tileSize, this);
        }

        if (insideHouse) {
            g.drawImage(manananggalImage,
                    5 * tileSize,
                    2 * tileSize,
                    tileSize, tileSize, this);
        }

        player.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Key: " + (hasKey ? "Collected" : "Not Found"), 20, 20);

        if (showDialogue) {
            g.setColor(Color.BLACK);
            g.fillRect(100, 350, 600, 120);
            g.setColor(Color.WHITE);
            g.drawRect(100, 350, 600, 120);

            g.drawString("Player: So this is where you hide...", 120, 380);
            g.drawString("Manananggal: krrrhhh... sssshhk... tchkk...", 120, 410);
        }
    }

    public void move(int dx, int dy) {

        if (showDialogue) return;

        int newCol = player.getCol() + dx;
        int newRow = player.getRow() + dy;

        if (newRow >= 0 && newRow < currentMap.length &&
                newCol >= 0 && newCol < currentMap[0].length) {

            int tile = currentMap[newRow][newCol];

            if (tile != 1 && tile != 2 && tile != 5 && tile != 6 && tile != 7) {

                player.setPosition(newCol, newRow);

                if (!hasKey && newRow == keyRow && newCol == keyCol) {
                    hasKey = true;
                }

                if (hasKey && !insideHouse &&
                        newRow == houseRow && newCol == houseCol) {

                    insideHouse = true;
                    currentMap = map2;
                    player.setPosition(1,1);
                    showDialogue = true;
                }
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player.setDirection("up");
            move(0,-1);
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player.setDirection("down");
            move(0,1);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.setDirection("left");
            move(-1,0);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setDirection("right");
            move(1,0);
        }
    }

    @Override public void keyReleased(KeyEvent e){}
    @Override public void keyTyped(KeyEvent e){}


    abstract class Character {
        protected int col, row;

        public Character(int col, int row) {
            this.col = col;
            this.row = row;
        }

        public int getCol(){ return col; }
        public int getRow(){ return row; }

        public void setPosition(int col, int row){
            this.col = col;
            this.row = row;
        }

        public abstract void draw(Graphics g);
    }

    class Player extends Character {

        private String direction = "down";

        public Player(int col, int row){
            super(col,row);
        }

        public void setDirection(String dir){
            direction = dir;
        }

        @Override
        public void draw(Graphics g){

            Image sprite = playerDown;

            if(direction.equals("up")) sprite = playerUp;
            if(direction.equals("left")) sprite = playerLeft;
            if(direction.equals("right")) sprite = playerRight;

            g.drawImage(sprite,
                    col * tileSize,
                    row * tileSize,
                    tileSize,
                    tileSize,
                    null);
        }
    }

    public static void main(String[] args) {
        new PD6();
    }
}