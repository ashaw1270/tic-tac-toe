import javax.swing.*;
import java.awt.*;

public class Board extends JFrame {
    private static final int SIZE = 500;
    public static final int GAP = 2;

    final Cell[][] cells;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final JLabel terminalStateLabel;

    private final Driver driver;

    public Board(Driver driver) {
        this.driver = driver;

        setLayout(new CardLayout());
        setSize(SIZE, SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.setSize(SIZE, SIZE);

        JPanel ticTacToePanel = new JPanel();
        ticTacToePanel.setLayout(new GridLayout(Game.SIZE, Game.SIZE));
        ticTacToePanel.setSize(SIZE, SIZE);
        cells = new Cell[Game.SIZE][Game.SIZE];
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                cells[row][col] = new Cell(driver, row, col, SIZE / Game.SIZE - GAP * (Game.SIZE - 1));
                ticTacToePanel.add(cells[row][col]);
            }
        }
        ticTacToePanel.setVisible(true);

        JPanel terminalStatePanel = new JPanel();
        terminalStatePanel.setLayout(null);
        terminalStatePanel.setSize(SIZE, SIZE);

        terminalStateLabel = new JLabel();
        terminalStateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        terminalStateLabel.setVerticalAlignment(SwingConstants.CENTER);
        terminalStateLabel.setSize(SIZE, SIZE);
        terminalStateLabel.setFont(new Font("Arial", Font.BOLD, SIZE / 5));
        terminalStateLabel.setForeground(Color.BLACK);
        terminalStateLabel.setVisible(true);

        terminalStatePanel.add(terminalStateLabel);
        terminalStatePanel.setVisible(true);

        mainPanel.add(ticTacToePanel, "ticTacToePanel");
        mainPanel.add(terminalStatePanel, "terminalStatePanel");
        mainPanel.setVisible(true);

        add(mainPanel);
        setVisible(true);
        cardLayout.show(mainPanel, "ticTacToePanel");
    }

    public void checkTerminal() {
        Integer terminal = driver.game.terminal(driver.game.nonActivePlayer());
        if (terminal != null) {
            if (terminal > 0)
                terminalStateLabel.setText(driver.game.nonActivePlayer() + " wins!");
            else if (terminal < 0)
                terminalStateLabel.setText(driver.game.activePlayer() + " wins!");
            else terminalStateLabel.setText("Draw!");

            new Thread(() -> {
                try {Thread.sleep(500);} catch (InterruptedException ignored) {}
                cardLayout.show(mainPanel, "terminalStatePanel");
                try {Thread.sleep(500);} catch (InterruptedException ignored) {}
                newGame();
            }).start();
        }
    }

    private void newGame() {
        driver.game.clear();
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                cells[row][col].setCharacter(' ');
            }
        }
        cardLayout.show(mainPanel, "ticTacToePanel");
        driver.bot.resetCurrentDepth();
    }
}
