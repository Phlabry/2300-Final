package Model;

public class PlayerNode {
    private Player player;
    private PlayerNode next;

    public PlayerNode(Player player) {
        this.player = player;
        this.next = null;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerNode getNext() {
        return next;
    }

    public void setNext(PlayerNode next) {
        this.next = next;
    }
}
