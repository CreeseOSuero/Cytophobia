package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

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
    Image waterImg, floorImg, treeImg, objectImg, correctHouseImg;

    Player player;

    boolean hasKey = false;
    boolean insideHouse = false;
    boolean showDialogue = false;
    
    boolean isBattleActive = false;
    int currentQuestionIndex = 0;
    String[] questions = {
        "How many chambers does the heart have?",
        "What are one-way doors in the heart called?",
        "Main artery carrying oxygen-rich blood from left ventricle?",
        "Funnel-shaped chamber collecting urine from calyces?",
        "Cluster of capillaries within the nephron?",
        "Functional units of the kidney (approx 1 million)?",
        "Cells carrying oxygen, rich in hemoglobin?",
        "Long tail-like projection in a neuron?",
        "Fatty, insulating layer around axons?",
        "Small gaps in the myelin sheath?"
    };
    String[] answers = {
        "4", "valves", "aorta", "renal pelvis", "glomerulus", 
        "nephrons", "red blood cell", "axon", "myelin sheath", "nodes of ranvier"
    };

    int keyRow = 3, keyCol = 1;
    int houseRow = 5, houseCol = 4;

    public Group9Map2() {
        floor1 = new ImageIcon(Menu.getRes("/assets9/Floortile.png")).getImage();
        wall1 = new ImageIcon(Menu.getRes("/assets9/Walltile.png")).getImage();
        window1 = new ImageIcon(Menu.getRes("/assets9/Wallwithwindow.png")).getImage();
        Fbed = new ImageIcon(Menu.getRes("/assets9/Floorwithbed.png")).getImage();
        Fblood = new ImageIcon(Menu.getRes("/assets9/Floortilewithblood.png")).getImage();
        Ftable = new ImageIcon(Menu.getRes("/assets9/Floorwithtable.png")).getImage();
        Wblood = new ImageIcon(Menu.getRes("/assets9/Wallwithblood.png")).getImage();
        Wspider = new ImageIcon(Menu.getRes("/assets9/Wallwithspider.png")).getImage();
        waterImg = new ImageIcon(Menu.getRes("/assets9/Watertile.png")).getImage();
        floorImg = new ImageIcon(Menu.getRes("/assets9/Rocktiles.png")).getImage();
        treeImg = new ImageIcon(Menu.getRes("/assets9/tree3.jpg")).getImage();
        objectImg = new ImageIcon(Menu.getRes("/assets9/mat.jpg")).getImage();
        correctHouseImg = new ImageIcon(Menu.getRes("/assets9/house.jpg")).getImage();

        playerDown = new ImageIcon(Menu.getRes("/assets9/front2.PNG")).getImage();
        playerUp = new ImageIcon(Menu.getRes("/assets9/back2.PNG")).getImage();
        playerLeft = new ImageIcon(Menu.getRes("/assets9/left2.PNG")).getImage();
        playerRight = new ImageIcon(Menu.getRes("/assets9/right2.PNG")).getImage();

        keyImage = new ImageIcon(Menu.getRes("/assets9/key.png")).getImage();
        manananggalImage = new ImageIcon(Menu.getRes("/assets9/monster.png")).getImage();

        player = new Player(1, 1);

        frame = new JFrame("PD8 - Manananggal Battle");
        frame.setSize(map1[0].length * tileSize + 16, map1.length * tileSize + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                g.drawImage(tile, col * tileSize, row * tileSize, tileSize, tileSize, this);
            }
        }

        if (!hasKey && !insideHouse) {
            g.drawImage(keyImage, keyCol * tileSize, keyRow * tileSize, tileSize, tileSize, this);
        }

        if (insideHouse) {
            g.drawImage(manananggalImage, 5 * tileSize, 2 * tileSize, tileSize, tileSize, this);
        }

        player.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Key: " + (hasKey ? "Collected" : "Not Found"), 20, 25);

        if (isBattleActive) {
            drawBattleUI(g);
        } else if (showDialogue) {
            drawDialogueBox(g, "Manananggal: Answer these questions, or suffer defeat!", "Press ENTER to start the battle...");
        }
    }

    private void drawDialogueBox(Graphics g, String line1, String line2) {
        g.setColor(new Color(0, 0, 0, 230));
        g.fillRect(50, 300, 650, 150);
        g.setColor(Color.RED);
        g.drawRect(50, 300, 650, 150);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.ITALIC, 20));
        g.drawString(line1, 70, 350);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString(line2, 70, 400);
    }

    private void drawBattleUI(Graphics g) {
        g.setColor(new Color(50, 0, 0, 240));
        g.fillRect(50, 250, 700, 200);
        g.setColor(Color.WHITE);
        g.drawRect(50, 250, 700, 200);
        
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("BATTLE: Answer correctly to survive!", 70, 280);
        
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Question " + (currentQuestionIndex + 1) + "/" + questions.length, 70, 310);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(questions[currentQuestionIndex], 70, 350);
        
        g.setFont(new Font("Arial", Font.ITALIC, 14));
        g.drawString("Press 'A' to answer this question", 70, 410);
    }

    public void move(int dx, int dy) {
        if (isBattleActive || showDialogue) return; // Prevent movement during battle

        int newCol = player.getCol() + dx;
        int newRow = player.getRow() + dy;

        if (newRow >= 0 && newRow < currentMap.length && newCol >= 0 && newCol < currentMap[0].length) {
            int tile = currentMap[newRow][newCol];
            if (tile != 1 && tile != 2 && tile != 6 && tile != 7) {
                player.setPosition(newCol, newRow);

                if (!hasKey && newRow == keyRow && newCol == keyCol) {
                    hasKey = true;
                }

                if (hasKey && !insideHouse && newRow == houseRow && newCol == houseCol) {
                    insideHouse = true;
                    currentMap = map2;
                    player.setPosition(1, 1);
                    showDialogue = true;
                }
            }
        }
        repaint();
    }

    private void handleBattleAnswer() {
        String userAnswer = JOptionPane.showInputDialog(this, questions[currentQuestionIndex]);
        if (userAnswer != null && userAnswer.equalsIgnoreCase(answers[currentQuestionIndex])) {
            JOptionPane.showMessageDialog(this, "Correct! The Manananggal weakens...");
            currentQuestionIndex++;
            
            if (currentQuestionIndex >= questions.length) {
                isBattleActive = false;
                JOptionPane.showMessageDialog(this, "You have defeated the Manananggal!");
                frame.dispose();
                
                long cur = System.currentTimeMillis() - Group9Map1.startTime;
                long fastest = Long.MAX_VALUE;
                try (BufferedReader reader = new BufferedReader(new FileReader(Menu.resolveFilePath("gr9fastest.log")))) {fastest = Long.parseLong(reader.readLine());}
                catch (IOException ex) {}
                String Scur = String.format("%d min %d sec", cur/60000, (cur/1000)%60);
                String Sfastest = String.format("%d min %d sec", fastest/60000, (fastest/1000)%60);
                if(fastest == Long.MAX_VALUE) JOptionPane.showMessageDialog(null, "First finish time: "+Scur);
                else if(fastest > cur) JOptionPane.showMessageDialog(null, "New record! Fastest finish time: "+Scur);
                else JOptionPane.showMessageDialog(null, "Current finish time: "+Scur+"\nFastest finish time: "+Sfastest);
                if(fastest == Long.MAX_VALUE || fastest > cur) 
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(Menu.resolveFilePath("gr9fastest.log")))) {
                        writer.write(String.valueOf(cur));
                        writer.newLine();
                    } catch(IOException ex) {}
                
                Menu.startNextLevel(9);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Wrong! The Manananggal shrieks! Try again.");
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (showDialogue && key == KeyEvent.VK_ENTER) {
            showDialogue = false;
            isBattleActive = true;
            repaint();
            return;
        }

        if (isBattleActive && key == KeyEvent.VK_A) {
            handleBattleAnswer();
            return;
        }

        if (!isBattleActive && !showDialogue) {
            if (key == KeyEvent.VK_UP) { player.setDirection("up"); move(0, -1); }
            if (key == KeyEvent.VK_DOWN) { player.setDirection("down"); move(0, 1); }
            if (key == KeyEvent.VK_LEFT) { player.setDirection("left"); move(-1, 0); }
            if (key == KeyEvent.VK_RIGHT) { player.setDirection("right"); move(1, 0); }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    abstract class GameCharacter {
        protected int col, row;
        public GameCharacter(int col, int row) { this.col = col; this.row = row; }
        public int getCol() { return col; }
        public int getRow() { return row; }
        public void setPosition(int col, int row) { this.col = col; this.row = row; }
        public abstract void draw(Graphics g);
    }

    class Player extends GameCharacter {
        private String direction = "down";
        public Player(int col, int row) { super(col, row); }
        public void setDirection(String dir) { direction = dir; }

        @Override
        public void draw(Graphics g) {
            Image sprite = playerDown;
            if (direction.equals("up")) sprite = playerUp;
            if (direction.equals("left")) sprite = playerLeft;
            if (direction.equals("right")) sprite = playerRight;
            g.drawImage(sprite, col * tileSize, row * tileSize, tileSize, tileSize, null);
        }
    }

    public static void main(String[] args) {
        new Group9Map2();
    }
}