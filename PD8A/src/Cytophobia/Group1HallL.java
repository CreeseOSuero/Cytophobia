package Cytophobia;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
public class Group1HallL implements KeyListener {
    // --- 1. ENCAPSULATION: All attributes set to PRIVATE ---
    private JFrame frame;
    private JLabel bg;
    
    private int[] mapLayout;
    private int[] interLayout;
    
    private int mapWidth = 32;
    private int mapHeight = 18;
    
    private int frameWidth = 1600;
    private int frameHeight = 900;
    
    private ImageIcon[] icons;
    private int[] iconSizes;
    private int iconCount = 10;
    private int ic = 0;
    
    private JLabel[] objs;
    private int io = 0;
    private boolean[] interDone;
    
    private ImageIcon[] plrStates;
    private JLabel plr;
    private volatile boolean plrMobile;
    private int plrDir = 3;
    private int plrState = 0;
    private int charX, charY;
    
    private JLabel darkEffect;
    private JLabel fadeEffect;
    private JLabel alphaGrad;
    
    private JLabel dialogueBox;
    private JLabel dialogueName;
    private JLabel dialogueText;
    
    private JLabel idFront;
    private JLabel idBack;
    
    private volatile boolean doingObjective;
    private volatile boolean doDialog;
    private volatile boolean isCompleting;
    private volatile int interType;
    private volatile String[] interDia;

    // --- 2. ENCAPSULATION: Public Accessors (Getters/Setters) ---
    
    public int getCharX() { return charX; }
    public void setCharX(int x) { 
        if(x >= 0 && x < mapWidth) this.charX = x; 
    }

    public int getCharY() { return charY; }
    public void setCharY(int y) { 
        if(y >= 0 && y < mapHeight) this.charY = y; 
    }

    public boolean isPlrMobile() { return plrMobile; }
    public void setPlrMobile(boolean mobile) { this.plrMobile = mobile; }

    public int getPlrDir() { return plrDir; }
    public void setPlrDir(int dir) { this.plrDir = dir; }

    // --- Internal Logic ---

    ImageIcon loadImg(String ref, int scaleX, int scaleY) {
        return new ImageIcon((new ImageIcon(getClass().getResource("/"+ref))).getImage().getScaledInstance((frameWidth/mapWidth) * scaleX,
                (frameHeight/mapHeight) * scaleY,
                Image.SCALE_DEFAULT));
    
    }
    
    private void addIcon(String ref, int scaleX, int scaleY) {
        icons[ic++] = loadImg(ref, scaleX, scaleY);
        iconSizes[(ic-1)*2] = scaleX;
        iconSizes[(ic-1)*2+1] = scaleY;
    }
    
    private void changeComponentConstraints(JFrame frame, Component comp, Rectangle cons) {
        ((GraphPaperLayout)(frame.getContentPane().getLayout())).setConstraints(comp, cons);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public Group1HallL() {
        frame = new JFrame();
        setPlrMobile(false);
        setCharX(21); 
        setCharY(8); 
        
        plrStates = new ImageIcon[12];
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 3; ++j)
                plrStates[i*3+j] = loadImg("assets1/"+("plr_"+i)+j+".PNG", 2, 2);
        
        plr = new JLabel(plrStates[plrDir*3+plrState]);
        
        icons = new ImageIcon[iconCount];
        iconSizes = new int[iconCount * 2];
        addIcon("assets1/plr_31.PNG", 2, 2);
        addIcon("assets1/IMG_5065.PNG", 1, 2);
        addIcon("assets1/IMG_5065.PNG", 3, 3);
        addIcon("assets1/IMG_5065.PNG", 3, 3);
        addIcon("assets1/IMG_5113.PNG", 3, 3);
        addIcon("assets1/IMG_5065.PNG", 3, 3);
        addIcon("assets1/IMG_5065.PNG", 3, 3);
        addIcon("assets1/IMG_5106.PNG", 6, 5);
        addIcon("assets1/IMG_5065.PNG", 3, 3);
        
        objs = new JLabel[6];
        interDone = new boolean[7];
        interDia = new String[]{" ", "...It's open.", "...Locked?", " ", " ", "...It won't open.", "...It needs a keycard."};

        mapLayout = new int[mapWidth * mapHeight];
        mapLayout[10*mapWidth+9] = 5;

        interLayout = new int[]{
             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,1,1,1,1,4,1,1,1,4,1,1,1,4,1,1,1,4,1,1,1,3,1,1,7,7,7,7,8,1,0,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,1,1,1,1,1,1,1,1,5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };

        bg = new JLabel(loadImg("assets1/IMG_5163.PNG", mapWidth, mapHeight));
        alphaGrad = new JLabel(loadImg("assets1/alpha_grad.png", mapWidth, 3));
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
        idFront = new JLabel(loadImg("assets1/id.png", 12, 10));
        idBack = new JLabel(loadImg("assets1/id_back.png", 12, 10));
        dialogueName.setForeground(Color.WHITE);
        dialogueText.setForeground(Color.WHITE);
    }

    public void setFrame() {
        frame.setLayout(new GraphPaperLayout(new Dimension(mapWidth, mapHeight)));
        frame.add(dialogueName, new Rectangle(2, 12, mapWidth - 2, 2));
        dialogueName.setFont(new Font("Courier New", Font.PLAIN, 40));
        frame.add(dialogueText, new Rectangle(2, 14, mapWidth - 2, 3));
        dialogueText.setFont(new Font("Arial Unicode MS", Font.PLAIN, 25));
        frame.add(dialogueBox, new Rectangle(0, 12, mapWidth, mapHeight - 12));
        frame.add(alphaGrad, new Rectangle(0, 9, mapWidth, 3));
        frame.add(idFront, new Rectangle(10, 0, 12, 10));
        frame.add(idBack, new Rectangle(10, 0, 12, 10));
        frame.add(fadeEffect, new Rectangle(0, 0, mapWidth, mapHeight));
        frame.add(plr, new Rectangle(getCharX(), getCharY(), 2, 2));
        frame.add(darkEffect, new Rectangle(0, 0, mapWidth, mapHeight));

        alphaGrad.setVisible(false);
        dialogueBox.setVisible(false);
        dialogueName.setVisible(false);
        dialogueText.setVisible(false);
        idFront.setVisible(false);
        idBack.setVisible(false);
        plr.setVisible(false);

        for (int x = 0; x < mapWidth; ++x) {
            for (int y = 0; y < mapHeight; ++y) {
                int iconId = mapLayout[y * mapWidth + x];
                if (iconId != 0) {
                    objs[io++] = new JLabel(icons[iconId - 1]);
                    frame.add(objs[io - 1], new Rectangle(x, y, iconSizes[(iconId - 1) * 2], iconSizes[(iconId - 1) * 2 + 1]));
                }
            }
        }

        frame.add(bg, new Rectangle(0, 0, mapWidth, mapHeight));
        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Thread storyThread = new Thread(() -> {
            try { runStoryline(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        storyThread.start();

        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { storyThread.interrupt(); }
        });
    }

    private void doTransitionEvent() {
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> {
                    darkEffect.setOpaque(true);
                    darkEffect.setBackground(Color.BLACK);
                    darkEffect.setVisible(true);
                    frame.repaint();
                });
                tWait(500);
                tWriteDia("SYSTEM", "Stage Clear!.", 1500, 1000);
                tWait(1000);
                SwingUtilities.invokeLater(() -> frame.repaint());
                long currentAttemptMillis = System.currentTimeMillis() - Group1Map.startTime;
                long fastestMillis = Long.MAX_VALUE; // Default if no file exists
                File recordFile = new File(Menu.resolveFilePath("g1fastest.txt"));
                try {
                    if (recordFile.exists()) {
                        java.util.Scanner reader = new java.util.Scanner(recordFile);
                        if (reader.hasNextLong()) {
                            fastestMillis = reader.nextLong();
                        }
                        reader.close();
                    }
                } catch (java.io.IOException e) {
                    System.out.println("Error reading record: " + e.getMessage());
                }
                if (currentAttemptMillis < fastestMillis) {
                    fastestMillis = currentAttemptMillis;
                    try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(recordFile))) {
                        writer.print(fastestMillis);
                    } catch (java.io.IOException e) {
                        System.out.println("Error saving record: " + e.getMessage());
                    }
                }
                String stats = String.format("Current Attempt: %d seconds\nFastest Attempt: %d seconds", 
                                            currentAttemptMillis / 1000, (fastestMillis / 1000));
                JOptionPane.showMessageDialog(frame, "Map Complete!\n" + stats);
                frame.dispose();
                Menu.startNextLevel(1);
            } catch (InterruptedException ex) { ex.printStackTrace(); }
        }).start();
    }

    private int tWriteDia(String name, String text, int textDuration, int delayAfter) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            alphaGrad.setVisible(true); dialogueBox.setVisible(true);
            dialogueName.setText(name); dialogueText.setText("_");
            dialogueName.setVisible(true); dialogueText.setVisible(true);
        });
        int delayPerChar = textDuration / text.length();
        final String[] buf = {""};
        for (char c : text.toCharArray()) {
            buf[0] += c;
            SwingUtilities.invokeLater(() -> dialogueText.setText(buf[0] + "_"));
            tWait(delayPerChar);
        }
        tWait(delayAfter);
        SwingUtilities.invokeLater(() -> {
            alphaGrad.setVisible(false); dialogueBox.setVisible(false);
            dialogueName.setVisible(false); dialogueText.setVisible(false);
        });
        return 0;
    }

    private void tWait(int ms) throws InterruptedException { Thread.sleep(ms); }

    public void runStoryline() throws InterruptedException {
        SwingUtilities.invokeLater(() -> plr.setVisible(true));
        for (int i = 255; i >= 0; i--) {
            final int alpha = i;
            SwingUtilities.invokeLater(() -> fadeEffect.setBackground(new Color(0, 0, 0, alpha)));
            tWait(4);
        }
        tWait(50);
        SwingUtilities.invokeLater(() -> { darkEffect.setVisible(false); fadeEffect.setVisible(false); });
        tWait(750);
        tWriteDia("Euno", "...What is that thing?!", 1000, 500);
        tWriteDia("Euno", "...It's getting closer! I need to get to the elevator!", 1000, 500);

        setPlrMobile(true);
        doingObjective = true;

        while (doingObjective) {
            tWait(20);
            if (doDialog) {
                if (interType == 4) {
                    tWait(500);
                    SwingUtilities.invokeLater(() -> objs[5].setIcon(icons[8]));
                    tWait(500);
                    SwingUtilities.invokeLater(() -> objs[5].setIcon(icons[1]));
                }
                tWriteDia("???", interDia[interType], 1000, 1000);
                setPlrMobile(true);
                doDialog = false;
            }
        }
    }

    private boolean keyPress = false;
    @Override
    public void keyPressed(KeyEvent e) {
        int prevX = getCharX(); int prevY = getCharY();
        if (e.getKeyCode() < 41 && e.getKeyCode() >= 37 && isPlrMobile() && !keyPress) {
            keyPress = true;
            setPlrDir(e.getKeyCode() - 37);

            if (getPlrDir() == 0) setCharX(getCharX() - 1);
            else if (getPlrDir() == 1) setCharY(getCharY() - 1);
            else if (getPlrDir() == 2) setCharX(getCharX() + 1);
            else if (getPlrDir() == 3) setCharY(getCharY() + 1);

            int tile = interLayout[getCharY() * mapWidth + getCharX()];
            if (tile == 0) {
                setCharX(prevX); setCharY(prevY);
            } else if (tile > 1 && (doingObjective || isCompleting)) {
                if (tile == 8) { isCompleting = false; }
                else if (tile == 7) { setPlrMobile(false); doTransitionEvent(); }
                else if (!interDone[tile - 2] || (tile - 2 == 2) || (tile - 2 == 5) || (tile - 2 == 6)) {
                    setPlrMobile(false);
                    interDone[tile - 2] = true;
                    if (tile == 2) { doingObjective = false; }
                    else { doDialog = true; interType = tile - 2; }
                }
            }
            changeComponentConstraints(frame, plr, new Rectangle(getCharX(), getCharY(), 2, 2));
            plr.setIcon(plrStates[getPlrDir() * 3 + plrState]);
            if (++plrState > 2) plrState = 0;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) { keyPress = false; }
    
    public static void main(String[] args) {
        (new Group1HallL()).setFrame();
    }
}
