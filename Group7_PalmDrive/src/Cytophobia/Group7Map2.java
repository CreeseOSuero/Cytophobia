package Cytophobia;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.io.*;

public class Group7Map2 implements KeyListener {
    JFrame frame;
    JLabel bg;
    
    int mapLayout[];
    
    int mapWidth = 32;
    int mapHeight = 18;
    
    int frameWidth = 1600;
    int frameHeight = 900;
    
    ImageIcon icons[];
    int iconSizes[];
    int iconCount = 10;
    int ic = 0;
    
    volatile Group7Player plr;
    volatile boolean objMode = false;
    
    int movementCooldown = 125;
    
    JLabel alphaGrad;
    
    JLabel dialogueBox;
    JLabel dialogueName;
    JLabel dialogueText;
    
    volatile JLabel mtiles[];
    volatile int cmtile = 0;
    volatile int cmidx = 0;
    volatile int mtileSeq[];
    volatile int seqlen = 0;
    volatile boolean finishedRound = false;
    volatile int round = 0;
    
    volatile List<Thread> genThreads;
    void addThread(Runnable r) {
        Thread e = new Thread(() -> {
            try {
                r.run();
            } finally {
                genThreads.remove(Thread.currentThread());
            }
        });
        genThreads.add(e);
        e.start();
    }
    ImageIcon loadImg(String ref, int scaleX, int scaleY) {
        return new ImageIcon((new ImageIcon(ref)).getImage().getScaledInstance((frameWidth/mapWidth) * scaleX, (frameHeight/mapHeight) * scaleY, Image.SCALE_DEFAULT));
    }
    
    int translateToMTIdx(int x, int y) {
        return (x-12)/2 + 2 * (y-6);
    }
    
    void setPath() {
        if (round == 0) {
            mtileSeq = new int[]{0, 1, 2, 6, 5, 4};
        } 
        else if (round == 1) {
            mtileSeq = new int[]{4, 8, 12, 13, 9, 5, 1, 2, 3, 7, 11, 10};
        } 
        else if (round == 2) {
            mtileSeq = new int[]{
                10, 11, 15, 14, 13, 12, 8, 9, 5, 4, 0, 1, 2, 6, 7, 3
            };
        }
    }
    
    public void showPath() throws InterruptedException {
        plr.setMobility(false);
        cmidx = 0;
        int slen = (round == 0) ? 6 : (round == 1) ? 12 : 16;
        seqlen = slen;
        setPath();

        for(int i = 0; i < slen; i++) {
            if(mtileSeq[i] == -1) break;

            final int tileIndex = mtileSeq[i]; 
            System.out.println("Showing tile: " + tileIndex);
            SwingUtilities.invokeLater(() -> mtiles[tileIndex].setVisible(true));
            tWait(1000);
            SwingUtilities.invokeLater(() -> mtiles[tileIndex].setVisible(false));
            tWait(100);
        }

        plr.setMobility(true);
    }
    
    void addIcon(String ref, int scaleX, int scaleY) {
        icons[ic++] = loadImg(ref, scaleX, scaleY);
        iconSizes[(ic-1)*2] = scaleX;
        iconSizes[(ic-1)*2+1] = scaleY;
    }
    
    public Group7Map2() {
        genThreads = new ArrayList<>();
        frame = new JFrame();
        
        
        ImageIcon s[] = new ImageIcon[12];
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 3; ++j)
                s[i*3+j] = loadImg(Group7Initializer.assetsFolder+("plr_"+i)+j+".PNG", 2, 2);
        plr = new Group7Player(new JLabel(), 14, 4, s); // 14, 4 true start, 12 6 4 by 4 start
        plr.setMobility(false);
        
        mtiles = new JLabel[16];
        mtileSeq = new int[16];
        
        icons = new ImageIcon[iconCount];
        iconSizes = new int[iconCount * 2];
        addIcon(Group7Initializer.assetsFolder+"mtile.png", 2, 2);
        addIcon(Group7Initializer.assetsFolder+"inverted_door.png", 2, 3);
        mapLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };
        bg = new JLabel(loadImg(Group7Initializer.assetsFolder+"room2.png", mapWidth, mapHeight));
        
        alphaGrad = new JLabel(loadImg(Group7Initializer.assetsFolder+"alpha_grad.png", mapWidth, 3));
        
        dialogueBox = new JLabel();
        dialogueBox.setBackground(Color.BLACK);
        dialogueBox.setOpaque(true);
        
        dialogueName = new JLabel();
        dialogueText = new JLabel();
        
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
        
        frame.add(plr.getJLabel(), new Rectangle(0,0,2,2));
        
        plr.updatePos(frame);
        
        alphaGrad.setVisible(false);
        dialogueBox.setVisible(false);
        dialogueName.setVisible(false);
        dialogueText.setVisible(false);
        
        for(int y = 0; y < mapHeight; ++y){
            for(int x = 0; x < mapWidth; ++x) {
                int iconId = mapLayout[y*mapWidth+x];
                if(iconId == 1) {
                    mtiles[cmtile] = new JLabel(icons[iconId-1]);
                    frame.add(mtiles[cmtile], new Rectangle(x, y, iconSizes[(iconId-1)*2], iconSizes[(iconId-1)*2+1]));
                    mtiles[cmtile].setVisible(false);
                    cmtile++;
                }
                else if(iconId != 0) {
                    frame.add(new JLabel(icons[iconId-1]), new Rectangle(x, y, iconSizes[(iconId-1)*2], iconSizes[(iconId-1)*2+1]));
                }
            }
        }
        
        frame.add(bg, new Rectangle(0,0,mapWidth,mapHeight));
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.addKeyListener(this);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                for(Thread t : genThreads) {
                    t.interrupt();
                }
                // need to close thread through interrupt, dont wanna have it running
            }
        });
        SwingUtilities.invokeLater(() -> {
            addThread(() -> {
                try {
                    tWriteDia("Euno", "...", 1000, 1000);
                    tWriteDia("Euno", "What is this room?", 2000, 1000);
                    tWriteDia("???", "Welcome, guest.", 1000, 1000);
                    for(int i = 0; i < 4; ++i) { plr.changeDirAndState(i, plr.getState()); tWait(500); }
                    tWriteDia("Euno", "Who's that?", 1000, 500);
                    tWriteDia("???", "It doesn't matter. Was it fun earlier?", 1000, 500);
                    tWriteDia("Euno", "... No?", 1000, 500);
                    tWriteDia("???", "You'll have fun... in this little game then.", 1000, 1000);
                    startBlinkingTile();
                    tWriteDia("???", "Go to that blinking tile.", 1000, 1000);
                    tWriteDia("Euno", "... Huh? ...", 1000, 500);
                    tWriteDia("Euno", "Tsk.. Alright then.", 1000, 500);
                    plr.setMobility(true);
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            });
        });
    }
    public static int getAttemptCount() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(Menu.resolveFilePath("gr7attempts.dat"))))) {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return 0;
        }
    }
    public static void incAttemptCount() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Menu.resolveFilePath("gr7attempts.dat"))))) {
            writer.write(String.valueOf(getAttemptCount()+1));
            writer.newLine();
        } catch (Exception e) {}
    }
    public static long getFastestFinishTime() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(Menu.resolveFilePath("gr7fastest.dat"))))) {
            return Long.parseLong(reader.readLine());
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }
    public static void setFastestFinishTime(long f) {
        if(f < getFastestFinishTime()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Menu.resolveFilePath("gr7fastest.dat"))))) {
                writer.write(String.valueOf(f));
                writer.newLine();
            } catch (Exception e) {}
        }
    }
    void startBlinkingTile() {
        addThread(() -> {
            try {
                round = 0;
                boolean t[] = {false};
                while(!objMode) {
                    SwingUtilities.invokeLater(() -> {mtiles[0].setVisible(t[0]);});
                    t[0] = !t[0];
                    tWait(1000);
                }
                SwingUtilities.invokeLater(() -> {mtiles[0].setVisible(false);});
                plr.setMobility(false);
                tWriteDia("???", "Follow the path shown by the tiles. There will be 3 rounds.", 3000, 2000);
                tWriteDia("???", "You have one chance. Good luck.", 1000, 1000);
                for(int i = 0; i < 3; ++i) {
                    tWriteDia("", "Round "+(i+1), 1000, 1000);
                    showPath();
                    plr.setMobility(true);
                    while(!finishedRound) {tWait(20);}
                    finishedRound = false;
                    round++;
                }
                
                tWriteDia("???", "You had succeeded, as expected.", 2000, 1000);
                tWriteDia("", "Map cleared", 1000, 1000);
                long cur = System.currentTimeMillis() - Group7Map1.startTime;
                long fast = getFastestFinishTime();
                if(fast == Long.MAX_VALUE) {
                    setFastestFinishTime(cur);
                    tWriteDia("", String.format("First finish time is %d min %d sec.", cur/60000, (cur/1000)%60), 1000, 3000);
                }
                else if(cur >= fast) {
                    tWriteDia("", String.format("Fastest finish time is %d min %d sec.", fast/60000, (fast/1000)%60), 1000, 3000);
                    tWriteDia("", String.format("Current finish time is %d min %d sec.", cur/60000, (cur/1000)%60), 1000, 3000);
                } else {
                    setFastestFinishTime(cur);
                    tWriteDia("", String.format("New fastest finish time of %d min %d sec!", fast/60000, (fast/1000)%60), 1000, 3000);
                }
                Menu.startNextLevel(7);
                frame.dispose();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        });
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
    
    boolean keyPress = false; // prevent longpress spam
    long prevMs = 0;
    
    boolean e1 = false;
    boolean e2 = false;
    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if(!e1 && e.getKeyCode() >= 41 || e.getKeyCode() < 37) {
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
        int prevX = plr.getX(), prevY = plr.getY();
        if(e.getKeyCode() < 41 && e.getKeyCode() >= 37 && plr.canMove() && !keyPress && (System.currentTimeMillis() - prevMs) >= movementCooldown) {
            prevMs = System.currentTimeMillis();
            keyPress = true;
            plr.changeDirAndState(e.getKeyCode() - 37);
            if(!objMode) {
                ((Group7Entity)plr).move(e.getKeyCode() - 37);
                if(plr.getX() >= 8 && plr.getX() <= 22 && plr.getY() >= 4 && plr.getY() <= 12) plr.updatePos(frame);
                else plr.setPos(prevX, prevY);
                if(plr.getX() == 12 && plr.getY() == 6) {
                    objMode = true;
                }
            } else {
                plr.move(2, 2, e.getKeyCode() - 37);
                if(mapLayout[plr.getY()*mapWidth + plr.getX()] != 1) plr.setPos(prevX, prevY);
                else {
                    cmtile = translateToMTIdx(plr.getX(), plr.getY());
                    plr.updatePos(frame);
                    if(cmidx+1 == seqlen-1) {
                        finishedRound = true;
                    } else if(cmtile != mtileSeq[++cmidx]) {
                        addThread(() -> {
                            try {
                                plr.setMobility(false);
                                tWriteDia("???", "Oops... wrong tile.", 2000, 1000);
                                tWriteDia("???", "Game over.", 2000, 5000);
                                incAttemptCount();
                                (new Group7Map1()).setFrame();
                                frame.dispose();
                            } catch (InterruptedException f) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        });
                    }
                }
            }
            
            prevMs = System.currentTimeMillis();
            keyPress = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {keyPress = false;}
    @Override
    public void keyTyped(KeyEvent e) {}
    
}
