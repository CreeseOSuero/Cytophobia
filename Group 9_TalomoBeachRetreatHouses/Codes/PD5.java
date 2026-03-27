/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pd5;

import javax.swing.*;
import java.awt.*;

//Banga, Baguio, Tongcua

public class PD5 extends JPanel{

    int tileSize = 80;
    
    int[][] map = {
            {1,1,1,1,1,1,1,1,1,1},
            {6,3,0,0,0,0,0,5,3,1},
            {6,5,0,4,0,0,0,0,0,7},
            {1,0,4,0,0,0,0,0,0,1},
            {7,0,0,0,0,0,4,0,0,6},
            {1,1,2,2,0,0,2,2,1,1}
    };
    
    Image floor1, wall1,window1;
    Image Fbed, Fblood, Ftable, Wblood, Wspider;
    
    public PD5() {
        floor1 = new ImageIcon("images/Floortile.png").getImage();
        wall1 = new ImageIcon("images/Walltile.png").getImage();
        window1 = new ImageIcon("images/Wallwithwindow.png").getImage();
        Fbed = new ImageIcon("images/Floorwithbed.png").getImage();
        Fblood = new ImageIcon("images/Floortilewithblood.png").getImage();
        Ftable = new ImageIcon("images/Floorwithtable.png").getImage();
        Wblood = new ImageIcon("images/Wallwithblood.png").getImage();
        Wspider = new ImageIcon("images/Wallwithspider.png").getImage();
        
        JFrame frame = new JFrame("PD5");
        frame.setSize(map[0].length * tileSize + 16,
                      map.length * tileSize + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {

                Image tile = floor1;

                if (map[row][col] == 1) tile = wall1;
                else if (map[row][col] == 2) tile = window1;
                else if (map[row][col] == 3) tile = Fbed;
                else if (map[row][col] == 4) tile = Fblood;
                else if (map[row][col] == 5) tile = Ftable;
                else if (map[row][col] == 6) tile = Wblood;
                else if (map[row][col] == 7) tile = Wspider;

                g.drawImage(tile, col * tileSize, row * tileSize,
                        tileSize, tileSize, this);
            }
        }
    }
    
    public static void main(String[] args) {
        new PD5();
    }
}
