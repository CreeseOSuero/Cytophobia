package Cytophobia;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static Cytophobia.Group4Map1.*;

public class Group4Player {
    public int startPositionX = 1;  public int startPositionY = 3; //start position of player
    public int currentIndex = Group4Map1.rectangleToIndex(startPositionX,startPositionY);
    public int pendingIndex;
    private boolean canMove = true;
    public boolean hasKey;
    private int characterMode = 0;
    public  static boolean Pressed = false;

    JLabel[] Elements = new JLabel[Group4Map1.totalGrids];
    int[] Layout = new int[Group4Map1.totalGrids];

    public JLabel[] getElements(){
        return Elements;
    }

    public int[] getLayout(){
        return  Layout;
    }

    public void Move(KeyEvent e){
        if (!Pressed && canMove){
            Pressed = true;
            if(e.getKeyCode()== KeyEvent.VK_RIGHT){
                Elements[currentIndex].setIcon(null);
                pendingIndex = currentIndex+1;
                if (!(Group4Map1.collisionLayout[pendingIndex] == 1)){
                    currentIndex = pendingIndex;
                }
                if (Group4Map1.interactionLayout[pendingIndex] != 0){
                    interactionEvent();
                } else if (Group4Map1.interactionLayout[currentIndex] != 0) {
                    interactionEvent();
                }
                updateCharacterRotation("r");
            }
            else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                Elements[currentIndex].setIcon(null);
                pendingIndex = currentIndex-1;
                if (!(Group4Map1.collisionLayout[pendingIndex] == 1)){
                    currentIndex = pendingIndex;
                }
                if (Group4Map1.interactionLayout[pendingIndex] != 0){
                    interactionEvent();
                } else if (Group4Map1.interactionLayout[currentIndex] != 0) {
                    interactionEvent();
                }
                updateCharacterRotation("l");
            }
            else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                Elements[currentIndex].setIcon(null);
                pendingIndex = currentIndex+mW;
                if (!(Group4Map1.collisionLayout[pendingIndex] == 1)){
                    currentIndex = pendingIndex;
                }
                if (Group4Map1.interactionLayout[pendingIndex] != 0){
                    interactionEvent();
                } else if (Group4Map1.interactionLayout[currentIndex] != 0) {
                    interactionEvent();
                }
                updateCharacterRotation("d");
            }
            else if(e.getKeyCode()==KeyEvent.VK_UP){
                Elements[currentIndex].setIcon(null);
                pendingIndex = currentIndex-mW;
                if (!(Group4Map1.collisionLayout[pendingIndex] == 1)){
                    currentIndex = pendingIndex;
                }
                if (Group4Map1.interactionLayout[pendingIndex] != 0){
                    interactionEvent();
                } else if (Group4Map1.interactionLayout[currentIndex] != 0) {
                    interactionEvent();
                }
                updateCharacterRotation("u");
            }
        }
    }

    public void updateCharacterRotation(String dir){
        pendingIndex = 0;
        switch (dir) {
            case "r" -> {
                if (characterMode == 0) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pconright_1);
                } else if (characterMode == 1) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pconright_2);
                } else {
                    characterMode = 0;
                    Elements[currentIndex].setIcon(Group4Map1.pconright_0);
                }
            }
            case "l" -> {
                if (characterMode == 0) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pconleft_1);
                } else if (characterMode == 1) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pconleft_2);
                } else {
                    characterMode = 0;
                    Elements[currentIndex].setIcon(Group4Map1.pconleft_0);
                }
            }
            case "u" -> {
                if (characterMode == 0) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pconup_1);
                } else if (characterMode == 1) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pconup_2);
                } else {
                    characterMode = 0;
                    Elements[currentIndex].setIcon(Group4Map1.pconup_0);
                }
            }
            case null, default -> {
                if (characterMode == 0) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pcondown_1);
                } else if (characterMode == 1) {
                    characterMode++;
                    Elements[currentIndex].setIcon(Group4Map1.pcondown_2);
                } else {
                    characterMode = 0;
                    Elements[currentIndex].setIcon(Group4Map1.pcondown_0);
                }
            }
        }
    }


    public void Restrict(){
        canMove = false;
    }

    public void Unrestrict(){
        canMove = true;
    }
}
