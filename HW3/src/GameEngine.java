import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by amin on 12/23/14.
 */
public class GameEngine {
    public static final int BOARD_SIZE = 9;
    public static final int CANDY_TYPES = 6;
    String username;
    int score;
    Point cursor;

    public boolean isSelected() {
        return selected;
    }

    boolean selected;

    public int[][] getBoard() {
        return board;
    }

    int board[][] = new int[BOARD_SIZE][BOARD_SIZE];
    Random rand = new Random();

    public GameEngine() {
        cursor = new Point(0, 0);
        for (int i = 0; i < BOARD_SIZE; i++) for (int j = 0; j < BOARD_SIZE; j++) board[i][j] = -1;
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {
                do {
                    board[i][j] = rand.nextInt(CANDY_TYPES);
                } while (getRemovedCandies().length > 0);
            }

    }

    private Point[] getRemovedCandies() {
        Set<Point> points = new HashSet<Point>();
        for (int i = 0; i < BOARD_SIZE - 3; i++)
            for (int j = 0; j < BOARD_SIZE - 3; j++) {
                if (board[i][j] == -1)
                    continue;
                if (board[i][j] == board[i][j + 1] && board[i][j + 1] == board[i][j + 2]) {
                    points.add(new Point(i, j));
                    points.add(new Point(i, j + 1));
                    points.add(new Point(i, j + 2));
                }
                if (board[i][j] == board[i + 1][j] && board[i + 1][j] == board[i + 2][j]) {
                    points.add(new Point(i, j));
                    points.add(new Point(i + 1, j));
                    points.add(new Point(i + 2, j));
                }
            }
        return points.toArray(new Point[points.size()]);
    }

    private int normalize(int a) {
        return Math.max(0, Math.min(BOARD_SIZE - 1, a));
    }

    private void normalizeCursor() {
        cursor.setLocation(normalize((int) cursor.getX()), normalize((int) cursor.getY()));
    }

    public void moveCursor(Point point) {
        cursor.translate((int) point.getX(), (int) point.getY());
        normalizeCursor();
    }


    public void selectCursor() {
        selected = true;
    }
}
