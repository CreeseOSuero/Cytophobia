package QUARTER3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PD6_GUI implements KeyListener {

    JFrame frame;
    JLabel bg, plr, chest;

    int mapWidth = 32;
    int mapHeight = 18;
    int frameWidth = 1600;
    int frameHeight = 900;

    int charX = 0;
    int charY = 9;

    boolean plrMobile = true;

    int interLayout[];

    String questions[] = {
        "Moonlight seeps through the cracked windows...\nWhat part of the blood carries oxygen?",
        "A silent figure drifts through the halls...\nWhich human cell has no nucleus?",
        "A curse twists those who enter...\nWhat molecule mutates to alter traits?",
        "The mansion breathes as if alive...\nWhat process uses oxygen to produce energy?",
        "A sickness spreads endlessly...\nHow do bacteria reproduce?"
    };

    String answers[] = {
        "red blood cells",
        "red blood cell",
        "dna",
        "cellular respiration",
        "binary fission"
    };

    int currentQuestion = 0;

    ImageIcon loadImg(String ref, int scaleX, int scaleY) {
        return new ImageIcon((new ImageIcon(ref)).getImage()
                .getScaledInstance((frameWidth / mapWidth) * scaleX,
                                   (frameHeight / mapHeight) * scaleY,
                                   Image.SCALE_DEFAULT));
    }

    void changeComponentConstraints(JFrame frame, Component comp, Rectangle cons) {
        ((GraphPaperLayout) (frame.getContentPane().getLayout())).setConstraints(comp, cons);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public PD6_GUI() {

        frame = new JFrame("Cytophobia");

        plr = new JLabel(loadImg("Images/plr_00.PNG", 2, 2));
        chest = new JLabel(loadImg("Images/chest.png", 2, 2));

        bg = new JLabel(loadImg("Images/Background.png", mapWidth, mapHeight));

        interLayout = new int[mapWidth * mapHeight];

        for (int i = 0; i < interLayout.length; i++)
            interLayout[i] = 0;

        // chest location
        interLayout[9 * mapWidth + 30] = 2;

        frame.setLayout(new GraphPaperLayout(new Dimension(mapWidth, mapHeight)));

        frame.add(plr, new Rectangle(charX, charY, 2, 2));
        frame.add(chest, new Rectangle(30, 9, 2, 2));
        frame.add(bg, new Rectangle(0, 0, mapWidth, mapHeight));

        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addKeyListener(this);

        frame.setVisible(true);
    }

    void startQuiz() {

        plrMobile = false;

        while (currentQuestion < questions.length) {

            String input = JOptionPane.showInputDialog(
                    frame,
                    questions[currentQuestion]
            );

            if (input == null)
                continue;

            input = input.toLowerCase().trim();

            if (input.contains(answers[currentQuestion])) {
                JOptionPane.showMessageDialog(frame, "Correct...");
                currentQuestion++;
            } else {
                JOptionPane.showMessageDialog(frame, "Wrong... Try again.");
            }
        }

        JOptionPane.showMessageDialog(frame, "The chest creaks open...");
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!plrMobile)
            return;

        int prevX = charX;
        int prevY = charY;

        if (e.getKeyCode() == 37)
            charX--;

        if (e.getKeyCode() == 38)
            charY--;

        if (e.getKeyCode() == 39)
            charX++;

        if (e.getKeyCode() == 40)
            charY++;

        if (charX < 0)
            charX = 0;

        if (charY < 0)
            charY = 0;

        if (charX > mapWidth - 2)
            charX = mapWidth - 2;

        if (charY > mapHeight - 2)
            charY = mapHeight - 2;

        int tileIndex = charY * mapWidth + charX;

        if (interLayout[tileIndex] == 2) {
            startQuiz();
        }

        changeComponentConstraints(frame, plr, new Rectangle(charX, charY, 2, 2));
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

}