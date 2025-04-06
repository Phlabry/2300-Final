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
    
    public void removePlayer(Player player) {
        if (firstPlayer == null) return;

        PlayerNode temp = firstPlayer;
        PlayerNode prev = null;

        do {
            if (temp.getPlayer().equals(player)) {
                if (temp == firstPlayer) {
                    // Only one player
                    if (firstPlayer.getNext() == firstPlayer) {
                        firstPlayer = null;
                        currentPlayer = null;
                    } else {
                        // Remove firstPlayer and find last node to update its next
                        PlayerNode last = firstPlayer;
                        while (last.getNext() != firstPlayer) {
                            last = last.getNext();
                        }
                        firstPlayer = firstPlayer.getNext();
                        last.setNext(firstPlayer);
                        if (currentPlayer == temp) {
                            currentPlayer = firstPlayer;
                        }
                    }
                } else {
                    prev.setNext(temp.getNext());
                    if (currentPlayer == temp) {
                        currentPlayer = temp.getNext();
                    }
                }
                playerAmount--;
                System.out.println(player.getName() + " has been eliminated from the game.");
                return;
            }
            prev = temp;
            temp = temp.getNext();
        } while (temp != firstPlayer);
    }


    
}
