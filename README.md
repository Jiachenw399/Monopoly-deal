# Monopoly Deal (JavaFX Project)

## 1. Project Overview
This project is a digital implementation of the Monopoly Deal card game.  
It supports local multiplayer gameplay for 2–4 players and focuses on core game mechanics including card drawing, playing cards, rent collection, and win condition checking.

The system is implemented in Java using JavaFX for the graphical user interface.

---

## 2. Objective of the Game
The objective of the game is to be the first player to collect **three complete property sets**, each of a different colour.

---

## 3. Game Setup
- Remove reference cards from the deck
- Shuffle all remaining cards
- Deal **5 cards** to each player
- Place the remaining cards face down as the **draw pile**
- Leave space for a **discard pile**

---

## 4. Game Flow

### 4.1 Turn Structure
Each turn consists of the following steps:

1. **Draw Phase**
    - Draw **2 cards**
    - If the player starts with **0 cards**, draw **5 cards instead**

2. **Action Phase**
    - Play up to **3 cards**
    - Players may choose to play fewer or no cards

3. **Discard Phase**
    - Maximum hand size is **7 cards**
    - If more than 7 cards, discard excess cards

---

## 5. Card Types

### 5.1 Money Cards
- Stored in the **bank**
- Used to pay other players

---

### 5.2 Property Cards
- Placed in front of the player
- Cannot be stored in the bank
- Used to form complete sets

---

### 5.3 Wild Property Cards
- Can represent multiple colours
- Can be reassigned during the player’s turn
- Cannot be used as money

---

### 5.4 Action Cards
Includes:
- Rent cards (charge rent)
- Steal property
- Deal breaker
- Birthday cards (collect from all players)
- Just Say No (cancel actions)

---

## 6. Payment Rules
- Players can only pay using:
    - Bank cards
    - Property cards
- Players **cannot pay using cards in hand**
- If unable to pay full amount:
    - Pay as much as possible
    - No debt remains

---

## 7. Winning Condition
A player wins when:
- They collect **3 complete property sets**
- Each set must be a **different colour**
- The win must occur during their own turn

---

## 8. System Design

The system follows an object-oriented design:

- `Game` manages the overall game flow
- `Player` stores player data and actions
- `Card` is the base class for all card types
    - `ActionCards`
    - `MoneyCards`
    - `PropertiesCards`
- `DrawPileAndDiscardPile` manages card drawing and reshuffling

---

## 9. Technologies Used
- Java
- JavaFX
- Maven

---

## 10. How to Run

1. Ensure Java 17+ is installed
2. Ensure JavaFX SDK is configured
3. Run the main class:

```bash
Main.java