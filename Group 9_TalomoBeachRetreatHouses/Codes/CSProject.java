/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cs.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Members: Baguio, Banga, Tongcua

public class CSProject extends JFrame {

   public CSProject(){
       setTitle("CS Project");
       setSize(450, 450);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       
       ImageIcon bgIcon = new ImageIcon(getClass().getResource("BG1.png"));
       Image bgImage = bgIcon.getImage();
       
       
       JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        
        add(backgroundPanel);
        
        setVisible(true);
   }
    
    public static void main(String[] args) {
        new CSProject();
    }
    
}
