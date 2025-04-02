package Model;
public class Table {
    private PlayerNode firstPlayer;
    private PlayerNode currentPlayer;

    // Adds a player to the circular linked list
    public void addPlayer(Player player) {
        PlayerNode newNode = new PlayerNode(player);
        if (firstPlayer == null) {
            firstPlayer = newNode;
            firstPlayer.setNext(firstPlayer); // Points to itself (circular)
            currentPlayer = firstPlayer;
        } else {
            PlayerNode temp = firstPlayer;
            while (temp.getNext() != firstPlayer) {
                temp = temp.getNext(); // Traverse to the last node
            }
            temp.setNext(newNode);
            newNode.setNext(firstPlayer); // Points back to the first player (circular)
        }
    }

    // Moves to the next player in the circle
    public void nextTurn() {
        currentPlayer = currentPlayer.getNext();
    }

    // Get the current player
    public Player getCurrentPlayer() {
        return currentPlayer.getPlayer();
    }

    // Displays the players
    public void displayPlayers() {
        PlayerNode temp = firstPlayer;
        do {
            System.out.println(temp.getPlayer().getName());
            temp = temp.getNext();
        } while (temp != firstPlayer);
    }
}
