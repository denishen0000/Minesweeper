# Minesweeper Game in Java

A simple console-based Minesweeper game implemented in Java. The goal is to clear the grid without detonating any hidden mines.

---

## Features

- **Console-based Interface**: Play directly in your terminal.
- **Difficulty Levels**: Easy, Medium, and Hard modes are implemented to make the gameplay for varibale.
- **Automatic Mine Placement**: Mines are randomly placed each game. For convenience, the first cell clicked is **never a bomb**.
- **Adjacency Calculation**: Numbers indicate how many mines are adjacent to a cell as in the well-known Minesweeper game.
- 
---

## File Overview

- **`Minesweeper.java`**: Main game logic, user input handling, and game loop.
- **`Vis.java`**: Visual representation of the game board.

> **Important Note**: `Vis.java` was not originally created by me. It has been edited and adapted from another source to fit this project. Original Creator contact: jmb15@williams.edu

---

## How to Run

1. **Clone the repository:**

```bash
git clone https://github.com/denishen0000/Minesweeper.git
```

2. **Navigate to the project directory:**

```bash
cd Minesweeper
```

3. **Compile the Java files:**

```bash
javac Minesweeper.java Vis.java
```

4. **Run the game:**

```bash
java Minesweeper
```

---

## Gameplay

- Choose the game mode by clicking on the white circles.
- Click on the cell you want to reveal.
- Use flags to mark suspected mines. Use SHIFT+RMB to put a flag into a cell.
- Double-click an open cell to reveal all adjacent cells. Flagged cells will not be revealed.
- The flag/bomb counter can be seen at the bottom of the screeen.
- Click E to exit to the menu 
- Click R to restart the game

---

## Screenshots

*(Optional: Add screenshots here if available)*  

```
Example terminal output:

  1 2 3 4 5
A . . 1 . .
B . 1 * 1 .
C . 1 1 1 .
```

---

## Acknowledgements

- `Vis.java` was adapted from an external source.
- Inspired by classic Minesweeper implementations.
- Thanks to contributors of original Minesweeper tutorials and projects.

---

## License

This project is open-source.

