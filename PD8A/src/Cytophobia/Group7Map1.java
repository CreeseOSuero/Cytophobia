package Cytophobia;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class Group7Map1 implements KeyListener, ActionListener {
    JFrame frame;
    JLabel bg;
    
    int roomLayout[];
    int roomInterLayout[]; // interaction layout, corresponding for collisions and interactable objects
    int lobbyInterLayout[]; // 
    
    String ordering[];
    
    int curInterLayout[];
    
    int mapWidth = 32;
    int mapHeight = 18;
    
    int frameWidth = 1600;
    int frameHeight = 900;
    
    long movementCooldown = 125;
    
    ImageIcon icons[];
    int iconSizes[];
    int iconCount = 10;
    int ic = 0;
    
    volatile JLabel objs[];
    int io = 0;
    boolean interDone[]; // interaction done check
    
    volatile String questions[];
    volatile String answers[];
    volatile int correct[];
    
    ImageIcon plrStates[];
    JLabel plr;
    volatile boolean plrMobile;
    int plrDir = 3;
    int plrState = 0;
    int charX, charY;
    ImageIcon lobbyImg, roomImg;
    
    JLabel lobDoor;
   
    JLabel darkEffect;
    JLabel mapEffect;
    JLabel alphaGrad;
    JLabel book;
    
    JLabel dialogueBox;
    JLabel dialogueName;
    JLabel dialogueText;
    
    JButton choiceA;
    JButton choiceB;
    
    JLabel monster;
    
    JLabel fragL;
    
    volatile int livesLeft;
    JLabel livesLeftText;
    
    // used in storyline thread, needs to be volatile
    
    volatile boolean inRoom;
    
    volatile int monX, monY;// -8, -4
    volatile boolean roomsLocked[];
    volatile boolean fragContained[];
    volatile int frags;
    volatile int curLob;
    
    volatile int finalCode[];
    volatile int codeOrder[];
    
    volatile List<Thread> genThreads;
    
    volatile boolean criticalMoment;
    volatile int chosen = -1;
    
    public volatile static long startTime;
    
    void addThread(Runnable r, List<Thread> l) {
        Thread e = new Thread(() -> {
            try {
                r.run();
            } finally {
                l.remove(Thread.currentThread());
            }
        });
        l.add(e);
        e.start();
    }
    
    ImageIcon loadImg(String ref, int scaleX, int scaleY) {
        return new ImageIcon((new ImageIcon(Menu.getRes("/"+ref))).getImage().getScaledInstance((frameWidth/mapWidth) * scaleX, (frameHeight/mapHeight) * scaleY, Image.SCALE_DEFAULT));
    }
    
    void addIcon(String ref, int scaleX, int scaleY) {
        icons[ic++] = loadImg(ref, scaleX, scaleY);
        iconSizes[(ic-1)*2] = scaleX;
        iconSizes[(ic-1)*2+1] = scaleY;
    }
    
    ImageIcon getIcon(int id) {
        return icons[id-1];
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

    int randomDigit() {
        return (int)(Math.random()*10);
    }
    
    public Group7Map1() {
        startTime = System.currentTimeMillis();
        genThreads = new ArrayList<>();
        
        frame = new JFrame();
        
        criticalMoment = false;
        inRoom = false;
        plrMobile = true;
        charX = 14; charY = 5;
        livesLeft = 3;
        roomsLocked = new boolean[]{true, false, true, true, true, true}; //last one isnt the room, its the exit
        fragContained = new boolean[5];
        codeOrder = new int[]{1, 3, 0, 2, 4};
        finalCode = new int[]{randomDigit(), randomDigit(), randomDigit(), randomDigit(), randomDigit()};
        plrStates = new ImageIcon[12];
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 3; ++j)
                plrStates[i*3+j] = loadImg(Group7Initializer.assetsFolder+("plr_"+i)+j+".PNG", 2, 2);
        plr = new JLabel(plrStates[plrDir*3+plrState]);
        
        ordering = new String[]{"1st", "2nd", "3rd", "4th", "5th"};
        questions = new String[]{"What is the powerhouse of the cell?", "What is the basic unit of life?", "What organ do plants use to make food?", "What gas do humans need to breathe in to survive?", "What part of the cell controls the cell’s activities?"};
        answers = new String[]{
            "Rough ER", "Mitochondria",
            "Cell", "Tissue",
            "Leaf", "Root",
            "Carbon dioxide", "Oxygen",
            "Cell wall", "Nucleus"
        };
        correct = new int[]{1,0,0,1,1};
        
        icons = new ImageIcon[iconCount];
        iconSizes = new int[iconCount * 2];
        addIcon(Group7Initializer.assetsFolder+"monster.PNG", 2, 4); // id 1, test tile, 0-index
        addIcon(Group7Initializer.assetsFolder+"bed.PNG", 2, 3); // id 2
        addIcon(Group7Initializer.assetsFolder+"door.PNG", 2, 3); // id 3
        addIcon(Group7Initializer.assetsFolder+"lamp.PNG", 1, 2); // id 4
        addIcon(Group7Initializer.assetsFolder+"painting.PNG", 3, 3); // id 5
        addIcon(Group7Initializer.assetsFolder+"inverted_door.PNG", 2, 3); // id 6
        addIcon(Group7Initializer.assetsFolder+"plant.PNG", 2, 2); // id 7
        addIcon(Group7Initializer.assetsFolder+"chair.PNG", 1, 1); // id 8
        addIcon(Group7Initializer.assetsFolder+"book.PNG", 12, 10); // id 9
        objs = new JLabel[6];
        roomLayout = new int[mapWidth*mapHeight];
        roomLayout[79] = 6;
        roomLayout[83] = 5;
        roomLayout[138] = 4;
        roomLayout[168] = 2;
        roomLayout[171] = 8;
        roomLayout[406] = 7;
        
        roomInterLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,2,2,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };
        lobbyInterLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,4,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,5,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        }; // 8, 22
        curInterLayout = lobbyInterLayout;
        lobbyImg = loadImg(Group7Initializer.assetsFolder+"lobby.PNG", mapWidth, mapHeight);
        roomImg = loadImg(Group7Initializer.assetsFolder+"room.PNG", mapWidth, mapHeight);
        
        lobDoor = new JLabel(getIcon(3));
        
        bg = new JLabel(lobbyImg);
        
        alphaGrad = new JLabel(loadImg(Group7Initializer.assetsFolder+"alpha_grad.png", mapWidth, 3));
        
        dialogueBox = new JLabel();
        dialogueBox.setBackground(Color.BLACK);
        dialogueBox.setOpaque(true);
        
        darkEffect = new JLabel();
        darkEffect.setBackground(Color.BLACK);
        darkEffect.setOpaque(true);
        
        dialogueName = new JLabel();
        dialogueText = new JLabel();
        
        mapEffect = new JLabel();
        mapEffect.setBackground(Color.BLACK);
        mapEffect.setOpaque(true);
        
        dialogueName.setForeground(Color.WHITE);
        dialogueText.setForeground(Color.WHITE);
        livesLeftText = new JLabel("Lives left: "+livesLeft);
        livesLeftText.setForeground(Color.WHITE);
        
        choiceA = new JButton();
        choiceA.setFont(new Font("Courier New", Font.PLAIN, 20));
        choiceA.setBackground(Color.BLACK);
        choiceA.setForeground(Color.WHITE);
        choiceA.setBorderPainted(false);
        choiceA.setOpaque(true);
        choiceA.setFocusPainted(false);
        
        choiceB = new JButton();
        choiceB.setFont(new Font("Courier New", Font.PLAIN, 20));
        choiceB.setBackground(Color.BLACK);
        choiceB.setForeground(Color.WHITE);
        choiceB.setBorderPainted(false);
        choiceB.setOpaque(true);
        choiceB.setFocusPainted(false);
        
        book = new JLabel(getIcon(9));
        
        monster = new JLabel(getIcon(1));
       
    }
   
    
    public void setFrame() {
        frame.setLayout(new GraphPaperLayout(new Dimension(mapWidth, mapHeight)));
        
        frame.add(choiceA, new Rectangle((mapWidth-10)/2, 2, 10, 2));
        choiceA.addActionListener(this);
        frame.add(choiceB, new Rectangle((mapWidth-10)/2, 6, 10, 2));
        choiceB.addActionListener(this);
        
        frame.add(dialogueName, new Rectangle(2,12,mapWidth-2,2));
        dialogueName.setFont(new Font("Courier New", Font.PLAIN, 40));
        frame.add(dialogueText, new Rectangle(2,14,mapWidth-2,3));
        dialogueText.setFont(new Font("Arial Unicode MS", Font.PLAIN, 25));
        frame.add(dialogueBox, new Rectangle(0,12,mapWidth,mapHeight-12));
        frame.add(alphaGrad, new Rectangle(0,9,mapWidth,3));
        frame.add(book, new Rectangle(10,0,12,10));
        
        frame.add(mapEffect, new Rectangle(0,0,mapWidth,mapHeight));
        frame.add(monster, new Rectangle(0,0,2,4));
        frame.add(livesLeftText, new Rectangle(1,0,mapWidth-2,2));
        livesLeftText.setFont(new Font("Courier New", Font.PLAIN, 40));
        frame.add(plr, new Rectangle(charX, charY, 2, 2));
        frame.add(darkEffect, new Rectangle(0,0,mapWidth,mapHeight));
        frame.add(lobDoor, new Rectangle(15,2,2,3));
        
        fragL = new JLabel(loadImg(Group7Initializer.assetsFolder+"paperfragment.PNG",1,1));
        frame.add(fragL, new Rectangle(21,13,1,1));
        fragL.setVisible(false);
        
        monster.setVisible(false);
        alphaGrad.setVisible(false);
        dialogueBox.setVisible(false);
        dialogueName.setVisible(false);
        dialogueText.setVisible(false);
        plr.setVisible(true);
        darkEffect.setVisible(false);
        choiceA.setVisible(false);
        choiceB.setVisible(false);
        book.setVisible(false);
        
        for(int x = 0; x < mapWidth; ++x){
            for(int y = 0; y < mapHeight; ++y) {
                int iconId = roomLayout[y*mapWidth+x];
                if(iconId != 0) {
                    objs[io++] = new JLabel(icons[iconId-1]);
                    objs[io-1].setVisible(false);
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
        SwingUtilities.invokeLater(() -> {
            storyThread.start();
        }); // start when ready only
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                storyThread.interrupt();
                for(Thread t : genThreads) {
                    t.interrupt();
                }
                // need to close thread through interrupt, dont wanna have it running
            }
        });
    }
    
    // SwingUtilities used for proper gui handling outside EDT thread of swing
    // according to google
    
    void switchBG() {
        SwingUtilities.invokeLater(() -> {
            System.out.println(inRoom);
            System.out.println(inRoom ? "lobby" : "room");
            bg.setIcon(inRoom ? lobbyImg : roomImg);
        });
    }
    
    void loadRoomObjs(boolean toggle) {
        SwingUtilities.invokeLater(() -> {
            for(JLabel obj: objs) {
                obj.setVisible(toggle);
            }
        });
    }
    
    void mapEffectFadeInOut(boolean out, int duration) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            mapEffect.setBackground(new Color(0,0,0, out ? 0 : 255));
            mapEffect.setVisible(true);
        });
        if(out) {
            for(int i[] = {0}; i[0] < 256; ++i[0]) {
                System.out.println(i[0]);
                SwingUtilities.invokeLater(() -> { mapEffect.setBackground(new Color(0,0,0,i[0])); frame.repaint(); });
                tWait(duration/256);
            }
        } else {
            for(int i[] = {255}; i[0] >= 0; --i[0]) {
                SwingUtilities.invokeLater(() -> { mapEffect.setBackground(new Color(0,0,0,i[0])); frame.repaint(); });
                tWait(duration/256);
            }
            mapEffect.setVisible(false);
        }
    }
    
    void switchMap() throws InterruptedException {
        plrMobile = false;
        mapEffectFadeInOut(true, 2000);
        switchBG();
        loadRoomObjs(!inRoom);
        lobDoor.setVisible(inRoom);
        tWait(10);
        inRoom = !inRoom;
        if(inRoom) {
            curInterLayout = roomInterLayout;
            if(fragContained[curLob] == false) {
                SwingUtilities.invokeLater(() -> {fragL.setVisible(true);});
            }
        }
        else { curInterLayout = lobbyInterLayout; SwingUtilities.invokeLater(() -> {fragL.setVisible(false);}); };
        mapEffectFadeInOut(false, 2000);
        plrMobile = true;
    }
    
    void nextMap() {
        Group7Map2 g = new Group7Map2();
        g.setFrame();
        frame.dispose();
    }
    
    void lessenLives() throws InterruptedException {
        livesLeft -= 1;
        SwingUtilities.invokeLater(() -> {
           livesLeftText.setText("Lives left: "+livesLeft);
        });
        if(livesLeft == 0) {
            SwingUtilities.invokeLater(() -> {
                mapEffect.setBackground(new Color(0,0,0));
                mapEffect.setVisible(true);
            });
            tWriteDia("☹︎", "Game over.", 2500, 2000);
            Group7Map2.incAttemptCount();
        }
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
    
    
    // shorter name ig
    void tWait(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }
    
    // kinda inefficient atm, but it works
    public void runStoryline() throws InterruptedException {
        tWait(1000);
        plrMobile = false;
        tWriteDia("", "Number of previous attempts: "+Group7Map2.getAttemptCount(), 2000, 1000);
        tWriteDia("Euno", "Seems to be a new place.", 2000, 1000);
        tWriteDia("Euno", "Hmm... is this a book?", 2000, 1000);
        SwingUtilities.invokeLater(() -> {
            book.setVisible(true);
        });
        tWait(1000);
        tWriteDia("Euno", "It has torn pages... I guess I need to find them.", 2000, 1000);
        SwingUtilities.invokeLater(() -> {
            book.setVisible(false);
        });
        mapEffectFadeInOut(false, 2000);
        plrMobile = true;
    }
    
    boolean keyPress = false; // prevent longpress spam
    long prevMs = 0;
    boolean e1 = false;
    boolean e2 = false;
    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if(!e1 && (e.getKeyCode() >= 41 || e.getKeyCode() < 37)) {
                e1 = true;
                throw new Exception("Arrow keys are used for movement. This will only pop up once.");
            }
            if(!e2 && (System.currentTimeMillis() - prevMs) < movementCooldown) {
                e2 = true;
                throw new Exception("There is a cooldown for movement, so movement spam won't work. This will only pop up once.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
        int prevX = charX, prevY = charY;
        System.out.println(plrMobile);
        if(e.getKeyCode() < 41 && e.getKeyCode() >= 37 && plrMobile && !keyPress && (System.currentTimeMillis() - prevMs) >= movementCooldown) {
            prevMs = System.currentTimeMillis();
            keyPress = true;
            plrDir = e.getKeyCode() - 37; // 0, 1, 2, or 3
            if(plrDir == 0) charX--;
            else if(plrDir == 1) charY--;
            else if(plrDir == 2) charX++;
            else if(plrDir == 3) charY++;
            
            int curInter = curInterLayout[charY*mapWidth+charX];
            
            if(curInter <= 0 || curInter > 3) {
                charX = prevX;
                charY = prevY;
            }  else if(curInter == 2 && curInterLayout[prevY*mapWidth+prevX] != 2) {
                if(roomsLocked[curLob] == false) {
                    addThread(() -> {
                        try { switchMap(); } catch (InterruptedException i) { Thread.currentThread().interrupt(); return; }
                    }, genThreads);
                } else if(curLob == 5 && frags == 5) {
                    plrMobile = false;
                    addThread(() -> {
                        try { 
                            String code = "";
                            tWriteDia("Euno", "If I combine these torn pages to the diary...", 2500, 1000);
                            for(int i = 0; i < 5; ++i) {
                                code += finalCode[i];
                                tWriteDia("Euno", ordering[i]+" page has digit "+finalCode[i], 2000, 1000);
                            }
                            code = (new StringBuffer(code)).reverse().toString();
                            tWriteDia("Euno", "The password to this lock is...", 2000, 1000);
                            tWriteDia("Euno", code, 1000, 1000);
                            tWait(500);
                            plr.setIcon(plrStates[3]);
                            tWait(500);
                            tWriteDia("Euno", "Seems like it works. I gotta get out now.", 2000, 1000);
                            SwingUtilities.invokeLater(() -> {
                                mapEffect.setBackground(new Color(0,0,0));
                                mapEffect.setVisible(true);
                            });
                            tWriteDia("☺", "7th map cleared.", 2500, 2000);
                            nextMap();
                        } catch (InterruptedException i) { 
                            Thread.currentThread().interrupt(); 
                            return; 
                        }
                    }, genThreads);
                } else {
                    addThread(() -> {
                        try { 
                            plrMobile = false;
                            if(curLob != 5) {
                                tWriteDia("","Locked.",1000,500); 
                            } else {
                                tWriteDia("","Locked by code.",1000,500); 
                            }
                            plrMobile = true; 
                        } catch (InterruptedException i) { 
                            Thread.currentThread().interrupt(); 
                            return; 
                        }
                    }, genThreads);
                }
            } 
            if(!inRoom) {
                if(curInter == 4 && curLob < 5) {
                    addThread(() -> {
                        try { 
                            SwingUtilities.invokeAndWait(() -> {
                                mapEffect.setBackground(new Color(0,0,0));
                                mapEffect.setVisible(true);
                            }); 
                            tWait(20);
                            SwingUtilities.invokeAndWait(() -> {
                                mapEffect.setVisible(false);
                                mapEffect.repaint();
                            }); 
                        } catch (InterruptedException i) { 
                            Thread.currentThread().interrupt(); 
                            return; 
                        } catch (InvocationTargetException i) {
                            System.exit(-1);
                        }
                    }, genThreads);
                    curLob++;
                    charX = 22;
                } else if(curInter == 5 && curLob > 0) {
                    addThread(() -> {
                        try { 
                            SwingUtilities.invokeAndWait(() -> {
                                mapEffect.setBackground(new Color(0,0,0));
                                mapEffect.setVisible(true);
                            }); 
                            tWait(20);
                            SwingUtilities.invokeAndWait(() -> {
                                mapEffect.setVisible(false);
                                mapEffect.repaint();
                            }); 
                        } catch (InterruptedException i) { 
                            Thread.currentThread().interrupt(); 
                            return; 
                        } catch (InvocationTargetException i) {
                            System.exit(-1);
                        }
                    }, genThreads);
                    curLob--;
                    charX = 8;
                }
            } else {
                if(charX == 20 && charY == 12 && !fragContained[curLob]) {
                    changeComponentConstraints(frame, monster, new Rectangle(18, 9, 2, 4));
                    monster.setVisible(true);
                    fragL.setVisible(false);
                    fragContained[curLob] = true;
                    addThread(() -> {
                        try { 
                            plrMobile = false;
                            tWait(1000);
                            tWriteDia("???", "Á̸͚͆͐ṇ̶̮͕̐͘ś̴̤͖̦w̶̦̑̆͜ȩ̵̱̲̈r̴̮̓͌͘", 2500, 1000);
                            tWriteDia("???", questions[frags], 2500, 1000);
                            chosen = -1;
                            SwingUtilities.invokeLater(() -> {
                                choiceA.setText(answers[frags*2]);
                                choiceB.setText(answers[frags*2+1]);
                                choiceA.setVisible(true);
                                choiceB.setVisible(true);
                            });
                            while(chosen == -1) {tWait(20);}
                            if(chosen != correct[frags]) {
                                tWriteDia("???", "Wrong.", 1000, 1000);
                                lessenLives();
                            } else {
                                tWriteDia("???", "Right.", 1000, 1000);
                            }
                            if(livesLeft != 0) {
                                SwingUtilities.invokeLater(() -> {
                                    monster.setVisible(false);
                                }); 
                                if(++frags < 5) {
                                    roomsLocked[codeOrder[frags]] = false;
                                    tWriteDia("", "Key & fragment found.", 2500, 1000);
                                } else {
                                    tWriteDia("Euno", "... Wait, I know now. Let's go to the last door.", 2500, 1000);
                                }
                                plrMobile = true;
                            }
                        } catch (InterruptedException i) { 
                            Thread.currentThread().interrupt(); 
                            return; 
                        }
                    }, genThreads);
                }
            }
            changeComponentConstraints(frame, plr, new Rectangle(charX, charY, 2, 2));
            plr.setIcon(plrStates[plrDir*3 + plrState]);
            if(++plrState > 2) plrState = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) { keyPress = false; } 

    @Override
    public void actionPerformed(ActionEvent e) {
        choiceA.setVisible(false);
        choiceB.setVisible(false);
        if(e.getSource() == choiceA) chosen = 0;
        if(e.getSource() == choiceB) chosen = 1;
        frame.requestFocusInWindow();
    }
}
