package GUI;

import 逻辑类.Game;

public class Main {
    static void main() {
        Game g = new Game();
        System.out.println(g.getDrawCards().getDrawCards().getFirst().getValue());
    }
}
