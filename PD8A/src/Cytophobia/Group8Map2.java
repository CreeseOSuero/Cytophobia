package Cytophobia;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Group8Map2 implements KeyListener {
    
    // Constants
    private static final int MAP_WIDTH = 32;
    private static final int MAP_HEIGHT = 18;
    private static final int FRAME_WIDTH = 1600;
    private static final int FRAME_HEIGHT = 900;
    private static final int PLAYER_SIZE = 2;
    private static final int TILE_SIZE_X = FRAME_WIDTH / MAP_WIDTH;
    private static final int TILE_SIZE_Y = FRAME_HEIGHT / MAP_HEIGHT;
    
    // UI Components
    private JFrame frame;
    private JLabel bg;
    private JLabel player;
    // REMOVED: chest JLabel - it's in the background image now
    
    // Game State
    private PlayerState playerState;
    private MapData mapData;
    private QuizSystem quizSystem;

    public Group8Map2() {
        System.out.println("=== PD6_GUI Starting ===");
        try {
            initializeFrame();
            initializePlayer();
            initializeMap();
            initializeUI();
            setupLayout();
            startGame();
            System.out.println("=== PD6_GUI Started Successfully ===");
        } catch (Exception ex) {
            System.err.println("FATAL ERROR in PD6_GUI: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error initializing PD6: " + ex.getMessage());
        }
    }
    
    private class PlayerState {
        ImageIcon[] sprites;
        volatile boolean mobile = true;
        int x = 0;
        int y = 9;
        int direction = 0;
        int animationFrame = 0;
        
        PlayerState() {
            System.out.println("Loading player sprites...");
            sprites = new ImageIcon[12];
            try {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        sprites[i * 3 + j] = loadImage("assets8/plr_" + i + j + ".PNG", PLAYER_SIZE, PLAYER_SIZE);
                    }
                }
            } catch (Exception ex) {
                System.err.println("Exception loading player sprites: " + ex.getMessage());
                ex.printStackTrace();
            }
            System.out.println("Player sprites loaded");
        }
        
        ImageIcon getCurrentSprite() {
            return sprites[direction * 3 + animationFrame];
        }
        
        void nextFrame() {
            animationFrame = (animationFrame + 1) % 3;
        }
        
        void move(int dx, int dy) {
            x += dx;
            y += dy;
            clampPosition();
        }
        
        void clampPosition() {
            x = Math.max(0, Math.min(x, MAP_WIDTH - PLAYER_SIZE));
            y = Math.max(0, Math.min(y, MAP_HEIGHT - PLAYER_SIZE));
        }
    }
    
    private class MapData {
        int[] layout;
        
        MapData() {
            System.out.println("Initializing map data...");
            try {
                layout = new int[MAP_WIDTH * MAP_HEIGHT];
                for (int i = 0; i < layout.length; i++) {
                    layout[i] = 0;
                }
                // Chest trigger zone at (28, 9) - invisible since it's in background
                layout[9 * MAP_WIDTH + 28] = 2;
                System.out.println("Map data initialized, chest trigger at (28,9)");
            } catch (Exception ex) {
                System.err.println("Exception initializing map: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        boolean isChest(int x, int y) {
            try {
                int idx = y * MAP_WIDTH + x;
                return idx >= 0 && idx < layout.length && layout[idx] == 2;
            } catch (Exception ex) {
                System.err.println("Exception checking chest: " + ex.getMessage());
                return false;
            }
        }
    }
    
    private class QuizSystem {
        private final String[] questions = {
            "Moonlight seeps through the cracked windows...\nWhat part of the blood carries oxygen?",
            "A silent figure drifts through the halls...\nWhich human cell has no nucleus?",
            "A curse twists those who enter...\nWhat molecule mutates to alter traits?",
            "The mansion breathes as if alive...\nWhat process uses oxygen to produce energy?",
            "A sickness spreads endlessly...\nHow do bacteria reproduce?"
        };
        
        private final String[] answers = {
            "red blood cells",
            "red blood cell",
            "dna",
            "cellular respiration",
            "binary fission"
        };
        
        private int currentQuestion = 0;
        
        void start() {
            System.out.println("Starting quiz...");
            try {
                playerState.mobile = false;
                
                while (currentQuestion < questions.length) {
                    String input = null;
                    try {
                        input = JOptionPane.showInputDialog(frame, questions[currentQuestion]);
                    } catch (Exception ex) {
                        System.err.println("Exception showing input dialog: " + ex.getMessage());
                        break;
                    }
                    
                    if (input == null) {
                        System.out.println("User cancelled input, retrying...");
                        continue;
                    }
                    
                    input = input.toLowerCase().trim();
                    System.out.println("User answered: " + input + " (expected: " + answers[currentQuestion] + ")");
                    
                    if (input.contains(answers[currentQuestion])) {
                        JOptionPane.showMessageDialog(frame, "Correct...");
                        currentQuestion++;
                        System.out.println("Correct! Progress: " + currentQuestion + "/" + questions.length);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Wrong... Try again.");
                        System.out.println("Wrong answer");
                    }
                }
                
                if (currentQuestion >= questions.length) {
                    JOptionPane.showMessageDialog(frame, "The chest creaks open...");
                    System.out.println("Quiz complete! Chest opened.");
                    frame.dispose();
                    
                    long cur = System.currentTimeMillis() - Group8Map1.startTime;
                    long fastest = Long.MAX_VALUE;
                    try (BufferedReader reader = new BufferedReader(new FileReader(Menu.resolveFilePath("gr8fastest.log")))) {fastest = Long.parseLong(reader.readLine());}
                    catch (IOException ex) {}
                    String Scur = String.format("%d min %d sec", cur/60000, (cur/1000)%60);
                    String Sfastest = String.format("%d min %d sec", fastest/60000, (fastest/1000)%60);
                    if(fastest == Long.MAX_VALUE) JOptionPane.showMessageDialog(null, "First finish time: "+Scur);
                    else if(fastest > cur) JOptionPane.showMessageDialog(null, "New record! Fastest finish time: "+Scur);
                    else JOptionPane.showMessageDialog(null, "Current finish time: "+Scur+"\nFastest finish time: "+Sfastest);
                    if(fastest == Long.MAX_VALUE || fastest > cur) 
                        try(BufferedWriter writer = new BufferedWriter(new FileWriter(Menu.resolveFilePath("gr8fastest.log")))) {
                            writer.write(String.valueOf(cur));
                            writer.newLine();
                        } catch(IOException ex) {}
                    
                    Menu.startNextLevel(8);
                }
                
                playerState.mobile = true;
                
            } catch (Exception ex) {
                System.err.println("Exception in quiz: " + ex.getMessage());
                ex.printStackTrace();
                playerState.mobile = true;
            }
        }
    }
    
    private void initializeFrame() {
        System.out.println("Initializing frame...");
        try {
            frame = new JFrame("Cytophobia - The Chest");
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addKeyListener(this);
            frame.setFocusable(true);
            System.out.println("Frame initialized");
        } catch (Exception ex) {
            System.err.println("Exception initializing frame: " + ex.getMessage());
            throw new RuntimeException("Failed to initialize frame", ex);
        }
    }
    
    private void initializePlayer() {
        System.out.println("Initializing player...");
        try {
            playerState = new PlayerState();
            player = new JLabel(playerState.getCurrentSprite());
            System.out.println("Player initialized at (0,9)");
        } catch (Exception ex) {
            System.err.println("Exception initializing player: " + ex.getMessage());
            throw new RuntimeException("Failed to initialize player", ex);
        }
    }
    
    private void initializeMap() {
        System.out.println("Initializing map...");
        try {
            mapData = new MapData();
            quizSystem = new QuizSystem();
            System.out.println("Map and quiz system initialized");
        } catch (Exception ex) {
            System.err.println("Exception initializing map: " + ex.getMessage());
            throw new RuntimeException("Failed to initialize map", ex);
        }
    }
    
    private void initializeUI() {
        System.out.println("Initializing UI...");
        try {
            bg = loadLabel("assets8/PD6_Background.png", MAP_WIDTH, MAP_HEIGHT, "Background");
            // REMOVED: chest = loadLabel(...) - chest is now in background image
            System.out.println("UI initialized");
        } catch (Exception ex) {
            System.err.println("Exception initializing UI: " + ex.getMessage());
            throw new RuntimeException("Failed to initialize UI", ex);
        }
    }
    
    private JLabel loadLabel(String path, int w, int h, String name) {
        System.out.println("Loading " + name + " from: " + path);
        try {
            ImageIcon icon = loadImage(path, w, h);
            if (icon == null || icon.getImage() == null || icon.getIconWidth() <= 0) {
                System.err.println("FAILED to load " + name + " - creating placeholder");
                JLabel placeholder = new JLabel(name + " MISSING", SwingConstants.CENTER);
                placeholder.setBackground(Color.DARK_GRAY);
                placeholder.setOpaque(true);
                placeholder.setForeground(Color.WHITE);
                return placeholder;
            }
            System.out.println(name + " loaded: " + icon.getIconWidth() + "x" + icon.getIconHeight());
            return new JLabel(icon);
        } catch (Exception ex) {
            System.err.println("Exception loading " + name + ": " + ex.getMessage());
            JLabel placeholder = new JLabel(name + " ERROR", SwingConstants.CENTER);
            placeholder.setBackground(Color.RED);
            placeholder.setOpaque(true);
            return placeholder;
        }
    }
    
    private void setupLayout() {
        System.out.println("Setting up layout...");
        try {
            frame.setLayout(new GraphPaperLayout(new Dimension(MAP_WIDTH, MAP_HEIGHT)));
            
            frame.add(player, new Rectangle(playerState.x, playerState.y, PLAYER_SIZE, PLAYER_SIZE));
            // REMOVED: frame.add(chest, ...) - chest is in background
            frame.add(bg, new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT));
            
            player.setVisible(true);
            bg.setVisible(true);
            
            System.out.println("Layout complete (chest is in background image)");
        } catch (Exception ex) {
            System.err.println("Exception setting up layout: " + ex.getMessage());
            throw new RuntimeException("Failed to setup layout", ex);
        }
    }
    
    private void startGame() {
        System.out.println("Starting game...");
        try {
            frame.setVisible(true);
            frame.requestFocusInWindow();
            System.out.println("Game started, player mobile: " + playerState.mobile);
        } catch (Exception ex) {
            System.err.println("Exception starting game: " + ex.getMessage());
            throw new RuntimeException("Failed to start game", ex);
        }
    }
    
    private ImageIcon loadImage(String ref, int scaleX, int scaleY) {
        String[] attempts = {
            ref,
            "/" + ref,
            "src/" + ref,
            "./" + ref,
            ref.toLowerCase(),
            ref.replace(".PNG", ".png"),
            ref.replace(".png", ".PNG")
        };
        
        for (String path : attempts) {
            try {
                java.net.URL url = getClass().getResource(path);
                if (url == null) {
                    url = getClass().getClassLoader().getResource(path);
                }
                if (url == null) {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        url = file.toURI().toURL();
                    }
                }
                
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    if (icon.getImage() != null && icon.getIconWidth() > 0) {
                        int w = TILE_SIZE_X * scaleX;
                        int h = TILE_SIZE_Y * scaleY;
                        Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT);
                        return new ImageIcon(scaled);
                    }
                }
            } catch (Exception ex) {
                // Try next
            }
        }
        return null;
    }
    
    @Override
    public void keyPressed(KeyEvent evt) {
        try {
            if (!playerState.mobile) {
                System.out.println("Player not mobile, ignoring key");
                return;
            }
            
            int prevX = playerState.x;
            int prevY = playerState.y;
            
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerState.direction = 0;
                    playerState.move(-1, 0);
                    break;
                case KeyEvent.VK_UP:
                    playerState.direction = 1;
                    playerState.move(0, -1);
                    break;
                case KeyEvent.VK_RIGHT:
                    playerState.direction = 2;
                    playerState.move(1, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    playerState.direction = 3;
                    playerState.move(0, 1);
                    break;
            }
            
            System.out.println("Player moved to (" + playerState.x + "," + playerState.y + ")");
            
            // Check chest collision at (28,9) - invisible trigger zone
            if (mapData.isChest(playerState.x, playerState.y) || 
                mapData.isChest(playerState.x + 1, playerState.y) ||
                mapData.isChest(playerState.x, playerState.y + 1) ||
                mapData.isChest(playerState.x + 1, playerState.y + 1)) {
                System.out.println("Chest area reached! Starting quiz...");
                new Thread(() -> quizSystem.start()).start();
            }
            
            playerState.nextFrame();
            player.setIcon(playerState.getCurrentSprite());
            ((GraphPaperLayout)frame.getContentPane().getLayout())
                .setConstraints(player, new Rectangle(playerState.x, playerState.y, PLAYER_SIZE, PLAYER_SIZE));
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            
        } catch (Exception ex) {
            System.err.println("Exception in keyPressed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent evt) {
        // Not used
    }
    
    @Override
    public void keyTyped(KeyEvent evt) {
        // Not used
    }
    
    public static void main(String[] args) {
        new Group8Map2();
    }
}
