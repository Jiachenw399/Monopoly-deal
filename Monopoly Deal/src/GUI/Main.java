package GUI;

import 基础类.ActionCards;
import 基础类.MoneyCards;
import 基础类.PropertiesCards;
import 逻辑类.Game;

public class Main {
    static void main() {
        Game g = new Game();
        int a = 0;
        for (int i = 0; i < g.getDrawCards().getDrawPile().size(); i++) {
            if(g.getDrawCards().getDrawPile().get(i).getClass().equals(ActionCards.class)) {
                a+=1;
            }
        }
        System.out.println(a);
    }
}
