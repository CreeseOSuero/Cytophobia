package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Group3HallwayMap2 implements KeyListener {

    JFrame frame;

    ImageIcon space, wall, tile, tile2, tile3, crack, crack2, crack3, blood, plant, bush, clue;
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

    boolean clueVisible = false;

    public Group3HallwayMap2() {
        frame = new JFrame("HallwayMap2");
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadIcons();
        createMapLayout();
        setupPlayer();
        showMap();
    }

    private void loadIcons() {
        space = new ImageIcon("");
        wall = new ImageIcon(Menu.getRes("/assets3/Walls/wall.JPEG"));
        tile = new ImageIcon(Menu.getRes("/assets3/Tiles/tiles.PNG"));
        tile2 = new ImageIcon(Menu.getRes("/assets3/Tiles/tile2.PNG"));
        tile3 = new ImageIcon(Menu.getRes("/assets3/Tiles/tile3.PNG"));
        crack = new ImageIcon(Menu.getRes("/assets3/Tiles/crack.PNG"));
        crack2 = new ImageIcon(Menu.getRes("/assets3/Tiles/crack2.PNG"));
        crack3 = new ImageIcon(Menu.getRes("/assets3/Tiles/crack3.PNG"));
        blood = new ImageIcon(Menu.getRes("/assets3/Tiles/blood.PNG"));
        plant = new ImageIcon(Menu.getRes("/assets3/Items/plant.jpg"));
        bush = new ImageIcon(Menu.getRes("/assets3/Items/bush.jpg"));
        clue = new ImageIcon(Menu.getRes("/assets3/Items/clue.jpg"));

        ImageIcon[] icons = {space, wall, tile, tile2, tile3, crack, crack2, crack3, blood, plant, bush, clue};

        for (ImageIcon icon : icons) {
            if (icon != null) {
                icon.setImage(icon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
            }
        }

        playerUp = new ImageIcon(Menu.getRes("/assets3/Player/walk_up_0.PNG"));
        playerDown = new ImageIcon(Menu.getRes("/assets3/Player/walk_down_0.PNG"));
        playerLeft = new ImageIcon(Menu.getRes("/assets3/Player/walk_left_0.PNG"));
        playerRight = new ImageIcon(Menu.getRes("/assets3/Player/walk_right_0.PNG"));

        playerUp.setImage(playerUp.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerDown.setImage(playerDown.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerLeft.setImage(playerLeft.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        playerRight.setImage(playerRight.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
    }

    private void createMapLayout() {
        tiles = new JLabel[mapWidth * mapHeight];

        mapLayout = new int[]{
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,1,9,8,5,3,2,4,5,2,3,7,10,0,0,
                0,0,1,4,6,3,2,8,2,3,8,7,4,11,0,0,
                0,0,1,3,4,2,3,2,4,7,2,5,3,11,0,0,
                0,0,1,5,3,6,2,7,3,2,6,8,3,11,0,0,
                0,0,1,4,3,2,8,2,8,2,4,2,7,11,0,0,
                0,0,1,8,2,4,3,7,2,3,2,2,2,11,0,0,
                0,0,1,10,2,2,4,2,5,2,2,4,8,9,0,0,
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
                case 8 -> tiles[i] = new JLabel(blood);
                case 9 -> tiles[i] = new JLabel(plant);
                case 10 -> tiles[i] = new JLabel(bush);

                // 🔥 TILE 11 = RANDOM FLOOR (2–8 LOOK)
                case 11 -> {
                    int random = (int)(Math.random() * 7) + 2;

                    ImageIcon chosen = switch (random) {
                        case 2 -> tile;
                        case 3 -> tile2;
                        case 4 -> tile3;
                        case 5 -> crack;
                        case 6 -> crack2;
                        case 7 -> crack3;
                        default -> blood;
                    };

                    tiles[i] = new JLabel(chosen);
                }
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

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (clueVisible) return;

        movement = 0;

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { direction = 3; movement = 1; }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) { direction = 2; movement = -1; }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) { direction = 1; movement = mapWidth; }
        else if (e.getKeyCode() == KeyEvent.VK_UP) { direction = 0; movement = -mapWidth; }

        if (movement != 0) {
            int newPos = characterPosition + movement;

            if (newPos < 0 || newPos >= mapLayout.length) return;

            if (mapLayout[newPos] == 11) {
                System.out.println("exit");
                frame.dispose();
                (new Group3Map2()).setFrame();
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
                checkTileInteraction(newPos);
            }
        }
    }

    private void checkTileInteraction(int pos) {
        int tile = mapLayout[pos];

        if (tile == 10 && !clueVisible) {
            clueVisible = true;

            JLabel clueLabel = new JLabel(new ImageIcon(
                    clue.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_DEFAULT)
            ));

            clueLabel.setBounds(0, 0, frameWidth, frameHeight);
            clueLabel.setOpaque(true);

            frame.getLayeredPane().add(clueLabel, JLayeredPane.PALETTE_LAYER);
            frame.repaint();

            javax.swing.Timer timer = new javax.swing.Timer(5000, e -> {
                frame.getLayeredPane().remove(clueLabel);
                frame.repaint();
                clueVisible = false;
            });

            timer.setRepeats(false);
            timer.start();
        }
    }

    public static void main(String[] args) {
        new Group3HallwayMap2();
    }
}