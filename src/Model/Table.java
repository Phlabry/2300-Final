package Model;
public class Table {
    private PlayerNode firstPlayer;
    private PlayerNode currentPlayer;

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
    }

    public void nextTurn() {
        currentPlayer = currentPlayer.getNext();
    }

    public Player getCurrentPlayer() {
        return currentPlayer.getPlayer();
    }

    public void displayPlayers() {
        PlayerNode temp = firstPlayer;
        do {
            System.out.println(temp.getPlayer().getName());
            temp = temp.getNext();
        } while (temp != firstPlayer);
    }
}
