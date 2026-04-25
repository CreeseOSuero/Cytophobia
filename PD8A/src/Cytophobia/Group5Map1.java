package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Group5Map1 implements KeyListener {
    JFrame frame;

    // Tile images
    ImageIcon wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8, floor, key, chest, bed1, bed2, sky;
    // Character sprites
    ImageIcon frontS, frontW, backS, backW, leftS, leftW, rightS, rightW;

    JLabel tiles[];
    JLabel character[];
    int characterPlace[];
    int characterPosition;
    int characterMode;
    int direction;
    int mapLayout[];
    int mapWidth = 32;
    int mapHeight = 18;
    int frameWidth = 1920;
    int frameHeight = 1080;

    // Pushable key variables
    int keyPosition;
    int chestPosition;
    boolean chestUnlocked = false;
    public static volatile long startTime;

    public Group5Map1() {
        startTime = System.currentTimeMillis();
        frame = new JFrame();
        characterMode = 0;
        characterPosition = (mapHeight / 2) * mapWidth + (mapWidth / 2);

        // Load images
        wall1 = new ImageIcon(Menu.getRes("/assets5/wall1.jpg"));
        wall2 = new ImageIcon(Menu.getRes("/assets5/wall2.jpg"));
        wall3 = new ImageIcon(Menu.getRes("/assets5/wall3.jpg"));
        wall4 = new ImageIcon(Menu.getRes("/assets5/wall4.jpg"));
        wall5 = new ImageIcon(Menu.getRes("/assets5/wall5.jpg"));
        wall6 = new ImageIcon(Menu.getRes("/assets5/wall6.jpg"));
        wall7 = new ImageIcon(Menu.getRes("/assets5/wall7.jpg"));
        wall8 = new ImageIcon(Menu.getRes("/assets5/wall8.jpg"));
        floor = new ImageIcon(Menu.getRes("/assets5/floor.PNG"));
        key = new ImageIcon(Menu.getRes("/assets5/key.PNG"));
        chest = new ImageIcon(Menu.getRes("/assets5/chest.jpg"));
        bed1 = new ImageIcon(Menu.getRes("/assets5/bed1.PNG"));
        bed2 = new ImageIcon(Menu.getRes("/assets5/bed2.PNG"));
        sky = new ImageIcon(Menu.getRes("/assets5/sky.jpg"));

        frontS = new ImageIcon(Menu.getRes("/assets5/plr_10.PNG"));
        frontW = new ImageIcon(Menu.getRes("/assets5/plr_11.PNG"));
        backS = new ImageIcon(Menu.getRes("/assets5/plr_30.PNG"));
        backW = new ImageIcon(Menu.getRes("/assets5/plr_31.PNG"));
        leftS = new ImageIcon(Menu.getRes("/assets5/plr_00.PNG"));
        leftW = new ImageIcon(Menu.getRes("/assets5/plr_01.PNG"));
        rightS = new ImageIcon(Menu.getRes("/assets5/plr_20.PNG"));
        rightW = new ImageIcon(Menu.getRes("/assets5/plr_21.PNG"));

        // Scale all icons
        wall1 = scaleIcon(wall1); wall2 = scaleIcon(wall2); wall3 = scaleIcon(wall3); wall4 = scaleIcon(wall4);
        wall5 = scaleIcon(wall5); wall6 = scaleIcon(wall6); wall7 = scaleIcon(wall7); wall8 = scaleIcon(wall8);
        floor = scaleIcon(floor); key = scaleIcon(key); chest = scaleIcon(chest); bed1 = scaleIcon(bed1);
        bed2 = scaleIcon(bed2); sky = scaleIcon(sky);
        frontS = scaleIcon(frontS); frontW = scaleIcon(frontW); backS = scaleIcon(backS); backW = scaleIcon(backW);
        leftS = scaleIcon(leftS); leftW = scaleIcon(leftW); rightS = scaleIcon(rightS); rightW = scaleIcon(rightW);

        // Initialize arrays
        tiles = new JLabel[mapWidth * mapHeight];
        character = new JLabel[mapWidth * mapHeight];
        characterPlace = new int[mapWidth * mapHeight];
        characterPlace[characterPosition] = 1;

        for (int i = 0; i < character.length; i++) {
            if (i == characterPosition) character[i] = new JLabel(frontS);
            else character[i] = new JLabel();
        }

        // Map layout (keep exactly your layout)
        mapLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,6,4,4,4,4,4,4,4,4,4,4,4,4,7,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,12,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,13,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,2,1,1,1,1,1,1,1,1,1,1,3,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,9,11,11,11,11,11,11,11,11,11,11,11,11,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };

        // Find positions of key and chest in mapLayout
        for (int i = 0; i < mapLayout.length; i++) {
            if (mapLayout[i] == 12) keyPosition = i;
            if (mapLayout[i] == 13) chestPosition = i;
        }

        // Initialize tiles
        for (int i = 0; i < tiles.length; i++) {
            switch(mapLayout[i]) {
                case 0: tiles[i] = new JLabel(sky); break;
                case 1: tiles[i] = new JLabel(floor); break;
                case 2: tiles[i] = new JLabel(bed1); break;
                case 3: tiles[i] = new JLabel(bed2); break;
                case 4: tiles[i] = new JLabel(wall1); break;
                case 5: tiles[i] = new JLabel(wall2); break;
                case 6: tiles[i] = new JLabel(wall3); break;
                case 7: tiles[i] = new JLabel(wall4); break;
                case 8: tiles[i] = new JLabel(wall5); break;
                case 9: tiles[i] = new JLabel(wall6); break;
                case 10: tiles[i] = new JLabel(wall7); break;
                case 11: tiles[i] = new JLabel(wall8); break;
                case 12: tiles[i] = new JLabel(key); break;
                case 13: tiles[i] = new JLabel(chest); break;
                default: tiles[i] = new JLabel(sky); break;
            }
        }
    }

    private ImageIcon scaleIcon(ImageIcon icon) {
        return new ImageIcon(icon.getImage().getScaledInstance(frameWidth / mapWidth, frameHeight / mapHeight, Image.SCALE_DEFAULT));
    }

    public void setFrame() {
        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(frameWidth, frameHeight));

        int tileWidth = frameWidth / mapWidth;
        int tileHeight = frameHeight / mapHeight;

        // Add tiles
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setBounds((i % mapWidth) * tileWidth, (i / mapWidth) * tileHeight, tileWidth, tileHeight);
            pane.add(tiles[i], JLayeredPane.DEFAULT_LAYER);
        }

        // Add character
        for (int i = 0; i < character.length; i++) {
            character[i].setBounds((i % mapWidth) * tileWidth, (i / mapWidth) * tileHeight, tileWidth, tileHeight);
            pane.add(character[i], JLayeredPane.PALETTE_LAYER);
        }

        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);
    }

    public boolean isWall(int index) {
        if (index < 0 || index >= mapLayout.length) return true;
        if (mapLayout[index] >= 4 && mapLayout[index] <= 11) return true;
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int nextPos = characterPosition;
        ImageIcon nextIcon = null;

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direction = 3;
            if ((characterPosition % mapWidth) < mapWidth - 1) nextPos = characterPosition + 1;
            nextIcon = (characterMode == 0) ? rightW : rightS;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            direction = 2;
            if ((characterPosition % mapWidth) > 0) nextPos = characterPosition - 1;
            nextIcon = (characterMode == 0) ? leftW : leftS;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            direction = 1;
            if ((characterPosition / mapWidth) < mapHeight - 1) nextPos = characterPosition + mapWidth;
            nextIcon = (characterMode == 0) ? backW : backS;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            direction = 0;
            if ((characterPosition / mapWidth) > 0) nextPos = characterPosition - mapWidth;
            nextIcon = (characterMode == 0) ? frontW : frontS;
        }

        // PUSH key logic
        if (nextPos == keyPosition) {
            int pushPos = -1;
            switch(direction) {
                case 0: pushPos = keyPosition - mapWidth; break;
                case 1: pushPos = keyPosition + mapWidth; break;
                case 2: pushPos = keyPosition - 1; break;
                case 3: pushPos = keyPosition + 1; break;
            }
            if (!isWall(pushPos) && pushPos != characterPosition) {
                tiles[keyPosition].setIcon(floor);
                keyPosition = pushPos;
                tiles[keyPosition].setIcon(key);
            } else {
                character[characterPosition].setIcon(nextIcon);
                characterMode = 1 - characterMode;
                return;
            }
        }

        // Move character
        if (!isWall(nextPos) || nextPos == keyPosition) {
            character[characterPosition].setIcon(null);
            characterPlace[characterPosition] = 0;
            characterPosition = nextPos;
            character[characterPosition].setIcon(nextIcon);
            characterPlace[characterPosition] = 1;
            characterMode = 1 - characterMode;
        }

        // Check if key reached chest
        if (keyPosition == chestPosition && !chestUnlocked) {
            boolean allCorrect = askQuiz();
            if (allCorrect) {
                chestUnlocked = true;
                JOptionPane.showMessageDialog(frame, "Chest unlocked! Congratulations!");
                frame.dispose();
                (new Group5Map2()).setFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Wrong answers! Try pushing the key again.");
            }
        }
    }

    private boolean askQuiz() {
        String[][] questions = {
            {"What is H2O commonly known as?", "water"},
            {"Atomic number of carbon?", "6"},
            {"pH of neutral solution?", "7"},
            {"Chemical symbol for sodium?", "Na"},
            {"What gas do plants release during photosynthesis?", "oxygen"}
        };

        for (String[] q : questions) {
            String answer = JOptionPane.showInputDialog(frame, q[0]);
            if (answer == null || !answer.trim().equalsIgnoreCase(q[1])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        Group5Map1 game = new Group5Map1();
        game.setFrame();
    }
}