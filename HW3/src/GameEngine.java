import java.awt.*;
import java.util.Random;

/**
 * Created by amin on 12/23/14.
 */
public class GameEngine {
    public static final int TABLE_SIZE = 9;
    public static final int CANDY_TYPES = 6;
    String username;
    int score;
    Point cursor;
    boolean isSelected;
    int board[][] = new int[TABLE_SIZE][TABLE_SIZE];
    Random rand = new Random();

    public GameEngine() {
        cursor = new Point(0, 0);
        for (int i = 0; i < TABLE_SIZE; i++)
            for (int j = 0; j < TABLE_SIZE; j++)
                board[i][j] = rand.nextInt(CANDY_TYPES);

    }

    private int normalize(int a) {
        return Math.max(0, Math.min(TABLE_SIZE - 1, a));
    }

    private void normalizeCursor() {
        cursor.setLocation(normalize((int) cursor.getX()), normalize((int) cursor.getY()));
    }

    public void moveCursor(Point point) {
        cursor.translate((int) point.getX(), (int) point.getY());
        normalizeCursor();
    }


    public void selectCursor() {
        isSelected = true;
    }
}
