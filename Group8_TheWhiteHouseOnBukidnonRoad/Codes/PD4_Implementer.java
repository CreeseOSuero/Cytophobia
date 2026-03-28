/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Codes;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author AIDEN
 */
public class PD4_Implementer {
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> {
                try {
                    new PD4_GUI();
                } catch (Exception ex) {
                    System.err.println("Fatal error in main lambda: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to start game: " + ex.getMessage());
                }
            });
        } catch (Exception ex) {
            System.err.println("Fatal error in main: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start game: " + ex.getMessage());
        }
    }
}

