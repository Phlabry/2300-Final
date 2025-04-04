# 2300-Final

# Poker Game (Java)

A basic implementation of Poker written in Java. Built as a final project for an Object-Oriented Programming course, this project demonstrates principles such as inheritance, encapsulation, abstraction, and polymorphism, while modeling real-world poker game mechanics including dealing, betting, hand evaluation, and game flow.

This project currently runs in the terminal but **will be expanded into a full graphical user interface (GUI)** version in the near future.

---

## 📁 Project Structure

The project is organized into a few key directories/packages and files. Here's an overview:

### Main Files
- **Main.java**: The driver file that runs the game.

### Model Package (Game Entities)
This package contains the core classes representing the game elements:
- **Card.java**: Defines a single playing card with properties for rank and suit.
- **Deck.java**: Represents a deck of 52 cards and handles the logic for shuffling and dealing cards to players.
- **Hand.java**: Represents a player's hand, containing their dealt cards and the logic to manage them.
- **Player.java**: Represents a player with attributes such as a hand of cards, money, and actions (folding, betting, etc.).
- **PlayerNode.java**: Implements a node in the circular linked list for player management. Helps in managing turns for players in the game.
- **Table.java**: Represents the poker table, managing the players, the pot, and the game state.

### Mechanics Package (Game Logic)
This package contains the core logic of the poker game:
- **PokerGame.java**: Handles the overall game mechanics, such as dealing cards, managing betting rounds, resolving hands, and determining the winner.
- **HandEvaluator.java**: Contains the logic for evaluating poker hands (Royal Flush, Straight, Full House, etc.) and determining the best hand.
- **Betting.java**: Enum that defines the possible betting actions a player can take: FOLD, CHECK, CALL, RAISE, and ALL_IN.

### UI Package (User Interface)
This package handles the graphical user interface components of the game:
- **GameUI.java**: Contains the logic for rendering the game's user interface, allowing players to interact with the game via buttons, menus, and other visual elements.

---

This structure is designed to separate different parts of the game logic and interface, making the code easier to understand and maintain.

---

##  Features

-  Card dealing and hand distribution  
-  Player betting logic including Raise, Call, Fold, Check, and All-In  
-  Poker hand evaluation logic  
-  Multiple players (user-defined)  
-  Console-based game flow (GUI version in development)  

---

##  How to Run

1. Clone the repository:
   ```bash
   https://github.com/Phlabry/2300-Final.git
   cd 2300-Final
   
---

## Concepts Used 

- Object-Oriented Design (OOP)
- Enums for clean action handling
- Modular, scalable architecture
- Strong encapsulation and abstraction

---

## Planned Features

- Graphical User Interface (GUI) using JavaFX or Swing
- Online multiplayer via sockets or API
- Advanced betting mechanics (flop, turn, river, side pots)
- Dynamic chip stack and pot visualization
- Animations and sound effects

---

## Authors

- Developed by: Aleksandre Khvadagadze, Justin Zhang, Sahand Darwish, Nolen Cowans
- Course: Object-Oriented Software Design  
