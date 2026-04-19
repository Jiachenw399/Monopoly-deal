# Monopoly Deal

A simplified JavaFX implementation of the _Monopoly Deal_ card game. Two or
more players race to complete three full property sets by banking money,
placing properties, and collecting rent from opponents.

## Features

- Scene-graph JavaFX UI with a main menu, rules page and interactive game screen.
- Clean rules engine that is fully unit-tested and has no UI dependency.
- Deterministic deck (seeded `Random`) supported for reproducible tests.
- Minimum-sufficient-payment rent settlement (no change given).
- Automatic end-of-turn hand-limit enforcement (return excess to the draw pile).

## Game Rules

There are three card categories:

| Category | Details                                                               |
| -------- | --------------------------------------------------------------------- |
| Money    | Denominations `$1M`, `$4M`, `$10M`. Played into the player's bank.    |
| Property | Each card belongs to one of ten colors. Played into the color's pile. |
| Rent     | Targets one opponent in the card's color.                             |

Turn flow:

1. **Draw** two cards at the start of your turn. If your hand is empty, draw
   five instead.
2. **Play** up to one card of each category (at most three plays per turn):
   - Money: goes to your bank.
   - Property: goes to your property area under its color.
   - Rent: legal only if both you and the targeted opponent own at least one
     property of that color. Rent amount = `properties_you_own_of_color × 2`.
3. **End turn.** If your hand has more than seven cards, return the excess to
   the bottom of the draw pile one card at a time.

Paying rent:

- The payer hands over the **minimum subset of bank cards whose total is at
  least the demanded amount**. Ties are broken by fewer cards. No change is
  given — for example, a demand of `$3M` against a bank of a single `$4M`
  card transfers the whole `$4M` card.
- If the payer's bank total is strictly less than the demand, the entire bank
  is transferred (short pay).

Winning:

- The first player to complete three full property sets wins. Set sizes per
  color are defined in `model.PropertyColor`.

## Project Layout

```
src/
  main/java/
    GUI/                 JavaFX views (MonopolyApp, MainMenu, RuleScreen,
                         GameScreen, CardView)
    logic/               Game state machine (Game, GamePhase, RentRequest)
    model/               Cards, deck, player, colors
  test/java/
    logic/               GameTest
    model/               CardTest, DrawPileAndDiscardPileTest, PlayerTest
```

Key entry points:

- `GUI.Main` — boots the JavaFX application.
- `logic.Game` — turn-based state machine used by the GUI and the tests.
- `model.Player` — hand, bank and property management plus rent payment logic.

## Build & Run

Requirements:

- JDK 21 or newer (the project targets Java 25 source/target — adjust in
  `pom.xml` if your JDK is older).
- Maven 3.9+.
- JavaFX 21 (pulled in automatically by the `javafx-maven-plugin`).

Common commands:

```bash
# Compile and run all unit tests
mvn test

# Run the JavaFX application
mvn javafx:run

# Run a single test class
mvn -Dtest=GameTest test
```

## Testing

Unit tests cover the rules engine end-to-end:

- `CardTest` — denominations, categories, property color metadata.
- `DrawPileAndDiscardPileTest` — deck composition, draw/discard/reshuffle,
  return-to-bottom semantics.
- `PlayerTest` — hand/bank/property operations, minimum-sufficient-payment
  subset search, and win detection.
- `GameTest` — draw rules, per-category play limits, rent flow (including the
  opponent-must-own-color precondition), discard phase, and win condition.

All assertions are JUnit 5.
