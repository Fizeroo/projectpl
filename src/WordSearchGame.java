import java.io.*;
import java.util.*;

public class WordSearchGame {
    private static final int GRID_SIZE = 10; // Size of the grid
    private char[][] grid;
    private List<String> words;
    private Set<String> foundWords;

    public WordSearchGame() {
        grid = new char[GRID_SIZE][GRID_SIZE];
        words = new ArrayList<>();
        foundWords = new HashSet<>();
    }

    // Read words from a file
    public void loadWords(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toUpperCase());
            }
        }
    }

    // Initialize the grid with random letters
    public void initializeGrid() {
        Random random = new Random();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = (char) ('A' + random.nextInt(26));
            }
        }
    }

    // Place words in the grid
    public void placeWords() {
        Random random = new Random();
        for (String word : words) {
            boolean placed = false;
            while (!placed) {
                int direction = random.nextInt(8); // 8 possible directions
                int row = random.nextInt(GRID_SIZE);
                int col = random.nextInt(GRID_SIZE);

                if (canPlaceWord(word, row, col, direction)) {
                    placeWord(word, row, col, direction);
                    placed = true;
                }
            }
        }
    }

    // Check if a word can be placed in the grid
    private boolean canPlaceWord(String word, int row, int col, int direction) {
        int length = word.length();
        for (int i = 0; i < length; i++) {
            int newRow = row, newCol = col;

            switch (direction) {
                case 0: newRow = row + i; break; // Horizontal right
                case 1: newRow = row - i; break; // Horizontal left
                case 2: newCol = col + i; break; // Vertical down
                case 3: newCol = col - i; break; // Vertical up
                case 4: newRow = row + i; newCol = col + i; break; // Diagonal down-right
                case 5: newRow = row - i; newCol = col - i; break; // Diagonal up-left
                case 6: newRow = row + i; newCol = col - i; break; // Diagonal down-left
                case 7: newRow = row - i; newCol = col + i; break; // Diagonal up-right
            }

            if (newRow < 0 || newRow >= GRID_SIZE || newCol < 0 || newCol >= GRID_SIZE ||
                    (grid[newRow][newCol] != '\u0000' && grid[newRow][newCol] != word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Place a word in the grid
    private void placeWord(String word, int row, int col, int direction) {
        for (int i = 0; i < word.length(); i++) {
            int newRow = row, newCol = col;

            switch (direction) {
                case 0: newRow = row + i; break;
                case 1: newRow = row - i; break;
                case 2: newCol = col + i; break;
                case 3: newCol = col - i; break;
                case 4: newRow = row + i; newCol = col + i; break;
                case 5: newRow = row - i; newCol = col - i; break;
                case 6: newRow = row + i; newCol = col - i; break;
                case 7: newRow = row - i; newCol = col + i; break;
            }

            grid[newRow][newCol] = word.charAt(i);
        }
    }

    // Display the grid
    public void displayGrid() {
        for (char[] row : grid) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    // Check if the entered word is correct
    public boolean checkWord(int startRow, int startCol, int endRow, int endCol) {
        StringBuilder sb = new StringBuilder();
        if (startRow == endRow) {
            for (int col = Math.min(startCol, endCol); col <= Math.max(startCol, endCol); col++) {
                sb.append(grid[startRow][col]);
            }
        } else if (startCol == endCol) {
            for (int row = Math.min(startRow, endRow); row <= Math.max(startRow, endRow); row++) {
                sb.append(grid[row][startCol]);
            }
        } else {
            int rowDir = Integer.compare(endRow, startRow);
            int colDir = Integer.compare(endCol, startCol);
            for (int row = startRow, col = startCol;
                 row != endRow + rowDir && col != endCol + colDir;
                 row += rowDir, col += colDir) {
                sb.append(grid[row][col]);
            }
        }

        String word = sb.toString();
        if (words.contains(word)) {
            foundWords.add(word);
            return true;
        }
        return false;
    }

    // Main game loop
    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Word Search Puzzle Game!");
        System.out.println("Generating word search puzzle...");
        initializeGrid();
        placeWords();
        System.out.println("Puzzle generated successfully!\n");

        while (foundWords.size() < words.size()) {
            displayGrid();
            System.out.println("\nEnter the word you found (start_row start_col end_row end_col):");
            try {
                int startRow = scanner.nextInt();
                int startCol = scanner.nextInt();
                int endRow = scanner.nextInt();
                int endCol = scanner.nextInt();

                if (checkWord(startRow, startCol, endRow, endCol)) {
                    System.out.println("Word found!");
                } else {
                    System.out.println("Invalid word or coordinates.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        System.out.println("Congratulations! You found all the hidden words!");
        scanner.close();
    }

    public static void main(String[] args) {
        WordSearchGame game = new WordSearchGame();
        try {
            game.loadWords("words.txt"); // Replace with your file path
            game.playGame();
        } catch (IOException e) {
            System.out.println("Error loading words from file: " + e.getMessage());
        }
    }
}