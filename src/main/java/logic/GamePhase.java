package logic;

/**
 * High-level turn phase used by the GUI and the {@link Game} state machine.
 */
public enum GamePhase {
    /** Start-of-turn card draw happens here automatically. */
    DRAW,
    /** Current player may play up to one money, one property and one rent card. */
    PLAY,
    /** A rent has been played; the targeted opponent must pay before play resumes. */
    AWAITING_PAYMENT,
    /** End of turn triggered with >7 hand cards; player must return the excess. */
    DISCARD,
    /** Game finished, a winner is available through {@link Game#getWinner()}. */
    GAME_OVER
}
