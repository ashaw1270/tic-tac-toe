import java.util.Arrays;

public class Game {
    public static final int SIZE = 3;

    final char player1;
    final char player2;
    private final char[][] board = new char[SIZE][SIZE];
    private final Driver driver;

    boolean player1Turn;

    public Game(Driver driver, char player1) {
        this.driver = driver;

        if (player1 == 'X') {
            this.player1 = 'X';
            this.player2 = 'O';
        } else if (player1 == 'O') {
            this.player1 = 'O';
            this.player2 = 'X';
        } else {
            throw new IllegalArgumentException("Player character must be either 'X' or 'O'");
        }

        for (int row = 0; row < SIZE; row++) {
            Arrays.fill(board[row], ' ');
        }

        player1Turn = true;
    }

    public char[][] getBoard() {
        return board;
    }

    public char activePlayer() {
        return player1Turn ? player1 : player2;
    }

    public char nonActivePlayer() {
        return player1Turn ? player2 : player1;
    }

    public char makeMove(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException("Row and column must be between 0 and " + (SIZE - 1));
        }

        board[row][col] = activePlayer();
        player1Turn = !player1Turn;
        return board[row][col];
    }

    public Integer terminal(char c) {
        // check for win
        if (checkRow(0, c) || checkRow(1, c) || checkRow(2, c) ||
                checkCol(0, c) || checkCol(1, c) || checkCol(2, c) ||
                checkDiag(c)) {
            return 1;
        }

        // check for loss
        char other = (c == player1) ? player2 : player1;
        if (checkRow(0, other) || checkRow(1, other) || checkRow(2, other) ||
                checkCol(0, other) || checkCol(1, other) || checkCol(2, other) ||
                checkDiag(other)) {
            return -1;
        }

        // check for non-terminal state
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == ' ') {
                    return null;
                }
            }
        }

        // otherwise, game is a draw
        return 0;
    }

    private boolean checkRow(int row, char letter) {
        return board[row][0] == letter && board[row][1] == letter && board[row][2] == letter;
    }

    private boolean checkCol(int col, char letter) {
        return board[0][col] == letter && board[1][col] == letter && board[2][col] == letter;
    }

    private boolean checkDiag(char letter) {
        return board[0][0] == letter && board[1][1] == letter && board[2][2] == letter ||
               board[0][2] == letter && board[1][1] == letter && board[2][0] == letter;
    }

    public void clear() {
        for (int row = 0; row < SIZE; row++) {
            Arrays.fill(board[row], ' ');
        }
        player1Turn = true;
    }

    @Override
    public String toString() {
        String output = "";
        for (int row = 0; row < SIZE; row++) {
            output += Arrays.toString(board[row]) + "\n";
        }
        return output;
    }

    public static void main(String[] args) {

    }
}
