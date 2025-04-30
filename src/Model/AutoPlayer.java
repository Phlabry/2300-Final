//Autoplayer with all their attributes of player: money, deck, etc.
package Model;

import java.util.Random;

public class AutoPlayer extends Player {
    public AutoPlayer(String name, int startingMoney, boolean isHuman) {
        super(name, startingMoney, isHuman);
    }

    public String decideAction() {
    	int rand = new Random().nextInt(100); // 0 to 99

        if (rand < 90) {
            return "call"; //90% chance
        } else {
            return "fold"; //10% chance
        }
    }
}