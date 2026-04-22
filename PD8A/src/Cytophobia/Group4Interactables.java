package Cytophobia;

import java.util.ArrayList;
import java.util.List;

import static Cytophobia.Group4Map1.*;

abstract class Group4Interactables {
    int iIndex = -1;
    List<Integer> iIndexArray = new ArrayList<>();

    Group4Interactables() {
    }

    public void editIndex(int inx, int iny, int wid, int hei){
        for (int x = inx; x < inx + wid; x++) {
            for (int y = iny; y < iny + hei; y++) {
                int i = x + (y * mW);
                if (i<totalGrids) {
                    iIndexArray.add(i);
                    interactionLayout[i] = 1;
                }
            }
        }
    }

    public void checkEvent(Group4Player P){
        if (iIndex != -1) {
            if (iIndex == P.pendingIndex){interactEvent(P);}
        } else {
            if (iIndexArray.contains(P.pendingIndex)){interactEvent(P);}
        }
    }
    
    public void interactEvent(Group4Player playerInteracted){}
}

class Crate extends Group4Interactables {
    boolean keyCrate;

    public Crate(int iInd, boolean k){
        keyCrate = k;
        iIndex = iInd;
    }

    @Override
    public void interactEvent(Group4Player playerInteracted){
        if (!playerInteracted.hasKey){
            if (keyCrate) {
                playerInteracted.hasKey = true;
                new Thread(() -> {
                    try {
                        playerInteracted.Restrict();
                        tWriteDia("Crate", "You found a key.", 1200, 1500);
                        playerInteracted.Unrestrict();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                new Thread(() -> {
                    try {
                        playerInteracted.Restrict();
                        tWriteDia("Crate", "There's nothing here.", 1200, 1500);
                        playerInteracted.Unrestrict();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        }
    }
}

class roomDoor extends Group4Interactables {
    boolean doorUnlocked = false;

    public roomDoor(int iInd){
        iIndex = iInd;
    }

    @Override
    public void interactEvent(Group4Player playerInteracted){
        if (!doorUnlocked){
            if (playerInteracted.hasKey){
                doorUnlocked = true;
                burnLayout(13, 4, 1, 3, Group4Map1.collisionLayout, 0);
                new Thread(() -> {
                    try {
                        playerInteracted.Restrict();
                        tWriteDia("Door", "You unlocked the door.", 1200, 1500);
                        playerInteracted.Restrict();
                        interactableElements[rectangleToIndex(13, 4)].setIcon(null);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                new Thread(() -> {
                    try {
                        playerInteracted.Restrict();
                        tWriteDia("Door", "This door is locked. I need to find the key.", 1200, 1500);
                        playerInteracted.Restrict();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        }
    }
}

class exitDoor extends Group4Interactables {
    public exitDoor(int iInd){
        iIndex = iInd;
    }

    @Override
    public void interactEvent(Group4Player playerInteracted){
        new Thread(() -> {
            try {
                P.Restrict();
                tWriteDia("Door", "You exit the door to another room.", 1200, 1500);
                blackScreen.setVisible(true);
                tWriteDia("", "<html> As you enter the silent room, a feeling of dread encapsulates your body.</html>", 3000, 4000);
                blackScreen.setVisible(false);

                //setup second area
                P.getElements()[P.currentIndex].setIcon(null);
                P.currentIndex = rectangleToIndex(2, 7);
                P.updateCharacterRotation("u");
                P.Restrict();
                mapElements[rectangleToIndex(0,0)].setIcon(background2);
                burnLayout(1,3, 14, 5, collisionLayout, 0);

                Group4Map1.Mon.editIndex(1,5, 3, 1);
                burnLayout(1,5, 3, 1,interactionLayout, 5);
                Group4Map1.Mon.editIndex(4,5, 1, 3);
                burnLayout(4,5, 1, 3,interactionLayout, 5);
                cDoorA.editIndex(3,2, 1, 1);
                cDoorB.editIndex(6,2, 1, 1);
                cDoorC.editIndex(9,2, 1, 1);
                cDoorD.editIndex(12,2, 1, 1);
                burnLayout(3,2, 1, 1,interactionLayout, 5);
                burnLayout(6,2, 1, 1,interactionLayout, 5);
                burnLayout(9,2, 1, 1,interactionLayout, 5);
                burnLayout(12,2, 1, 1,interactionLayout, 5);



            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}



class choiceDoor extends Group4Interactables {
    char Ans;
    public choiceDoor(int iInd, char A){
        Ans = A;
        iIndex = iInd;
    }


    @Override
    public void interactEvent(Group4Player playerInteracted){
        if (Group4Map1.inQuiz) {
            new Thread(() -> {
                try {
                    P.Restrict();
                    if (Ans == quizAns[quizIndex]) {
                        tWriteDia("Tikbalang", "Correct.", 800, 800);
                        quizIndex++;
                        askNext();
                    } else {
                        triesLeft -= 1;
                        if (triesLeft > 0){
                            tWriteDia("Tikbalang", "Wrong. You have " + triesLeft + " chance(s) left.", 1000, 800);
                            askNext();
                        } else {
                            inQuiz = false;

                            tWriteDia("Tikbalang", "You are unfortunate, traveler.", 1500, 1500);

                            tWriteDia("Tikbalang", "Now, accept your doom.", 1500, 1500);

                            blackScreen.setVisible(true);

                            tWriteDia("", "Game over. You have failed.", 3000, 3000);

                        }
                    }
                } catch (InterruptedException ex) {
                }
            }).start();
        }
    }
}


class Monster extends Group4Interactables {
    public Monster(int iInd){
        iIndex = iInd;
    }

    @Override
    public void interactEvent(Group4Player playerInteracted){
        if (!introMonster) {
            introMonster = true;
            P.Restrict();
            blackScreen.setVisible(true);
            new Thread(() -> {
                try {
                    tWait(2000);
                    P.updateCharacterRotation("r");
                    interactableElements[rectangleToIndex(7, 5)].setIcon(monster0);
                    burnLayout(7, 5, 1, 1, collisionLayout, 1);

                    tWriteDia("", "<html>A great monster appears before you. You identify it as one of the famous creatures in Philippine Mythology... the Tikbalang.</html>", 3000, 3000);

                    tWriteDia("", "<html> It looks down on you with great contempt, as if offering you a challenge. </html>", 3000, 3000);

                    tWait(2000);

                    blackScreen.setVisible(false);

                    tWriteDia("Tikbalang", "<html>I have locked all the exits in this house. You are trapped here with me. <html>", 3000, 3000);
                    tWriteDia("Tikbalang", "<html>I will let you escape, but you must answer 10 of my riddles. If you refuse, you shall reside in these walls forever.</html>", 3000, 3000);
                    tWriteDia("Tikbalang", "<html>To answer, you must walk through one of these doors as your choice. (A, B, C, D). Choose wisely, for I will only permit 3 mistakes.</html>", 3000, 3000);

                    startQuiz();

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }
}



