package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Members: Baguio, Banga, Tongcua

public class Group9Map2 extends JPanel implements KeyListener {
    JFrame frame;
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

    public Group9Map2() {

        floor1 = new ImageIcon("assets9/Floortile.png").getImage();
        wall1 = new ImageIcon("assets9/Walltile.png").getImage();
        window1 = new ImageIcon("assets9/Wallwithwindow.png").getImage();
        Fbed = new ImageIcon("assets9/Floorwithbed.png").getImage();
        Fblood = new ImageIcon("assets9/Floortilewithblood.png").getImage();
        Ftable = new ImageIcon("assets9/Floorwithtable.png").getImage();
        Wblood = new ImageIcon("assets9/Wallwithblood.png").getImage();
        Wspider = new ImageIcon("assets9/Wallwithspider.png").getImage();
        
        waterImg = new ImageIcon("assets9/Watertile.png").getImage();
        floorImg = new ImageIcon("assets9/Rocktiles.png").getImage();
        treeImg = new ImageIcon("assets9/tree3.jpg").getImage();
        objectImg = new ImageIcon("assets9/mat.jpg").getImage();
        correctHouseImg = new ImageIcon("assets9/house.jpg").getImage();
        wrongHouseImg = new ImageIcon("assets9/house.jpg").getImage();

        playerDown = new ImageIcon("assets9/front2.png").getImage();
        playerUp = new ImageIcon("assets9/back2.png").getImage();
        playerLeft = new ImageIcon("assets9/left2.png").getImage();
        playerRight = new ImageIcon("assets9/right2.png").getImage();

        keyImage = new ImageIcon("assets9/key.png").getImage();
        manananggalImage = new ImageIcon("assets9/monster.png").getImage();

        player = new Player(1,1);
        
        frame = new JFrame("PD7");
        frame.setSize(map1[0].length * tileSize + 16,
                map1.length * tileSize + 39);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
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
                else if (value == 8) tile = waterImg; // Fixed missing value 8
                else if (value == 9) tile = floorImg;
                else if (value == 10) tile = treeImg;
                else if (value == 11) tile = objectImg;
                else if (value == 12) tile = correctHouseImg;

                g.drawImage(tile, col * tileSize, row * tileSize,
                        tileSize, tileSize, this);
            }
        }

        if (!hasKey && !insideHouse) {
            g.drawImage(keyImage, keyCol * tileSize, keyRow * tileSize,
                    tileSize, tileSize, this);
        }

        if (insideHouse) {
            g.drawImage(manananggalImage, 5 * tileSize, 2 * tileSize,
                    tileSize, tileSize, this);
        }

        player.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Key: " + (hasKey ? "Collected" : "Not Found"), 20, 20);

        if (showDialogue) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(50, 320, 700, 120);
            g.setColor(Color.WHITE);
            g.drawRect(50, 320, 700, 120);

            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Player: So this is where you hide...", 70, 360);
            g.drawString("Manananggal: krrrhhh... sssshhk... tchkk...", 70, 400);
            
            Timer timer = new Timer(3000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    Menu.startNextLevel(9);
                }
            });

            timer.start();
        }
    }

//Exception Handling starts here

    public void move(int dx, int dy) {
        if (showDialogue) {
            showDialogue = false; // Dismiss dialogue on next move attempt
            repaint();
            return;
        }

        int newCol = player.getCol() + dx;
        int newRow = player.getRow() + dy;

        if (newRow >= 0 && newRow < currentMap.length &&
            newCol >= 0 && newCol < currentMap[0].length) {

            int tile = currentMap[newRow][newCol];

            // Collision check
            if (tile != 1 && tile != 2 && tile != 6 && tile != 7) {
                player.setPosition(newCol, newRow);

                if (!hasKey && newRow == keyRow && newCol == keyCol) {
                    hasKey = true;
                }

                if (hasKey && !insideHouse && newRow == houseRow && newCol == houseCol) {
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
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) { player.setDirection("up"); move(0,-1); }
        if (key == KeyEvent.VK_DOWN) { player.setDirection("down"); move(0,1); }
        if (key == KeyEvent.VK_LEFT) { player.setDirection("left"); move(-1,0); }
        if (key == KeyEvent.VK_RIGHT) { player.setDirection("right"); move(1,0); }
    }

    @Override public void keyReleased(KeyEvent e){}
    @Override public void keyTyped(KeyEvent e){}

    abstract class GameCharacter {
        protected int col, row;
        public GameCharacter(int col, int row) { this.col = col; this.row = row; }
        public int getCol(){ return col; }
        public int getRow(){ return row; }
        public void setPosition(int col, int row){ this.col = col; this.row = row; }
        public abstract void draw(Graphics g);
    }

    class Player extends GameCharacter {
        private String direction = "down";
        public Player(int col, int row){ super(col,row); }
        public void setDirection(String dir){ direction = dir; }

        @Override
        public void draw(Graphics g){
            Image sprite = playerDown;
            if(direction.equals("up")) sprite = playerUp;
            if(direction.equals("left")) sprite = playerLeft;
            if(direction.equals("right")) sprite = playerRight;

            g.drawImage(sprite, col * tileSize, row * tileSize, tileSize, tileSize, null);
        }
    }

    public static void main(String[] args) {
        new Group9Map2();
    }
}
