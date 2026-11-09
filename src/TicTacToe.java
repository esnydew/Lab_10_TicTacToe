import java.util.Arrays;
import java.util.Scanner;

public class TicTacToe {
    // Class Declarations
    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static String[][] board = new String[ROWS][COLS];
    private static boolean hasX;
    private static boolean hasO;

    /**
     * The main method.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // Declarations
        Scanner in = new Scanner(System.in);
        String currentTurn;
        String player1;
        int player1Score = 0;
        int player2Score = 0;
        int gameNum = 1; // Variable to keep track of gameNum.
        int move; // Variable to keep track of the current move. Used to begin checking for a win at move 5;
        int playerRow;
        int playerCol;
        boolean playGame;
        boolean gameOver;
        boolean validMove;
        final String PLAYER_X = "X";
        final String PLAYER_O = "O";

        // Ask if the players would like to start the game.
        playGame = SafeInput.getYNConfirm(in, "Would you like to play 2-Player Tic-Tac-Toe?");
        if(playGame) System.out.println("Player 1 will start as X, and move first. Player 2 will start as O, and move second. On replay, it swaps: player 2 gets X, and player 1 gets O.");
        while(playGame) {
            // Reset the game.
            clearBoard();
            gameOver = false;
            move = 1;
            System.out.printf("%nGame #%d.%n", gameNum);
            if(gameNum % 2 == 0) {
                player1 = PLAYER_O;
            } else {
                player1 = PLAYER_X;
            }

            while(!gameOver) {
                // Check whether it should be player X's or player O's turn.
                if(move % 2 == 0) {
                    currentTurn = PLAYER_O;
                } else {
                    currentTurn = PLAYER_X;
                }

                validMove = false;
                System.out.printf("Move %d. Player %s's turn (%s).%n", move, (player1.equals(currentTurn)) ? 1 : 2, currentTurn);
                while(!validMove) {
                    display();
                    // Get the coordinates of the player's move.
                    playerRow = SafeInput.getRangedInt(in, String.format("Please enter the row (from top-to-bottom) you'd like to place an %s in", currentTurn), 1, ROWS) - 1;
                    playerCol = SafeInput.getRangedInt(in, String.format("Please enter the column (from left-to-right) you'd like to place an %s in", currentTurn), 1, COLS) - 1;
                    // Pass player's move coordinates to the validMove method.
                    validMove = isValidMove(playerRow, playerCol);
                    // Add move to the board
                    if(validMove) {
                        board[playerRow][playerCol] = currentTurn;
                        System.out.println();
                    } else {
                        System.out.printf("Invalid move at row %d, column %d! That space is already filled with '%s'!%n", playerRow + 1, playerCol + 1, board[playerRow][playerCol]);
                        System.out.printf("%nStill move %d. Player %s's turn (%s).%n", move, (player1.equals(currentTurn)) ? 1 : 2, currentTurn);
                    }
                }

                // If a win or tie is possible, begin checking.
                if(move >= 5) {
                    if(isWin(currentTurn)) {
                        // End game on win.
                        gameOver = true;
                        display();
                        // Handle player wins, and update score accordingly.
                        if(player1.equals(currentTurn)) {
                            System.out.printf("Player 1 (%s) wins!%n", currentTurn);
                            player1Score += 1;
                        } else {
                            System.out.printf("Player 2 (%s) wins!%n", currentTurn);
                            player2Score += 1;
                        }
                    } else if(isTie()) {
                        // End game on tie.
                        gameOver = true;
                        display();
                        System.out.println("It's a tie! Neither player scores.");
                    }
                }
                // Increment move counter.
                move += 1;
            }
            System.out.printf("Game Number %d.%nPlayer 1's Score: %d.%nPlayer 2's Score: %d.%nScore: %d-%d.%n", gameNum,player1Score, player2Score, player1Score, player2Score);
            // Ask if the players would like to play again.
            playGame = SafeInput.getYNConfirm(in, "Would you like to play again?");
            if(playGame) gameNum += 1;
        }
        in.close();
    }

    /**
     * Sets all the board elements to a space
     */
    private static void clearBoard() {
        // Iterate through each row of the board
        for (String[] row : board) {
            // Fill each row with a space
            Arrays.fill(row, " ");
        }
    }

    /**
     * Shows the tic-tac-toe board as part of the prompt for the user's move choice.
     */
    private static void display() {
        System.out.println(); // Padding for the grid.
        // Iterate through the ROWS of board with y first. Place a newline.
        for(int y = 0; y < board.length; y++) {
            // Iterate through the COLUMNS of the board on ROW y second. Place the current position, followed by a divider
            for(int x = 0; x < board[y].length; x++) {
                // If the COLUMN x is the last one, only print the status of that space; otherwise, print the status of that space and a | divider.
                System.out.printf((x == board[y].length - 1) ? "%s %n" : "%s | ", board[y][x]);
            }
            // Row dividers would have to be remade if the board size ever changed.
            System.out.println((y < board.length - 1) ? "--+---+--" : "");
        }
    }

    /**
     * Returns true if there is a space at the passed move coordinates, thus being a legal move.
     * @param row the row the move is on.
     * @param col the column the move is in.
     * @return a boolean for whether the move is legal.
     */
    private static boolean isValidMove(int row, int col) {
        return board[row][col].equals(" ");
    }

    /**
     * Checks to see if there's a win state on the current board for a specified player (X or O). Calls three additional methods for each kind of win.
     * @param player the player specified to check a win state for.
     * @return a boolean for whether the player won or not.
     */
    private static boolean isWin(String player) {
        return isRowWin(player) || isColWin(player) || isDiagonalWin(player);
    }

    /**
     * Checks for a column win for a player.
     * @param player the player specified to check a win state for.
     * @return a boolean for if there is a column win.
     */
    private static boolean isColWin(String player) {
        // Declarations
        boolean isWin;

        // IPO
        // Guess optimistically.
        for(int col = 0; col < board[0].length; col++) {
            // Start each column by assuming a win is true.
            isWin = true;
            for(String[] row : board) {
                // If any spaces within a column aren't the player's mark, state that the column doesn't win and break the loop early.
                if(!row[col].equals(player)) {
                    isWin = false;
                    break;
                }
            }
            // Check to see if the column won; if so, return true.
            if(isWin) return true;
        }
        // Once all columns have been confirmed non-winners, return false.
        return false;
    }

    /**
     * Checks for a row win for a player.
     * @param player the player specified to check a win state for.
     * @return a boolean for if there is a row win.
     */
    private static boolean isRowWin(String player) {
        // Declarations
        boolean isWin;

        // IPO
        // Guess optimistically.
        for(String[] row : board) {
            // Start each row by assuming a win is true.
            isWin = true;
            for(String col : row) {
                // If any spaces within a row aren't the player's mark, state that the row doesn't win and break the loop early.
                if(!col.equals(player)) {
                    isWin = false;
                    break;
                }
            }
            // Check to see if the row won; if so, return true.
            if(isWin) return true;
        }
        // Once all rows have been confirmed non-winners, return false.
        return false;
    }

    /**
     * Checks for a diagonal win for a player.
     * @param player the player specified to check a win state for.
     * @return a boolean for if there is a diagonal win.
     */
    private static boolean isDiagonalWin(String player) {
        // Declarations
        boolean isDownWin = true;
        boolean isUpWin = true;

        // IPO
        // Guess optimistically
        for(int i = 0; i < board.length; i++) {
            // If any spaces in a backwards diagonal don't match the player, state that the diagonal doesn't win.
            if(!board[i][i].equals(player)) isDownWin = false;
            // If any spaces in a forwards diagonal don't match the player, state that the diagonal doesn't win.
            if(!board[i][board.length - 1 - i].equals(player)) isUpWin = false;
        }
        // Once the diagonals have been checked, return whether there was a win.
        return isDownWin || isUpWin;
    }

    /**
     * Checks for a tie state: all spaces on the board filled or there is an X and O in every possible vector.
     * @return a boolean for if there is a tie.
     */
    private static boolean isTie() {
        // Will not correctly end game early in some instances. isFull() is a reliable fall back, but may miss some ties.
        return isFull() || (isRowTie() && isColTie() && isDiagonalTie());
    }

    /**
     * A helper method to check whether the board is full; if a board is full and there is no win, then it must be a tie.
     * @return a boolean for if the board is full.
     */
    private static boolean isFull() {
        for(String[] row : board) {
            for(String col : row) {
                if(col.equals(" ")) return false;
            }
        }
        return true;
    }

    /**
     * A helper method to check whether there is a possible winning row.
     * @return a boolean for whether there are no possible winning rows.
     */
    private static boolean isRowTie() {
        for (String[] row : board) {
            hasX = false;
            hasO = false;
            for (String col : row) {
                if (col.equals("X")) {
                    hasX = true;
                } else if (col.equals("O")) {
                    hasO = true;
                }
                // Break the loop early if a row has both an X and O.
                if (hasX && hasO) break;
            }
            if (!(hasX && hasO)) return false;
        }
        return true;
    }

    /**
     * A helper method to check whether there is a possible winning column.
     * @return a boolean for whether there are no possible winning columns.
     */
    private static boolean isColTie() {
        for (int col = 0; col < board[0].length; col++) {
            hasX = false;
            hasO = false;
            for (String[] row : board) {
                if (row[col].equals("X")) {
                    hasX = true;
                } else if (row[col].equals("O")) {
                    hasO = true;
                }
                // Break the loop early if a column has both an X and O
                if (hasX && hasO) break;
            }
            if (!(hasX && hasO)) return false;
        }
        return true;
    }

    /**
     * A helper method to check whether there is a possible winning diagonal.
     * @return a boolean for whether there are no possible winning diagonals.
     */
    private static boolean isDiagonalTie() {
        // Declarations
        int j;

        // IPO
        for(int d = 0; d < 2; d++) {
            hasX = false;
            hasO = false;
            for(int i = 0; i < board.length; i++) {
                j = (d == 0) ? i : board.length - 1 - i;
                if (board[i][j].equals("X")) {
                    hasX = true;
                } else if (board[i][j].equals("O")) {
                    hasO = true;
                }
                if(hasX && hasO) break;
            }
            if(!(hasX && hasO)) return false;
        }
        return true;
    }
}