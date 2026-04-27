package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class Group6Map2 implements KeyListener {

    JFrame frame;

    ImageIcon i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12, i13, i14, i15, i16;
    ImageIcon playerup,playerdown,playerleft,playerright;
   

    JLabel tiles[];
    JLabel character[];

    int mapLayout[];
    int characterLayout[];
    int characterPosition;

    int mapWidth = 16;
    int mapHeight = 9;
    int frameWidth = 1600;
    int frameHeight = 900;

    boolean hasKey = false;
    boolean quizCompleted = false;

    int keyTrashIndex;

    List<Question> questions;

    int[] originalMapLayout;
    JButton resetButton;

    public Group6Map2() {

        JOptionPane.showMessageDialog(null,
                "Push the desk to find the key in one of the trash cans, open the locker thats not quite like the rest. Answer at least 3 questions correctly and make your escape!"
                        + "\n*Helpful Tip: Press the reset button if your desk gets stuck!*");

        frame = new JFrame();

        i1 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/1.png")));
        i2 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/2.png")));
        i3 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/3.png")));
        i4 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/8.png")));
        i5 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/18.png")));
        i6 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/23.png")));
        i7 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/20.png")));
        i8 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/21.png")));
        i9 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/14.png")));
        i10 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/24.png")));
        i11 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/5.png")));
        i12 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/4.png")));
        i13 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/11.png")));
        i14 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/12.png")));
        i15 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/15.png")));
        i16 = scale(new ImageIcon(Menu.getRes("/assets6/Images4/25.png")));

        playerup = scale(new ImageIcon(Menu.getRes("/assets6/Images4/playerup.png")));
        playerdown = scale(new ImageIcon(Menu.getRes("/assets6/Images4/playerdown.png")));
        playerleft = scale(new ImageIcon(Menu.getRes("/assets6/Images4/playerleft.png")));
        playerright = scale(new ImageIcon(Menu.getRes("/assets6/Images4/playerright.png")));

        tiles = new JLabel[mapWidth * mapHeight];
        character = new JLabel[mapWidth * mapHeight];

        characterLayout = new int[]{
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
                3,3,18,18,18,3,5,5,5,3,18,24,18,3,3,3,
                6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,
                6,2,2,2,2,19,2,2,2,2,2,2,2,2,2,6,
                6,2,2,19,2,2,2,2,2,2,2,19,2,2,2,3,
                3,2,9,2,2,19,2,2,2,19,2,2,2,2,2,3,
                3,4,2,2,2,2,2,2,2,2,2,2,2,2,2,6,
                8,2,18,18,18,3,5,5,5,3,18,18,18,18,3,3,
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
        };

        mapLayout = new int[]{
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
                3,3,6,18,5,3,18,5,24,6,18,6,18,5,3,3,
                3,2,2,2,10,2,2,2,2,2,2,2,2,2,7,3,
                3,13,2,2,2,19,2,2,2,2,19,2,2,2,2,3,
                3,2,2,19,2,2,2,2,2,2,2,2,19,10,2,3,
                3,2,9,2,2,19,2,2,2,19,2,2,2,2,2,3,
                3,8,2,2,2,2,2,2,2,2,2,2,2,13,2,3,
                3,3,3,25,18,6,18,25,18,5,18,6,18,25,3,3,
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
        };

        originalMapLayout = mapLayout.clone();

        ArrayList<Integer> trashList = new ArrayList<>();
        for(int i=0;i<mapLayout.length;i++){
            if(mapLayout[i]==19) trashList.add(i);
        }
        Collections.shuffle(trashList);
        keyTrashIndex = trashList.get(0);

        for(int i=0;i<tiles.length;i++){
            tiles[i] = new JLabel();
            updateTileIcon(i);

            character[i] = new JLabel();
            character[i].setOpaque(false);

            if(characterLayout[i]==4){
                character[i].setIcon(playerdown);
                characterPosition = i;
            }
        }

        setupQuestions();

        resetButton = new JButton("Reset");
        resetButton.setFocusable(false);
        resetButton.addActionListener(e -> resetGame());
    }

    private void saveGame(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(characterPosition);
            writer.println(hasKey);
            writer.println(quizCompleted);
            writer.println(keyTrashIndex);
            for (int i = 0; i < mapLayout.length; i++) {
                writer.print(mapLayout[i]);
                if (i < mapLayout.length - 1) writer.print(" ");
            }
            writer.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTileIcon(int index){
        switch(mapLayout[index]){
            case 1 -> tiles[index].setIcon(i1);
            case 2 -> tiles[index].setIcon(i3);
            case 3 -> tiles[index].setIcon(i2);
            case 6 -> tiles[index].setIcon(i12);
            case 8 -> tiles[index].setIcon(i4);
            case 18 -> tiles[index].setIcon(i5);
            case 19 -> tiles[index].setIcon(i6);
            case 21 -> tiles[index].setIcon(i8);
            case 22 -> tiles[index].setIcon(i7);
            case 9 -> tiles[index].setIcon(i9);
            case 24 -> tiles[index].setIcon(i10);
            case 5 -> tiles[index].setIcon(i11);
            case 13 -> tiles[index].setIcon(i13);
            case 7 -> tiles[index].setIcon(i14);
            case 10 -> tiles[index].setIcon(i15);
            case 25 -> tiles[index].setIcon(i16);
        }
    }

    private void setupQuestions(){
        questions = new ArrayList<>();

        questions.add(new Question("What is the chemical symbol for water?", "H2O"));
        questions.add(new Question("What is the closest planet to the sun?", "Mercury"));
        questions.add(new Question("How many factors does a prime number have?", "2"));
        questions.add(new Question("What is the powerhouse of the cell?", "Mitochondria"));
        questions.add(new Question("What force pulls objects toward Earth?", "Gravity"));
        questions.add(new Question("What organ pumps blood through the body?", "Heart"));
        questions.add(new Question("What is the center of an atom called?", "Nucleus"));
        questions.add(new Question("How many continents are there on Earth?", "7"));
        questions.add(new Question("What is the chemical symbol for salt?", "NaCl"));
        questions.add(new Question("What happens when like charges interact?", "Repel"));
        questions.add(new Question("What happens when unlike charges interact?", "Attract"));
        questions.add(new Question("What type of bond involves sharing electrons?", "Covalent bond"));
        questions.add(new Question("What is the charge of a proton?", "Positive"));
        questions.add(new Question("What is the charge of an electron?", "Negative"));
        questions.add(new Question("What particle has no charge?", "Neutron"));
    }

    public ImageIcon scale(ImageIcon img){
        return new ImageIcon(img.getImage().getScaledInstance(
                frameWidth/mapWidth,
                frameHeight/mapHeight,
                Image.SCALE_DEFAULT));
    }

    public void setFrame(){
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GraphPaperLayout(new Dimension(mapWidth,mapHeight)));

        int x=0,y=0;
        for(int i=0;i<tiles.length;i++){
            gridPanel.add(tiles[i],new Rectangle(x,y,1,1));
            gridPanel.add(character[i],new Rectangle(x,y,1,1),0);
            x++;
            if(x%mapWidth==0){ x=0; y++; }
        }

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(resetButton, BorderLayout.SOUTH);

        frame.setSize(frameWidth,frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.requestFocusInWindow();
    }

    public void move(int dir, ImageIcon sprite){

        int next = characterPosition + dir;

        try {

            if(next < 0 || next >= mapLayout.length) return;

            if(mapLayout[next] == 2 || mapLayout[next] == 24){
                character[characterPosition].setIcon(null);
                characterPosition = next;
                character[characterPosition].setIcon(sprite);
            }

            else if(mapLayout[next] == 9){

                int push = next + dir;

                if(push < 0 || push >= mapLayout.length) return;

                if(mapLayout[push] == 2){
                    mapLayout[push] = 9;
                    updateTileIcon(push);
                    mapLayout[next] = 2;
                    updateTileIcon(next);
                }

                else if(mapLayout[push] == 19){

                    boolean foundKey = (push == keyTrashIndex);

                    if(foundKey){
                        JOptionPane.showMessageDialog(frame,"You found the key!");
                        hasKey = true;
                    }

                    mapLayout[push] = 9;
                    updateTileIcon(push);

                    if(foundKey){
                        mapLayout[next] = 22;
                    } else {
                        mapLayout[next] = 21;
                    }
                    updateTileIcon(next);
                }

                else return;

                character[characterPosition].setIcon(null);
                characterPosition = next;
                character[characterPosition].setIcon(sprite);
            }

            if(mapLayout[next]==24 && hasKey && !quizCompleted){
                startQuiz();
            }

        } catch (Exception e) {
            return;
        }
    }

    private void startQuiz(){
        int correct = 0;
        Collections.shuffle(questions);

        for(int i=0;i<5;i++){
            Question q = questions.get(i);
            String answer = JOptionPane.showInputDialog(frame,q.question);
            if(answer!=null && answer.equalsIgnoreCase(q.answer))
                correct++;
        }

        if(correct>=3){
            JOptionPane.showMessageDialog(frame,"Game is complete!");
            quizCompleted = true;
            frame.dispose();
            
            long cur = System.currentTimeMillis() - Group6Map1.startTime;
            long fastest = Long.MAX_VALUE;
            try (BufferedReader reader = new BufferedReader(new FileReader(Menu.resolveFilePath("gr6fastest.log")))) {fastest = Long.parseLong(reader.readLine());}
            catch (IOException ex) {}
            String Scur = String.format("%d min %d sec", cur/60000, (cur/1000)%60);
            String Sfastest = String.format("%d min %d sec", fastest/60000, (fastest/1000)%60);
            if(fastest == Long.MAX_VALUE) JOptionPane.showMessageDialog(null, "First finish time: "+Scur);
            else if(fastest > cur) JOptionPane.showMessageDialog(null, "New record! Fastest finish time: "+Scur);
            else JOptionPane.showMessageDialog(null, "Current finish time: "+Scur+"\nFastest finish time: "+Sfastest);
            if(fastest == Long.MAX_VALUE || fastest > cur) 
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(Menu.resolveFilePath("gr6fastest.log")))) {
                    writer.write(String.valueOf(cur));
                    writer.newLine();
                } catch(IOException ex) {}
            
            float curError = (float)Group6Map1.wrongAnswers / (float)Group6Map1.answered;
            float lowest = 2.0f;
            try (BufferedReader reader = new BufferedReader(new FileReader(Menu.resolveFilePath("error_percentage.log")))) {lowest = Float.parseFloat(reader.readLine());}
            catch (IOException ex) {}
            String ScurError = String.format("%.2f%%", curError*100);
            String Slowest = String.format("%.2f%%", lowest*100);
            if(lowest == 2.0f) JOptionPane.showMessageDialog(null, "First time! Error%: "+ScurError);
            else if(lowest > curError) JOptionPane.showMessageDialog(null, "Record broken! Error%: "+ScurError);
            else JOptionPane.showMessageDialog(null, "Current Error%: "+ScurError+"\nLowest Error%: "+Slowest);
            if(lowest > curError) 
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(Menu.resolveFilePath("error_percentage.log")))) {
                    writer.write(String.valueOf(curError));
                    writer.newLine();
                } catch(IOException ex) {}
            
            Menu.startNextLevel(6);
        } else {
            JOptionPane.showMessageDialog(frame,"Not enough correct answers. Try again!");
        }
    }

    private void resetGame(){

        mapLayout = originalMapLayout.clone();

        hasKey = false;
        quizCompleted = false;

        for(int i=0;i<character.length;i++){
            character[i].setIcon(null);
            if(characterLayout[i]==4){
                characterPosition = i;
                character[i].setIcon(playerdown);
            }
        }

        ArrayList<Integer> trashList = new ArrayList<>();
        for(int i=0;i<mapLayout.length;i++){
            if(mapLayout[i]==19) trashList.add(i);
        }
        Collections.shuffle(trashList);
        keyTrashIndex = trashList.get(0);

        for(int i=0;i<tiles.length;i++){
            updateTileIcon(i);
        }

        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) move(1,playerright);
        if(e.getKeyCode()==KeyEvent.VK_LEFT) move(-1,playerleft);
        if(e.getKeyCode()==KeyEvent.VK_DOWN) move(mapWidth,playerdown);
        if(e.getKeyCode()==KeyEvent.VK_UP) move(-mapWidth,playerup);
        frame.repaint();
    }

    @Override public void keyTyped(KeyEvent e){}
    @Override public void keyReleased(KeyEvent e){}

    private static class Question{
        String question;
        String answer;
        Question(String q,String a){
            question=q;
            answer=a;
        }
    }

    public static void main(String[] args){
        Group6Map2 game = new Group6Map2();
        game.setFrame();
    }
}
//libres,medina,nogara <3
//chatgpt was used in making this code