package gr5cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PD6 implements KeyListener {

    JFrame frame;

    // ====== SETTINGS ======
    int mapWidth = 32;
    int mapHeight = 12;   // matches your actual rows
    int tileSize = 60;    // square tiles

    int frameWidth = mapWidth * tileSize;
    int frameHeight = mapHeight * tileSize;

    // ====== IMAGES ======
    ImageIcon wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8;
    ImageIcon floor, bed1, bed2, sky, couch, shelf, carpet, table, plant, paper;
    ImageIcon goldkey, safe;

    ImageIcon frontS, frontW, backS, backW, leftS, leftW, rightS, rightW;

    JLabel[] tiles;
    JLabel[] character;

    int[] mapLayout;

    int characterPosition;
    int characterMode = 0;
    int direction;

    int keyPosition;
    int safePosition;
    boolean safeUnlocked = false;

    public PD6() {

        frame = new JFrame("PDBL");

        characterPosition = (mapHeight / 2) * mapWidth + (mapWidth / 2);

        // ===== LOAD IMAGES (scaled properly) =====
        wall1 = load("Images/wall1.jpg");
        wall2 = load("Images/wall2.jpg");
        wall3 = load("Images/wall3.jpg");
        wall4 = load("Images/wall4.jpg");
        wall5 = load("Images/wall5.jpg");
        wall6 = load("Images/wall6.jpg");
        wall7 = load("Images/wall7.jpg");
        wall8 = load("Images/wall8.jpg");

        floor = load("Images/floor.png");
        bed1 = load("Images/bed1.png");
        bed2 = load("Images/bed2.png");
        sky = load("Images/sky.jpg");

        couch = load("Images/couch.png");
        shelf = load("Images/shelf.png");
        carpet = load("Images/carpet.png");
        table = load("Images/table.png");
        plant = load("Images/plant.png");
        paper = load("Images/paper.png");

        goldkey = load("Images/goldkey.png");
        safe = load("Images/safe.png");

        frontS = load("Images/plr_10.PNG");
        frontW = load("Images/plr_11.PNG");
        backS = load("Images/plr_30.PNG");
        backW = load("Images/plr_31.PNG");
        leftS = load("Images/plr_00.PNG");
        leftW = load("Images/plr_01.PNG");
        rightS = load("Images/plr_20.PNG");
        rightW = load("Images/plr_21.PNG");

        // ===== MAP =====
        mapLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,6,4,4,4,4,4,4,4,4,4,4,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,12,1,1,1,1,1,1,1,1,13,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,14,1,1,19,1,1,17,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,11,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,15,1,1,1,1,18,16,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,9,11,11,11,11,11,11,11,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };

        tiles = new JLabel[mapLayout.length];
        character = new JLabel[mapLayout.length];

        for (int i = 0; i < mapLayout.length; i++) {
            tiles[i] = new JLabel();
            character[i] = new JLabel();
            character[i].setOpaque(false);

            switch (mapLayout[i]) {
                case 1 -> tiles[i].setIcon(floor);
                case 2 -> tiles[i].setIcon(bed1);
                case 3 -> tiles[i].setIcon(bed2);
                case 4 -> tiles[i].setIcon(wall1);
                case 5 -> tiles[i].setIcon(wall2);
                case 6 -> tiles[i].setIcon(wall3);
                case 7 -> tiles[i].setIcon(wall4);
                case 8 -> tiles[i].setIcon(wall5);
                case 9 -> tiles[i].setIcon(wall6);
                case 10 -> tiles[i].setIcon(wall7);
                case 11 -> tiles[i].setIcon(wall8);
                case 12 -> tiles[i].setIcon(couch);
                case 13 -> tiles[i].setIcon(shelf);
                case 14 -> tiles[i].setIcon(carpet);
                case 15 -> tiles[i].setIcon(table);
                case 16 -> tiles[i].setIcon(plant);
                case 17 -> { tiles[i].setIcon(goldkey); keyPosition = i; }
                case 18 -> { tiles[i].setIcon(safe); safePosition = i; }
                case 19 -> tiles[i].setIcon(paper);
                default -> tiles[i].setIcon(sky);
            }
        }

        character[characterPosition].setIcon(frontS);
    }

    private ImageIcon load(String path) {
        Image img = new ImageIcon(path).getImage();
        Image scaled = img.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public boolean isWall(int index) {
        if (index < 0 || index >= mapLayout.length) return true;

        int tile = mapLayout[index];

        if (tile >= 4 && tile <= 11) return true;
        if (tile == 2 || tile == 3 || tile == 12 || tile == 13 ||
            tile == 15 || tile == 16) return true;

        if (tile == 18 && !safeUnlocked) return true;

        return false;
    }

    public void setFrame() {

        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(frameWidth, frameHeight));

        for (int i = 0; i < tiles.length; i++) {

            int x = (i % mapWidth) * tileSize;
            int y = (i / mapWidth) * tileSize;

            tiles[i].setBounds(x, y, tileSize, tileSize);
            pane.add(tiles[i], JLayeredPane.DEFAULT_LAYER);

            character[i].setBounds(x, y, tileSize, tileSize);
            pane.add(character[i], JLayeredPane.PALETTE_LAYER);
        }

        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
    }

    @Override
public void keyPressed(KeyEvent e) {
    int nextPos = characterPosition;
    ImageIcon nextIcon = null;

    switch (e.getKeyCode()) {
        case KeyEvent.VK_RIGHT -> {
            direction = 3;
            if ((characterPosition % mapWidth) < mapWidth - 1) nextPos += 1;
            nextIcon = (characterMode == 0) ? rightW : rightS;
        }
        case KeyEvent.VK_LEFT -> {
            direction = 2;
            if ((characterPosition % mapWidth) > 0) nextPos -= 1;
            nextIcon = (characterMode == 0) ? leftW : leftS;
        }
        case KeyEvent.VK_DOWN -> {
            direction = 1;
            if ((characterPosition / mapWidth) < mapHeight - 1) nextPos += mapWidth;
            nextIcon = (characterMode == 0) ? backW : backS;
        }
        case KeyEvent.VK_UP -> {
            direction = 0;
            if ((characterPosition / mapWidth) > 0) nextPos -= mapWidth;
            nextIcon = (characterMode == 0) ? frontW : frontS;
        }
    }

    // Handle pushing key
    if (nextPos == keyPosition) {
        int pushPos = switch (direction) {
            case 0 -> keyPosition - mapWidth;
            case 1 -> keyPosition + mapWidth;
            case 2 -> keyPosition - 1;
            case 3 -> keyPosition + 1;
            default -> keyPosition;
        };

        // Prevent pushing off map
        if (pushPos < 0 || pushPos >= mapLayout.length) return;

        if (pushPos == safePosition && !safeUnlocked) {
            String answer = JOptionPane.showInputDialog(frame, "What is the radian of (0,-1)?");
            if (answer != null && (answer.equalsIgnoreCase("3pi/2") || answer.equals("270"))) {
                safeUnlocked = true;
                JOptionPane.showMessageDialog(frame, "Correct! Safe Unlocked!");
                tiles[keyPosition].setIcon(floor);
                tiles[safePosition].setIcon(floor);
                keyPosition = -1;
            } else {
                JOptionPane.showMessageDialog(frame, "Wrong answer!");
            }
            return;
        }

        if (!isWall(pushPos)) {
            tiles[keyPosition].setIcon(floor);
            keyPosition = pushPos;
            tiles[keyPosition].setIcon(goldkey);
        } else return;
    }

    // Move player
    if (!isWall(nextPos)) {
        character[characterPosition].setIcon(null);
        characterPosition = nextPos;
        character[characterPosition].setIcon(nextIcon);
        characterMode = 1 - characterMode;
    }
}

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        new PD6().setFrame();
    }
}