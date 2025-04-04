# 2300-Final

# Poker Game (Java)

A basic implementation of Poker written in Java. Built as a final project for an Object-Oriented Programming course, this project demonstrates principles such as inheritance, encapsulation, abstraction, and polymorphism, while modeling real-world poker game mechanics including dealing, betting, hand evaluation, and game flow.

This project currently runs in the terminal but **will be expanded into a full graphical user interface (GUI)** version in the near future.

---

## 📁 Project Structure
PokerGame/
├── Main.java                 # Driver file to run the game
├── Model/
│   ├── Card.java            # Represents a playing card
│   ├── Deck.java            # Represents a deck of 52 cards
│   ├── Player.java          # Represents a player with money, hand, and actions
├── Mechanics/
│   ├── PokerGame.java       # Core game logic (dealer, betting rounds, hand resolution)
│   ├── HandEvaluator.java   # Evaluates poker hands and determines the winner
│   └── Betting/
│       ├── BettingAction.java  # Enum for actions: FOLD, CHECK, CALL, RAISE, ALL_IN

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
