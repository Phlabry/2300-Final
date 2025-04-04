# 2300-Final

# Poker Game (Java)

A basic implementation of Poker written in Java. Built as a final project for an Object-Oriented Programming course, this project demonstrates principles such as inheritance, encapsulation, abstraction, and polymorphism, while modeling real-world poker game mechanics including dealing, betting, hand evaluation, and game flow.

This project currently runs in the terminal but **will be expanded into a full graphical user interface (GUI)** version in the near future.

---

## 📁 Project Structure

The project is organized into a few key directories and files. Here's an overview:

### Main Files
- **Main.java**: The driver file that runs the game.

### Model (Game Entities)
This folder contains the core classes representing the game elements:
- **Card.java**: Defines a single playing card (rank and suit).
- **Deck.java**: Represents a deck of 52 cards and handles card dealing.
- **Player.java**: Represents a player with a hand of cards, money, and actions (folding, betting, etc.).

### Mechanics (Game Logic)
This folder contains the core logic of the poker game:
- **PokerGame.java**: Handles the core game mechanics, including the dealer, betting rounds, and hand resolution.
- **HandEvaluator.java**: Contains the logic to evaluate poker hands (Royal Flush, Straight, Full House, etc.).
  
### Betting
A subfolder within **Mechanics** that deals with betting actions:
- **BettingAction.java**: Enum representing the possible actions a player can take during betting: FOLD, CHECK, CALL, RAISE, ALL_IN.

---

This structure is designed to separate different parts of the game logic, making the code easier to understand and maintain.

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
   git clone https://github.com/your-username/poker-game-java.git
   cd poker-game-java
   
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
