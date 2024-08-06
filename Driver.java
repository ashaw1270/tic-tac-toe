import java.util.Scanner;

public class Driver {
    final Board board;
    final Bot bot;
    final Game game;

    public Driver() {
        try (Scanner reader = new Scanner(System.in)) {
            while (true) {
                System.out.print("X or O? ");
                String input = reader.nextLine();
                if (input.equalsIgnoreCase("X")) {
                    game = new Game(this, 'X');
                    break;
                }
                if (input.equalsIgnoreCase("O")) {
                    game = new Game(this, 'O');
                    break;
                }
                System.out.print("Invalid input. ");
            }
        }

        board = new Board(this);
        bot = new Bot(this);
    }

    public static void main(String[] args) {
        new Driver();
    }
}
