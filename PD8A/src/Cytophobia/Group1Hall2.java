package Cytophobia;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class Group1Hall2 implements KeyListener {
    JFrame frame;
    JLabel bg;
    
    int mapLayout[];
    int interLayout[]; // interaction layout, corresponding for collisions and interactable objects
    
    int mapWidth = 32;
    int mapHeight = 18;
    
    int frameWidth = 1600;
    int frameHeight = 900;
    
    ImageIcon icons[];
    int iconSizes[];
    int iconCount = 10;
    int ic = 0;
    
    JLabel objs[];// Labels for placed map objects
    int io = 0;
    boolean interDone[]; // Tracks whether each interaction has already been completed
    
    ImageIcon plrStates[];// Player sprite states (direction × animation frame)
    JLabel plr;
    volatile boolean plrMobile;// Whether the player is allowed to move (volatile for thread safety)
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
    
    // used in storyline thread, needs to be volatile
    volatile boolean doingObjective;
    volatile boolean doDialog;
    volatile boolean isCompleting;
    volatile int interType;
    volatile String interDia[];
    
    ImageIcon loadImg(String ref, int scaleX, int scaleY) {
        return new ImageIcon((new ImageIcon(getClass().getResource("/"+ref))).getImage().getScaledInstance((frameWidth/mapWidth) * scaleX,
                (frameHeight/mapHeight) * scaleY,
                Image.SCALE_DEFAULT));
    
    }
    
    void addIcon(String ref, int scaleX, int scaleY) {
        icons[ic++] = loadImg(ref, scaleX, scaleY);
        iconSizes[(ic-1)*2] = scaleX;
        iconSizes[(ic-1)*2+1] = scaleY;
    }
    
    // some hack ig, dont wanna use big jlabel array js for character
    void changeComponentConstraints(JFrame frame, Component comp, Rectangle cons) {
        // use setConstraints from graph paper layout, which is basically the layout of the content pane of the frame
        // setContraints has args component and constraints, in which JLabel is a component ig
        ((GraphPaperLayout)(frame.getContentPane().getLayout())).setConstraints(comp, cons);
        // update the frame now
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    

    public Group1Hall2() {
        frame = new JFrame();
        
        plrMobile = false;
        charX = 5; charY = 8; //CHARACTER POSITION
        plrStates = new ImageIcon[12];
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 3; ++j)
                plrStates[i*3+j] = loadImg("assets1/"+("plr_"+i)+j+".PNG", 2, 2);
        plr = new JLabel(plrStates[plrDir*3+plrState]);
        
        icons = new ImageIcon[iconCount];
        iconSizes = new int[iconCount * 2];
        addIcon("assets1/plr_31.PNG", 2, 2); // id 1, testing only, 0-index
        addIcon("assets1/IMG_5065.PNG", 1, 2); // id 2 1-index
        addIcon("assets1/IMG_5065.PNG", 3, 3); // id 3 2-index
        addIcon("assets1/IMG_5065.PNG", 3, 3); // id 4 3-index
        addIcon("assets1/IMG_5065.PNG", 3, 3); // id 5 4-index
        addIcon("assets1/IMG_5065.PNG", 3, 3); // id 6 5-index
        addIcon("assets1/IMG_5065.PNG", 3, 3); // id 7 6-index
        addIcon("assets1/IMG_5106.PNG", 6, 5); // id 8 7-index ELEVATOR
        addIcon("assets1/IMG_5065.PNG", 3, 3); // id 9 8-index
        objs = new JLabel[6];
        interDone = new boolean[7];
        interDia = new String[]{"",
            "...It's open.",//interLayout ID 3
            "...Still locked.", //interLayout ID 4
            "", //interLayout ID 5
            "", //interLayout ID 6
            "...It won't open. I need a keycard.", //interLayout ID 7
            "...It needs a keycard.", //interLayout ID 8
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
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        }; // for positioning, this doesnt have to be even done, had to do it anyway
        interLayout = new int[]{
             0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,1,1,1,1,1,1,1,1,3,1,1,1,4,1,1,1,4,1,1,1,4,1,1,7,7,7,7,7,1,0,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        }; // .. oh the pain
        bg = new JLabel(loadImg("assets1/IMG_5159.PNG", mapWidth, mapHeight));
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
        idFront = new JLabel(loadImg("assets1/id.png",12,10));
        idBack = new JLabel(loadImg("assets1/id_back.png",12,10));
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
        
        // need to run storyline in another thread since delays are blocking when in Swing's Event Dispatch Thread
        Thread storyThread = new Thread(() -> {
            try {
                runStoryline();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        });
        storyThread.start();
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                storyThread.interrupt();
                // need to close thread through interrupt, dont wanna have it running
            }
        });
    }
    
    
    // SwingUtilities used for proper gui handling outside EDT thread of swing
    // according to google
    
    // hahhahahaha
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
            dialogueName.setVisible(false);
        });
        return 0;
    }
    
    
    // shorter name ig
    void tWait(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }
    
    // kinda inefficient atm, but it works
    public void runStoryline() throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            plr.setVisible(true);
        });
        // player fade in
        for(int i[] = {255}; i[0] >= 0; --i[0]) {
            SwingUtilities.invokeLater(() -> {
                fadeEffect.setBackground(new Color(0,0,0,i[0]));
                frame.repaint();
            });
            tWait(4);
        }
        
        tWait(50);
        SwingUtilities.invokeLater(() -> { // remove the other effects after some time ig, to prevent it
            darkEffect.setVisible(false); 
            fadeEffect.setVisible(false);
        });
        tWait(750);
        tWriteDia("Euno", "...The door vanished again.", 1000, 500);
        
        
        plrMobile = true;
        doingObjective = true;
        
        while(doingObjective) {
            tWait(20);
            if (interLayout[charY * mapWidth + charX] == 3) {
        plrMobile = false;
        tWriteDia("Euno", "The key fits perfectly in the lock.", 1000, 500);
        SwingUtilities.invokeLater(() -> {
            Group1Room3 nextMap = new Group1Room3(); 
            nextMap.setFrame(); 
            frame.dispose(); 
        });
        
        break; // Exit the loop as this window is closing
    }
            if(doDialog) {
                if(interType == 4) {
                    tWait(500);
                    SwingUtilities.invokeLater(() -> { objs[5].setIcon(icons[8]); });
                    tWait(500);
                    SwingUtilities.invokeLater(() -> { objs[5].setIcon(icons[1]); });
                }
                tWriteDia("Euno", interDia[interType], 1000, 1000);
                plrMobile = true;
                doDialog = false;
            }
        }
        
        plrMobile = true;
        doingObjective = false;
        doDialog = false;
        
        isCompleting = true;
        
        Point oLoc = frame.getContentPane().getLocation();
        final boolean back[] = {false};
        while(isCompleting) {
            tWait(20);
            SwingUtilities.invokeLater(() -> { frame.getContentPane().setLocation(oLoc.x + (back[0] ? 0 : 15), oLoc.y); });
            back[0] = !back[0];
        }
        SwingUtilities.invokeLater(() -> { 
            frame.getContentPane().setLocation(oLoc);
        });
        

        
        tWriteDia("Euno", "What is that thing?!", 2000, 2000);
        tWriteDia("Euno","It's getting closer! I need to get to the elevator!", 1000, 500);
        
    }
    
    boolean keyPress = false; // prevent longpress spam
    @Override
public void keyPressed(KeyEvent e) {
    int prevX = charX, prevY = charY;

    if (e.getKeyCode() < 41 && e.getKeyCode() >= 37 && plrMobile && !keyPress) {
        keyPress = true;

        // update direction FIRST (turning should always work)
        plrDir = e.getKeyCode() - 37; // 0 = left, 1 = up, 2 = right, 3 = down

        // attempt movement
        if (plrDir == 0) charX--;
        else if (plrDir == 1) charY--;
        else if (plrDir == 2) charX++;
        else if (plrDir == 3) charY++;

        int tile = interLayout[charY * mapWidth + charX];

        // block ONLY walls
        if (tile == 0) {
            charX = prevX;
            charY = prevY;
        }
        // interactions (still walkable)
        else if (tile > 1 && (doingObjective || isCompleting)) {
            int type = tile;

            if (type == 8) {
                isCompleting = false;
            }
            else if (!interDone[type - 2]|| (type - 2 == 2)|| (type - 2 == 5)|| (type - 2 == 6)) {
                plrMobile = false;
                interDone[type - 2] = true;

                if (type == 2) {
                } else {
                    doDialog = true;
                    interType = type - 2;
                }
                 
            }
        }

        // ALWAYS update position & sprite
        changeComponentConstraints(frame, plr, new Rectangle(charX, charY, 2, 2));
        plr.setIcon(plrStates[plrDir * 3 + plrState]);
        if (++plrState > 2) plrState = 0;
    }
}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) { keyPress = false; }   
    
    public static void main(String[] args) {
        (new Group1Hall2()).setFrame();
    }
}
