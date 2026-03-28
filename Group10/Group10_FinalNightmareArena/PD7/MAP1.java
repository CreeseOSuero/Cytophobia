package PD7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Node {
    int pos;
    Node parent;
    Node(int pos, Node parent) {
        this.pos = pos;
        this.parent = parent;
    }
}

class BoundaryException extends Exception {
    public BoundaryException(String message) {
        super(message);
    }
}

class WallCollisionException extends Exception {
    public WallCollisionException(String message) {
        super(message);
    }
}

public class MAP1 implements KeyListener, ActionListener, WindowListener {
    JFrame frame;
    ImageIcon wall, tile, door, NPCIcon, PotionIcon, FinishedPotion;
    
    // Animation variables
    ImageIcon[][] playerSprites; 
    int animationFrame = 0; 
    int direction = 3; // Start facing down

    JLabel tiles[];
    JLabel character[];
    int mapLayout[];
    int characterPlace[];
    int mapWidth = 11;
    int mapHeight = 11;
    int frameWidth = 600;
    int frameHeight = 600;
    
    int characterPosition = -1;
    int enemyPosition = -1;
    int potionPosition = -1;
    boolean enemyDespawned = false;
    
    Timer enemyTimer;

    public MAP1() {
        frame = new JFrame("Eliminate the Enemy");
        wall = scaleIcon("Images/wall.jpg");
        tile = scaleIcon("Images/floor.png");
        door = scaleIcon("Images/door.jpg");
        NPCIcon = scaleIcon("Images/boy1.png");
        PotionIcon = scaleIcon("Images/potion.png");
        FinishedPotion = scaleIcon("Images/finishedPotion.png");
        playerSprites = new ImageIcon[4][3];
        for (int d = 0; d < 4; d++) {
            for (int f = 0; f < 3; f++) {
                playerSprites[d][f] = scaleIcon("Images/Copy of plr_" + d + f + ".png");
            }
        }
        characterPlace = new int[]{
            1,1,1,1,1,1,1,1,1,1,1,
            1,0,0,0,0,0,0,0,0,0,1,
            1,0,0,3,0,0,0,0,0,0,1,
            1,0,0,0,0,4,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,2,1,
            1,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,1,
            1,0,0,0,0,0,0,0,0,0,1,
            1,1,1,1,1,1,1,1,1,1,1
        };

        mapLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,
            0,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,5,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,0,
            2,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,0,
            0,0,0,0,0,0,0,0,0,0,0
        };

        character = new JLabel[mapWidth * mapHeight];
        tiles = new JLabel[mapWidth * mapHeight];

        for (int i = 0; i < character.length; i++) {
            if (characterPlace[i] == 2) {
                character[i] = new JLabel(playerSprites[direction][0]);
                characterPosition = i;
            } else if (characterPlace[i] == 3) {
                character[i] = new JLabel(NPCIcon);
                enemyPosition = i;
            } else if (characterPlace[i] == 4) {
                character[i] = new JLabel(PotionIcon);
                potionPosition = i;
            } else if (characterPlace[i] == 5) {
                character[i] = new JLabel(FinishedPotion);
                potionPosition = i;
            } else {
                character[i] = new JLabel();
            }

            if (mapLayout[i] == 0) tiles[i] = new JLabel(wall);
            else if (mapLayout[i] == 1) tiles[i] = new JLabel(tile);
            else if (mapLayout[i] == 2) tiles[i] = new JLabel(door);
            else tiles[i] = new JLabel();
        }
    }

    private ImageIcon scaleIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        return new ImageIcon(icon.getImage().getScaledInstance(
            (frameWidth / mapWidth), (frameHeight / mapHeight), Image.SCALE_DEFAULT));
    }

    public void setFrame() {
        frame.setLayout(new GraphPaperLayout(new Dimension(mapWidth, mapHeight)));
        int x = 0, y = 0, w = 1, h = 1;
        for (int i = 0; i < character.length; i++) {
            frame.add(character[i], new Rectangle(x, y, w, h));
            x++;
            if (x % mapWidth == 0) { x = 0; y++; }
        }
        x = 0; y = 0;
        for (int i = 0; i < tiles.length; i++) {
            frame.add(tiles[i], new Rectangle(x, y, w, h));
            x++;
            if (x % mapWidth == 0) { x = 0; y++; }
        }

        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);
        frame.addWindowListener(this);

        enemyTimer = new Timer(500, this);
        enemyTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int oldPos = characterPosition;
        int nextPos = -1;
        int newDir = direction;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            newDir = 0; nextPos = characterPosition - 1;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            newDir = 1; nextPos = characterPosition - mapWidth;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            newDir = 2; nextPos = characterPosition + 1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            newDir = 3; nextPos = characterPosition + mapWidth;
        } else if(e.getKeyCode()==KeyEvent.VK_SPACE){{
            if(isAdjacent(characterPosition, potionPosition)){
                enemyTimer.stop();
                boolean correct = askQuestion();

                if(correct){
                    character[potionPosition].setIcon(FinishedPotion);
                    characterPlace[potionPosition] = 5;
                    despawnEnemy();
                }
                else{
                    JOptionPane.showMessageDialog(frame, "Wrong mixture! The enemy remains.", "Incorrect", JOptionPane.WARNING_MESSAGE);
                    enemyTimer.start();
                }
                return;
}
                 int doorPosition = locateDoor();
                    if(doorPosition != -1){
                        if(enemyDespawned){
                            JOptionPane.showMessageDialog(
                                frame, "You have escaped the room. ", "Level Complete", JOptionPane.INFORMATION_MESSAGE
                            );
                            frame.dispose();
                            new MAP2().setFrame();
                        }
                        else{
                            enemyTimer.stop();
                            JOptionPane.showMessageDialog(
                                frame, "The door is locked. Eliminate the enemy first.", "Door Locked", JOptionPane.WARNING_MESSAGE
                            );
                            enemyTimer.start();
                        }
                    }
            }
        }

            try {
        if (nextPos < 0 || nextPos >= characterPlace.length) {
            throw new BoundaryException("Player has hit a boundary.");
        }

        if (characterPlace[nextPos] != 0) {
            throw new WallCollisionException("Player has hit a wall.");
        }

        direction = newDir;
        animationFrame = (animationFrame + 1) % 3;
        character[oldPos].setIcon(null);
        characterPlace[oldPos] = 0;
        characterPosition = nextPos;
        characterPlace[characterPosition] = 2;
        character[characterPosition].setIcon(playerSprites[direction][animationFrame]);
    } catch (BoundaryException be) {
        System.out.println(be.getMessage());
        
    } catch (WallCollisionException we) {
        System.out.println(we.getMessage());
    }
    }

    public boolean askQuestion(){
    String question = "What is the proper balanced equation for producing 2 HCl molecules";
    String[] choices = {
        "H2 + Cl2",
        "1/2 H2 + 1/2 Cl2",
        "2 H2 + 2 Cl2",
        "1/2 H2 + 2 Cl2"
    };

    int correctAnswerIndex = 0;
    int answer = JOptionPane.showOptionDialog(
        frame, question, "Mix the Chemicals", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]
    );
    return answer == correctAnswerIndex;
}
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (characterPosition != -1) {
            animationFrame = 0;
            character[characterPosition].setIcon(playerSprites[direction][0]);
        }
    }
    public void moveEnemy() {
        if (enemyPosition == -1 || characterPosition == -1) return;
        
        int nextPos = trackPlayer();
        if (nextPos == characterPosition) {
            enemyTimer.stop();
            JOptionPane.showMessageDialog(frame, "Try again.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new MAP1().setFrame();
            return;
        }
        if (nextPos != enemyPosition) {
            character[enemyPosition].setIcon(null);
            characterPlace[enemyPosition] = 0;
            enemyPosition = nextPos;
            characterPlace[enemyPosition] = 3;
            character[enemyPosition].setIcon(NPCIcon);
        }
    }

    public int trackPlayer() {
        boolean[] visited = new boolean[mapWidth * mapHeight];
        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.add(new Node(enemyPosition, null));
        visited[enemyPosition] = true;
        int[] directions = {-mapWidth, mapWidth, -1, 1};

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.pos == characterPosition) {
                while (current.parent != null && current.parent.pos != enemyPosition) {
                    current = current.parent;
                }
                return current.pos;
            }
            for (int d : directions) {
                int next = current.pos + d;
                if (next < 0 || next >= mapWidth * mapHeight) continue;
                if (visited[next]) continue;
                if (characterPlace[next] == 0 || next == characterPosition) {
                    visited[next] = true;
                    queue.add(new Node(next, current));
                }
            }
        }
        return enemyPosition;
    }

    public void despawnEnemy() {
        if(enemyPosition != -1) {
            character[enemyPosition].setIcon(null);
            characterPlace[enemyPosition] = 0;
            enemyPosition = -1; 
            enemyDespawned = true;
        }
        enemyTimer.stop();
        JOptionPane.showMessageDialog(frame, "The Enemy has been eliminated.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public int locateDoor(){
    int[] check = {
        characterPosition + 1, characterPosition - 1, characterPosition + mapWidth, characterPosition - mapWidth
    };

    for(int pos : check){
        if(pos >= 0 && pos < mapLayout.length){
            if(mapLayout[pos] == 2){
                return pos;
            }
        }
    }
    return -1;
}

    public boolean isAdjacent(int pos1, int pos2) {
        if (pos1 == -1 || pos2 == -1) return false;
        return Math.abs(pos1 - pos2) == 1 || Math.abs(pos1 - pos2) == mapWidth;
    }
    @Override public void actionPerformed(ActionEvent e) { if (e.getSource() == enemyTimer) moveEnemy(); }
    @Override public void windowClosed(WindowEvent e) { enemyTimer.stop(); }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}

    public static void main(String[] args) {
        new MAP1().setFrame();
    }
}