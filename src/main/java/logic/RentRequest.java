package logic;

import model.Player;
import model.PropertyColor;

/**
 * Immutable description of a pending rent payment.
 */
public final class RentRequest {
    private final Player collector;
    private final Player payer;
    private final PropertyColor color;
    private final int amount;

    public RentRequest(Player collector, Player payer, PropertyColor color, int amount) {
        this.collector = collector;
        this.payer = payer;
        this.color = color;
        this.amount = amount;
    }

    public Player getCollector() { return collector; }
    public Player getPayer() { return payer; }
    public PropertyColor getColor() { return color; }
    public int getAmount() { return amount; }
}
