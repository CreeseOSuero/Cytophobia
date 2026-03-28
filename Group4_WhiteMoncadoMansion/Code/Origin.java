package Code;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//group members: Reyno & Rebuyon

public class Origin implements KeyListener{
    JFrame frame;

    ImageIcon background;
    static ImageIcon background2;
    ImageIcon door0;
    static ImageIcon monster0;
    ImageIcon cover;
    ImageIcon test;

    static ImageIcon pconup_0;
    static ImageIcon pconup_1;
    static ImageIcon pconup_2;
    static ImageIcon pcondown_0;
    static ImageIcon pcondown_1;
    static ImageIcon pcondown_2;
    static ImageIcon pconleft_0;
    static ImageIcon pconleft_1;
    static ImageIcon pconleft_2;
    static ImageIcon pconright_0;
    static ImageIcon pconright_1;
    static ImageIcon pconright_2;

    int fW = 1920;  int fH = 1080; //frame width and height in px
    static int mW = 16;  static int mH = 9; //number of grids along width and height

    static int totalGrids = mW * mH;


    int[] mapLayout = new int[totalGrids];
    static int[] interactionLayout = new int[totalGrids];
    static int[] collisionLayout = new int[totalGrids];

    static JLabel[] mapElements = new JLabel[totalGrids];
    static JLabel[] interactableElements = new JLabel[totalGrids];

    int[] passSequence = {0,2,1,2,3};
    int[] currentSequence = new int[passSequence.length];
    int currentSequenceIndex = 0;
    boolean unlocked = false;

    //Objects

    static Player P;

    //borrowed from tut:
    static JLabel alphaGrad;
    static JLabel dialogueBox;
    static JLabel dialogueName;
    static JLabel dialogueText;
    static JLabel blackScreen;

    static Crate crate1, crate2, crate3, crate4, crate5;
    static exitDoor eDoor;
    static roomDoor rDoor;
    static Monster Mon;
    static choiceDoor cDoorA, cDoorB, cDoorC, cDoorD;

    Origin(){
        frame = new JFrame();
        frame.setLayout(new GraphPaperLayout(new Dimension(mW,mH)));

        //from tut:

        alphaGrad = new JLabel(setupImage("/Assets/alpha_grad.png", mW, 5)) ;
        dialogueBox = new JLabel();
        dialogueBox.setBackground(Color.BLACK);
        //dialogueBox.setOpaque(true);

        blackScreen = new JLabel();
        dialogueName = new JLabel("name");
        dialogueText = new JLabel("text");
        dialogueName.setForeground(Color.WHITE);
        dialogueName.setFont(new Font("Courier New", Font.PLAIN, 40));
        dialogueText.setFont(new Font("Arial Unicode MS", Font.PLAIN, 25));
        dialogueText.setForeground(Color.WHITE);

        blackScreen.setBackground(Color.black);
        blackScreen.setOpaque(true);

        alphaGrad.setVisible(false);
        dialogueBox.setVisible(false);
        dialogueName.setVisible(false);
        dialogueText.setVisible(false);

        //----------------------------

        background = setupImage("/Assets/background.png", mW, mH);
        background2 = setupImage("/Assets/background2.png", mW, mH);
        door0 = setupImage("/Assets/door0.png", 1, 3);
        monster0 = setupImage("/Assets/monster.png", 1, 2);
        //cover = setupImage("/Images/cover.png", 4, 3);
        //test =setupImage("/Images/test.jpg");

        pconup_0 = setupImage("/Assets/pconup_0.PNG");
        pconup_1 = setupImage("/Assets/pconup_1.PNG");
        pconup_2 = setupImage("/Assets/pconup_2.PNG");
        pcondown_0 = setupImage("/Assets/pcondown_0.PNG");
        pcondown_1 = setupImage("/Assets/pcondown_1.PNG");
        pcondown_2 = setupImage("/Assets/pcondown_2.PNG");
        pconleft_0 = setupImage("/Assets/pconleft_0.PNG");
        pconleft_1 = setupImage("/Assets/pconleft_1.PNG");
        pconleft_2 = setupImage("/Assets/pconleft_2.PNG");
        pconright_0 = setupImage("/Assets/pconright_0.PNG");
        pconright_1 = setupImage("/Assets/pconright_1.PNG");
        pconright_2 = setupImage("/Assets/pconright_2.PNG");

        java.util.Arrays.fill(currentSequence, -1);

        //--------------------------------------------------------

        //map layout setup

            //layout array edit
        burnLayout(0, 0, 1, 1, mapLayout, 1);

            //assigning icons to layout codes
        assignIcon(mapLayout, mapElements, null, 0);
        assignIcon(mapLayout, mapElements, background, 1);

        
        //--------------------------------------------------------


            //layout array edit
        burnLayout(13, 6, 1, 1, interactionLayout, 6); // room door
        rDoor = new roomDoor(rectangleToIndex(13, 6));

        burnLayout(10, 2, 1, 1, interactionLayout, 7); // exit door
        eDoor = new exitDoor(rectangleToIndex(10, 2));
        burnLayout(13, 3, 1, 1, interactionLayout, 8); // monster trigger

        burnLayout(2, 6, 1, 2,interactionLayout, 1); //crate1
        crate1 = new Crate(rectangleToIndex(2, 6), true);
        burnLayout(3, 7, 1, 1,interactionLayout, 2); //crate2
        crate2 = new Crate(rectangleToIndex(3, 7), false);
        burnLayout(5, 6, 1, 2,interactionLayout, 3); //crate3
        crate3 = new Crate(rectangleToIndex(5, 6), false);
        burnLayout(6, 6, 1, 1,interactionLayout, 4); //crate4
        crate4 = new Crate(rectangleToIndex(6, 6), false);
        burnLayout(7, 3, 1, 1,interactionLayout, 5); //crate5
        crate5 = new Crate(rectangleToIndex(7, 3), false);
        Mon = new Monster(-1);
        cDoorA = new choiceDoor(-1, 'A');
        cDoorB = new choiceDoor(-1, 'B');
        cDoorC = new choiceDoor(-1, 'C');
        cDoorD = new choiceDoor(-1, 'D');

        burnLayout(13, 4, 1, 1, interactionLayout, 9); // room door (icon)
        burnLayout(11, 3, 1, 1, interactionLayout, 10); //monster (icon)
        //burnLayout(15, 1, 1, 1,interactionLayout, 8); //cover

            //assigning icons to layout codes
        assignIcon(interactionLayout, interactableElements, null, 0); //needed to declare blank JLabel instance
        assignIcon(interactionLayout, interactableElements, door0, 9);
        assignIcon(interactionLayout, interactableElements, null, 10);
        //assignIcon(interactionLayout, interactableElements, cover, 8);
              
        //--------------------------------------------------------

        //collision layout setup
        burnLayout(0, 0, 16, 3, collisionLayout, 1); //top border + wall
        burnLayout(0, 8, 16, 1, collisionLayout, 1); //bottom border
        burnLayout(0, 0, 1, 9, collisionLayout, 1); //left border
        burnLayout(15, 0, 1, 16, collisionLayout, 1); //right border
        burnLayout(8, 0, 1, 4, collisionLayout, 1); // room left wall
        burnLayout(8, 4, 5, 3, collisionLayout, 1); // room bottom wall
        burnLayout(14, 4, 1, 3, collisionLayout, 1); // room bottom right wall
        burnLayout(13, 4, 1, 3, collisionLayout, 1); // room door

        burnLayout(2, 6, 1, 2,collisionLayout, 1); //crate1
        burnLayout(3, 7, 1, 1,collisionLayout, 1); //crate2
        burnLayout(5, 6, 1, 2,collisionLayout, 1); //crate3
        burnLayout(6, 6, 1, 1,collisionLayout, 1); //crate4
        burnLayout(7, 3, 1, 1,collisionLayout, 1); //crate5
        //--------------------------------------------------------//

        //character layout setup

        P = new Player();

            //layout array edit
        burnLayout(P.startPositionX, P.startPositionY, 1, 1,P.getLayout(), 1);

            //assigning icons to layout codes
        assignIcon(P.getLayout(), P.getElements(), null, 0); //needed to declare blank JLabel instance
        assignIcon(P.getLayout(), P.getElements(), pconright_0, 1);

        //--------------------------------------------------------

        //add elements to frame, respecting z order

        // manual additions ~the lion doesn't bother with unnecessary nuances~

        frame.add(dialogueName, new Rectangle(1,5,4,1));
        frame.add(dialogueText, new Rectangle(1,6,14,2));
        frame.add(dialogueBox, new Rectangle(1,5,14,3));
        frame.add(blackScreen, new Rectangle(0,0,mW,mH));
        frame.add(alphaGrad, new Rectangle(0, 4, mW, 5));


        // method additions

        placeElements(P.getElements());
        placeElements(interactableElements);
        placeElements(mapElements);

        //--------------------------------------------------------


        frame.setVisible(true);
        frame.setSize(fW,fH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addKeyListener(this);
        mapElements[40].setOpaque(true);

        startScreen();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    boolean pressed = false;
    @Override //input -> check pending pos -> collision check + check interactions -> update character
    public void keyPressed(KeyEvent e) {
        P.Move(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        P.Pressed = false;
    }

    public ImageIcon setupImage(String dir, int gridSizeX, int gridSizeY){
        return new ImageIcon(
                new ImageIcon(getClass().getResource(dir)).getImage().getScaledInstance((fW/mW)*gridSizeX, (fH/mH)*gridSizeY, Image.SCALE_SMOOTH));
    }
    public ImageIcon setupImage(String dir){
        return new ImageIcon(
                new ImageIcon(getClass().getResource(dir)).getImage().getScaledInstance(fW/mW, fH/mH, Image.SCALE_SMOOTH));
    }

    //interaction variables

     //rand crate with key
    static boolean defeatedMonster = false;
    boolean doorUnlocked = false;
    static boolean introMonster = false;
    static boolean inQuiz = false;
    boolean doneQuiz = false;
    static int quizIndex = 0;

    static String[] quizQ = {
            "<html><b>Which organelle is known as the 'powerhouse' of the cell because it generates ATP?</b><br>A. Ribosome<br>B. Golgi Apparatus<br>C. Mitochondria<br>D. Nucleus</html>",
            "<html><b>In a DNA molecule, which nitrogenous base always pairs with Cytosine?</b><br>A. Adenine<br>B. Guanine<br>C. Thymine<br>D. Uracil</html>",
            "<html><b>What is the primary purpose of mitosis in multicellular organisms?</b><br>A. Production of gametes<br>B. Growth and tissue repair<br>C. Genetic diversity<br>D. Energy storage</html>",
            "<html><b>Which of the following refers to the genetic makeup or set of alleles of an organism?</b><br>A. Phenotype<br>B. Genotype<br>C. Prototype<br>D. Karyotype</html>",
            "<html><b>What process do plants use to convert light energy into chemical energy stored in glucose?</b><br>A. Fermentation<br>B. Transpiration<br>C. Photosynthesis<br>D. Respiration</html>",
            "<html><b>Which scientist is most famously associated with the theory of natural selection?</b><br>A. Gregor Mendel<br>B. Charles Darwin<br>C. Louis Pasteur<br>D. Robert Hooke</html>",
            "<html><b>Which system is responsible for transporting oxygen, nutrients, and waste throughout the body?</b><br>A. Circulatory<br>B. Respiratory<br>C. Digestive<br>D. Endocrine</html>",
            "<html><b>In an energy pyramid, which level contains the most available energy?</b><br>A. Primary Consumers<br>B. Secondary Consumers<br>C. Producers<br>D. Decomposers</html>",
            "<html><b>What is the main function of an enzyme in a biological reaction?</b><br>A. Increase activation energy<br>B. Act as a catalyst<br>C. Provide heat<br>D. Store DNA</html>",
            "<html><b>Which of the following is a characteristic maintained by all living things to stay stable?</b><br>A. Locomotion<br>B. Photosynthesis<br>C. Homeostasis<br>D. Nervous System</html>"
    };

    static char[] quizAns = {'C', 'B', 'B', 'B', 'C', 'B', 'A', 'C', 'B', 'C'};

    public static void shuffleQA(){
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < quizQ.length; i++) {
            indices.add(i);
        }

        Collections.shuffle(indices);

        String[] shuffledQ = new String[quizQ.length];
        char[] shuffledAns = new char[quizAns.length];

        for (int i = 0; i < indices.size(); i++) {
            int originalIndex = indices.get(i);
            shuffledQ[i] = quizQ[originalIndex];
            shuffledAns[i] = quizAns[originalIndex];
        }

        System.arraycopy(shuffledQ, 0, quizQ, 0, quizQ.length);
        System.arraycopy(shuffledAns, 0, quizAns, 0, quizAns.length);
    }

    public static void interactionEvent() {
        crate1.checkEvent(P);
        crate2.checkEvent(P);
        crate3.checkEvent(P);
        crate4.checkEvent(P);
        crate5.checkEvent(P);
        eDoor.checkEvent(P);
        rDoor.checkEvent(P);
        Mon.checkEvent(P);
        cDoorA.checkEvent(P);
        cDoorB.checkEvent(P);
        cDoorC.checkEvent(P);
        cDoorD.checkEvent(P);
    }

    public static void startQuiz() {
        shuffleQA();
        inQuiz = true;
        P.Restrict();
        quizIndex = 0;

        new Thread(() -> {
            try {
                tWriteDia("Tikbalang", "Let's start.", 800, 800);
                askNext();
            } catch (InterruptedException e) {}
        }).start();
    }

    public static void askNext() throws InterruptedException {
        if (quizIndex >= quizQ.length) {
            tWriteDia("Tikbalang", "Well done. A bit unexpected, but you have passed the trial.", 1500, 1500);

            tWriteDia("Tikbalang", "You don't belong here anymore. Goodbye.", 1500, 1500);

            blackScreen.setVisible(true);

            tWriteDia("", "<html>You suddenly feel light-headed and dizzy. Your consciousness starts to fade.</html>", 3000, 3000);

            tWriteDia("", "<html>Suddenly, as if time skipped an hour, you find yourself lying on the ground outside the place.</html>", 3000, 3000);

            tWriteDia("", "<html>Congratulations, unfortunate wanderer. You have survived the Moncado White Mansion.</html>", 3000, 3000);

            inQuiz = false;
            defeatedMonster = true;
            return;
        }
        tWriteDia("Tikbalang", quizQ[quizIndex], 3000, 800, true);
        P.Unrestrict();
    }

    public void startScreen(){
        P.Restrict();

        new Thread(() -> {
            try {
                tWait(3000);

                tWriteDia("", "<html>General Hilario Moncado constructed the dilapidated Moncado White Mansion in the 1930s, " +
                        "and it is haunted by the ghosts of Moncado's loyal supporters.</html>", 3000, 4000);

                tWriteDia("", "<html> Inside, the walls smell like flesh. " +
                        "An eerie atmosphere haunts the area.</html>", 3000, 4000);

                tWriteDia("", "<html>You stumble into the building's second floor... as the stairs collapse behind you, " +
                        "you must find a way to escape the room. </html>", 3000, 4000);

                tWait(2000);

                blackScreen.setVisible(false);


                tWriteDia("", "Hmm, boxes.", 1000, 2000);
                tWriteDia("", "Maybe I can find something useful in them. (Walk into obstacles to interact)", 2000, 2000);

                P.Unrestrict();

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        new Origin();
    }

    //borrowed from tut

    static void tWait(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    static void tWriteDia(String name, String text, int textDuration, int delayAfter) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            alphaGrad.setVisible(true);
            dialogueBox.setVisible(true);
            dialogueName.setText(name);
            dialogueText.setText("_");
            dialogueName.setVisible(true);
            dialogueText.setVisible(true);
            P.Restrict();
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
            P.Unrestrict();
        });
    }

    static void tWriteDia(String name, String text, int textDuration, int delayAfter, boolean disableAutoClear) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            alphaGrad.setVisible(true);
            dialogueBox.setVisible(true);
            dialogueName.setText(name);
            dialogueText.setText("_");
            dialogueName.setVisible(true);
            dialogueText.setVisible(true);
            P.Restrict();
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
    }

    //layout functions

    static int rectangleToIndex(int x, int y){
        return (x + (y * mW));
    }

    static void burnLayout(int inx, int iny, int wid, int hei, int[] layout, int val){
        for (int x = inx; x < inx + wid; x++) {
            for (int y = iny; y < iny + hei; y++) {
                int i = x + (y * mW);
                if (i<layout.length) {layout[i] = val;}
            }
        }
    }

    void assignIcon(int[] layout, JLabel[] labels, ImageIcon icon, int val){
        for(int i = 0; i < totalGrids; i++) {
            if(layout[i] == val) {
                labels[i] = new JLabel(icon);
            }
        }
    }

    void placeElements(JLabel[] labels){
        for(int i = 0; i < totalGrids; i++){
            int x = i % mW;
            int y = i / mW;
            if (labels[i] != null){
                if (labels[i].getIcon() != null){
                    frame.add(labels[i], new Rectangle(x, y, labels[i].getIcon().getIconWidth()/(fW/mW), labels[i].getIcon().getIconHeight()/(fH/mH)));

                } else {
                    frame.add(labels[i], new Rectangle(x, y, 1, 1));
                }
            }
        }
    }
}