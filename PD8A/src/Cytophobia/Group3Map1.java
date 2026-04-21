package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Group3Map1 implements KeyListener {
    JFrame frame;
    ImageIcon space, wall, tile, tile2, tile3, crack, crack2, crack3, window, window2, window3, doorEn, doorEx, ticket, plant, bush, blood;
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

    int ticketsCollected = 0;
    final int REQUIRED_TICKETS = 5;

    public Group3Map1() {
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
        doorEx = new ImageIcon("assets3/Walls/door2.JPEG");
        doorEn = new ImageIcon("assets3/Walls/door1.JPEG");
        ticket = new ImageIcon("assets3/Items/ticket.jpg");
        plant = new ImageIcon("assets3/Items/plant.jpg"); 
        bush = new ImageIcon("assets3/Items/bush.jpg");
        blood = new ImageIcon("assets3/Tiles/blood.PNG");
        
        ImageIcon[] icons = {space, wall, tile, tile2, tile3, crack, crack2,crack3, window, window2, window3, doorEx, doorEn, ticket, plant, bush, blood};

        for (ImageIcon icon : icons) {
            if (icon != null) {
                icon.setImage(icon.getImage().getScaledInstance(
                        tileSize, tileSize, Image.SCALE_DEFAULT));
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
                0,0,1,9,10,8,1,1,1,1,9,8,10,1,0,0,
                0,0,14,3,2,13,2,3,4,16,2,6,3,15,0,0,
                0,0,7,4,16,2,2,4,3,7,4,2,4,16,0,0,
                0,0,13,2,3,2,5,3,13,2,16,2,3,2,0,0,
                0,0,4,16,2,2,4,5,2,2,2,13,2,4,0,0,
                0,0,3,6,3,2,2,16,2,3,6,4,2,3,0,0,
                0,0,15,4,2,5,13,3,4,2,5,16,4,14,0,0,
                0,0,1,11,1,1,1,1,1,1,1,1,12,1,0,0,
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
                case 13 -> tiles[i] = new JLabel(ticket);
                case 14 -> tiles[i] = new JLabel(plant);
                case 15 -> tiles[i] = new JLabel(bush);
                case 16 -> tiles[i] = new JLabel(blood);
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

        showIntroDialog();
    }

    private void showIntroDialog() {
        String[] messages = {
                "So finally you've arrived...",
                "I've heard about you, y'know.",
                "You've intrigued me, and you made me wait...",
                "How long? Well long enough to piss me off.",
                "I'll see you soon. Be prepared."
        };

        JDialog dialog = new JDialog(frame, true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JLabel label = new JLabel(messages[0], SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        JButton next = new JButton("Next");

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(next, BorderLayout.SOUTH);

        final int[] i = {0};

        next.addActionListener(e -> {
            i[0]++;
            if (i[0] < messages.length) label.setText(messages[i[0]]);
            else dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void showExitDialog() {
        JDialog dialog = new JDialog(frame, "???", true);
        dialog.setSize(500, 220);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JLabel message = new JLabel(
                "<html><center>Pretty easy isn't it?</center></html>",
                SwingConstants.CENTER
        );
        message.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttons = new JPanel();
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");

        buttons.add(yes);
        buttons.add(no);

        dialog.add(message, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);

        ActionListener choice = e -> {
            message.setText("<html><center>Don't worry.<br>We've just started.</center></html>");
            buttons.removeAll();

            JButton proceed = new JButton("Proceed");
            proceed.addActionListener(ev -> {
                dialog.dispose();
                frame.dispose();
                new Group3HallwayMap(); 
            });

            buttons.add(proceed);
            buttons.revalidate();
            buttons.repaint();
        };

        yes.addActionListener(choice);
        no.addActionListener(choice);

        dialog.getRootPane().registerKeyboardAction(
                e -> {},
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        dialog.setVisible(true);
    }

    boolean canMove(int newPos) {
        if (newPos < 0 || newPos >= mapLayout.length) return false;
        int[] blocked = {0, 1, 8, 9, 10, 11, 12, 14, 15};
        for (int b : blocked) if (mapLayout[newPos] == b) return false;
        return true;
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
        new Group3Map1().setFrame();
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

            if (mapLayout[newPos] == 13) {
                ticketsCollected++;
                mapLayout[newPos] = 2;
                tiles[newPos].setIcon(tile);
            }

            if (mapLayout[newPos] == 12 && ticketsCollected >= REQUIRED_TICKETS) {
                showExitDialog();
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
}
