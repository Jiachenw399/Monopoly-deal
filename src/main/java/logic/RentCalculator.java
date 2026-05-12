package logic;

import model.Player;
import model.PropertiesCards;
import model.PropertyColor;

public class RentCalculator {
    public int calculateRent(Player player, PropertyColor color) {
        int propertyCount = countPropertiesByColor(player, color);

        if (propertyCount == 0) {
            return 0;
        }

        int baseRent = calculateBaseRent(color, propertyCount);
        int buildingRent = calculateBuildingRent(player, color);

        return baseRent + buildingRent;
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

    private int calculateBaseRent(PropertyColor color, int propertyCount) {
        int safeCount = Math.min(propertyCount, color.getAmountToCompleteSet());

        return switch (color) {
            case BROWN -> switch (safeCount) {
                case 1 -> 1;
                default -> 2;
            };

            case LIGHT_BLUE -> switch (safeCount) {
                case 1 -> 1;
                case 2 -> 2;
                default -> 3;
            };

            case PINK, ORANGE -> switch (safeCount) {
                case 1 -> 1;
                case 2 -> 2;
                default -> 4;
            };

            case RED, YELLOW -> switch (safeCount) {
                case 1 -> 2;
                case 2 -> 3;
                default -> 6;
            };

            case BLACK, LIGHT_GREEN -> switch (safeCount) {
                case 1 -> 1;
                case 2 -> 2;
                case 3 -> 3;
                default -> 4;
            };

            case DARK_GREEN -> switch (safeCount) {
                case 1 -> 2;
                case 2 -> 4;
                default -> 7;
            };

            case DARK_BLUE -> switch (safeCount) {
                case 1 -> 3;
                default -> 8;
            };
        };
    }//still need to modified

    private int calculateBuildingRent(Player player, PropertyColor color) {
        int rent = 0;

        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color) {
                if (card.hasHouse()) {
                    rent += 3;
                    break;
                }
            }
        }

        for (PropertiesCards card : player.getPropertyCards()) {
            if (card.getCurrentColor() == color) {
                if (card.hasHotel()) {
                    rent += 4;
                    break;
                }
            }
        }

        return rent;
    }
}