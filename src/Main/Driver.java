// driver
package Main;
import Model.*;
import Mechanics.*;
import java.util.*;

public class Driver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Player> players = new ArrayList<>();
        System.out.println("Enter number of players:");
        int numPlayers = scanner.nextInt();

        for (int i = 0; i < numPlayers; i++) {
            scanner.nextLine();
            System.out.println("Enter name for Player " + (i + 1) + ":");
            String name = scanner.nextLine();
            System.out.println("Enter starting money for " + name + ":");
            int money = scanner.nextInt();
            players.add(new Player(name, money));
        }

        PokerGame game = new PokerGame(players, scanner);
        game.startGame();
        
        scanner.close();
    }
}
