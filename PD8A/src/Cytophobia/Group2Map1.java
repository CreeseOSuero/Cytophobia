package Cytophobia;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.geom.*; 
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
//groupmates: Tanya June Deocampo & Savine Gail Cansancio 
//gemini was used in making this :DDDD
class InvalidKeyException extends Exception {
    public InvalidKeyException(String message) {
        super(message);
    }
}

public class Group2Map1 implements KeyListener {
    boolean hasEnteredLibrary = false;
    boolean isDistressed = false;
    boolean a = false;
    
    int currentQuizIdx = 0;
    JPanel container;
    String[][] quizData = {
    {"How do enzymes speed up reactions?", "A) Heat", "B) Lower Activation Energy", "C) Add Reactants", "D) Change pH", "B"},
    {"What is the 'powerhouse'?", "A) Nucleus", "B) Ribosome", "C) Mitochondria", "D) Golgi", "C"},
    {"Difference in cells?", "A) Size", "B) Nucleus/Organelles", "C) DNA presence", "D) Bacteria only", "B"},
    {"Why are leaves green?", "A) Absorb all", "B) Iron", "C) Reflects Green", "D) Protection", "C"},
    {"Natural Selection is...", "A) Choosing traits", "B) Survival of fittest", "C) Physical strength", "D) One lifetime", "B"}
    };
    JPanel bookUI;
    JLabel quizQuestion;
    JButton[] choices;
    ShadowLayer shadow;
    JLabel scareOverlay;
    Timer typewriterTimer;
    boolean hasEnteredBedroom = false;
    boolean hasEnteredBathroom = false;
    int charIndex = 0;
    private boolean introPlayed = false;
    private boolean hintMoveShown = false;
    private boolean hintPickShown = false;
    private boolean hintKeyShown = false;
    private boolean hasTriggeredHallway = false;
    String fullMessage = "";
    JFrame f; 
    int mirrorCount = 0; 
    JLabel nameTag;
    JLabel dialogueBox; 
    JLabel dialogueText;
    boolean hasSeenTutorial = false;
    ImageIcon[] animd = new ImageIcon[3];
    ImageIcon[] animr = new ImageIcon[3];
    ImageIcon[] animl = new ImageIcon[3];
    ImageIcon[] animu = new ImageIcon[3];
    ImageIcon  w,w1,w2,w3,w4, t, D, d, T, B, b, idle, mirror,mirrornormal,lantern, key1,lore, key2, book, table, bookshelf;
    JLabel ts[], O[], CH[], b1, B1;
    boolean canMove = true; 
    Timer currentHideTimer;
    boolean hasKey1 = false; 
    boolean hasKey2 = false; 
    boolean hasKey3 = false;
    boolean doorOpen = false;
    boolean hintShown = false;
    int lastHintTile = -1;
    int lastDir=0;
    JPanel gamePanel;
    JScrollPane cameraScroll;
    int tileW, tileH;
    boolean hasFlashlight = false;
    double visionRadius = 1.1;
    int mL[], OP[], CP[];
    int mW = 17;
    int mH = 26;
    int fW = 1600;
    int fH = 900; 
    int cm = 0;     
    int cp = 0;  
    int step = 0;
    public Group2Map1() {
        f = new JFrame("PD Map");
        animd[2] = new ImageIcon("assets2/animd3.png");
        animd[1] = new ImageIcon("assets2/animd2.png");
        animd[0] = new ImageIcon("assets2/animd1.png");
        animr[0] = new ImageIcon("assets2/animr1.png");
        animr[1] = new ImageIcon("assets2/animr2.png");
        animr[2] = new ImageIcon("assets2/animr3.png");
        animl[0] = new ImageIcon("assets2/animl1.png"); 
        animl[1] = new ImageIcon("assets2/animl2.png"); 
        animl[2] = new ImageIcon("assets2/animl3.png"); 
        animu[0] = new ImageIcon("assets2/animu1.png");
        animu[1] = new ImageIcon("assets2/animu2.png"); 
        animu[2] = new ImageIcon("assets2/animu3.png");
        idle = new ImageIcon("assets2/idle.png");
        table = new ImageIcon("assets2/table.png");
        key1 = new ImageIcon("assets2/key1.png");
        key2 = new ImageIcon("assets2/bookshelf.png");
        book = new ImageIcon("assets2/book.png");
        lore = new ImageIcon("assets2/bookshelf.png");
        bookshelf = new ImageIcon("assets2/bookshelf.png");
        mirror = new ImageIcon("assets2/mirror.png");
        lantern = new ImageIcon("assets2/lantern.png");
        mirrornormal = new ImageIcon("assets2/mirrornormal.png");
        w = new ImageIcon("assets2/wall.png");
        w1 = new ImageIcon("assets2/wall1.png");
        w2 = new ImageIcon("assets2/wall2.jpg");
        w3 = new ImageIcon("assets2/wall3.png");
        w4 = new ImageIcon("assets2/wall4.png");
        t = new ImageIcon("assets2/floor.png");
        b = new ImageIcon("assets2/bed.png");
        B = new ImageIcon("assets2/bathtub.png");
        D = new ImageIcon("assets2/door.png");
        d = new ImageIcon("assets2/drawer.png");
        T = new ImageIcon("assets2/toilet.png");
        this.tileW = (fW / mW);
        this.tileH = (fH / mH);
        int bedW = (int)(tileW/1.15);
        int bigW = tileW; 
        int bigH = tileH;
        BufferedImage blackImg = new BufferedImage(tileW, tileH, BufferedImage.TYPE_INT_RGB);            
        Graphics g = blackImg.getGraphics();
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, tileW, tileH);
        g.dispose(); 
        w=new ImageIcon(w.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        table=new ImageIcon(table.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        lantern=new ImageIcon(lantern.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        key1=new ImageIcon(key1.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        key2=new ImageIcon(key2.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        book=new ImageIcon(book.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        bookshelf=new ImageIcon(bookshelf.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        mirror=new ImageIcon(mirror.getImage().getScaledInstance(tileW*2, tileH*2, Image.SCALE_DEFAULT));
        mirrornormal=new ImageIcon(mirrornormal.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        animl[0]=new ImageIcon(animl[0].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animl[1]=new ImageIcon(animl[1].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animl[2]=new ImageIcon(animl[2].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animr[1]=new ImageIcon(animr[1].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animr[2]=new ImageIcon(animr[2].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animr[0]=new ImageIcon(animr[0].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animd[0]=new ImageIcon(animd[0].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animd[1]=new ImageIcon(animd[1].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animd[2]=new ImageIcon(animd[2].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animu[1]=new ImageIcon(animu[1].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animu[0]=new ImageIcon(animu[0].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        animu[2]=new ImageIcon(animu[1].getImage().getScaledInstance(bigW, bigH, Image.SCALE_DEFAULT));
        w=new ImageIcon(w.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        lore=new ImageIcon(lore.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        w1=new ImageIcon(w1.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        w2=new ImageIcon(w2.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        w3=new ImageIcon(w3.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        w4=new ImageIcon(w4.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        t=new ImageIcon(t.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        T=new ImageIcon(T.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        d=new ImageIcon(d.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        D=new ImageIcon(D.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        b=new ImageIcon(b.getImage().getScaledInstance(bedW, tileH*2, Image.SCALE_DEFAULT));
        B=new ImageIcon(B.getImage().getScaledInstance(tileW, tileH, Image.SCALE_DEFAULT));
        O=new JLabel[mW*mH];
        ts=new JLabel[mH*mW];
        CH=new JLabel[mW*mH];
        B1 = new JLabel(B);
        b1 = new JLabel(b);
        CP=new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        };
        for(int i=0;i<CH.length;i++){
            if(CP[i]==1){
                CH[i]=new JLabel(animd[0]);
                cp=i; 
            }
            else if(CP[i]==2) {
                CH[i]=new JLabel(table);
            }
            else CH[i]=new JLabel();
        }
        OP=new int[]{
            0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,
            0,12,3,9,5,10,0,0,0,3,3,11,5,0,0,1,0,
            0,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,
            0,0,0,4,0,0,0,0,0,0,0,0,0,4,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,6,0,0,0,0,0,0,0,0,0,0,
            0,3,5,5,5,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        };
        for(int i=0; i<O.length; i++){
    if (OP[i] == 2) {
        O[i] = new JLabel(T);
    } else if (OP[i] == 4) {
        O[i] = new JLabel(D); 
    } 
    else if (OP[i] == 1) {
        O[i] = new JLabel(B); 
    }
    else if (OP[i]==3){
        O[i]=new JLabel(d);
    }
    else if(OP[i]==11){
        O[i]=new JLabel(book);
    }
    else if (OP[i]==7){
        O[i]=new JLabel(key1);
    }
    else if (OP[i]==8){
        O[i]=new JLabel(lantern);
    }
    else if (OP[i]==10){
        O[i]=new JLabel(key2);
    }
    else if (OP[i]==6){
        O[i]=new JLabel(lore);
    }
    else if (OP[i]==9){
        O[i]=new JLabel(mirrornormal);
    }
    else if (OP[i]==5){
        O[i]=new JLabel(bookshelf);
    }
    else {
        O[i] = new JLabel();  
    }
    O[i].setOpaque(false); 
}
        mL=new int[]{
    3,3,3,3,3,3,3,2,3,3,3,3,3,3,3,3,3,
    3,0,0,0,0,0,3,0,3,0,0,0,0,0,0,0,3,
    3,0,0,0,0,0,3,0,3,0,0,0,0,0,0,0,3,
    3,0,0,0,0,0,3,0,3,0,0,0,0,0,0,0,3,
    3,3,3,5,3,3,3,0,3,3,3,3,3,6,3,3,3,
    3,0,3,0,3,0,0,0,0,0,0,0,3,0,3,0,3,
    3,0,3,0,3,0,3,3,3,3,3,0,3,0,0,0,3,
    3,0,0,0,3,0,0,0,3,0,0,0,3,3,3,0,3,
    3,0,3,3,3,0,3,3,3,3,3,0,0,0,0,0,3,
    3,0,3,0,0,0,3,0,0,0,3,0,3,0,3,3,3,
    3,0,3,0,3,0,3,0,0,0,3,0,3,0,3,0,3,
    3,0,3,0,3,0,3,3,0,3,3,0,3,0,3,0,3,
    3,0,3,0,3,0,0,3,0,3,0,0,3,0,3,0,3,
    3,0,3,0,3,3,3,3,0,3,0,0,3,0,3,0,3,
    3,0,3,0,0,0,0,0,0,3,3,3,0,0,3,0,3,
    3,0,0,0,3,3,3,3,0,0,0,0,0,0,0,0,3,
    3,3,3,3,3,0,0,3,3,3,3,3,3,0,3,3,3,
    3,0,0,0,0,0,0,0,3,3,3,0,3,0,3,3,3,
    3,0,0,0,0,0,0,0,0,0,3,3,3,0,0,0,3,
    3,7,0,0,0,0,0,0,3,0,0,0,0,0,3,0,3,
    3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
    3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
    3,0,0,0,0,0,0,3,3,3,3,3,3,3,3,3,3,
    3,0,0,0,0,0,0,3,3,3,3,3,3,3,3,3,3,
    3,0,0,0,0,0,0,3,3,3,3,3,3,3,3,3,3,
    3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
        };
        for(int i=0;i<ts.length;i++){
            if(mL[i]==0) ts[i]= new JLabel(t);
            else if(mL[i]==1) ts[i]=new JLabel(w);
            else if(mL[i]==2) ts[i]=new JLabel(w1);
            else if(mL[i]==3) ts[i]=new JLabel(w2);
            else if(mL[i]==4) ts[i]=new JLabel(w3);
            else if(mL[i]==5) ts[i]=new JLabel(w4);
            else if(mL[i]==7) ts[i]=new JLabel(t);
            else ts[i]=new JLabel(w4);
        }
    }   
public void setFrame() {
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().setBackground(Color.BLACK);
    f.setLayout(new GridBagLayout()); 

    container = new JPanel(null); 
    container.setPreferredSize(new Dimension(960, 560));
    container.setBackground(Color.BLACK);

    gamePanel = new JPanel(new GraphPaperLayout(new Dimension(mW, mH)));
    gamePanel.setPreferredSize(new Dimension(fW, fH));
    gamePanel.setBackground(Color.BLACK);

    gamePanel.add(b1, new Rectangle(1,1, 1, 2));
    for (int i = 0; i < CH.length; i++) gamePanel.add(CH[i], new Rectangle(i % mW, i / mW, 1, 1));
    for (int i = 0; i < O.length; i++) gamePanel.add(O[i], new Rectangle(i % mW, i / mW, 1, 1));
    for (int i = 0; i < ts.length; i++) {
        ts[i].setVisible(true); 
        O[i].setVisible(true);
        gamePanel.add(ts[i], new Rectangle(i % mW, i / mW, 1, 1));
    }

    cameraScroll = new JScrollPane(gamePanel);
    cameraScroll.setBorder(null);
    cameraScroll.setBounds(0, 0, 960, 560);
    cameraScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    cameraScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    dialogueBox = new JLabel();
    dialogueBox.setBounds(180, 420, 600, 100);
    dialogueBox.setBackground(new Color(10, 10, 10, 230)); 
    dialogueBox.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(60, 0, 0), 3), 
        BorderFactory.createLineBorder(Color.BLACK, 1)          
    ));
    dialogueBox.setOpaque(true);
    dialogueBox.setVisible(false);

    nameTag = new JLabel(" Euno ", SwingConstants.CENTER);
    nameTag.setBounds(180, 395, 80, 25); 
    nameTag.setBackground(new Color(60, 0, 0)); 
    nameTag.setForeground(Color.WHITE);
    nameTag.setFont(new Font("Serif", Font.BOLD, 14));
    nameTag.setOpaque(true);
    nameTag.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    nameTag.setVisible(false);

    dialogueText = new GlitchLabel("", SwingConstants.CENTER);
    dialogueText.setBounds(190, 430, 580, 80);
    dialogueText.setForeground(Color.WHITE);
    dialogueText.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
    dialogueText.setVisible(false);

    bookUI = new JPanel(null);
    bookUI.setBounds(130, 50, 700, 500); 
    bookUI.setOpaque(false);
    bookUI.setVisible(false);

    quizQuestion = new JLabel("Question here?", SwingConstants.CENTER);
    quizQuestion.setForeground(new Color(40, 30, 30)); 
    quizQuestion.setFont(new Font("Serif", Font.BOLD, 18)); 
    quizQuestion.setBounds(65, 80, 260, 300); 
    bookUI.add(quizQuestion);

    choices = new JButton[4];
    for(int i=0; i<4; i++) {
        choices[i] = new JButton();
        choices[i].setBounds(375, 110 + (i * 65), 260, 45);
        choices[i].setContentAreaFilled(false); 
        choices[i].setBorderPainted(false);     
        choices[i].setFocusPainted(false);      
        choices[i].setForeground(new Color(60, 20, 20)); 
        choices[i].setFont(new Font("Serif", Font.BOLD, 17));
        choices[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        final int index = i;
        choices[i].addMouseListener(new MouseAdapter() {
           @Override public void mouseEntered(MouseEvent e) { choices[index].setForeground(Color.RED); }
           @Override public void mouseExited(MouseEvent e) { choices[index].setForeground(new Color(60, 20, 20)); }
        });
        bookUI.add(choices[i]);
    }

    JLabel bookBackground = new JLabel(new ImageIcon(new ImageIcon("assets2/quiz.png")
        .getImage().getScaledInstance(700, 500, Image.SCALE_SMOOTH)));
    bookBackground.setBounds(0, 0, 700, 500);
    bookUI.add(bookBackground);

    scareOverlay = new JLabel();
    scareOverlay.setBounds(0, 0, 960, 560);
    scareOverlay.setHorizontalAlignment(JLabel.CENTER);
    scareOverlay.setVisible(false);

    shadow = new ShadowLayer(); 
    shadow.setBounds(0, 0, 960, 560);

    JLayeredPane lp = new JLayeredPane();
    lp.setBounds(0, 0, 960, 560);
    lp.add(cameraScroll, JLayeredPane.DEFAULT_LAYER); 
    lp.add(shadow, Integer.valueOf(50));
    lp.add(scareOverlay, Integer.valueOf(60));
    lp.add(dialogueBox, Integer.valueOf(100));
    lp.add(nameTag, Integer.valueOf(101));     
    lp.add(dialogueText, Integer.valueOf(102)); 
    lp.add(bookUI, Integer.valueOf(150)); 
    
    container.add(lp);
    f.add(container);

    f.setSize(1000, 700);
    f.setResizable(false);
    f.setLocationRelativeTo(null); 
    f.addKeyListener(this); 
    f.setVisible(true);
    f.requestFocusInWindow();

    visionRadius = 0.9; 
    updateCamera();

    Timer startTimer = new Timer(100, e -> {
        updateLighting();
        gamePanel.revalidate();
        gamePanel.repaint();
        f.repaint();
    });
    startTimer.setRepeats(false);
    startTimer.start();

if (!introPlayed) {
    Timer startDialogue = new Timer(1500, e -> {
        typeText("Ugh... my head. How did I...? Where is this?");
        startDialogueTimer(5000);
        introPlayed = true; 
    });
    startDialogue.setRepeats(false);
    startDialogue.start();
    }
}



public void triggerDeathSequence() {
    canMove = false;
    bookUI.setVisible(false);
    scareOverlay.setIcon(new ImageIcon(new ImageIcon("assets2/scary_face.jpg")
        .getImage().getScaledInstance(960, 560, Image.SCALE_SMOOTH)));
    scareOverlay.setVisible(true);
    Timer deathTimer = new Timer(50, new ActionListener() {
        int elapsed = 0;
        public void actionPerformed(ActionEvent e) {
            int x = (int)(Math.random() * 20) - 10;
            int y = (int)(Math.random() * 20) - 10;
            container.setLocation(x, y);
            elapsed += 50;
            
            if (elapsed >= 3000) { 
                ((Timer)e.getSource()).stop();
                System.exit(0); 
            }
        }
    });
    deathTimer.start();
}

private void setupQuestion(JLabel qLab, JButton[] btns, JPanel panel) {
    if (currentQuizIdx >= quizData.length) {
        panel.setVisible(false);
        typeText("The book vanished.. and i seem to have a key");
        canMove = true; 
        f.requestFocusInWindow();
        hasKey3 = true;
        startDialogueTimer(6000);
        return;
    }

    qLab.setText("<html><center>" + quizData[currentQuizIdx][0] + "</center></html>");
    for (int i = 0; i < 4; i++) {
        btns[i].setText(quizData[currentQuizIdx][i+1]);
        for(ActionListener al : btns[i].getActionListeners()) btns[i].removeActionListener(al);
        
        final int choice = i;
        btns[i].addActionListener(e -> {
            char letter = (char)('A' + choice);
            if (String.valueOf(letter).equals(quizData[currentQuizIdx][5])) {
                currentQuizIdx++;
                setupQuestion(qLab, btns, panel);
            } else {
                panel.setVisible(false);
                triggerDeathSequence(); 
            }
        });
    }
}

public void showDistressedDialogue(String text, int speed) {
    if (typewriterTimer != null) typewriterTimer.stop();
    if (currentHideTimer != null) currentHideTimer.stop();

    ((GlitchLabel)dialogueText).isGlitching = true;
    dialogueText.setForeground(new Color(200, 0, 0)); 
    dialogueBox.setBackground(new Color(40, 0, 0, 250)); 
    
    dialogueText.setText("");
    canMove = false; 
    fullMessage = text;
    charIndex = 0;
    
    dialogueBox.setVisible(true);
    dialogueText.setVisible(true);
    nameTag.setVisible(true); 

    typewriterTimer = new Timer(speed, e -> {
        if (charIndex < fullMessage.length()) {
            dialogueText.setText(fullMessage.substring(0, charIndex + 1));
            int shakeX = (int)(Math.random() * 4) - 2;
            int shakeY = (int)(Math.random() * 4) - 2;
            dialogueBox.setLocation(180 + shakeX, 420 + shakeY);
            charIndex++;
        } else {
            typewriterTimer.stop();
        }
    });
    typewriterTimer.start();
}

public void showHorrorHint(String hint) {
    typeText(hint);
    nameTag.setVisible(false); 
    dialogueText.setForeground(Color.WHITE); 
    startDialogueTimer(3000);
}
public void updateCamera() {
        int px = (cp % mW) * tileW;
        int py = (cp / mW) * tileH;
        
        int viewX = px - (cameraScroll.getWidth() / 2) + (tileW / 2);
        int viewY = py - (cameraScroll.getHeight() / 2) + (tileH / 2);
        
        cameraScroll.getHorizontalScrollBar().setValue(viewX);
        cameraScroll.getVerticalScrollBar().setValue(viewY);
    }

public void updateLighting() {
    double flicker = (Math.random() * 0.05) - 0.025; 
    
    if (hasFlashlight) {
        visionRadius = 1.4 + flicker;
    } else {
        visionRadius = 1.1 + flicker;
    }

    if (shadow != null) {
        shadow.repaint();
    }
}
public void typeText(String text) {
    if (typewriterTimer != null) typewriterTimer.stop();
    if (currentHideTimer != null) currentHideTimer.stop();

    ((GlitchLabel)dialogueText).isGlitching = false;
    dialogueText.setForeground(Color.WHITE);
    dialogueBox.setBackground(new Color(10, 10, 10, 230));
    dialogueBox.setLocation(180, 420); 

    dialogueText.setText("");
    canMove = false; 
    fullMessage = text;
    charIndex = 0;

    dialogueBox.setVisible(true);
    dialogueText.setVisible(true);
    nameTag.setVisible(true); 

    typewriterTimer = new Timer(40, e -> {
        if (charIndex < fullMessage.length()) {
            dialogueText.setText(fullMessage.substring(0, charIndex + 1));
            charIndex++;
        } else {
            typewriterTimer.stop();
        }
    });
    typewriterTimer.start();
}
    private boolean handleInteraction(int tileInFront) {
    boolean interacted = false;

    if (OP[tileInFront] == 7) { 
        O[tileInFront].setIcon(null);
        OP[tileInFront] = 0;
        hasKey1 = true;
        typeText("Huh. I wonder what this key is for");
        startDialogueTimer(5000);
        interacted = true;
    } 
    if (OP[tileInFront] == 3) { 
        showHorrorHint("Nothing... just full of dust..");
        startDialogueTimer(5000);
        interacted = true;
    } 
    if (OP[tileInFront] == 5) { 
        showHorrorHint("Nothing... just old books..");
        startDialogueTimer(5000);
        interacted = true;
    } 
    if (OP[tileInFront] == 12) { 
        showHorrorHint("Bed is cold and just.. stinks.. yuck.");
        startDialogueTimer(5000);
        interacted = true;
    } 
    if (OP[tileInFront] == 1) { 
        showHorrorHint("The bathtub is damp...");
        startDialogueTimer(5000);
        interacted = true;
    } 
    if (OP[tileInFront] == 2) { 
        showHorrorHint("Smells like.. nevermind.");
        startDialogueTimer(5000);
        interacted = true;
    } 
    else if (OP[tileInFront] == 10) { 
        hasKey2 = true;
        showHorrorHint("...That opened something....");
        startDialogueTimer(5000);
        interacted = true;
    }
    else if (OP[tileInFront] == 6) { 
    interacted = true;
    canMove = false;
    showHorrorHint("[You find a book wedged among the brittle remains of other books.]");   
    Timer lorePart2 = new Timer(5000, e -> {
        typeText("\"She walks where paths are lost. White among the turns.\"");
        Timer lorePart3 = new Timer(5000, ea -> {
        showHorrorHint("[Footsteps echo. Soft. Bare.]");  
            Timer lorePart4 = new Timer(5000, ut -> {
        typeText("\"Do not run. She finds those who forget the way.\"");  
        Timer lorePart5 = new Timer(5000, q -> {
        typeText("...I'm not alone.");
        startDialogueTimer(3000);
    });
        lorePart5.setRepeats(false);
        lorePart5.start();
    });
    lorePart4.setRepeats(false);
    lorePart4.start();
    });
    lorePart3.setRepeats(false);
    lorePart3.start();
    });
    lorePart2.setRepeats(false);
    lorePart2.start();
    
}
    else if (OP[tileInFront] == 11) { 
    O[tileInFront].setIcon(null);
    OP[tileInFront] = 0;
    interacted = true;
    canMove = false; 
    currentQuizIdx = 0; 

    bookUI.setVisible(true);
    setupQuestion(quizQuestion, choices, bookUI); 
}
    else if (OP[tileInFront] == 8) { 
        O[tileInFront].setIcon(null);
        OP[tileInFront] = 0;
        hasFlashlight = true;
        visionRadius = 1.6;
        updateLighting();
        showHorrorHint("...Lantern obtained...");
        startDialogueTimer(3500);
        interacted = true;
    }
    else if (OP[tileInFront] == 9) { 
    mirrorCount++;
    interacted = true;
    if (mirrorCount == 1) {
        typeText("This mirror looks... off...");
        startDialogueTimer(2500);
    } 
    else if (mirrorCount == 2) {
    scareOverlay.setIcon(new ImageIcon(new ImageIcon("assets2/mirror.png").getImage()
              .getScaledInstance(960, 560, Image.SCALE_SMOOTH)));
    
    scareOverlay.setBounds(cameraScroll.getX(), cameraScroll.getY(), 960, 560);
    
    scareOverlay.setBackground(new Color(150, 0, 0, 150));
    scareOverlay.setOpaque(true);
    scareOverlay.setVisible(true);
    
    showDistressedDialogue("....WH-... WHAT.. IS WRONG WITH THIS MIRROR...?!", 67);
    
    Timer flashOff = new Timer(1000, e -> {
        scareOverlay.setVisible(false);
        scareOverlay.setIcon(null);
    });
    flashOff.setRepeats(false);
    flashOff.start();
    
    startDialogueTimer(6700);
}
    else if (mirrorCount == 3) {
        showDistressedDialogue("Maybe it was just my imagination...", 150);
        startDialogueTimer(6700);
    } 
    else {
        typeText("I already saw this mirror.");
        startDialogueTimer(2500);
    }
}
    else if (OP[tileInFront] == 4) {
        int wallType = mL[tileInFront];
        boolean canOpen = false;

        if (wallType == 5 && hasKey1) {
            typeText("..The key is a perfect fit....");
            startDialogueTimer(4000);
            canOpen = true;
        } 
        else if (wallType == 6 && hasKey2) {
            typeText("Huh the door is open now..");
            startDialogueTimer(4000);
            canOpen = true;
        } 
        else if (wallType == 2 && hasKey3) {
            typeText("Is this the end?..");
            Timer winTimer = new Timer(5000, e -> {
            f.dispose();
            Group2Map2 g = new Group2Map2();
                g.setFrame();
            });
            winTimer.setRepeats(false);
            winTimer.start();
            canMove=false;
        } 
        else {
            if (wallType == 5) {typeText("Hmm.. locked. I guess I need to find a key.");startDialogueTimer(4000);}
            else if (wallType == 2){ typeText("Door isnt budging..");startDialogueTimer(4000);}
            else if (wallType == 6) {typeText("This door doesn't have a keyhole... maybe a lever?");
            startDialogueTimer(3000);}
        }

        if (canOpen) {
            mL[tileInFront] = 0; 
            OP[tileInFront] = 0;
            O[tileInFront].setIcon(null);
            ts[tileInFront].setIcon(t);
            interacted = true;
        }
    }    
    return interacted;
}
@Override
public void keyPressed(KeyEvent e) {
    if (!canMove) return; 
    int keyCode = e.getKeyCode();
    
    try {
        boolean isActionKey = false;
        boolean isMoveKey = false;
        int newCp = cp;
        ImageIcon[] currentAnim = animd;

        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) { 
            newCp = cp + 1; currentAnim = animr; lastDir = 1; isMoveKey = true; 
        }
        else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) { 
            newCp = cp - 1; currentAnim = animl; lastDir = -1; isMoveKey = true; 
        }
        else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) { 
            newCp = cp + mW; currentAnim = animd; lastDir = mW; isMoveKey = true; 
        }
        else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) { 
            newCp = cp - mW; currentAnim = animu; lastDir = -mW; isMoveKey = true; 
        }

        int tileInFront = cp + lastDir;

        if (keyCode == KeyEvent.VK_E) {
            isActionKey = true;
            if (tileInFront >= 0 && tileInFront < OP.length) {
                handleInteraction(tileInFront);
            }
        }

        if (!isMoveKey && !isActionKey) {
            throw new InvalidKeyException("Invalid Input");
        }

        if (isMoveKey) {
            if (tileInFront >= 0 && tileInFront < OP.length && OP[tileInFront] != 0 && !hasSeenTutorial) {
                hasSeenTutorial = true; 
                showHorrorHint("... Press 'E' to interact with doors and objects...");
            }
            
            if (cp == 314 && !hasEnteredLibrary) {
        hasEnteredLibrary = true; // Set this IMMEDIATELY to prevent re-entry
        canMove = false; // Lock movement for the entire sequence
    
        typeText("This place must be a library..");
    
        // Start a timer for the second half of the thought
        Timer libraryPart = new Timer(4000, dwadaw -> {
        typeText("Maybe I'll find something about this place..?");
        
        // Only re-enable movement after the SECOND message is done
        startDialogueTimer(4000); // This sets canMove = true after 4 seconds
        });
        libraryPart.setRepeats(false);
        libraryPart.start();
    }

            if (cp == 71 && !hasEnteredBedroom) {
                hasEnteredBedroom = true;
                typeText("A bedroom?.. who even lives here..");
                startDialogueTimer(4000);
            }

            if (cp == 81 && !hasEnteredBathroom) {
                hasEnteredBathroom = true;
                typeText("A bathroom?... creepy");
                startDialogueTimer(4000);
            }

            if (cp == 229 && !hasTriggeredHallway) {
                hasTriggeredHallway = true; 
                canMove = false; 
                typeText("Hello? Is anyone here?!");   
                
                Timer scarePart1 = new Timer(5000, dih -> {
                    showHorrorHint("[Nobody responds...]");
                    Timer scarePart2 = new Timer(2000, ea -> {
                        showHorrorHint("[The sound of dripping water from the ceiling rings in the air.]");  
                        Timer scarePart3 = new Timer(5000, ut -> {
                            typeText("I need to get out of here..");  
                            startDialogueTimer(3000); 
                        });
                        scarePart3.setRepeats(false);
                        scarePart3.start();
                    });
                    scarePart2.setRepeats(false);
                    scarePart2.start();
                });
                scarePart1.setRepeats(false);
                scarePart1.start();
            }

            if (newCp >= 0 && newCp < mL.length) {
                if (mL[newCp] == 0 && OP[newCp] == 0) {
                    CH[cp].setIcon(null); 
                    cp = newCp;
                    step = (step == 0) ? 1 : 0;
                    CH[cp].setIcon(currentAnim[step]);
                    updateLighting();
                    updateCamera();
                } else {
                    CH[cp].setIcon(currentAnim[0]);
                }
            }
        }

    } catch (InvalidKeyException ex) {
        int tileInFront = cp + lastDir;
        if (tileInFront >= 0 && tileInFront < OP.length && OP[tileInFront] != 0 && !hintPickShown) {
            typeText("Press E to interact with items");
            startDialogueTimer(3000);
            hintPickShown = true;
        } 
        else if ((hasKey1 || hasKey2 || hasKey3) && !hintKeyShown) {
            typeText("I should press E to use my keys on doors...");
            startDialogueTimer(3000);
            hintKeyShown = true;
        }
        else if (!hintMoveShown) {
            typeText("Press W to go up, S to go down, A to go left, and D to go right");
            startDialogueTimer(3000);
            hintMoveShown = true;
        }
    }
}

    public void keyReleased(KeyEvent e) { 
    if(lastDir==1){
        CH[cp].setIcon(animr[0]);
    }
    else if(lastDir==-1){
        CH[cp].setIcon(animl[0]);
    }
    else if(lastDir==mW){
        CH[cp].setIcon(animd[0]);
    }
    else if(lastDir==-mW){
        CH[cp].setIcon(animu[0]);
    }
    }
    public void keyTyped(KeyEvent e) {}
    
    private void startDialogueTimer(int duration) {
    if (currentHideTimer != null) currentHideTimer.stop(); 
    currentHideTimer = new Timer(duration, e1 -> {
        dialogueBox.setVisible(false);
        dialogueText.setVisible(false);
        nameTag.setVisible(false); 
        canMove = true; 
    });
    currentHideTimer.setRepeats(false);
    currentHideTimer.start();
}
     
   public void teleportPlayer(int newPos) {
    CH[cp].setIcon(null);
    cp = newPos; 
    CH[cp].setIcon(animu[0]); 
    
    updateCamera();
    cameraScroll.revalidate();

    Timer fixTimer = new Timer(50, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i = 0; i < 3; i++) {
                updateLighting(); 
            }
            gamePanel.revalidate();
            gamePanel.repaint();
            f.repaint();
        }
    });
    fixTimer.setRepeats(false);
    fixTimer.start();
}
    public void updateMapAfterKey() {
    for (int i = 0; i < mL.length; i++) {
        if (mL[i] == 5) {
            mL[i] = 0;    
        }
    }
    f.repaint(); 
}
    class ShadowLayer extends JPanel {
    public ShadowLayer() {
        setOpaque(false);
        setBounds(0, 0, 960, 560); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Area darkness = new Area(new Rectangle(0, 0, 960, 560));

        int offsetX = cameraScroll.getHorizontalScrollBar().getValue();
        int offsetY = cameraScroll.getVerticalScrollBar().getValue();
        
        int pX = (cp % mW) * tileW + (tileW / 2) - offsetX;
        int pY = (cp / mW) * tileH + (tileH / 2) - offsetY;
        int r = (int)(visionRadius * tileW);

        Polygon lightShape = new Polygon();
        for (double i = 0; i <= 360; i += 0.5) {
            double rad = Math.toRadians(i);
            double dist = getDistToWall(rad);
            int lx = (int) (pX + Math.cos(rad) * (dist * tileW));
            int ly = (int) (pY + Math.sin(rad) * (dist * tileW));
            lightShape.addPoint(lx, ly);
        }

        Area lightArea = new Area(lightShape);
        darkness.subtract(lightArea);

        g2d.setColor(Color.BLACK);
        g2d.fill(darkness);

        if (r > 0) {
            RadialGradientPaint glow = new RadialGradientPaint(
                new Point2D.Float(pX, pY), r, 
                new float[]{0.0f, 0.5f, 1.0f}, 
                new Color[]{
                    new Color(255, 220, 150, 40),
                    new Color(100, 50, 0, 100),
                    new Color(0, 0, 0, 255)
                }
            );
            g2d.setPaint(glow);
            g2d.fill(lightArea);
        }
    }

    private double getDistToWall(double rad) {
        double startX = (cp % mW) + 0.5;
        double startY = (cp / mW) + 0.5;
        
        for (double d = 0; d < visionRadius; d += 0.01) {
            int cx = (int)(startX + Math.cos(rad) * d);
            int cy = (int)(startY + Math.sin(rad) * d);
            
            if (cx >= 0 && cx < mW && cy >= 0 && cy < mH) {
                int idx = cy * mW + cx;
                if (mL[idx] != 0 || OP[idx] == 4) {
                    return d + 0.4; 
                }
            }
        }
        return visionRadius;
    }
}
    class GlitchLabel extends JLabel {
    public boolean isGlitching = false;
    private Timer glitchTimer;

    public GlitchLabel(String text, int align) {
        super(text, align);
        glitchTimer = new Timer(50, e -> { if(isGlitching) repaint(); });
        glitchTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isGlitching) {
            super.paintComponent(g);
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(getFont());
        g2d.setColor(getForeground());

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
    public static void main(String[] args) {
        Group2Map1 m = new Group2Map1();
        m.setFrame();
    }
}



