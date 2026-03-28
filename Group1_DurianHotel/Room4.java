package Q4;

import Q3.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
public class Room4 implements KeyListener {
    JFrame frame;
    JLabel bg;
    
    int mapLayout[];
    int interLayout[]; 
    
    int mapWidth = 32;
    int mapHeight = 18;
    
    int frameWidth = 1600;
    int frameHeight = 900;
    
    ImageIcon icons[];
    int iconSizes[];
    int iconCount = 10;
    int ic = 0;
    
    JLabel objs[];
    int io = 0;
    boolean interDone[]; 
    
    ImageIcon plrStates[];
    JLabel plr;
    volatile boolean plrMobile;
    int plrDir = 3;
    int plrState = 0;
    int charX, charY;
    
    JLabel darkEffect;
    JLabel fadeEffect;
    JLabel alphaGrad;
    
    JLabel dialogueBox;
    JLabel dialogueName;
    JLabel dialogueText;
    
    JLabel idFront;
    JLabel idBack;
    
    volatile boolean doingObjective;
    volatile boolean doDialog;
    volatile boolean isCompleting;
    volatile int interType;
    volatile String interDia[];
    
    ImageIcon loadImg(String ref, int scaleX, int scaleY) {
    try {
        File imageFile = new File(ref);
        
        // Check if the file actually exists 
        if (!imageFile.exists()) {
            throw new java.io.FileNotFoundException("Resource missing: " + ref);
        }

        return new ImageIcon((new ImageIcon(ref)).getImage().getScaledInstance(
            (frameWidth / mapWidth) * scaleX, 
            (frameHeight / mapHeight) * scaleY, 
            Image.SCALE_DEFAULT));

    } catch (Exception e) {
        // Displays a clear and helpful error message as required by PD7 
        JOptionPane.showMessageDialog(frame, 
            "Error loading game assets: " + e.getMessage() + 
            "\nPlease ensure your folder structure is correct.", 
            "Critical File Error", 
            JOptionPane.ERROR_MESSAGE);
        
        // Prevents program from continuing in an unstable state 
        System.exit(1); 
        return null;
    }
}
    
    void addIcon(String ref, int scaleX, int scaleY) {
        icons[ic++] = loadImg(ref, scaleX, scaleY);
        iconSizes[(ic-1)*2] = scaleX;
        iconSizes[(ic-1)*2+1] = scaleY;
    }
    
    void changeComponentConstraints(JFrame frame, Component comp, Rectangle cons) {
        ((GraphPaperLayout)(frame.getContentPane().getLayout())).setConstraints(comp, cons);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public Room4() {
        frame = new JFrame();
        plrMobile = false;
        charX = 15; charY = 15; 
        plrStates = new ImageIcon[12];
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 3; ++j)
                plrStates[i*3+j] = loadImg("cytopro/"+("plr_"+i)+j+".PNG", 2, 2);
        plr = new JLabel(plrStates[plrDir*3+plrState]);
        
        icons = new ImageIcon[iconCount];
        iconSizes = new int[iconCount * 2];
        addIcon("cytopro/plr_31.PNG", 2, 2); 
        addIcon("cytopro/IMG_5147.PNG", 3, 2); 
        objs = new JLabel[6];
        interDone = new boolean[7];
        interDia = new String[]{"",
            "'Which Protein Helps with Movement?'", // Tile 3 (Question)
            "A note with the word 'Contractile and Motor' sticks to the drawer. Open it?",               // Tile 4 (Correct Answer)
            "A note with the word 'Structural and Receptor' sticks to the drawer. Open it?",               // Tile 5 (Wrong Answer)
            "",                                                      
            "...It won't open.",                                     // Tile 7
            "The drawer released toxic gas. GAME OVER.",             
        };
        mapLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        }; 
        interLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,4,4,1,1,1,0,0,1,1,1,5,5,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        }; 
        bg = new JLabel(loadImg("cytopro/IMG_5116.png", mapWidth, mapHeight));
        alphaGrad = new JLabel(loadImg("cytopro/alpha_grad.png", mapWidth, 3));
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
        idFront = new JLabel(loadImg("cytopro/id.png",12,10));
        idBack = new JLabel(loadImg("cytopro/id_back.png",12,10));
        dialogueName.setForeground(Color.WHITE);
        dialogueText.setForeground(Color.WHITE);
    }

    public void setFrame() {
        frame.setLayout(new GraphPaperLayout(new Dimension(mapWidth, mapHeight)));
        
        frame.add(dialogueName, new Rectangle(2,12,mapWidth-2,2));
        dialogueName.setFont(new Font("Courier New", Font.PLAIN, 40));
        frame.add(dialogueText, new Rectangle(2,14,mapWidth-2,3));
        dialogueText.setFont(new Font("Arial Unicode MS", Font.PLAIN, 25));
        frame.add(dialogueBox, new Rectangle(0,12,mapWidth,mapHeight-12));
        frame.add(alphaGrad, new Rectangle(0,9,mapWidth,3));
        
        frame.add(idFront, new Rectangle(10,0,12, 10));
        frame.add(idBack, new Rectangle(10,0,12, 10));
        
        frame.add(fadeEffect, new Rectangle(0,0,mapWidth,mapHeight));
        frame.add(plr, new Rectangle(charX, charY, 2, 2));
        frame.add(darkEffect, new Rectangle(0,0,mapWidth,mapHeight));
        
        alphaGrad.setVisible(false);
        dialogueBox.setVisible(false);
        dialogueName.setVisible(false);
        dialogueText.setVisible(false);
        idFront.setVisible(false);
        idBack.setVisible(false);
        plr.setVisible(false);
        
        for(int x = 0; x < mapWidth; ++x){
            for(int y = 0; y < mapHeight; ++y) {
                int iconId = mapLayout[y*mapWidth+x];
                if(iconId != 0) {
                    objs[io++] = new JLabel(icons[iconId-1]);
                    frame.add(objs[io-1], new Rectangle(x, y, iconSizes[(iconId-1)*2], iconSizes[(iconId-1)*2+1]));
                }
            }
        }
        
        frame.add(bg, new Rectangle(0,0,mapWidth,mapHeight));
        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        
        Thread storyThread = new Thread(() -> {
            try {
                runStoryline();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        });
        storyThread.start();
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
        int delayPerChar = textDuration/text.length(), i = 0;
        final String[] buf = {""};
        while(i < text.length()) {
            buf[0] += text.charAt(i++);
            SwingUtilities.invokeLater(() -> {dialogueText.setText(buf[0]+"_");});
            tWait(delayPerChar);
        }
        for(i = 0; i < delayAfter/500; ++i) {
            if(i%2 == 0) SwingUtilities.invokeLater(() -> {dialogueText.setText(buf[0]+"_");});
            else SwingUtilities.invokeLater(() -> {dialogueText.setText(buf[0]);});
            tWait(500);
        }
        tWait(delayAfter%500);
        SwingUtilities.invokeLater(() -> { 
            alphaGrad.setVisible(false);
            dialogueBox.setVisible(false);
            dialogueName.setText("");
            dialogueText.setText("");
            dialogueName.setVisible(false);
            dialogueText.setVisible(false);
        });
        return 0;
    }
    
    void tWait(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }
    
    public void runStoryline() throws InterruptedException {
        SwingUtilities.invokeLater(() -> { plr.setVisible(true); });
        for(int i[] = {255}; i[0] >= 0; --i[0]) {
            SwingUtilities.invokeLater(() -> {
                fadeEffect.setBackground(new Color(0,0,0,i[0]));
                frame.repaint();
            });
            tWait(4);
        }
        
        tWait(50);
        SwingUtilities.invokeLater(() -> { 
            darkEffect.setVisible(false); 
            fadeEffect.setVisible(false);
        });
        tWait(750);
        
        plrMobile = true;
        doingObjective = true;
        
        while(doingObjective) {
            tWait(20);
            if(doDialog) {
                if (interType == 1 || interType == 5) { // Question (3) & Locked Door (7)
                    tWriteDia("Euno", interDia[interType], 1000, 1000);
                } 
                else if (interType == 2 || interType == 3) { // Drawers 4 & 5
                    // Immediately show the JOptionPane with the label question
                    int choice = JOptionPane.showConfirmDialog(frame, interDia[interType], "", JOptionPane.YES_NO_OPTION);
                    
                    if (choice == JOptionPane.YES_OPTION) {
                        if (interType == 2) { // Mitosis
                            tWriteDia("Euno", "I found a key.", 1000, 1000);
                            doingObjective = false; 
                        } else { // Meiosis
                            plr.setVisible(false);
                                darkEffect.setVisible(true);
                            tWriteDia("System", "The drawer released toxic gas. GAME OVER.", 1000, 1000);
                            System.exit(0); 
                        }
                    } else {
                        // User chose NO, allow them to walk away
                        tWriteDia("Euno", "...Better be safe than sorry.", 500, 500);
                    }
                }
                plrMobile = true;
                doDialog = false;
            }
        }
        
        interLayout[16 * mapWidth + 15] = 8; 
        SwingUtilities.invokeLater(() -> { objs[0].setIcon(icons[4]); });
        
        plrMobile = true;
        isCompleting = true;
        
        Point oLoc = frame.getContentPane().getLocation();
        final boolean back[] = {false};
        
        tWriteDia("Euno", "...Another earthquake? This is getting redundant.", 2000, 1000);
        
        while(isCompleting) {
            tWait(20);
            SwingUtilities.invokeLater(() -> { frame.getContentPane().setLocation(oLoc.x + (back[0] ? 0 : 15), oLoc.y); });
            back[0] = !back[0];
        }
        
        SwingUtilities.invokeLater(() -> { frame.getContentPane().setLocation(oLoc); });
        SwingUtilities.invokeLater(() -> {
            Hall4 nextMap = new Hall4(); 
            nextMap.setFrame(); 
            frame.dispose(); 
        });
    }
    
    boolean keyPress = false; 
    @Override
    public void keyPressed(KeyEvent e) {
        int prevX = charX, prevY = charY;
        if (e.getKeyCode() < 41 && e.getKeyCode() >= 37 && plrMobile && !keyPress) {
            keyPress = true;
            plrDir = e.getKeyCode() - 37; 
            if (plrDir == 0) charX--;
            else if (plrDir == 1) charY--;
            else if (plrDir == 2) charX++;
            else if (plrDir == 3) charY++;

            int tile = interLayout[charY * mapWidth + charX];
            if (tile == 0) {
                charX = prevX; charY = prevY;
            } else if (tile > 1 && (doingObjective || isCompleting)) {
                if (tile == 8) {
                    isCompleting = false;
                } else {
                    int typeIndex = tile - 2;
                    if (!interDone[typeIndex] || typeIndex == 1 || typeIndex == 2 || typeIndex == 3 || typeIndex == 5) {
                        plrMobile = false;
                        interDone[typeIndex] = true;
                        
                        if (tile == 2) {
                            plrMobile = true; 
                        } else {
                            doDialog = true;
                            interType = typeIndex;
                        }
                    }
                }
            }
            changeComponentConstraints(frame, plr, new Rectangle(charX, charY, 2, 2));
            plr.setIcon(plrStates[plrDir * 3 + plrState]);
            if (++plrState > 2) plrState = 0;
        }
    }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) { keyPress = false; }   
}