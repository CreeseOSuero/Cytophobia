package Cytophobia;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public class Group8Map1 implements KeyListener {
    
    // Constants
    private static final int MAP_WIDTH = 32;
    private static final int MAP_HEIGHT = 18;
    private static final int FRAME_WIDTH = 1600;
    private static final int FRAME_HEIGHT = 900;
    private static final int PLAYER_SIZE = 2;
    
    // UI Components
    private JFrame frame;
    private JLabel bg;
    private JLabel player;
    private JPanel overlay;
    private JLabel fadeEffect;
    private JLabel dialogueBox;
    private JLabel dialogueName;
    private JLabel dialogueText;
    private JLabel alphaGrad;
    
    // Player State
    private PlayerState playerState;
    private MapData mapData;
    private GameState gameState;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Group8Map1());
    }
    
    public Group8Map1() {
        System.out.println("=== PD4_GUI Starting ===");
        try {
            initializeFrame();
            initializePlayer();
            initializeMap();
            initializeUI();
            initializeOverlay();
            setupLayout();
            startGame();
            System.out.println("=== PD4_GUI Started Successfully ===");
        } catch (Exception ex) {
            System.err.println("FATAL ERROR: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    
    private void initializeFrame() {
        System.out.println("Initializing frame...");
        frame = new JFrame("Cytophobia - The House");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        System.out.println("Frame initialized");
    }
    
    private void initializePlayer() {
        System.out.println("Initializing player...");
        playerState = new PlayerState();
        player = new JLabel(playerState.getCurrentSprite());
        System.out.println("Player initialized, sprite: " + (playerState.getCurrentSprite() != null));
    }
    
    private void initializeMap() {
        System.out.println("Initializing map...");
        mapData = new MapData();
        System.out.println("Map initialized");
    }
    
    private void initializeUI() {
        System.out.println("Initializing UI...");
        
        // Try multiple path formats
        bg = loadLabel("assets8/PD4_Background.png", MAP_WIDTH, MAP_HEIGHT, "Background");
        
        fadeEffect = new JLabel();
        fadeEffect.setBackground(Color.BLACK);
        fadeEffect.setOpaque(true);
        
        dialogueBox = new JLabel();
        dialogueBox.setBackground(Color.BLACK);
        dialogueBox.setOpaque(true);
        
        dialogueName = new JLabel();
        dialogueName.setForeground(Color.WHITE);
        dialogueName.setFont(new Font("Courier New", Font.PLAIN, 40));
        
        dialogueText = new JLabel();
        dialogueText.setForeground(Color.WHITE);
        dialogueText.setFont(new Font("Arial Unicode MS", Font.PLAIN, 25));
        
        alphaGrad = loadLabel("assets8/alpha_grad.png", MAP_WIDTH, 3, "AlphaGrad");
        System.out.println("UI initialized");
    }
    
    private JLabel loadLabel(String path, int w, int h, String name) {
        System.out.println("Loading " + name + " from: " + path);
        ImageIcon icon = loadImage(path, w, h);
        if (icon == null || icon.getImage() == null || icon.getIconWidth() <= 0) {
            System.err.println("FAILED to load " + name + " - creating placeholder");
            // Create colored placeholder
            JLabel placeholder = new JLabel(name + " MISSING");
            placeholder.setBackground(Color.RED);
            placeholder.setOpaque(true);
            placeholder.setForeground(Color.WHITE);
            placeholder.setHorizontalAlignment(SwingConstants.CENTER);
            return placeholder;
        }
        System.out.println(name + " loaded successfully: " + icon.getIconWidth() + "x" + icon.getIconHeight());
        return new JLabel(icon);
    }
    
    private void initializeOverlay() {
        System.out.println("Initializing overlay...");
        overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Graphics2D g2d = (Graphics2D) g;
                    int tileW = getWidth() / MAP_WIDTH;
                    int tileH = getHeight() / MAP_HEIGHT;
                    float centerX = (playerState.x + 1) * tileW;
                    float centerY = (playerState.y + 1) * tileH;
                    float radius = 300f;
                    float[] dist = {0.0f, 1.0f};
                    Color[] colors = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 245)};
                    
                    RadialGradientPaint paint = new RadialGradientPaint(
                        new Point2D.Float(centerX, centerY), radius, dist, colors);
                    g2d.setPaint(paint);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } catch (Exception ex) {
                    System.err.println("Overlay paint error: " + ex.getMessage());
                }
            }
        };
        overlay.setOpaque(false);
        System.out.println("Overlay initialized");
    }
    
    private void setupLayout() {
        System.out.println("Setting up layout...");
        frame.setLayout(new GraphPaperLayout(new Dimension(MAP_WIDTH, MAP_HEIGHT)));
        
        System.out.println("Adding components to frame...");
        frame.add(player, new Rectangle(playerState.x, playerState.y, PLAYER_SIZE, PLAYER_SIZE));
        frame.add(bg, new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT));
        frame.add(dialogueName, new Rectangle(2, 12, MAP_WIDTH - 2, 2));
        frame.add(dialogueText, new Rectangle(2, 14, MAP_WIDTH - 2, 3));
        frame.add(dialogueBox, new Rectangle(0, 12, MAP_WIDTH, MAP_HEIGHT - 12));
        frame.add(alphaGrad, new Rectangle(0, 9, MAP_WIDTH, 3));
        frame.add(fadeEffect, new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT));
        
        frame.setGlassPane(overlay);
        overlay.setVisible(true);
        
        // Initial visibility - MAKE EVERYTHING VISIBLE FOR DEBUG
        fadeEffect.setVisible(false);
        alphaGrad.setVisible(false);
        dialogueBox.setVisible(false);
        dialogueName.setVisible(false);
        dialogueText.setVisible(false);
        player.setVisible(true); // Make player visible immediately
        bg.setVisible(true);     // Make background visible immediately
        
        System.out.println("Layout complete - bg visible: " + bg.isVisible() + ", player visible: " + player.isVisible());
    }
    
    private void startGame() {
        System.out.println("Starting game...");
        gameState = new GameState();
        
        // Start immediately without story thread for debugging
        playerState.mobile = true;
        
        frame.setVisible(true);
        frame.requestFocusInWindow();
        System.out.println("Game started, frame visible");
    }
    
    // Image loading with multiple fallback attempts
    private ImageIcon loadImage(String ref, int scaleX, int scaleY) {
        String[] attempts = {
            ref,                                    // As-is
            "/" + ref,                              // With leading slash
            ref.replace("assets8/", "assets8/"),      // Ensure case
            "src/" + ref,                           // From src folder
            "./" + ref,                             // Relative
            ref.toLowerCase(),                      // Lowercase
            ref.replace(".PNG", ".png").replace(".png", ".PNG") // Try both cases
        };
        
        for (String path : attempts) {
            try {
                System.out.println("  Trying path: " + path);
                java.net.URL url = getClass().getResource(path);
                if (url == null) {
                    url = getClass().getClassLoader().getResource(path);
                }
                if (url == null) {
                    // Try file system
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        url = file.toURI().toURL();
                    }
                }
                
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    if (icon.getImage() != null && icon.getIconWidth() > 0) {
                        // Scale it
                        int w = (FRAME_WIDTH / MAP_WIDTH) * scaleX;
                        int h = (FRAME_HEIGHT / MAP_HEIGHT) * scaleY;
                        Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT);
                        System.out.println("  SUCCESS with: " + path);
                        return new ImageIcon(scaled);
                    }
                }
            } catch (Exception ex) {
                System.out.println("  Failed: " + ex.getMessage());
            }
        }
        
        System.err.println("ALL ATTEMPTS FAILED for: " + ref);
        return null;
    }
    
    // Simple inner classes
    private class PlayerState {
        ImageIcon[] sprites = new ImageIcon[12];
        volatile boolean mobile = false;
        int direction = 3;
        int animationFrame = 0;
        int x = 2, y = 16;
        
        PlayerState() {
            System.out.println("Loading player sprites...");
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    sprites[i * 3 + j] = loadImage("assets8/plr_" + i + j + ".PNG", PLAYER_SIZE, PLAYER_SIZE);
                }
            }
            System.out.println("Sprites loaded");
        }
        
        ImageIcon getCurrentSprite() {
            return sprites[direction * 3 + animationFrame];
        }
        
        void nextFrame() {
            animationFrame = (animationFrame + 1) % 3;
        }
        
        void turnRight() {
            direction = (direction + 1) % 4;
        }
        
        void move(int dx, int dy) {
            x += dx;
            y += dy;
            x = Math.max(0, Math.min(x, MAP_WIDTH - PLAYER_SIZE));
            y = Math.max(0, Math.min(y, MAP_HEIGHT - PLAYER_SIZE));
        }
    }
    
    private class MapData {
        int[] layout = {
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,0,0,0,0,1,0,0,0,0,
            0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
        };
        
        boolean isWall(int x, int y) {
            int idx = y * MAP_WIDTH + x;
            return idx >= 0 && idx < layout.length && layout[idx] == 1;
        }
        
        boolean isTransition(int x, int y) {
            return x >= 28 && x <= 30 && y >= 3 && y <= 5;
        }
    }
    
    private class GameState {
        boolean doorChecked = false;
        boolean wallDoorChecked = false;
        boolean keyPressed = false;
    }
    
    // Key handling
    @Override
    public void keyPressed(KeyEvent evt) {
        if (!playerState.mobile || gameState.keyPressed) return;
        gameState.keyPressed = true;
        
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
        
        // Check transition
        if (mapData.isTransition(playerState.x, playerState.y)) {
            System.out.println("Transition triggered!");
            return;
        }
        
        // Collision
        for (int dx = 0; dx < 2; dx++) {
            for (int dy = 0; dy < 2; dy++) {
                if (mapData.isWall(playerState.x + dx, playerState.y + dy)) {
                    playerState.x = prevX;
                    playerState.y = prevY;
                    break;
                }
            }
        }
        
        // Update visual
        playerState.nextFrame();
        player.setIcon(playerState.getCurrentSprite());
        ((GraphPaperLayout)frame.getContentPane().getLayout())
            .setConstraints(player, new Rectangle(playerState.x, playerState.y, PLAYER_SIZE, PLAYER_SIZE));
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        overlay.repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent evt) {
        gameState.keyPressed = false;
    }
    
    @Override
    public void keyTyped(KeyEvent evt) {}
}
