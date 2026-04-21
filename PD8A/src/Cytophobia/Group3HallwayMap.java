package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Group3HallwayMap implements KeyListener {

    JFrame frame;

    ImageIcon space, wall, tile, tile2, tile3, crack, crack2, crack3, blood, plant;
    ImageIcon playerUp, playerDown, playerLeft, playerRight;

    JLabel[] tiles;
    JLabel[] characters;
    int[] charactersLayout;

    int characterPosition, movement, direction;

    int[] mapLayout;
    int mapWidth = 16;
    int mapHeight = 10;

    int tileSize = 64;
    int frameWidth = mapWidth * tileSize;
    int frameHeight = mapHeight * tileSize;

    public Group3HallwayMap() {
        frame = new JFrame("Hallway");
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadIcons();
        createMapLayout();
        setupPlayer();

        showDialog();
    }

    private void loadIcons() {
        space = new ImageIcon("assets3/void.png");
        wall = new ImageIcon("assets3/Walls/wall.JPEG");
        tile = new ImageIcon("assets3/Tiles/tiles.PNG");
        tile2 = new ImageIcon("assets3/Tiles/tile2.PNG");
        tile3 = new ImageIcon("assets3/Tiles/tile3.PNG");
        crack = new ImageIcon("assets3/Tiles/crack.PNG");
        crack2 = new ImageIcon("assets3/Tiles/crack2.PNG");
        crack3 = new ImageIcon("assets3/Tiles/crack3.PNG");
        blood = new ImageIcon("assets3/Tiles/blood.PNG");
        plant = new ImageIcon("assets3/Items/plant.jpg");

        ImageIcon[] icons = {space, wall, tile, tile2, tile3, crack, crack2, crack3, blood, plant};

        for (ImageIcon icon : icons) {
            if (icon != null) {
                icon.setImage(icon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
            }
        }

        playerUp = new ImageIcon("assets3/Player/walk_up_0.PNG");
        playerDown = new ImageIcon("assets3/Player/walk_down_0.PNG");
        playerLeft = new ImageIcon("assets3/Player/walk_left_0.PNG");
        playerRight = new ImageIcon("assets3/Player/walk_right_0.PNG");

        playerUp.setImage(playerUp.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerDown.setImage(playerDown.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerLeft.setImage(playerLeft.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerRight.setImage(playerRight.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
    }

    private void createMapLayout() {
        tiles = new JLabel[mapWidth * mapHeight];

        mapLayout = new int[]{
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,1,8,3,2,2,2,4,5,2,3,7,1,0,0,
                0,0,1,4,6,2,2,8,2,3,8,7,4,1,0,0,
                0,0,1,9,4,2,2,2,4,2,2,5,9,1,0,0,
                0,0,1,5,3,2,2,7,2,2,6,8,3,1,0,0,
                0,0,1,4,3,2,8,2,2,2,4,2,7,1,0,0,
                0,0,1,9,2,4,3,2,2,3,2,2,9,1,0,0, 
                0,0,1,8,2,2,4,2,5,2,2,4,8,1,0,0,
                0,0,1,5,2,8,5,2,3,8,2,3,2,1,0,0,
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
                case 8 -> tiles[i] = new JLabel(blood);
                case 9 -> tiles[i] = new JLabel(plant);
            }
        }
    }

    private void setupPlayer() {
        characters = new JLabel[mapWidth * mapHeight];
        charactersLayout = new int[mapWidth * mapHeight];

        charactersLayout[1 * mapWidth + 12] = 1;

        for (int i = 0; i < characters.length; i++) {
            if (charactersLayout[i] == 1) {
                characters[i] = new JLabel(playerDown);
                characterPosition = i;
            } else {
                characters[i] = new JLabel();
            }
        }
    }

    boolean canMove(int newPos) {
        if (newPos < 0 || newPos >= mapLayout.length) return false;
        int tile = mapLayout[newPos];
        return tile != 0 && tile != 1 && tile != 9;
    }

    private void showMap() {
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

        frame.addKeyListener(this);
        frame.setVisible(true);
    }

    private void showDialog() {
        JDialog dialog = new JDialog(frame, "Hallway", true);
        dialog.setSize(500, 220);
        dialog.setLayout(new BorderLayout());
        dialog.setUndecorated(true);

        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel();
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        String[] lines = {
                "The air feels heavier here...",
                "Wanna find out why, hmph?",
                "Keep moving then, and keep an eye out for clues with the pretty plants."
        };

        final int[] index = {0};
        label.setText("<html><center>" + lines[0] + "</center></html>");

        yesButton.addActionListener(e -> {
            index[0]++;
            if (index[0] < lines.length) {
                label.setText("<html><center>" + lines[index[0]] + "</center></html>");

                if (index[0] == lines.length - 1) {
                    yesButton.setText("Alright");
                    noButton.setVisible(false);
                }

            } else {
                dialog.dispose();
                showMap(); // ✅ now works
            }
        });

        noButton.addActionListener(e -> {
            label.setText("<html><center>You started this.<br>Finish it.</center></html>");
        });

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        movement = 0;

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { direction = 3; movement = 1; }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) { direction = 2; movement = -1; }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) { direction = 1; movement = mapWidth; }
        else if (e.getKeyCode() == KeyEvent.VK_UP) { direction = 0; movement = -mapWidth; }

        if (movement != 0) {
            int newPos = characterPosition + movement;

            int transitionRow = 8;
            int rowStart = transitionRow * mapWidth;
            int rowEnd = rowStart + mapWidth - 1;

            if (newPos >= rowStart && newPos <= rowEnd) {
                frame.dispose();
                new Group3HallwayMap2();
                return;
            }

            if (canMove(newPos)) {
                characters[characterPosition].setIcon(null);
                characters[newPos].setIcon(
                        direction == 0 ? playerUp :
                        direction == 1 ? playerDown :
                        direction == 2 ? playerLeft : playerRight
                );
                characterPosition = newPos;
            }
        }
    }

    public static void main(String[] args) {
        new Group3HallwayMap();
    }
}