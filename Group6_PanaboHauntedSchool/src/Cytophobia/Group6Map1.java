package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class Group6Map1 implements KeyListener {

    JFrame frame;
    ImageIcon i1,i2,i3,i4,i5,i6,i7,i8,i9,i10;
    ImageIcon playerup,playerdown,playerleft,playerright;
    ImageIcon monsterstand, monsterup, monsterdown, monsterleft, monsterright;

    JLabel monster;
    int monsterPosition;
    
    JLabel tiles[];
    int mapLayout[];
    int mapWidth=16;
    int mapHeight=9;
    int frameWidth=1600;
    int frameHeight=900;
    
    JLayeredPane layeredPane;

    JLabel character[];
    int characterLayout[];
    int characterPosition;
    int questionsAnswered = 0; 
    
    public static volatile long startTime = System.currentTimeMillis();
    public static volatile int answered = 0;
    public static volatile int wrongAnswers = 0;
    
    int questionLocation;

    class Question {
        String question; 
        String[] options;
        int correctIndex;

        Question(String q, String[] o, int c) {
            question = q;
            options = o; 
            correctIndex = c;
        }
    }

    Question[] questions = {
        new Question("What is the chemical symbol for water?", new String[]{"H2O", "O2", "CO2", "HO"}, 0),
        new Question("What is the closest planet to the Sun?", new String[]{"Venus", "Earth", "Mercury", "Mars"}, 2),
        new Question("How many factors does a prime number have?", new String[]{"1", "2", "3", "Infinite"}, 1),
        new Question("What is the powerhouse of the cell?", new String[]{"Nucleus", "Ribosome", "Mitochondria", "Cytoplasm"}, 2),
        new Question("What force pulls objects toward Earth?", new String[]{"Magnetism", "Friction", "Gravity", "Inertia"}, 2),
        new Question("What particle has no charge?", new String[]{"Proton", "Electron", "Neutron"}, 2)
    };

    boolean[] usedQuestions = new boolean[questions.length];
    
    int[] dirX = {-1, 0, 1, 0};
    int[] dirY = {0, -1, 0, 1};
     
    public int getNextMPos() {
        int[] path = new int[mapWidth*mapHeight];
        int mX = monsterPosition % mapWidth, mY = monsterPosition / mapWidth;
        int pX = characterPosition % mapWidth, pY = characterPosition / mapWidth;
        Queue f = new LinkedList<>();
        List<Integer> visited = new ArrayList<>();
        f.add(monsterPosition);
        s: while(!f.isEmpty()) {
            int cpos = (int)f.poll();
            int cX = cpos % mapWidth;
            int cY = cpos / mapWidth;
            if(cX == pX && cY == pY) break;
            for(int i = 0; i < 4; ++i) {
                if((cX + dirX[i]) >= 0 && (cX + dirX[i]) < mapWidth && (cY + dirY[i]) >= 0 && (cY + dirY[i]) < mapHeight) {
                    if(cX+dirX[i] == mX && cY+dirY[i] == mY) continue;
                    if(mapLayout[cX+dirX[i]+mapWidth*(cY+dirY[i])] != 3 && mapLayout[cX+dirX[i]+mapWidth*(cY+dirY[i])] != 9) continue;
                    if(!visited.contains(cX + dirX[i] + (cY + dirY[i])*mapWidth)) {
                        f.add((cX + dirX[i]) + mapWidth * (cY + dirY[i]));
                        visited.add(cX + dirX[i] + (cY + dirY[i])*mapWidth);
                        path[cX + dirX[i] + (cY + dirY[i])*mapWidth] = cX + mapWidth*cY;
                        if(cX + dirX[i] == pX && cY + dirY[i] == pY) break s;
                    }
                }
            }
        }
        int cpos = characterPosition;
        int prevpos = -1;
        while(cpos != monsterPosition) {
            prevpos = cpos;
            cpos = path[cpos];
        }
        return prevpos;
    }
    
    public void setMonsterPos(int x, int y) {
        monster.setBounds(x*frameWidth/mapWidth, y*frameHeight/mapHeight, frameWidth/mapWidth, frameHeight/mapHeight);
        monsterPosition = x + y * mapWidth;
        layeredPane.revalidate();
        layeredPane.repaint();
    }
    
    public void moveMonsterToPlayer() {
        int npos = getNextMPos();
        if(npos-monsterPosition == mapWidth) monster.setIcon(monsterdown);
        else if(npos-monsterPosition == -mapWidth) monster.setIcon(monsterup);
        else if(npos-monsterPosition == 1) monster.setIcon(monsterright);
        else if(npos-monsterPosition == -1) monster.setIcon(monsterleft);
        setMonsterPos(npos % mapWidth, npos / mapWidth);
        
    }
    
    public void spawnMonster() {
        monster.setVisible(true);
    }
    
    public void setupMonster() {
        monster = new JLabel(monsterstand);
        monster.setVisible(false);
        setMonsterPos(13, 2);
        layeredPane.add(monster, 2);
    }
    
    public Group6Map1() {
        startTime = System.currentTimeMillis();
        answered = 0;
        wrongAnswers = 0;
        JOptionPane.showMessageDialog(null,
                "Every paper holds a question. Answer all the questions correctly and unlock the door!");

        frame = new JFrame();

        characterPosition=-1;

        monsterstand = new ImageIcon((new ImageIcon(Menu.getRes("/assets6/Images3/monsterstand.PNG"))).getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        monsterleft = new ImageIcon((new ImageIcon(Menu.getRes("/assets6/Images3/monsterleft.PNG"))).getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        monsterup = new ImageIcon((new ImageIcon(Menu.getRes("/assets6/Images3/monsterup.PNG"))).getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        monsterright = new ImageIcon((new ImageIcon(Menu.getRes("/assets6/Images3/monsterright.PNG"))).getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        monsterdown = new ImageIcon((new ImageIcon(Menu.getRes("/assets6/Images3/monsterdown.PNG"))).getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i1=new ImageIcon(Menu.getRes("/assets6/Images3/1.png"));
        i2=new ImageIcon(Menu.getRes("/assets6/Images3/2.png"));
        i3=new ImageIcon(Menu.getRes("/assets6/Images3/3.png"));
        i4=new ImageIcon(Menu.getRes("/assets6/Images3/4.png"));
        i5=new ImageIcon(Menu.getRes("/assets6/Images3/5.png"));
        i6=new ImageIcon(Menu.getRes("/assets6/Images3/6.png"));
        i7=new ImageIcon(Menu.getRes("/assets6/Images3/7.png"));
        i8=new ImageIcon(Menu.getRes("/assets6/Images3/8.png"));
        i9=new ImageIcon(Menu.getRes("/assets6/Images3/9.png"));
        i10=new ImageIcon(Menu.getRes("/assets6/Images3/10.png"));

        playerup = new ImageIcon(Menu.getRes("/assets6/Images3/up.png"));
        playerdown = new ImageIcon(Menu.getRes("/assets6/Images3/down.png"));
        playerleft = new ImageIcon(Menu.getRes("/assets6/Images3/left.png"));
        playerright = new ImageIcon(Menu.getRes("/assets6/Images3/right.png"));

        i1=new ImageIcon(i1.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i2=new ImageIcon(i2.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i3=new ImageIcon(i3.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i4=new ImageIcon(i4.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i5=new ImageIcon(i5.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i6=new ImageIcon(i6.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i7=new ImageIcon(i7.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i8=new ImageIcon(i8.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i9=new ImageIcon(i9.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        i10=new ImageIcon(i10.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));

        playerup = new ImageIcon(playerup.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        playerdown = new ImageIcon(playerdown.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        playerleft = new ImageIcon(playerleft.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));
        playerright = new ImageIcon(playerright.getImage().getScaledInstance(frameWidth/mapWidth, frameHeight/mapHeight, Image.SCALE_DEFAULT));

        characterLayout = new int[]{
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,
            1,2,3,3,3,3,7,7,7,3,6,3,4,3,5,1,
            1,2,9,4,3,3,7,7,7,3,3,3,3,3,2,1,
            1,5,3,3,9,3,4,3,6,3,9,3,3,6,5,1,
            1,2,3,4,3,3,6,3,3,3,3,4,10,8,2,1,
            1,5,3,3,6,3,3,9,3,6,3,9,3,8,2,1,
            1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
        };

        mapLayout = new int[]{
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,
            1,2,3,3,3,3,7,7,7,3,6,3,4,3,5,1,
            1,2,9,4,3,3,7,7,7,3,3,3,3,3,2,1,
            1,5,3,3,9,3,4,3,6,3,9,3,3,6,5,1,
            1,2,3,4,3,3,6,3,3,3,3,4,3,8,2,1,
            1,5,3,3,6,3,3,9,3,6,3,9,3,8,2,1,
            1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
        };

        tiles = new JLabel[mapWidth*mapHeight];
        character = new JLabel[mapWidth*mapHeight];

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(frameWidth, frameHeight));

        for(int i=0;i<tiles.length;i++){
            if(mapLayout[i]==1) tiles[i]=new JLabel(i1);
            else if(mapLayout[i]==2) tiles[i]=new JLabel(i2);
            else if(mapLayout[i]==3) tiles[i]=new JLabel(i3);
            else if(mapLayout[i]==4) tiles[i]=new JLabel(i4);
            else if(mapLayout[i]==5) tiles[i]=new JLabel(i5);
            else if(mapLayout[i]==6) tiles[i]=new JLabel(i6);
            else if(mapLayout[i]==7) tiles[i]=new JLabel(i7);
            else if(mapLayout[i]==8) tiles[i]=new JLabel(i8);
            else if(mapLayout[i]==9) {tiles[i]=new JLabel(i9); questionLocation=i;}

            tiles[i].setBounds((i % mapWidth)*(frameWidth/mapWidth),
                               (i / mapWidth)*(frameHeight/mapHeight),
                               frameWidth/mapWidth,
                               frameHeight/mapHeight);
            layeredPane.add(tiles[i], Integer.valueOf(0));

            if(characterLayout[i]==10){
                character[i]=new JLabel(playerdown);
                characterPosition=i;
            } else {
                character[i]=new JLabel();
            }

            character[i].setBounds((i % mapWidth)*(frameWidth/mapWidth),
                                   (i / mapWidth)*(frameHeight/mapHeight),
                                   frameWidth/mapWidth,
                                   frameHeight/mapHeight);
            layeredPane.add(character[i], Integer.valueOf(1));
        }
        
        setupMonster();

        frame.add(layeredPane);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setSize(frameWidth,frameHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.requestFocus();
    }
    
    int c = 0;
    public void keyPressed(KeyEvent e) {
        int next = characterPosition;
        ImageIcon currentSprite = playerdown;

        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            next += 1;
            currentSprite = playerright;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            next -= 1;
            currentSprite = playerleft;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            next += mapWidth;
            currentSprite = playerdown;
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP) {
            next -= mapWidth;
            currentSprite = playerup;
        }
        else return;

        if(next < 0 || next >= mapWidth * mapHeight) return;
        if(mapLayout[next] == 2 || mapLayout[next] == 4 || mapLayout[next] == 5 || mapLayout[next] == 6 || mapLayout[next] == 7) return;

        character[characterPosition].setIcon(null);
        character[next].setIcon(currentSprite);
        characterPosition = next;

        if(mapLayout[characterPosition] == 9) {
            int randomIndex;
            do {
                randomIndex = (int)(Math.random() * questions.length);
            } while (usedQuestions[randomIndex]); 

            Question q = questions[randomIndex];

            int choice = JOptionPane.showOptionDialog(
                frame,
                q.question,
                "Question Time!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                q.options,
                q.options[0]
            );
            answered++;
            if(choice == q.correctIndex) {
                JOptionPane.showMessageDialog(frame, "Correct! 🎉");
                mapLayout[characterPosition] = 3;
                tiles[characterPosition].setIcon(i3);
                questionsAnswered++;
                usedQuestions[randomIndex] = true;
            } else if(choice != -1) {
                JOptionPane.showMessageDialog(frame,"Wrong!");
                if(++wrongAnswers >= 3) {
                    spawnMonster();
                }
            }
        }

        if (mapLayout[characterPosition] == 8) {
            if (questionsAnswered >= 5) {
                JOptionPane.showMessageDialog(frame, "Level Complete! Lets move to the next room!");
                frame.dispose();
                Group6Map2 nextLevel = new Group6Map2();
                nextLevel.setFrame();
            } else {
                JOptionPane.showMessageDialog(frame,
                    "There are still questions left! (" + questionsAnswered + "/5)"
                );
            }
        }
        if((mapLayout[characterPosition] == 3 || mapLayout[characterPosition] == 9) && monster.isVisible() && ++c >= 2) {
            c = 0;
            moveMonsterToPlayer();
            if(monsterPosition == characterPosition) {
                JOptionPane.showMessageDialog(frame, "You failed. Re-attempt.");
                frame.dispose();
                new Group6Map1();
            }
        }
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

    public static void main(String[] args){
        System.setProperty("sun.java2d.uiScale", "1.0");
        new Group6Map1();
    }
}



//libres,medina,nogara <3
//debugged using chatgpt