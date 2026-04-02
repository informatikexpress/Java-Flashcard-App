# Java Flashcard App 🧠

A classic, two-part desktop application built with Java and the Swing GUI toolkit. This project allows users to create custom flashcard sets, save them locally to their computer, and load them later to study and test their knowledge.

## Features

This application is split into two standalone programs:

### 1. FlashCard Builder
* **Create Cards:** A user-friendly interface to type in questions and answers.
* **Dynamic Storage:** Temporarily holds created cards in memory using an `ArrayList`.
* **Save to Disk:** Uses File I/O to export the completed deck of cards as a local `.txt` file, using a custom delimiter to separate questions and answers.
* **Auto-Clear:** Automatically clears text fields and refocuses the cursor after a card is added for a smooth typing experience.

### 2. FlashCard Player
* **Load Sets:** Opens a file chooser dialog to load previously saved `.txt` flashcard sets from the computer.
* **Parsing:** Reads the text file line by line and reconstructs the `FlashCard` objects using `StringTokenizer`.
* **Study Mode:** Displays the question first. Clicking "Show Answer" reveals the back of the card, and clicking "Next Card" iterates through the deck until all cards are learned.

## Tech Stack
* **Language:** Java
* **GUI Framework:** `javax.swing` & `java.awt` (JFrame, JPanel, JTextArea, JMenuBar, JFileChooser)
* **Data Structures:** `ArrayList`, `Iterator`
* **File Handling:** `File`, `FileWriter`, `FileReader`, `BufferedWriter`, `BufferedReader`
