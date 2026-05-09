package GUI;

import model.Card;
import model.Player;
import model.PropertiesCards;
import model.PropertyColor;

import java.util.ArrayList;

public class PlayerInfoHelper {
    public static int getBankTotal(Player player) {
        int total = 0;

        for (Card card : player.getBankCards()) {
            total += card.getValue();
        }

        return total;
    }

    public static int getCompletedSetCount(Player player) {
        int completedSets = 0;

        for (PropertyColor color : PropertyColor.values()) {
            int current = getPropertyCountByCurrentColor(player, color);

            if (current >= color.getAmountToCompleteSet()) {
                completedSets++;
            }
        }

        return completedSets;
    }

    public static int getPropertyCountByCurrentColor(Player player, PropertyColor color) {
        int count = 0;

        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color) {
                count++;
            }
        }

        return count;
    }

    public static int getPropertyCountByColor(Player player, PropertyColor color) {
        return getPropertyCountByCurrentColor(player, color);
    }

    public static boolean hasPropertyColor(Player player, PropertyColor color) {
        return getPropertyCountByCurrentColor(player, color) > 0;
    }

    public static boolean hasHouse(Player player, PropertyColor color) {
        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color && card.hasHouse()) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasHotel(Player player, PropertyColor color) {
        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color && card.hasHotel()) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<PropertiesCards> getCompleteSetByColor(Player player, PropertyColor color) {
        ArrayList<PropertiesCards> result = new ArrayList<>();

        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color) {
                result.add(card);
            }
        }

        if (result.size() >= color.getAmountToCompleteSet()) {
            return result;
        }

        return new ArrayList<>();
    }

    public static boolean canBeStolenBySlyDeal(Player targetPlayer, PropertiesCards card) {
        PropertyColor color = card.getCurrentColor();

        if (color == null) {
            return true;
        }

        int count = getPropertyCountByCurrentColor(targetPlayer, color);
        return count < color.getAmountToCompleteSet();
    }

    public static String getShortColorName(PropertyColor color) {
        return switch (color) {
            case DARK_BLUE -> "D.BLUE";
            case ORANGE -> "ORANGE";
            case BLACK -> "BLACK";
            case RED -> "RED";
            case DARK_GREEN -> "D.GREEN";
            case BROWN -> "BROWN";
            case PINK -> "PINK";
            case LIGHT_BLUE -> "L.BLUE";
            case LIGHT_GREEN -> "L.GREEN";
            case YELLOW -> "YELLOW";
        };
    }
}
