import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by amin on 12/23/14.
 */
public class GameEngine {
    public static final int BOARD_SIZE = 9;
    int board[][] = new int[BOARD_SIZE][BOARD_SIZE];
    public static final int CANDY_TYPES = 6;
    String username;
    int score;
    Point cursor;
    KeyEvent keyEvent = null;
    public boolean locked;

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    GameBoard gameBoard;
    boolean selected = false;
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

    public void processKey() {
        if (keyEvent == null)
            return;
        locked = true;
        Point point = new Point(0, 0);
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                point.move(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                point.move(1, 0);
                break;
            case KeyEvent.VK_UP:
                point.move(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                point.move(0, 1);
                break;
            case KeyEvent.VK_SPACE:
                selectCursor();
                point = null;
                break;
        }
        if (point != null)
            moveCursor(point);
        keyEvent = null;
        locked = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public int[][] getBoard() {
        return board;
    }

    private Point[] getRemovedCandies() {
        Set<Point> points = new HashSet<Point>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE - 2; j++) {
                if (board[i][j] == -1)
                    continue;
                if (board[i][j] == board[i][j + 1] && board[i][j + 1] == board[i][j + 2]) {
                    points.add(new Point(i, j));
                    points.add(new Point(i, j + 1));
                    points.add(new Point(i, j + 2));
                }
            }
        for (int i = 0; i < BOARD_SIZE - 2; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == -1)
                    continue;
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

    private void normalizePoint(Point point) {
        point.setLocation(normalize((int) point.getX()), normalize((int) point.getY()));
    }

    public void moveCursor(Point point) {
        if (isSelected()) {
            Point newPoint = new Point(cursor);
            newPoint.translate(point.x, point.y);
            normalizePoint(newPoint);
            if (!newPoint.equals(cursor)) {
                gameBoard.swapCandies(cursor, newPoint);
                swapCandies(newPoint);
                if (getRemovedCandies().length == 0) {
                    gameBoard.swapCandies(cursor, newPoint);
                    swapCandies(newPoint);
                }
                else {
                    gameBoard.eatCandies(getRemovedCandies());
                }
                selected = false;
            }
        }
        else {
            cursor.translate(point.x, point.y);
            normalizePoint(cursor);
        }
        gameBoard.repaint();
    }

    private void swapCandies(Point newPoint) {
        int tmp = board[cursor.x][cursor.y];
        board[cursor.x][cursor.y] = board[newPoint.x][newPoint.y];
        board[newPoint.x][newPoint.y] = tmp;
    }


    public void selectCursor() {
        selected = true;
    }
}
