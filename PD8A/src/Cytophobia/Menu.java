package Cytophobia;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Paths;
import javax.swing.*;

public class Menu implements ActionListener {
    JFrame frame;
    JLabel bg;
    JButton btnNewGame;
    JButton btnContinue;
    JButton btnHelp;
    JButton btnBack;
    JButton btnPrev;
    JButton btnNext;
    JButton lvlTitle;
    JLabel guide;
    JLabel semiOpaqueBg;
    
    int frameHeight = 480;
    int frameWidth = 720;
    int layoutH = 12;
    int layoutW = 18;
    int btnW = 4; // amount of grids taken
    int btnH = 1; // same thing
    int btnVSplit = 1; // amount of grids inbetween buttons
    int btnsYOffset = 5; // offset from top of the screen to 1st button
    
    public static int curLvl = 1;
    public static int lvlUnlocked = 1;
    public static String[] lvlNames = {
        "Durian Hotel",
        "Haunted House on Champaca Street",
        "Bangoy International Airport",
        "White Moncado Mansion",
        "Bunkhouse in Tugbok",
        "Panabo Haunted School",
        "Palm Drive",
        "White House on Bukidnon Road",
        "Talomo Beach Retreat House",
        "Final Nightmare"
    };
    
    public ImageIcon loadImg(String path, int scaleW, int scaleH) {
        return new ImageIcon((new ImageIcon(path)).getImage().getScaledInstance((frameWidth/layoutW) * scaleW,
                (frameHeight/layoutH) * scaleH,
                Image.SCALE_DEFAULT));
    } 
    
    void menuToggle(boolean f) {
        semiOpaqueBg.setVisible(!f);
        btnNewGame.setVisible(f);
        btnContinue.setVisible(f);
        btnHelp.setVisible(f);
        btnBack.setVisible(!f);
    }
    void setContOff() {
        btnPrev.setVisible(false);
        btnNext.setVisible(false);
        lvlTitle.setVisible(false);
        guide.setVisible(false);
    }
    
    void updCont() {
        lvlTitle.setVisible(true);
        if(curLvl == 1) btnPrev.setVisible(false);
        else btnPrev.setVisible(true);
        if(curLvl == 10) btnNext.setVisible(false);
        else if(curLvl == lvlUnlocked) btnNext.setVisible(false);
        else btnNext.setVisible(true);
        lvlTitle.setText(lvlNames[curLvl-1]);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnNewGame) {
            try {
                File f = new File(resolveFilePath("lvlunlocked.dat"));
                if(f.exists()) {
                    f.delete();
                }
            } catch (Exception ex) {
                System.out.println("Unable to reset level unlocked");
            }
            curLvl = 1;
            lvlUnlocked = 1;
            startLevel(curLvl);
            frame.dispose();
        } else if(e.getSource() == btnContinue) {
            menuToggle(false);
            updCont();
        } else if(e.getSource() == btnHelp) {
            guide.setVisible(true);
            menuToggle(false);
        } else if(e.getSource() == btnBack) {
            setContOff();
            menuToggle(true);
        } else if(e.getSource() == btnNext) {
            curLvl++;
            updCont();
        } else if(e.getSource() == btnPrev) {
            curLvl--;
            updCont();
        } else if(e.getSource() == lvlTitle) {
            startLevel(curLvl);
            frame.dispose();
        }
        
    }
    
    public void allowTransparentBg(JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
    }
    
    public Menu() {
        getLvlUnlocked();
        frame = new JFrame();
        btnNewGame = new JButton(loadImg("assets0/bt_newgame.png", btnW, btnH)); allowTransparentBg(btnNewGame);
        btnContinue = new JButton(loadImg("assets0/bt_continue.png", btnW, btnH)); allowTransparentBg(btnContinue);
        btnHelp = new JButton(loadImg("assets0/bt_help.png", btnW, btnH)); allowTransparentBg(btnHelp);
        btnBack = new JButton(loadImg("assets0/bt_back.png", btnW, btnH)); allowTransparentBg(btnBack);
        btnPrev = new JButton("<"); allowTransparentBg(btnPrev);
        btnNext = new JButton(">"); allowTransparentBg(btnNext);
        guide = new JLabel(loadImg("assets0/guide.gif", layoutW, layoutH));
        btnPrev.setForeground(new Color(100,0,0));
        btnNext.setForeground(new Color(100,0,0));
        lvlTitle = new JButton(""); allowTransparentBg(lvlTitle);
        lvlTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lvlTitle.setForeground(new Color(100,0,0));
        lvlTitle.setFont(new Font("Serif", Font.BOLD, 30));
        bg = new JLabel(loadImg("assets0/bg_menu.gif", layoutW, layoutH));
        semiOpaqueBg = new JLabel();
        semiOpaqueBg.setBackground(new Color(0,0,0,225));
        semiOpaqueBg.setOpaque(true);
        
    }
    public void setFrame() {
        frame.setLayout(new GraphPaperLayout(new Dimension(layoutW, layoutH)));
        
        int btnX = (layoutW - btnW)/2; // layoutW & btnW must have same parity (odd-odd or even-even)
        
        frame.add(btnNewGame, new Rectangle(btnX, btnsYOffset, btnW, btnH));
        frame.add(btnContinue, new Rectangle(btnX, btnsYOffset + (btnH + btnVSplit), btnW, btnH));
        frame.add(btnHelp, new Rectangle(btnX, btnsYOffset + (btnH + btnVSplit)*2, btnW, btnH));
        frame.add(btnBack, new Rectangle(btnX, layoutH-2, btnW, btnH));
        frame.add(guide, new Rectangle(0,0,layoutW, layoutH));
        frame.add(btnPrev, new Rectangle(0, 5, 2, 1));
        frame.add(btnNext, new Rectangle(layoutW-2, 5, 2, 1));
        frame.add(lvlTitle, new Rectangle(2, 5, layoutW-4, 1));
        btnPrev.setFont(new Font("Serif", Font.BOLD, 24));
        btnNext.setFont(new Font("Serif", Font.BOLD, 24));
        frame.add(semiOpaqueBg, new Rectangle(0,0,layoutW, layoutH));
        semiOpaqueBg.setVisible(false);
        frame.add(bg, new Rectangle(0, 0, layoutW, layoutH));;
        
        btnNewGame.addActionListener(this);
        btnContinue.addActionListener(this);
        btnHelp.addActionListener(this);
        btnBack.addActionListener(this);
        btnBack.setVisible(false);
        btnNext.setVisible(false);
        btnPrev.setVisible(false);
        guide.setVisible(false);
        btnNext.addActionListener(this);
        btnPrev.addActionListener(this);
        lvlTitle.addActionListener(this);
        lvlTitle.setVisible(false);
        
        frame.getContentPane().setPreferredSize(new Dimension(frameWidth, frameHeight));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
    }
    
    public static String resolveFilePath(String path) {
        try {
            File jarF = new File(Menu.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String jarDir = jarF.getParent();
            if(jarF.isFile()) {
                return Paths.get(jarF.getParent()).resolve(path).toString();
            } else return path;
        } catch (Exception e) {
            return path;
        }
    }
    
    public static void getLvlUnlocked() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(resolveFilePath("lvlunlocked.dat"))));
            lvlUnlocked = Integer.parseInt(reader.readLine());
            curLvl = lvlUnlocked;
        } catch (IOException e) {
            lvlUnlocked = 1;
            curLvl = 1;
            setLvlUnlocked(1);
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch(IOException e) {
                
            }
        }
    }
    public static void setLvlUnlocked(int lvlset) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(resolveFilePath("lvlunlocked.dat"))));
            writer.write(String.valueOf(lvlset));
            writer.newLine();
        } catch(IOException e) {
            System.out.println("Error: Unable to log lvl unlocked.");
        } finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {}
            
        }
    }
    
    public static void startLevel(int lvl) {
        switch(lvl) {
            case 1: { (new Group1Map()).setFrame();} break;
            case 2: { (new Group2Map1()).setFrame();} break;
            case 3: { (new Group3Map1()).setFrame();} break;
            case 4: { new Group4Map1();} break;
            case 5: { (new Group5Map1()).setFrame();} break;
            case 6: {new Group6Map1();} break;
            case 7: { (new Group7Map1()).setFrame();} break;
            case 8: { new Group8Map1();} break;
            case 9: { new Group9Map1();} break;
            case 10: { (new Group10Map1()).setFrame();} break;
        }
    }
    
    public static void startNextLevel(int lvl) {
        if(lvl != 10) {
            getLvlUnlocked();
            if(lvl == lvlUnlocked) {
                setLvlUnlocked(++lvlUnlocked);
                curLvl = lvlUnlocked;
            }
            int result = JOptionPane.showConfirmDialog(
                null,
                "Do you want to continue?",
                "Cytophobia",
                JOptionPane.YES_NO_OPTION
             );

            if (result == JOptionPane.YES_OPTION) {
                startLevel(curLvl);
            } else {
                (new Menu()).setFrame();
            }
        } else (new Menu()).setFrame();
        
    }
    
    public static void main(String[] args) {
        Menu m = new Menu();
        m.setFrame();
        
    }
    
}
