package model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import logic.Game;
import model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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

    @Test
    public void testTakeMoney_AllMoneyTaken() {
        Player playerA = new Player(new DrawPileAndDiscardPile());
        Player playerB = new Player(new DrawPileAndDiscardPile());

        playerB.getBankCards().add(new MoneyCards(5));
        playerB.getBankCards().add(new MoneyCards(2));
        playerB.getBankCards().add(new MoneyCards(3));

        int totalBefore = 10;
        playerA.takeMoney(totalBefore, playerB);

        assertEquals(0, playerB.getBankCards().size(), "对手银行区应该被清空");
        assertEquals(3, playerA.getBankCards().size(), "应该获得所有3张钱卡");

        int totalReceived = playerA.getBankCards().stream()
                .mapToInt(Card::getValue)
                .sum();
        assertEquals(10, totalReceived, "应该收到总共10M$");
    }

    @Test
    public void testTakeMoney_ExactAmount() {
        Player playerA = new Player(new DrawPileAndDiscardPile());
        Player playerB = new Player(new DrawPileAndDiscardPile());

        playerB.getBankCards().add(new MoneyCards(5));
        playerB.getBankCards().add(new MoneyCards(2));
        playerB.getBankCards().add(new MoneyCards(3));

        playerA.takeMoney(7, playerB);

        int remainingTotal = playerB.getBankCards().stream()
                .mapToInt(Card::getValue)
                .sum();
        assertTrue(remainingTotal >= 0, "剩余金额应该>=0");

        int takenTotal = playerA.getBankCards().stream()
                .mapToInt(Card::getValue)
                .sum();
        assertEquals(7, takenTotal, "应该恰好收取7M$");
    }

    @Test
    public void testTakeMoney_PartialPayment() {
        Player playerA = new Player(new DrawPileAndDiscardPile());
        Player playerB = new Player(new DrawPileAndDiscardPile());

        playerB.getBankCards().add(new MoneyCards(10));
        playerB.getBankCards().add(new MoneyCards(5));
        playerB.getBankCards().add(new MoneyCards(2));
        playerB.getBankCards().add(new MoneyCards(1));

        playerA.takeMoney(7, playerB);

        int takenTotal = playerA.getBankCards().stream()
                .mapToInt(Card::getValue)
                .sum();
        assertEquals(7, takenTotal, "应该收取7M$");

        int remainingTotal = playerB.getBankCards().stream()
                .mapToInt(Card::getValue)
                .sum();
        assertEquals(11, remainingTotal, "对手应该剩余11M$");
    }

    @Test
    public void testTakeMoney_OptimalCombination() {
        Player playerA = new Player(new DrawPileAndDiscardPile());
        Player playerB = new Player(new DrawPileAndDiscardPile());

        playerB.getBankCards().add(new MoneyCards(5));
        playerB.getBankCards().add(new MoneyCards(3));
        playerB.getBankCards().add(new MoneyCards(2));
        playerB.getBankCards().add(new MoneyCards(1));

        playerA.takeMoney(5, playerB);

        int takenTotal = playerA.getBankCards().stream()
                .mapToInt(Card::getValue)
                .sum();
        assertEquals(5, takenTotal, "应该收取5M$");
        assertEquals(1, playerA.getBankCards().size(), "最优解应该是只拿一张5M$的卡");
    }

    @Test
    public void testTakeMoney_EmptyBank() {
        Player playerA = new Player(new DrawPileAndDiscardPile());
        Player playerB = new Player(new DrawPileAndDiscardPile());

        playerA.takeMoney(5, playerB);

        assertEquals(0, playerA.getBankCards().size(), "对手没钱时不应该获得任何卡");
        assertEquals(0, playerB.getBankCards().size(), "对手银行区应该仍为空");
    }

    @Test
    public void testTakeMoney_SingleCard() {
        Player playerA = new Player(new DrawPileAndDiscardPile());
        Player playerB = new Player(new DrawPileAndDiscardPile());

        playerB.getBankCards().add(new MoneyCards(10));

        playerA.takeMoney(5, playerB);

        assertEquals(1, playerA.getBankCards().size(), "应该获得1张卡");
        assertEquals(10, playerA.getBankCards().get(0).getValue(), "应该获得10M$的卡");
        assertEquals(0, playerB.getBankCards().size(), "对手银行区应该被清空");
    }
}

