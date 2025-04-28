package Mechanics;

import Model.Player;
import java.util.Set;

public class Pot {
    private int amount;
    private Set<Player> contenders;

    public Pot(int amount, Set<Player> contenders) {
        this.amount = amount;
        this.contenders = contenders;
    }

    public int getAmount() {
        return amount;
    }

    public Set<Player> getContenders() {
        return contenders;
    }

    public void addToAmount(int extra) {
        this.amount += extra;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setContenders(Set<Player> contenders) {
        this.contenders = contenders;
    }
    
    @Override
    public String toString() {
    	return "Pot of $" + amount + " between " + contenders;
    }
}
