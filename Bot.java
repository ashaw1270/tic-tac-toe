import java.util.Arrays;

public class Bot {
    private final Driver driver;

    private final double[][] scores;

    private int currentDepth;

    final Object LOCK = new Object();

    public Bot(Driver driver) {
        this.driver = driver;
        scores = new double[Game.SIZE][Game.SIZE];
        currentDepth = 0;

        new Thread(() -> {
            synchronized (LOCK) {
                while (true) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    currentDepth++;

                    Arrays.fill(scores[0], 0);
                    Arrays.fill(scores[1], 0);
                    Arrays.fill(scores[2], 0);

                    int[] move = bestMove(driver.game.player2);
                    System.out.println("Move: " + Arrays.toString(move) + "\n\n");
                    makeMove(move[0], move[1]);

                    currentDepth++;
                }
            }
        }).start();
    }

    public void resetCurrentDepth() {
        System.out.println("here");
        currentDepth = 0;
    }

    public int[] bestMove(char letter) {
        char[][] board = driver.game.getBoard();
        double bestScore = Double.MAX_VALUE;
        int[] move = new int[2];
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                if (board[row][col] == ' ') {
                    board[row][col] = letter;
                    minimax(row, col);
                    board[row][col] = ' ';

                    double score = scores[row][col];
                    if (score < bestScore) {
                        bestScore = score;
                        move[0] = row;
                        move[1] = col;
                    } else if (score == bestScore) {
                        if (Math.random() < 0.5) {
                            move[0] = row;
                            move[1] = col;
                        }
                    }
                }
            }
        }

        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                if (board[row][col] == ' ') {
                    System.out.println(row + ", " + col + ": " + scores[row][col]);
                }
            }
        }

        System.out.println();

        return move;
    }

    public void minimax(int row, int col) {
        minimax(row, col, false);
    }

    private int depth() {
        int depth = 0;
        char[][] board = driver.game.getBoard();
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                if (board[row][col] != ' ') {
                    depth++;
                }
            }
        }
        return depth;
    }

    private void minimax(int r, int c, boolean max) {
        /*if (r == 0 && c == 2) {
            System.out.println(depth() + ", " + currentDepth);
        }*/
        Integer terminal = driver.game.terminal(driver.game.player2);
        if (terminal != null) {
            if (terminal < 0) {
                if (depth() == currentDepth + 1)
                    scores[r][c] = Double.MAX_VALUE;
                else scores[r][c] += 1. / depth();
            } else if (terminal > 0) {
                if (depth() == currentDepth + 1) {
                    scores[r][c] = -Double.MAX_VALUE;
                }
            }
        } else {
            char player = max ? driver.game.player2 : driver.game.player1;
            char[][] board = driver.game.getBoard();
            for (int row = 0; row < Game.SIZE; row++) {
                for (int col = 0; col < Game.SIZE; col++) {
                    if (board[row][col] == ' ') {
                        board[row][col] = player;
                        minimax(r, c, !max);
                        board[row][col] = ' ';
                    }
                }
            }
        }
    }

    private long minimax(long score, boolean max) {
        Integer terminal = driver.game.terminal(driver.game.player2);
        if (terminal != null) {
            return terminal;
        }

        char player = max ? driver.game.player2 : driver.game.player1;
        char[][] board = driver.game.getBoard();
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                if (board[row][col] == ' ') {
                    board[row][col] = player;
                    long minimax = minimax(score, !max);
                    score += minimax;
                    board[row][col] = ' ';
                }
            }
        }
        return score;
    }

    /*private long minimax(int r, int c, int depth, boolean max) {
        counts[r][c]++;
        char player = max ? driver.game.player2 : driver.game.player1;

        Integer terminal = driver.game.terminal(player);
        if (terminal != null) {
            return terminal;
        }

        long bestScore = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        char[][] board = driver.game.getBoard();
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                if (board[row][col] == ' ') {
                    board[row][col] = player;
                    long score = minimax(r, c, ++depth, !max);
                    bestScore = max ? Math.max(bestScore, score) : Math.min(bestScore, score);
                    board[row][col] = ' ';
                }
            }
        }
        scores[r][c] += bestScore;
        return bestScore;
    }*/

    public void makeMove(int row, int col) {
        Cell moveCell = driver.board.cells[row][col];
        if (moveCell.getCharacter() == ' ') {
            moveCell.setCharacter(driver.game.makeMove(row, col));
        }
        driver.board.checkTerminal();
    }

    public static void main(String[] args) {
    }
}
