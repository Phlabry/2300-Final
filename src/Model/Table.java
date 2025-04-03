package Model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private PlayerNode firstPlayer;
    private PlayerNode currentPlayer;
    private int playerAmount;

    public void addPlayer(Player player) {
        PlayerNode newNode = new PlayerNode(player);
        if (firstPlayer == null) {
            firstPlayer = newNode;
            firstPlayer.setNext(firstPlayer); 
            currentPlayer = firstPlayer;
        } else {
            PlayerNode temp = firstPlayer;
            while (temp.getNext() != firstPlayer) {
                temp = temp.getNext(); 
            }
            temp.setNext(newNode);
            newNode.setNext(firstPlayer); 
        }
        
        playerAmount++;
    }

    public void nextTurn() {
        currentPlayer = currentPlayer.getNext();
    }

    public Player getCurrentPlayer() {
        return currentPlayer.getPlayer();
    }
    
    public Player getFirstPlayer() {
        return firstPlayer.getPlayer();
    }

    public void displayPlayers() {
        PlayerNode temp = firstPlayer;
        do {
            System.out.println(temp.getPlayer().getName());
            temp = temp.getNext();
        } while (temp != firstPlayer);
    }
    
    public List<Player> getPlayers() {
        List<Player> playerList = new ArrayList<>();
        PlayerNode temp = firstPlayer;
        if (temp != null) {
            do {
                playerList.add(temp.getPlayer());
                temp = temp.getNext();
            } while (temp != firstPlayer);
        }
        return playerList;
    }
    
    public int getPlayerAmount() {
    	return this.playerAmount;
    }
}
