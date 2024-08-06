import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Cell extends JPanel implements MouseListener {
    private final Driver driver;
    private final int row;
    private final int col;
    private final JLabel label;

    private char character;

    public Cell(Driver driver, int row, int col, int size) {
        this.driver = driver;
        this.row = row;
        this.col = col;
        this.character = ' ';

        setBorder(BorderFactory.createLineBorder(Color.BLACK, Board.GAP));
        setLayout(null);
        addMouseListener(this);

        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setSize(size, size);
        label.setFont(new Font("Arial", Font.BOLD, size));
        label.setForeground(Color.BLACK);
        label.setVisible(true);
        add(label);

        setVisible(true);
    }

    public char getCharacter() {
        return character;
    }

    public static void main(String[] args) {

    }

    public void setCharacter(char character) {
        if (character == 'X') {
            this.character = character;
            label.setText(String.valueOf(character));
            label.setForeground(Color.RED);
        } else if (character == 'O') {
            this.character = character;
            label.setText(String.valueOf(character));
            label.setForeground(Color.BLUE);
        } else if (character == ' ') {
            this.character = character;
            label.setText(String.valueOf(character));
        } else {
            throw new IllegalArgumentException("Character must be 'X' or 'O'");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (character == ' ') {
            setCharacter(driver.game.makeMove(row, col));
        }
        driver.board.checkTerminal();

        synchronized (driver.bot.LOCK) {
            driver.bot.LOCK.notify();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
