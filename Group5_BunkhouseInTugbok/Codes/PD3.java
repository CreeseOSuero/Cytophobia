package gr5cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PD3 implements KeyListener {
    JFrame frame;

    // Tile images
    ImageIcon wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8, floor, key, chest, bed1, bed2, sky;

    JLabel tiles[];
    int mapLayout[];
    int mapWidth = 32;
    int mapHeight = 18;
    int frameWidth = 1920;
    int frameHeight = 1080;



    public PD3() {
        frame = new JFrame();

        // Load images
        wall1 = new ImageIcon("Images/wall1.jpg");
        wall2 = new ImageIcon("Images/wall2.jpg");
        wall3 = new ImageIcon("Images/wall3.jpg");
        wall4 = new ImageIcon("Images/wall4.jpg");
        wall5 = new ImageIcon("Images/wall5.jpg");
        wall6 = new ImageIcon("Images/wall6.jpg");
        wall7 = new ImageIcon("Images/wall7.jpg");
        wall8 = new ImageIcon("Images/wall8.jpg");
        floor = new ImageIcon("Images/floor.png");
        key = new ImageIcon("Images/key.png");
        chest = new ImageIcon("Images/chest.jpg");
        bed1 = new ImageIcon("Images/bed1.png");
        bed2 = new ImageIcon("Images/bed2.png");
        sky = new ImageIcon("Images/sky.jpg");


        // Scale all icons
        wall1 = scaleIcon(wall1); wall2 = scaleIcon(wall2); wall3 = scaleIcon(wall3); wall4 = scaleIcon(wall4);
        wall5 = scaleIcon(wall5); wall6 = scaleIcon(wall6); wall7 = scaleIcon(wall7); wall8 = scaleIcon(wall8);
        floor = scaleIcon(floor); key = scaleIcon(key); chest = scaleIcon(chest); bed1 = scaleIcon(bed1);
        bed2 = scaleIcon(bed2); sky = scaleIcon(sky);

        // Initialize arrays
        tiles = new JLabel[mapWidth * mapHeight];

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

        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);
    }


    public static void main(String[] args) {
        PD3 game = new PD3();
        game.setFrame();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}