package PD7;

import Quarter3.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

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

class Player {
    private int score;
    private int health = 100;

    public void addScore(int s) {
        if (s > 0) score += s;
    }

    public void addScore(int s, String reason) {
        if (s > 0) {
            score += s;
            System.out.println("+" + s + " score from " + reason);
        }
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    public int getScore() {
        return score;
    }

    public int getHealth() {
        return health;
    }
}

class Tile {
    protected int type;

    public Tile(int type) {
        this.type = type;
    }

    public void interact() {
    }
}

public class MAP2 implements KeyListener, WindowListener {

    JFrame frame;

    ImageIcon wall, tile, door, beartrap;
    ImageIcon[][] playerSprites;

    JLabel tiles[];
    JLabel character[];

    int mapLayout[];
    boolean trapActivated[];
    int mapWidth = 20;
    int mapHeight = 20;

    int frameWidth = 600;
    int frameHeight = 600;

    int characterPosition;
    int direction = 3;
    int animationFrame = 0;

    Player player = new Player();
    Random random = new Random();
    boolean gameOver = false;

    private String[][] questions = {
        {"What is H2O?", "Water"},
        {"What is the chemical symbol for gold?", "Au"},
        {"What is 15 + 27?", "42"},
        {"What gas do plants absorb?", "Carbon dioxide"},
        {"What is the powerhouse of the cell?", "Mitochondria"},
        {"What is 8 x 7?", "56"},
        {"What element has the symbol Fe?", "Iron"},
        {"What is 144 / 12?", "12"}
    };

    public MAP2() {

        frame = new JFrame("Arena Map - Bear Trap Edition");

        wall = scaleIcon("Images/wall.jpg");
        tile = scaleIcon("Images/floor.png");
        door = scaleIcon("Images/door.jpg");
        beartrap = scaleIcon("Images/beartrap.png");

        playerSprites = new ImageIcon[4][3];
        for (int d = 0; d < 4; d++) {
            for (int f = 0; f < 3; f++) {
                playerSprites[d][f] = scaleIcon("Images/Copy of plr_" + d + f + ".png");
            }
        }

        mapLayout = new int[]{
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,1,1,1,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,1,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,3,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,3,1,1,1,1,1,1,1,1,1,3,1,1,1,0,
            0,1,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,3,1,1,1,1,1,1,1,1,3,1,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,3,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,0,
            0,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
            0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };

        trapActivated = new boolean[mapLayout.length];
        
        characterPosition = 5 * mapWidth + 5;

        tiles = new JLabel[mapLayout.length];
        character = new JLabel[mapLayout.length];

        for (int i = 0; i < mapLayout.length; i++) {
            if (mapLayout[i] == 0) tiles[i] = new JLabel(wall);
            else if (mapLayout[i] == 1) tiles[i] = new JLabel(tile);
            else if (mapLayout[i] == 2) tiles[i] = new JLabel(door);
            else if (mapLayout[i] == 3) tiles[i] = new JLabel(beartrap);

            character[i] = new JLabel();
        }
        
        character[characterPosition].setIcon(playerSprites[direction][0]);
    }

    private ImageIcon scaleIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        if (img == null) {
            System.err.println("Warning: Could not load image: " + path);
            return icon;
        }
        Image scaled = img.getScaledInstance(
                frameWidth / mapWidth,
                frameHeight / mapHeight,
                Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }

    public void setFrame() {
        frame.setLayout(new Quarter3.GraphPaperLayout(new Dimension(mapWidth, mapHeight)));

        for (int i = 0; i < character.length; i++) {
            int x = i % mapWidth;
            int y = i / mapWidth;
            frame.add(character[i], new Rectangle(x, y, 1, 1));
        }
        
        for (int i = 0; i < tiles.length; i++) {
            int x = i % mapWidth;
            int y = i / mapWidth;
            frame.add(tiles[i], new Rectangle(x, y, 1, 1));
        }

        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.addWindowListener(this);
        
        showInstructions();
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(frame,
            "Welcome to Bear Trap Arena!\n\n" +
            "Navigate with arrow keys.\n" +
            "Step on bear traps (X) to activate them by answering questions.\n" +
            "Activated traps damage you for 10 HP if you step on them again!\n" +
            "Reach the door to win!\n\n" +
            "Starting Health: " + player.getHealth() + " HP",
            "How to Play",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean activateTrap() {
        int qIndex = random.nextInt(questions.length);
        String question = questions[qIndex][0];
        String answer = questions[qIndex][1];

        String userAnswer = JOptionPane.showInputDialog(frame,
            "TRAP ACTIVATION REQUIRED!\n\n" +
            "Question: " + question + "\n\n" +
            "Enter your answer:",
            "Trap Puzzle",
            JOptionPane.QUESTION_MESSAGE);

        if (userAnswer == null) return false;

        boolean correct = userAnswer.trim().equalsIgnoreCase(answer);
        
        if (correct) {
            JOptionPane.showMessageDialog(frame,
                "Correct! Trap is now ACTIVATED!\n" +
                "Avoid stepping on it or you'll take damage!",
                "Trap Activated!",
                JOptionPane.WARNING_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(frame,
                "Wrong! The answer was: " + answer + "\n" +
                "Trap remains dormant.",
                "Failed Activation",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void checkTrapDamage() {
        JOptionPane.showMessageDialog(frame,
            "OUCH! You stepped on an activated trap!\n" +
            "You took 10 damage!\n" +
            "Current Health: " + player.getHealth() + " HP",
            "Trap Damage!",
            JOptionPane.WARNING_MESSAGE);

        if (player.getHealth() <= 0) {
            gameOver = true;
            JOptionPane.showMessageDialog(frame,
                "GAME OVER!\nYou ran out of health!",
                "Game Over",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    
    public void validateMove(int currentPos, int nextPos, int dir)
        throws BoundaryException, WallCollisionException {
    if (nextPos < 0 || nextPos >= mapLayout.length) {
        throw new BoundaryException("Player is out of bounds.");
    }

    if (dir == 0 && currentPos % mapWidth == 0) {
        throw new BoundaryException("Player has hit the left side.");
    }

    if (dir == 2 && (currentPos + 1) % mapWidth == 0) {
        throw new BoundaryException("Player has hit the right side.");
    }

    if (mapLayout[nextPos] == 0) {
        throw new WallCollisionException("Player has hit a wall.");
    }
}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;

        int nextPos = characterPosition;
    int newDir = direction;

    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        newDir = 0;
        nextPos = characterPosition - 1;
    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
        newDir = 1;
        nextPos = characterPosition - mapWidth;
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        newDir = 2;
        nextPos = characterPosition + 1;
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        newDir = 3;
        nextPos = characterPosition + mapWidth;
    } else {
        return;
    }

    try {
        validateMove(characterPosition, nextPos, newDir);
        direction = newDir;
        if (mapLayout[nextPos] == 2) {
            player.addScore(player.getHealth() * 10, "remaining health bonus");
            JOptionPane.showMessageDialog(frame,
                "YOU WIN!\nScore: " + player.getScore() + "\nHealth remaining: " + player.getHealth());
            System.exit(0);
        }

        if (mapLayout[nextPos] == 3) {
            if (!trapActivated[nextPos]) {
                if (activateTrap()) {
                    trapActivated[nextPos] = true;
                }
            } else {
                player.takeDamage(10);
                checkTrapDamage();
            }
        }

        character[characterPosition].setIcon(null);
        characterPosition = nextPos;
        animationFrame = (animationFrame + 1) % 3;
        character[characterPosition].setIcon(playerSprites[direction][animationFrame]);
    } catch (BoundaryException ex) {
        System.out.println("Boundary Collision: " + ex.getMessage());
    } catch (WallCollisionException ex) {
        System.out.println("Wall Collision: " + ex.getMessage());
    }
        }

    @Override 
    public void keyReleased(KeyEvent e) {
        if (characterPosition >= 0 && characterPosition < character.length) {
            animationFrame = 0;
            character[characterPosition].setIcon(playerSprites[direction][0]);
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}

    public static void main(String[] args) {
        new MAP2().setFrame();
    }
}