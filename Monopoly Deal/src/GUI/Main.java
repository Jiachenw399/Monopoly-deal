package GUI;

import 基础类.DrawCards;

public class Main {
    static void main() {
        DrawCards dc = new DrawCards();
        System.out.println(dc.getDrawCards().getFirst().getValue());
        System.out.println(dc.getDrawCards().getLast().getValue());
        System.out.println(dc.getDrawCards().size());
    }
}
