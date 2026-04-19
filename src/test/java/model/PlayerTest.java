package model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void testSomething() {
        // 1. Setup（准备）
        Player p = new Player(new DrawPileAndDiscardPile());

        // 2. Call（调用）
        p.takeCard(6);

        // 3. Assert（验证）
        assertEquals(1, p.getHandCards().size());
    }
}
