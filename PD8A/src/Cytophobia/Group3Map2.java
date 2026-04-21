package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Group3Map2 implements KeyListener {
    JFrame frame;

    ImageIcon space, wall, tile, tile2, tile3, crack, crack2, crack3;
    ImageIcon window, window2, window3, doorEn, doorEx;
    ImageIcon plant, bush, blood, C1, C2, C3, C4, C5;

    JLabel[] tiles;

    ImageIcon playerIcon1, playerIcon2, playerIcon3, playerIcon4;
    JLabel[] characters;

    int[] charactersLayout;
    int characterPosition, movement, direction;

    int[] mapLayout;

    int mapWidth = 16;
    int mapHeight = 10;

    int tileSize = 64;
    int frameWidth = mapWidth * tileSize;
    int frameHeight = mapHeight * tileSize;

    boolean dialogueTriggered = false;

    boolean gateUnlocked = false;
    int seqIndex = 0;
    int[] sequence = {20, 18, 21, 19, 22};

    public Group3Map2() {
        frame = new JFrame();

        space = new ImageIcon("assets3/void.png");
        wall = new ImageIcon("assets3/Walls/wall.JPEG");
        tile = new ImageIcon("assets3/Tiles/tiles.PNG");
        tile2 = new ImageIcon("assets3/Tiles/tile2.PNG");
        tile3 = new ImageIcon("assets3/Tiles/tile3.PNG");
        crack = new ImageIcon("assets3/Tiles/crack.PNG");
        crack2 = new ImageIcon("assets3/Tiles/crack2.PNG");
        crack3 = new ImageIcon("assets3/Tiles/crack3.PNG");

        window = new ImageIcon("assets3/Walls/window.JPEG");
        window2 = new ImageIcon("assets3/Walls/window2.JPEG");
        window3 = new ImageIcon("assets3/Walls/window3.JPEG");

        doorEn = new ImageIcon("assets3/Walls/door1.JPEG");
        doorEx = new ImageIcon("assets3/Walls/door2.JPEG");

        plant = new ImageIcon("assets3/Items/plant.jpg");
        bush = new ImageIcon("assets3/Items/bush.jpg");
        blood = new ImageIcon("assets3/Tiles/blood.PNG");

        C1 = new ImageIcon("assets3/Cubicles/C1.jpg");
        C2 = new ImageIcon("assets3/Cubicles/C2.jpg");
        C3 = new ImageIcon("assets3/Cubicles/C3.jpg");
        C4 = new ImageIcon("assets3/Cubicles/C4.jpg");
        C5 = new ImageIcon("assets3/Cubicles/C5.jpg");

        ImageIcon[] icons = {
                space, wall, tile, tile2, tile3,
                crack, crack2, crack3,
                window, window2, window3,
                doorEn, doorEx,
                plant, bush, blood,
                C1, C2, C3, C4, C5
        };

        for (ImageIcon icon : icons) {
            if (icon != null) {
                icon.setImage(icon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
            }
        }

        playerIcon1 = new ImageIcon("assets3/Player/walk_up_0.PNG");
        playerIcon2 = new ImageIcon("assets3/Player/walk_down_0.PNG");
        playerIcon3 = new ImageIcon("assets3/Player/walk_left_0.PNG");
        playerIcon4 = new ImageIcon("assets3/Player/walk_right_0.PNG");

        playerIcon1.setImage(playerIcon1.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerIcon2.setImage(playerIcon2.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerIcon3.setImage(playerIcon3.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerIcon4.setImage(playerIcon4.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));

        tiles = new JLabel[mapWidth * mapHeight];

        mapLayout = new int[]{
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,14,16,18,19,14,20,14,21,22,2,15,1,0,0,
                0,0,3,3,2,2,2,3,4,16,2,6,3,1,0,0,
                0,0,7,4,16,2,2,4,3,7,4,2,3,17,0,0,
                0,0,2,2,3,2,5,3,2,2,16,2,5,17,0,0,
                0,0,4,16,2,2,4,5,2,2,2,2,2,17,0,0,
                0,0,3,6,3,2,2,16,2,3,6,4,2,1,0,0,
                0,0,15,4,2,5,2,3,4,2,5,16,4,1,0,0,
                0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };

        for (int i = 0; i < tiles.length; i++) {
            switch (mapLayout[i]) {
                case 0 -> tiles[i] = new JLabel(space);
                case 1 -> tiles[i] = new JLabel(wall);
                case 2 -> tiles[i] = new JLabel(tile);
                case 3 -> tiles[i] = new JLabel(tile2);
                case 4 -> tiles[i] = new JLabel(tile3);
                case 5 -> tiles[i] = new JLabel(crack);
                case 6 -> tiles[i] = new JLabel(crack2);
                case 7 -> tiles[i] = new JLabel(crack3);
                case 8 -> tiles[i] = new JLabel(window);
                case 9 -> tiles[i] = new JLabel(window2);
                case 10 -> tiles[i] = new JLabel(window3);
                case 11 -> tiles[i] = new JLabel(doorEn);
                case 12 -> tiles[i] = new JLabel(doorEx);
                case 14 -> tiles[i] = new JLabel(plant);
                case 15 -> tiles[i] = new JLabel(bush);
                case 16 -> tiles[i] = new JLabel(blood);
                case 17 -> tiles[i] = new JLabel(gateUnlocked ? space : wall);
                case 18 -> tiles[i] = new JLabel(C1);
                case 19 -> tiles[i] = new JLabel(C2);
                case 20 -> tiles[i] = new JLabel(C3);
                case 21 -> tiles[i] = new JLabel(C4);
                case 22 -> tiles[i] = new JLabel(C5);
            }
        }

        characters = new JLabel[mapWidth * mapHeight];
        charactersLayout = new int[mapWidth * mapHeight];

        charactersLayout[7 * mapWidth + 3] = 1;

        for (int i = 0; i < characters.length; i++) {
            if (charactersLayout[i] == 1) {
                characters[i] = new JLabel(playerIcon2);
                characterPosition = i;
            } else {
                characters[i] = new JLabel();
            }
        }
    }

    private void showEndingDialogue() {
    String[] lines = {
            "It was nice playing with you.",
            "It's been a while since people came here... after what happened.",
            "It's fun to have people around here.",
            "I never wanted to scare you.",
            "I just wanted to play with someone.",
            "I never got to play with kids my age.",
            "Because I never grew up, never got old beyond 7 months.",
            "Thank you for playing with me..",
            "I finally experienced it, goodbye."
    };

    JDialog dialog = new JDialog(frame, true);
    dialog.setSize(500, 220);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(frame);
    dialog.setUndecorated(true);

    JLabel label = new JLabel(lines[0], SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 15));

    JButton button = new JButton("Proceed");
    JPanel panel = new JPanel();
    panel.add(button);

    dialog.add(label, BorderLayout.CENTER);
    dialog.add(panel, BorderLayout.SOUTH);

    final int[] index = {0};

    button.addActionListener(e -> {
        index[0]++;

        if (index[0] < lines.length) {
            label.setText(lines[index[0]]);
        } else {
            dialog.dispose();
            frame.dispose();
            Menu.startNextLevel(3);
        }
    });

    dialog.setVisible(true);
}

    boolean canMove(int newPos) {
        if (newPos < 0 || newPos >= mapLayout.length) return false;

        int[] blocked = gateUnlocked
                ? new int[]{0, 1, 8, 9, 10, 11, 14, 15}
                : new int[]{0, 1, 8, 9, 10, 11, 14, 15, 17};

        for (int b : blocked) {
            if (mapLayout[newPos] == b) return false;
        }

        return true;
    }

    private void updateGateTiles() {
        for (int i = 0; i < tiles.length; i++) {
            if (mapLayout[i] == 17) {
                tiles[i].setIcon(gateUnlocked ? space : wall);
            }
        }
    }

    public void setFrame() {
        frame.setLayout(null);

        int x = 0, y = 0;

        for (JLabel c : characters) {
            frame.add(c);
            c.setBounds(x * tileSize, y * tileSize, tileSize, tileSize);
            if (++x == mapWidth) { x = 0; y++; }
        }

        x = 0; y = 0;

        for (JLabel t : tiles) {
            frame.add(t);
            t.setBounds(x * tileSize, y * tileSize, tileSize, tileSize);
            if (++x == mapWidth) { x = 0; y++; }
        }

        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addKeyListener(this);
    }

    public static void main(String[] args) {
        new Group3Map2().setFrame();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        movement = 0;

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direction = 3;
            movement = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            direction = 2;
            movement = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            direction = 1;
            movement = mapWidth;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            direction = 0;
            movement = -mapWidth;
        }

        if (movement == 0) return;

        int newPos = characterPosition + movement;

        if (newPos < 0 || newPos >= mapLayout.length) return;

        int val = mapLayout[newPos];

        if (seqIndex < sequence.length && val == sequence[seqIndex]) {
            seqIndex++;
        } else if (val == 20 || val == 18 || val == 21 || val == 19 || val == 22) {
            seqIndex = 0;
        }

        if (seqIndex >= sequence.length) {
            gateUnlocked = true;
            updateGateTiles();
        }

        if (val == 17 && gateUnlocked && !dialogueTriggered) {
            dialogueTriggered = true;
            showEndingDialogue();
            return;
        }

        if (canMove(newPos)) {
            characters[characterPosition].setIcon(null);

            characters[newPos].setIcon(
                    direction == 0 ? playerIcon1 :
                    direction == 1 ? playerIcon2 :
                    direction == 2 ? playerIcon3 : playerIcon4
            );

            characterPosition = newPos;
        }
    }
}