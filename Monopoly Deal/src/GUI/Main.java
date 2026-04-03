package GUI;

import 基础类.ActionCards;
import 基础类.MoneyCards;
import 基础类.PropertiesCards;
import 逻辑类.Game;

public class Main {
    static void main() {
        Game g = new Game();
        MoneyCards c = new MoneyCards(5);
        MoneyCards c2 = new MoneyCards(3);
        g.getPlayers().getFirst().getBankCards().add(c);
        g.getPlayers().getFirst().getBankCards().add(c2);
        g.getPlayers().get(1).takeMoney(3,g.getPlayers().get(0));//GUI
    }
}
