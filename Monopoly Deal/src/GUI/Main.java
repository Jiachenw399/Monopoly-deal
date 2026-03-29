package GUI;

import 基础类.ActionCards;
import 基础类.Player;
import 基础类.PropertiesCards;
import 逻辑类.Game;

public class Main {
    static void main() {
        Game g = new Game();
        int a = 0;
        System.out.println(g);
        for (int i = 0; i < g.getDrawCards().getDrawCards().size(); i++) {
            if(g.getDrawCards().getDrawCards().get(i).getClass().equals(ActionCards.class)) {
                a+=1;
            }
            if(g.getDrawCards().getDrawCards().get(i).getClass().equals(PropertiesCards.class)) {
                a+=1;
            }
        }
        System.out.println(a);
    }
}
