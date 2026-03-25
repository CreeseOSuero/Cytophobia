package cs.goated;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*; 
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Arrays;

// --- OOP IMPLEMENTATION CLASSES ---
class Enemy {
    private int position; 
    private int speed;

    public Enemy(int startPos, int speed) {
        this.position = startPos;
        this.speed = speed;
    }

    public void updateLocation(int newPos) { this.position = newPos; }
    public void updateLocation(int x, int y, int mW) { this.position = (y * mW) + x; }

    public void moveLogic() {} 

    public int getPosition() { return position; }
    public void setPosition(int p) { this.position = p; }
}

class ChaserEnemy extends Enemy { 
    public ChaserEnemy(int startPos, int speed) { super(startPos, speed); }

    @Override 
    public void moveLogic() { /* Chase logic implemented in main loop */ }
}

public class PD5 implements KeyListener {
    private long lastMoveTime = 0;
    private final int moveDelay = 250; 
    int lastMove = -1;

    JFrame f; 
    ImageIcon wall, trash, barrel, table, stairs, shelf, planks, painting, floors, clock, box, saltIcon, saltBagIcon, saltCounterIcon, matchIcon, matchCounterIcon, effigyIcon, doorIcon;
    JLabel ts[], O[], CH[], stairs1, clock1, shelf1, shelf2, shelf3, shelf4, shelf5, table1, table2, painting1, plank1, plank2, trash1, box1, barrel1;
    
    int mL[], OP[];
    int mW = 26;
    int mH = 25;
    
    int fW = 2400; 
    int fH = 1800;    
    int tileW, tileH;

    int cp = 62; 
    int lastDir = 0;
    int step = 0;
    boolean canMove = true;

    // --- GAMEPLAY SYSTEM VARIABLES ---
    boolean hasSaltBag = false;
    int saltCount = 3;
    boolean hasMatches = false;
    int matchCount = 0;
    int effigyCount = 5;

    ImageIcon[] animd = new ImageIcon[3];
    ImageIcon[] animr = new ImageIcon[3];
    ImageIcon[] animl = new ImageIcon[3];
    ImageIcon[] animu = new ImageIcon[3];
    
    ChaserEnemy enemyLogic;
    JLabel enemyLabel;
    Timer enemyTimer;
    ImageIcon enemyIcon;

    JPanel container;
    JScrollPane cameraScroll;
    ShadowLayer shadow;

    JLabel dialogueBox, dialogueText, nameTag;

    public PD5() {
        f = new JFrame("PD Map - Integrated Build");

        this.tileW = (fW / mW);
        this.tileH = (fH / mH);     

        // --- LOADING & SCALING ---
        wall = scaleIcon("Images/walll.png", tileW, tileH);
        trash = scaleIcon("Images/trash.png", tileW, tileH);
        barrel = scaleIcon("Images/barrel.png", tileW, tileH);
        table = scaleIcon("Images/tablee.png", tileW, tileH);
        stairs = scaleIcon("Images/stairs.png", tileW*3, tileH*3); 
        shelf = scaleIcon("Images/shelf.png", tileW, tileH);
        planks = scaleIcon("Images/planks.png", tileW, tileH);
        painting = scaleIcon("Images/painting.png", tileW, tileH);
        floors = scaleIcon("Images/floors.png", tileW, tileH);
        clock = scaleIcon("Images/clock.png", tileW, tileH);
        box = scaleIcon("Images/box.png", tileW, tileH);
        
        saltIcon = scaleIcon("Images/saltIcon.png", tileW, tileH);
        saltBagIcon = scaleIcon("Images/saltBagIcon.png", tileW, tileH);
        saltCounterIcon = scaleIcon("Images/saltCounter.png", 45, 45);
        
        matchIcon = scaleIcon("Images/matches.png", tileW, tileH);
        matchCounterIcon = scaleIcon("Images/matchCounter.png", 45, 45);
        effigyIcon = scaleIcon("Images/effigy.png", tileW, tileH);
        doorIcon = scaleIcon("Images/door.png", tileW, tileH);

        for(int i=0; i<3; i++) {
            animd[i] = scaleIcon("Images/animd" + (i+1) + ".png", tileW, tileH);
            animr[i] = scaleIcon("Images/animr" + (i+1) + ".png", tileW, tileH);
            animl[i] = scaleIcon("Images/animl" + (i+1) + ".png", tileW, tileH);
            animu[i] = scaleIcon("Images/animu" + (i+1) + ".png", tileW, tileH);
        }

        enemyLogic = new ChaserEnemy(70, 400); 
        enemyIcon = scaleIcon("Images/enemy.png", tileW, tileH);
        enemyLabel = new JLabel(enemyIcon);

        OP=new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0,0,0,
            0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,6,6,6,0,0,7,7,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0, 
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,7,0,0,0,0,7,6,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        };

        mL=new int[]{
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
            2,2,2,5,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2, 
            2,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2,
            2,0,0,2,2,5,2,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2, 
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2,
            2,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2,
            2,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2, 
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,2,2,2,2,2,0,0,2,
            2,0,0,2,2,2,2,2,2,2,2,0,0,0,2,2,2,2,5,2,2,2,2,0,0,2, 
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
        };

        O=new JLabel[mW*mH];
        ts=new JLabel[mH*mW]; 
        CH=new JLabel[mW*mH]; 
        stairs1 = new JLabel(stairs);

        for(int i=0;i<O.length;i++){
            CH[i] = new JLabel();
            if (OP[i]==4) O[i]=new JLabel(saltBagIcon);
            else if(OP[i]==2) O[i]=new JLabel(clock);
            else if(OP[i]==3) O[i]=new JLabel(barrel);
            else if(OP[i]==5) O[i]=new JLabel(box);
            else if(OP[i]==6) O[i]=new JLabel(shelf);
            else if(OP[i]==7) O[i]=new JLabel(painting);
            else if(OP[i]==8) O[i]=new JLabel(matchIcon);
            else if(OP[i]==9) O[i]=new JLabel(effigyIcon);
            else O[i]=new JLabel();
        }
        CH[cp].setIcon(animd[0]);

        for(int i=0;i<ts.length;i++){
            if(mL[i]==0) ts[i]= new JLabel(floors);
            else if(mL[i]==2 || mL[i]==5) ts[i]=new JLabel(wall);
            else if(mL[i]==9) ts[i]=new JLabel(floors);
        }

        enemyTimer = new Timer(600, e -> moveEnemyAI());
        enemyTimer.start();

        dialogueBox = new JLabel();
        dialogueBox.setBounds(240, 500, 600, 100);
        dialogueBox.setBackground(new Color(10, 10, 10, 230));
        dialogueBox.setOpaque(true);
        dialogueBox.setVisible(false);

        nameTag = new JLabel(" Euno ", SwingConstants.CENTER);
        nameTag.setBounds(240, 475, 80, 25);
        nameTag.setBackground(new Color(60, 0, 0));
        nameTag.setForeground(Color.WHITE);
        nameTag.setOpaque(true);
        nameTag.setVisible(false);

        dialogueText = new GlitchLabel("", SwingConstants.CENTER);
        dialogueText.setBounds(250, 510, 580, 80);
        dialogueText.setForeground(Color.WHITE);
        dialogueText.setFont(new Font("Serif", Font.ITALIC, 20));
        dialogueText.setVisible(false);
    }   

    private ImageIcon scaleIcon(String path, int w, int h) {
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
    }

    public void setFrame() {
        container = new JPanel(new GraphPaperLayout(new Dimension(mW, mH)));
        container.setPreferredSize(new Dimension(fW, fH));

        for (int i = 0; i < CH.length; i++) {
            if (CH[i] != null) container.add(CH[i], new Rectangle(i % mW, i / mW, 1, 1));
        }
        container.add(enemyLabel, new Rectangle(enemyLogic.getPosition() % mW, enemyLogic.getPosition() / mW, 1, 1));

        for (int i = 0; i < O.length; i++) {
            if (O[i] != null) container.add(O[i], new Rectangle(i % mW, i / mW, 1, 1));
        }

        container.add(stairs1, new Rectangle(9, 0, 2, 2));

        for (int i = 0; i < ts.length; i++) {
            if (ts[i] != null) container.add(ts[i], new Rectangle(i % mW, i / mW, 1, 1));
        }

        cameraScroll = new JScrollPane(container);
        cameraScroll.setBorder(null);
        cameraScroll.setBounds(0, 0, 1080, 700); 
        cameraScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cameraScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        JLayeredPane lp = f.getLayeredPane();
        lp.add(cameraScroll, Integer.valueOf(0));

        shadow = new ShadowLayer();
        shadow.setBounds(0, 0, 1080, 700);
        lp.add(shadow, Integer.valueOf(50));

        lp.add(dialogueBox, Integer.valueOf(100));
        lp.add(nameTag, Integer.valueOf(101));
        lp.add(dialogueText, Integer.valueOf(102));

        f.setSize(1080, 700); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null); 
        f.addKeyListener(this);
        f.setVisible(true);
        
        updateCamera();
    }

    private void checkWinCondition() {
        if (effigyCount <= 0) {
            int exitTile = 38; 
            OP[exitTile] = 10; // Door is an Object now
            O[exitTile].setIcon(doorIcon);
            
            enemyTimer.stop();
            enemyTimer = new Timer(1000, e -> moveEnemyAI());
            enemyTimer.start();
            
            showDialogue("THE DOOR IS OPEN. RUN!");
            shadow.repaint();
        }
    }

    private void triggerWin() {
        canMove = false;
        enemyTimer.stop();
            JOptionPane.showMessageDialog(f, "The end?..");
        System.exit(0);
    }

    public void moveEnemyAI() {
        int startNode = enemyLogic.getPosition();
        int targetNode = cp; 

        if (startNode == targetNode) return;

        // --- SCREEN SHAKE LOGIC ---
        if (effigyCount <= 0) {
            int dist = Math.abs((startNode % mW) - (cp % mW)) + Math.abs((startNode / mW) - (cp / mW));
            if (dist < 8) {
                int intensity = (8 - dist) * 2;
                f.setLocation(f.getX() + (int)(Math.random()*intensity-intensity/2), 
                              f.getY() + (int)(Math.random()*intensity-intensity/2));
            }
        }

        int[] edgeTo = new int[mL.length];
        java.util.Arrays.fill(edgeTo, -1);
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        java.util.HashSet<Integer> visited = new java.util.HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        boolean found = false;
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            if (curr == targetNode) { found = true; break; }

            int[] neighbors = {curr + 1, curr - 1, curr + mW, curr - mW};
            for (int next : neighbors) {
                boolean canPassSalt = (effigyCount <= 0);
                boolean isPathable = (next >= 0 && next < mL.length && (mL[next] == 0 || mL[next] == 9));

                if (isPathable && (canPassSalt || OP[next] != 3) && !visited.contains(next)) {
                    if (Math.abs((next % mW) - (curr % mW)) <= 1) {
                        visited.add(next);
                        edgeTo[next] = curr;
                        queue.add(next);
                    }
                }
            }
        }

        if (found) {
            int nextMove = targetNode;
            while (edgeTo[nextMove] != startNode && edgeTo[nextMove] != -1) {
                nextMove = edgeTo[nextMove];
            }

            enemyLogic.setPosition(nextMove);
            container.add(enemyLabel, new Rectangle(nextMove % mW, nextMove / mW, 1, 1), 0);
            enemyLabel.setLocation((nextMove % mW) * tileW + (int)(Math.random()*4-2), 
                                   (nextMove / mW) * tileH + (int)(Math.random()*4-2));
            
            container.revalidate();
            container.repaint();

            if (nextMove == cp) {
                canMove = false;
                enemyTimer.stop();
                JOptionPane.showMessageDialog(f, "NO ESCAPE.");
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!canMove) return;
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_Q) {
            if (mL[cp] == 9 && hasMatches && matchCount > 0) {
                effigyCount--; matchCount--;
                showDialogue("Effigy burned.");
                checkWinCondition();
                mL[cp] = 0; O[cp].setIcon(null);
            } 
            else if (hasMatches && matchCount > 0) {
                int[] sides = {cp+1, cp-1, cp+mW, cp-mW};
                for(int s : sides) {
                    if(s >= 0 && s < mL.length && mL[s] == 5) {
                        effigyCount--; matchCount--;
                        mL[s] = 2; 
                        showDialogue("Effigy burned.");
                        checkWinCondition();
                        break;
                    }
                }
            }
            shadow.repaint();
        }

        if (keyCode == KeyEvent.VK_E) {
            boolean acted = false;
            if (OP[cp] == 4) { 
                hasSaltBag = true; OP[cp] = 0; O[cp].setIcon(null); 
                showDialogue("Found salt bag.");
                acted = true;
            }
            else if (OP[cp] == 8) { 
                hasMatches = true; matchCount += 5; OP[cp] = 0; O[cp].setIcon(null); 
                showDialogue("Found matches.");
                acted = true;
            }
            if (!acted && hasSaltBag && saltCount > 0 && OP[cp] == 0 && mL[cp] == 0) {
                OP[cp] = 3; O[cp].setIcon(saltIcon); saltCount--;
            }
            shadow.repaint();
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime < moveDelay) return; 

        int newCp = cp;
        ImageIcon[] currentAnim = animd;

        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) { newCp = cp + 1; currentAnim = animr; lastDir = 1; }
        else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) { newCp = cp - 1; currentAnim = animl; lastDir = -1; }
        else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) { newCp = cp + mW; currentAnim = animd; lastDir = mW; }
        else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) { newCp = cp - mW; currentAnim = animu; lastDir = -mW; }

        if (newCp != cp && newCp >= 0 && newCp < mL.length) {
            // Check for Door (Object Type 10)
            if (OP[newCp] == 10) {
                triggerWin();
                return;
            }
            if (mL[newCp] == 0 || mL[newCp] == 9) { 
                lastMoveTime = currentTime; 
                CH[cp].setIcon(null); 
                cp = newCp;
                step = (step == 1) ? 2 : 1; 
                CH[cp].setIcon(currentAnim[step]);
                updateCamera();
                shadow.repaint();
            } else {
                CH[cp].setIcon(currentAnim[0]); 
            }
        }
    }

    private void showDialogue(String s) {
        dialogueBox.setVisible(true); dialogueText.setText(s); dialogueText.setVisible(true);
        new Timer(2000, ev -> { dialogueBox.setVisible(false); dialogueText.setVisible(false); }).start();
    }

    public void keyReleased(KeyEvent e) { 
        if(lastDir == 1) CH[cp].setIcon(animr[0]);
        else if(lastDir == -1) CH[cp].setIcon(animl[0]);
        else if(lastDir == mW) CH[cp].setIcon(animd[0]);
        else if(lastDir == -mW) CH[cp].setIcon(animu[0]);
    }

    public void updateCamera() {
        int px = (cp % mW) * tileW;
        int py = (cp / mW) * tileH;
        cameraScroll.getHorizontalScrollBar().setValue(px - (1080 / 2) + (tileW / 2));
        cameraScroll.getVerticalScrollBar().setValue(py - (700 / 2) + (tileH / 2));
    }

    public void keyTyped(KeyEvent e) {}

    class ShadowLayer extends JComponent {
        public ShadowLayer() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int px = (cp % mW) * tileW - cameraScroll.getHorizontalScrollBar().getValue();
            int py = (cp / mW) * tileH - cameraScroll.getVerticalScrollBar().getValue();
            
            Area mask = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
            int r = 400; 
            Ellipse2D hole = new Ellipse2D.Double(px + (tileW/2) - (r/2), py + (tileH/2) - (r/2), r, r);
            mask.subtract(new Area(hole));
            
            if (effigyCount <= 0) {
                g2.setColor(new Color(150, 0, 0, 160)); 
            } else {
                g2.setColor(Color.BLACK);
            }
            g2.fill(mask);

            g2.setColor(Color.WHITE); g2.setFont(new Font("Monospaced", Font.BOLD, 22));
            if (hasSaltBag) {
                g2.drawImage(saltCounterIcon.getImage(), 30, 30, null);
                g2.drawString("x" + saltCount, 85, 60);
            }
            if (hasMatches) {
                g2.drawImage(matchCounterIcon.getImage(), 30, 85, null);
                g2.drawString("x" + matchCount, 85, 115);
            }
            g2.setColor(Color.RED);
            g2.drawString("EFFIGIES LEFT: " + effigyCount, getWidth() - 250, 60);
        }
    }

    class GlitchLabel extends JLabel {
        public boolean isGlitching = true;
        public GlitchLabel(String text, int align) {
            super(text, align);
            new Timer(50, e -> { if(isGlitching) repaint(); }).start();
        }
        @Override
        protected void paintComponent(Graphics g) {
            if (!isGlitching) { super.paintComponent(g); return; }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(getFont()); g2d.setColor(getForeground());
            String text = getText();
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            for (char c : text.toCharArray()) {
                int ox = (int) (Math.random() * 4) - 2;
                int oy = (int) (Math.random() * 4) - 2;
                g2d.drawString(String.valueOf(c), x + ox, y + oy);
                x += fm.charWidth(c);
            }
        }
    }
}