package Cytophobia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Group5Map2 implements KeyListener {

    JFrame frame;

    // ===== SETTINGS =====
    int mapWidth = 32;
    int mapHeight = 18;
    int tileSize = 60;
    int frameWidth = mapWidth * tileSize;
    int frameHeight = mapHeight * tileSize;

    // ===== IMAGES =====
    ImageIcon wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8;
    ImageIcon floor, bed1, bed2, sky, couch, shelf, carpet, table, plant, paper;
    ImageIcon key, chest, goldkey, safe;
    ImageIcon frontS, frontW, backS, backW, leftS, leftW, rightS, rightW;

    JLabel[] tiles;
    JLabel[] character;

    int[] mapLayout;
    int characterPosition;
    int characterMode = 0;
    int direction;

    int keyPosition = -1;
    int chestPosition = -1;
    boolean chestUnlocked = false;

    int goldKeyPosition = -1;
    int safePosition = -1;
    boolean safeUnlocked = false;

    public Group5Map2() {
        frame = new JFrame("PD Merge");

        characterPosition = (mapHeight / 2) * mapWidth + (mapWidth / 2);

        // ===== LOAD IMAGES =====
        wall1 = load("assets5/wall1.jpg");
        wall2 = load("assets5/wall2.jpg");
        wall3 = load("assets5/wall3.jpg");
        wall4 = load("assets5/wall4.jpg");
        wall5 = load("assets5/wall5.jpg");
        wall6 = load("assets5/wall6.jpg");
        wall7 = load("assets5/wall7.jpg");
        wall8 = load("assets5/wall8.jpg");

        floor = load("assets5/floor.PNG");
        bed1 = load("assets5/bed1.PNG");
        bed2 = load("assets5/bed2.PNG");
        sky = load("assets5/sky.jpg");

        couch = load("assets5/couch.PNG");
        shelf = load("assets5/shelf.PNG");
        carpet = load("assets5/carpet.PNG");
        table = load("assets5/table.PNG");
        plant = load("assets5/plant.PNG");
        paper = load("assets5/paper.PNG");

        key = load("assets5/key.PNG");
        chest = load("assets5/chest.jpg");
        goldkey = load("assets5/goldkey.PNG");
        safe = load("assets5/safe.PNG");

        frontS = load("assets5/plr_10.PNG");
        frontW = load("assets5/plr_11.PNG");
        backS = load("assets5/plr_30.PNG");
        backW = load("assets5/plr_31.PNG");
        leftS = load("assets5/plr_00.PNG");
        leftW = load("assets5/plr_01.PNG");
        rightS = load("assets5/plr_20.PNG");
        rightW = load("assets5/plr_21.PNG");

        // ===== NEW MAP LAYOUT =====
        mapLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,6,4,4,4,4,4,4,4,4,4,4,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,12,1,1,1,1,1,1,1,1,13,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,14,1,1,19,1,1,17,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,11,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,5,15,1,1,1,1,18,16,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,9,11,11,11,11,1,1,11,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,6,4,4,4,4,4,1,1,4,4,4,4,4,7,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,12,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,13,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,1,1,1,1,1,1,1,1,1,1,1,1,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,5,2,1,1,1,1,1,1,1,1,1,1,3,10,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,9,11,11,11,11,11,11,11,11,11,11,11,11,8,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };

        tiles = new JLabel[mapLayout.length];
        character = new JLabel[mapLayout.length];

        // ===== INITIALIZE TILES =====
        for (int i = 0; i < mapLayout.length; i++) {
            tiles[i] = new JLabel();
            character[i] = new JLabel();
            character[i].setOpaque(false);

            switch (mapLayout[i]) {
                case 1 -> tiles[i].setIcon(floor);
                case 2 -> tiles[i].setIcon(bed1);
                case 3 -> tiles[i].setIcon(bed2);
                case 4 -> tiles[i].setIcon(wall1);
                case 5 -> tiles[i].setIcon(wall2);
                case 6 -> tiles[i].setIcon(wall3);
                case 7 -> tiles[i].setIcon(wall4);
                case 8 -> tiles[i].setIcon(wall5);
                case 9 -> tiles[i].setIcon(wall6);
                case 10 -> tiles[i].setIcon(wall7);
                case 11 -> tiles[i].setIcon(wall8);
                case 12 -> { tiles[i].setIcon(key); keyPosition = i; }
                case 13 -> { tiles[i].setIcon(chest); chestPosition = i; }
                case 14 -> tiles[i].setIcon(carpet);
                case 15 -> tiles[i].setIcon(table);
                case 16 -> tiles[i].setIcon(plant);
                case 17 -> { tiles[i].setIcon(goldkey); goldKeyPosition = i; }
                case 18 -> { tiles[i].setIcon(safe); safePosition = i; }
                case 19 -> tiles[i].setIcon(paper);
                default -> tiles[i].setIcon(sky);
            }
        }

        character[characterPosition].setIcon(frontS);

        loadProgress(); // Load saved chest/safe status
    }

    private ImageIcon load(String path) {
        Image img = new ImageIcon(getClass().getResource("/"+path)).getImage();
        Image scaled = img.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public boolean isWall(int index) {
        if (index < 0 || index >= mapLayout.length) return true;
        int tile = mapLayout[index];
        if (tile >= 4 && tile <= 11) return true;
        if (tile == 2 || tile == 3 || tile == 14 || tile == 15 || tile == 16) return true;
        if ((index == chestPosition && !chestUnlocked) || (index == safePosition && !safeUnlocked)) return true;
        return false;
    }

    public void setFrame() {
        JLayeredPane pane = new JLayeredPane();
        pane.setPreferredSize(new Dimension(frameWidth, frameHeight));

        for (int i = 0; i < tiles.length; i++) {
            int x = (i % mapWidth) * tileSize;
            int y = (i / mapWidth) * tileSize;

            tiles[i].setBounds(x, y, tileSize, tileSize);
            pane.add(tiles[i], JLayeredPane.DEFAULT_LAYER);

            character[i].setBounds(x, y, tileSize, tileSize);
            pane.add(character[i], JLayeredPane.PALETTE_LAYER);
        }

        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int nextPos = characterPosition;
        ImageIcon nextIcon = null;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> { direction = 3; if ((characterPosition % mapWidth) < mapWidth-1) nextPos++; nextIcon = (characterMode==0)?rightW:rightS; }
            case KeyEvent.VK_LEFT  -> { direction = 2; if ((characterPosition % mapWidth) >0) nextPos--; nextIcon = (characterMode==0)?leftW:leftS; }
            case KeyEvent.VK_DOWN  -> { direction = 1; if ((characterPosition/mapWidth) < mapHeight-1) nextPos += mapWidth; nextIcon = (characterMode==0)?backW:backS; }
            case KeyEvent.VK_UP    -> { direction = 0; if ((characterPosition/mapWidth) >0) nextPos -= mapWidth; nextIcon = (characterMode==0)?frontW:frontS; }
        }

        // PUSH key logic
        if (nextPos == keyPosition || nextPos == goldKeyPosition) {
            int pushPos = switch(direction) {
                case 0 -> nextPos - mapWidth;
                case 1 -> nextPos + mapWidth;
                case 2 -> nextPos - 1;
                case 3 -> nextPos + 1;
                default -> nextPos;
            };
            if (pushPos<0 || pushPos>=mapLayout.length) return;

            // Chest puzzle
            if (nextPos == keyPosition && pushPos == chestPosition && !chestUnlocked) {
                if (askQuiz()) {
                    chestUnlocked = true;
                    saveProgress();
                    JOptionPane.showMessageDialog(frame,"Chest unlocked!");
                } else {
                    JOptionPane.showMessageDialog(frame,"Wrong! Try again.");
                }
                return;
            }

            // Safe puzzle
            if (nextPos == goldKeyPosition && pushPos == safePosition && !safeUnlocked) {
                String answer = JOptionPane.showInputDialog(frame,"Radian of (0,-1)?");
                if (answer != null && (answer.equalsIgnoreCase("3pi/2") || answer.equals("270"))) {
                    safeUnlocked = true;
                    saveProgress();
                    JOptionPane.showMessageDialog(frame,"Safe unlocked!");
                    tiles[goldKeyPosition].setIcon(floor);
                    tiles[safePosition].setIcon(floor);
                    goldKeyPosition = -1;
                } else {
                    JOptionPane.showMessageDialog(frame,"Wrong answer!");
                }
                return;
            }
            if(safeUnlocked && chestUnlocked) {
                frame.dispose();
                
                long cur = System.currentTimeMillis() - Group5Map1.startTime;
                long fastest = Long.MAX_VALUE;
                try (BufferedReader reader = new BufferedReader(new FileReader(Menu.resolveFilePath("gr5fastest.log")))) {fastest = Long.parseLong(reader.readLine());}
                catch (IOException ex) {}
                String Scur = String.format("%d min %d sec", cur/60000, (cur/1000)%60);
                String Sfastest = String.format("%d min %d sec", fastest/60000, (fastest/1000)%60);
                if(fastest == Long.MAX_VALUE) JOptionPane.showMessageDialog(null, "First finish time: "+Scur);
                else if(fastest > cur) JOptionPane.showMessageDialog(null, "New record! Fastest finish time: "+Scur);
                else JOptionPane.showMessageDialog(null, "Current finish time: "+Scur+"\nFastest finish time: "+Sfastest);
                if(fastest == Long.MAX_VALUE || fastest > cur) 
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(Menu.resolveFilePath("gr5fastest.log")))) {
                        writer.write(String.valueOf(cur));
                        writer.newLine();
                    } catch(IOException ex) {}

                Menu.startNextLevel(5);
            }

            if (!isWall(pushPos)) {
                if (nextPos == keyPosition) { tiles[keyPosition].setIcon(floor); keyPosition = pushPos; tiles[keyPosition].setIcon(key); }
                if (nextPos == goldKeyPosition) { tiles[goldKeyPosition].setIcon(floor); goldKeyPosition = pushPos; tiles[goldKeyPosition].setIcon(goldkey); }
            } else return;
        }

        if (!isWall(nextPos)) {
            character[characterPosition].setIcon(null);
            characterPosition = nextPos;
            character[characterPosition].setIcon(nextIcon);
            characterMode = 1 - characterMode;
        }
    }

    private boolean askQuiz() {
        ArrayList<String[]> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Menu.resolveFilePath("questions.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) questions.add(parts);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Questions file not found!");
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading questions file!");
            return false;
        }

        for (String[] q : questions) {
            String ans = JOptionPane.showInputDialog(frame, q[0]);
            boolean correct = ans != null && ans.trim().equalsIgnoreCase(q[1]);
            logAnswer(q[0], ans, correct);
            if (!correct) return false;
        }
        return true;
    }

    private void logAnswer(String question, String answer, boolean correct) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(Menu.resolveFilePath("answers.txt"), true))) {
            pw.println(question + " | Your answer: " + answer + " | Correct: " + correct);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error logging answer!");
        }
    }

    private void saveProgress() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(Menu.resolveFilePath("progress.txt")))) {
            pw.println("chestUnlocked=" + chestUnlocked);
            pw.println("safeUnlocked=" + safeUnlocked);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving progress!");
        }
    }

    private void loadProgress() {
        try (BufferedReader br = new BufferedReader(new FileReader(Menu.resolveFilePath("progress.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("chestUnlocked=")) chestUnlocked = Boolean.parseBoolean(line.split("=")[1]);
                if (line.startsWith("safeUnlocked=")) safeUnlocked = Boolean.parseBoolean(line.split("=")[1]);
            }
        } catch (FileNotFoundException e) {
            // first time playing, no file yet
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading progress!");
        }
    }

    @Override public void keyTyped(KeyEvent e){}
    @Override public void keyReleased(KeyEvent e){}

    public static void main(String[] args){
        new Group5Map2().setFrame();
    }
}