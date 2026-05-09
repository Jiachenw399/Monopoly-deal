package logic;

import model.Player;
import model.PropertiesCards;
import model.PropertyColor;

public class RentCalculator {
    public int calculateRent(Player player, PropertyColor color) {
        int propertyCount = countPropertiesByColor(player, color);
        int buildingRent = calculateBuildingRent(player, color);

        return propertyCount + buildingRent;
    }

    private int countPropertiesByColor(Player player, PropertyColor color) {
        int count = 0;

        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color) {
                count++;
            }
        }

        return count;
    }

    private int calculateBuildingRent(Player player, PropertyColor color) {
        int rent = 0;

        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color) {
                if (card.hasHouse()) {
                    rent += 3;
                }

                if (card.hasHotel()) {
                    rent += 4;
                }
            }
        }

        return rent;
    }
}