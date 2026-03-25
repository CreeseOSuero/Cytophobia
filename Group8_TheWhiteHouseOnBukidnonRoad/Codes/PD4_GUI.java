/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QUARTER3;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public class PD4_GUI implements KeyListener {

    JFrame frame;
    JLabel bg, plr, enemy;
    JPanel overlay; // Lighting layer

    // Logic Arrays
    int interLayout[]; // Collision grid
    boolean interDone[]; // Check if interaction happened
    String interDia[];   // Dialogues

    // Dimensions
    int mapWidth = 32;
    int mapHeight = 18;
    int frameWidth = 1600;
    int frameHeight = 900;

    // Player State
    ImageIcon plrStates[];
    volatile boolean plrMobile;
    int plrDir = 3;
    int plrState = 0;
    
    // STARTING POSITION: OUTSIDE (Bottom Left)
    int charX = 2, charY = 16; 
    boolean hasKey = false; // Logic for the door

    // Enemy State
    int enemyX = 25, enemyY = 5; // Starts INSIDE the house
    Timer enemyTimer;            // Timer for enemy movement

    // UI & Effects
    JLabel glitchEffect;
    JLabel darkEffect;
    JLabel fadeEffect;
    JLabel alphaGrad;
    JLabel dialogueBox;
    JLabel dialogueName;
    JLabel dialogueText;
    JLabel idFront;
    JLabel idBack;
    
    // Story State
    volatile boolean doingObjective;
    volatile boolean doDialog;
    volatile boolean isCompleting;
    volatile int interType;
    boolean keyPress = false;

    // ================= HELPER METHODS =================

    ImageIcon loadImg(String ref, int scaleX, int scaleY) {
        if (getClass().getResource(ref) == null && !ref.startsWith("/")) {
             // System.out.println("Missing image: " + ref); 
        }
        return new ImageIcon((new ImageIcon(ref)).getImage()
                .getScaledInstance((frameWidth / mapWidth) * scaleX, (frameHeight / mapHeight) * scaleY, Image.SCALE_DEFAULT));
    }

    void changeComponentConstraints(JFrame frame, Component comp, Rectangle cons) {
        ((GraphPaperLayout) (frame.getContentPane().getLayout())).setConstraints(comp, cons);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    
    void tWait(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    // ================= CONSTRUCTOR =================
    
    public PD4_GUI() {
        frame = new JFrame("Cytophobia");

        // 1. SETUP DATA & ARRAYS
        plrMobile = false; // Starts frozen for intro
        plrStates = new ImageIcon[12];
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                plrStates[i * 3 + j] = loadImg("Images/plr_" + i + j + ".PNG", 2, 2);
            }
        }
        plr = new JLabel(plrStates[9]); 

        // MAP LAYOUT:
        // 0 = Floor, 1 = Wall, 9 = Door
        // 7 = Key (Outside), 6 = Barricade, 8 = Window
        interLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,8,8,0,0,0,0,0,8,8,0,0,0,0,0,8,8,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,3,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,6,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,0,0,0,0,0,0,0,0,0,
            1,1,1,1,1,1,1,1,1,1,1,1,1,9,9,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, // WALL & DOOR(9)
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, // OUTSIDE
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7, // KEY(7)
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };

        interDone = new boolean[8];
        interDia = new String[]{
            "Just some dusty old books.",        // (Type 2 - 2 = Index 0) Bookshelf
            "The sheets are twisted and cold.",  // (Type 3 - 2 = Index 1) Bed
            "",                                  // (Type 4 - 2 = Index 2) Unused
            "The stairs are blocked.",           // (Type 5 - 2 = Index 3) Stairs
            "I can't go there.",                 // (Type 6 - 2 = Index 4) Barricade "cant"
            "I found the House Key under a rock!", // (Type 7 - 2 = Index 5) Key
            "The window is sealed shut.",        // (Type 8 - 2 = Index 6) Window
            "The door is locked. I need a key."  // (Type 9 - 2 = Index 7) DOOR LOCKED
        };

        // 2. SETUP UI COMPONENTS
        bg = new JLabel(loadImg("Images/Background.png", mapWidth, mapHeight));
        alphaGrad = new JLabel(loadImg("Images/alpha_grad.png", mapWidth, 3));
        glitchEffect = new JLabel(loadImg("Images/glitch_effect.gif", mapWidth, mapHeight));
        
        dialogueBox = new JLabel();
        dialogueBox.setBackground(Color.BLACK);
        dialogueBox.setOpaque(true);
        
        darkEffect = new JLabel();
        darkEffect.setBackground(Color.BLACK);
        darkEffect.setOpaque(true);
        
        dialogueName = new JLabel();
        dialogueText = new JLabel();
        
        fadeEffect = new JLabel();
        fadeEffect.setBackground(Color.BLACK);
        fadeEffect.setOpaque(true);
        
        idFront = new JLabel(loadImg("Images/id.png", 12, 10));
        idBack = new JLabel(loadImg("Images/id_back.png", 12, 10));
        
        dialogueName.setForeground(Color.WHITE);
        dialogueText.setForeground(Color.WHITE);
        dialogueName.setFont(new Font("Courier New", Font.PLAIN, 40));
        dialogueText.setFont(new Font("Arial Unicode MS", Font.PLAIN, 25));

        // 3. SETUP ENEMY
        enemy = new JLabel(loadImg("Images/enemy.png", 2, 2)); 

        // 4. ASSEMBLE FRAME
        frame.setLayout(new GraphPaperLayout(new Dimension(mapWidth, mapHeight)));
        
        // Z-Index Order:
        // Bottom: Player, Enemy, BG
        frame.add(plr, new Rectangle(charX, charY, 2, 2));
        frame.add(enemy, new Rectangle(enemyX, enemyY, 2, 2));
        frame.add(bg, new Rectangle(0, 0, mapWidth, mapHeight));
        
        // Middle: Interactables (ID Cards)
        frame.add(idFront, new Rectangle(10, 0, 12, 10));
        frame.add(idBack, new Rectangle(10, 0, 12, 10));
        
        // Top: UI & Dialogue
        frame.add(dialogueName, new Rectangle(2, 12, mapWidth - 2, 2));
        frame.add(dialogueText, new Rectangle(2, 14, mapWidth - 2, 3));
        frame.add(dialogueBox, new Rectangle(0, 12, mapWidth, mapHeight - 12));
        frame.add(alphaGrad, new Rectangle(0, 9, mapWidth, 3));

        // Very Top: Effects
        frame.add(fadeEffect, new Rectangle(0, 0, mapWidth, mapHeight));
        frame.add(glitchEffect, new Rectangle(0, 0, mapWidth, mapHeight));
        frame.add(darkEffect, new Rectangle(0, 0, mapWidth, mapHeight));

        // 5. LIGHTING OVERLAY (Flashlight Effect)
        overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Calculate tile size
                int tileW = getWidth() / mapWidth;
                int tileH = getHeight() / mapHeight;
                // Center flashlight on player
                float centerX = (charX + 1) * tileW;
                float centerY = (charY + 1) * tileH;
                float radius = 300f; 
                float[] dist = {0.0f, 1.0f};
                Color[] colors = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 245)}; // Transparent to Dark
                
                RadialGradientPaint p = new RadialGradientPaint(
                        new Point2D.Float(centerX, centerY), radius, dist, colors);
                g2d.setPaint(p);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setOpaque(false);
        frame.setGlassPane(overlay);
        overlay.setVisible(true);

        // 6. INITIAL VISIBILITY
        glitchEffect.setVisible(false);
        alphaGrad.setVisible(false);
        dialogueBox.setVisible(false);
        dialogueName.setVisible(false);
        dialogueText.setVisible(false);
        idFront.setVisible(false);
        idBack.setVisible(false);
        plr.setVisible(false);

        // 7. SETUP WINDOW
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        // 8. ENEMY AI TIMER
        enemyTimer = new Timer(700, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEnemyMovement();
            }
        });
        enemyTimer.start(); 

        // 9. STORYLINE THREAD
        Thread storyThread = new Thread(() -> {
            try {
                runStoryline();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        storyThread.start();
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                storyThread.interrupt();
            }
        });
    }

    // ================= ENEMY AI =================
    void updateEnemyMovement() {
        if (!plrMobile) return; 
        
        int dx = charX - enemyX;
        int dy = charY - enemyY;

        // If further than 1 tile away, move closer
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            int nextX = enemyX;
            int nextY = enemyY;

            // Move along the axis with the largest distance
            if (Math.abs(dx) >= Math.abs(dy)) nextX += Integer.signum(dx);
            else nextY += Integer.signum(dy);

            // Collision check
            int tileIndex = nextY * mapWidth + nextX;
            if (tileIndex >= 0 && tileIndex < interLayout.length) {
                int tile = interLayout[tileIndex];
                // Enemy is blocked by Walls (1), Doors (9), and Barricades (6)
                if (tile != 1 && tile != 6 && tile != 9) { 
                    enemyX = nextX;
                    enemyY = nextY;
                }
            }
            changeComponentConstraints(frame, enemy, new Rectangle(enemyX, enemyY, 2, 2));
        }
    }

    // ================= STORYLINE LOGIC =================
    public void runStoryline() throws InterruptedException {
        tWriteDia("", "...", 1, 3000); // Shortened intro
        SwingUtilities.invokeLater(() -> {
            fadeEffect.setVisible(true);
            plr.setVisible(true);
        });
        
        // Fade in
        for (int i[] = {255}; i[0] >= 0; --i[0]) {
            SwingUtilities.invokeLater(() -> {
                fadeEffect.setBackground(new Color(0, 0, 0, i[0]));
                frame.repaint();
            });
            tWait(4);
        }
        
        SwingUtilities.invokeLater(() -> {
            darkEffect.setVisible(false);
            fadeEffect.setVisible(false);
        });

        tWriteDia("???", "I need to get inside.", 1000, 500);
        
        plrMobile = true; 
        doingObjective = true;

        while (doingObjective) {
            tWait(20);
            if (doDialog) {
                tWriteDia("???", interDia[interType], 1000, 1000);
                plrMobile = true;
                doDialog = false;
            }
        }

        // Logic for ending tutorial / finding main objective
        // (You can expand this later)
    }

    int tWriteDia(String name, String text, int textDuration, int delayAfter) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            alphaGrad.setVisible(true);
            dialogueBox.setVisible(true);
            dialogueName.setText(name);
            dialogueText.setText("_");
            dialogueName.setVisible(true);
            dialogueText.setVisible(true);
        });
        int delayPerChar = (text.length() > 0) ? textDuration / text.length() : 0;
        int i = 0;
        final String[] buf = {""};
        while (i < text.length()) {
            buf[0] += text.charAt(i++);
            SwingUtilities.invokeLater(() -> { dialogueText.setText(buf[0] + "_"); });
            tWait(delayPerChar);
        }
        for (i = 0; i < delayAfter / 500; ++i) {
            if (i % 2 == 0) SwingUtilities.invokeLater(() -> { dialogueText.setText(buf[0] + "_"); });
            else SwingUtilities.invokeLater(() -> { dialogueText.setText(buf[0]); });
            tWait(500);
        }
        tWait(delayAfter % 500);
        SwingUtilities.invokeLater(() -> {
            alphaGrad.setVisible(false);
            dialogueBox.setVisible(false);
            dialogueName.setText("");
            dialogueText.setText("");
            dialogueName.setVisible(false);
        });
        return 0;
    }

    // ================= MOVEMENT & INPUT =================
    @Override
public void keyPressed(KeyEvent e) {

int prevX = charX;
int prevY = charY;

if (e.getKeyCode() < 41 && e.getKeyCode() >= 37 && plrMobile && !keyPress) {

keyPress = true;
plrDir = e.getKeyCode() - 37;

if (plrDir == 0) charX--;
else if (plrDir == 1) charY--;
else if (plrDir == 2) charX++;
else if (plrDir == 3) charY++;

// =========================
// Boundary Check
// =========================
if (charX < 0) charX = 0;
if (charY < 0) charY = 0;
if (charX > mapWidth - 2) charX = mapWidth - 2;
if (charY > mapHeight - 2) charY = mapHeight - 2;

// =========================
// CHECK ALL 4 PLAYER TILES
// =========================
boolean blocked = false;
int tileHit = 0;

for (int dx = 0; dx < 2; dx++) {
for (int dy = 0; dy < 2; dy++) {

int checkX = charX + dx;
int checkY = charY + dy;

int tileIndex = checkY * mapWidth + checkX;

if (tileIndex >= 0 && tileIndex < interLayout.length) {

int tile = interLayout[tileIndex];

if (tile == 1) {
blocked = true;
}

if (tile > 1) {
tileHit = tile;
}
}
}
}

// =========================
// WALL COLLISION
// =========================
if (blocked) {
charX = prevX;
charY = prevY;
}

// =========================
// DOOR LOGIC
// =========================
if (tileHit == 9) {

if (!hasKey) {

doDialog = true;
interType = 7;
plrMobile = false;

charX = prevX;
charY = prevY;
}
}

// =========================
// INTERACTIONS
// =========================
if (tileHit > 1 && tileHit != 9 && (doingObjective || isCompleting)) {

int type = tileHit;

if (type != 8 && !interDone[type - 2]) {

plrMobile = false;
interDone[type - 2] = true;

if (type == 7) {

hasKey = true;
doDialog = true;
interType = 5;

} else {

doDialog = true;
interType = type - 2;
}
}

else if (type == 8) {

isCompleting = false;
}
}

// =========================
// Update Player
// =========================
changeComponentConstraints(frame, plr, new Rectangle(charX, charY, 2, 2));

plr.setIcon(plrStates[plrDir * 3 + plrState]);

if (++plrState > 2)
plrState = 0;

overlay.repaint();
}
}


    @Override
    public void keyReleased(KeyEvent e) {
        keyPress = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Required by KeyListener interface
    }
}