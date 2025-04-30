package Mechanics;

import Model.Player;
import java.util.Set;

public class Pot {
    private int amount;
    private Set<Player> eligiblePlayers;

    public Pot(int amount, Set<Player> eligiblePlayers) {
        this.amount = amount;
        this.eligiblePlayers = eligiblePlayers;
    }

    public int getAmount() {
        return amount;
    }

    public Set<Player> getEligiblePlayers() {
        return eligiblePlayers;
    }

    public void addAmount(int extra) {
        this.amount += extra;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setEligiblePlayers(Set<Player> eligiblePlayers) {
        this.eligiblePlayers = eligiblePlayers;
    }
    
    @Override
    public String toString() {
    	return "Pot of $" + amount + " between " + eligiblePlayers;
    }
}
