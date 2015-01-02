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
    Point mouseCursor = null;
    KeyEvent keyEvent = null;
    public boolean locked;
    private boolean gameOver = false;

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    GameBoard gameBoard;
    boolean selected = false;
    Random rand = new Random();

    /**
     * Creates the game and generates a board that is valid
     *
     * @param username the name of the palyer
     */
    public GameEngine(String username) {
        this.username = username;
        cursor = new Point(0, 0);
        do {
            resetBoard();
            for (int i = 0; i < BOARD_SIZE; i++)
                for (int j = 0; j < BOARD_SIZE; j++) {
                    do {
                        board[i][j] = rand.nextInt(CANDY_TYPES);
                    } while (getRemovedCandies().length > 0);
                }
        } while (isLost());

    }

    /**
     * Checks if any valid move is left to do
     * @return true if the player can still continue, false otherwise
     */
    private boolean isLost() {
        boolean answer = true;
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE - 1; j++) {
                Point p1 = new Point(i, j), p2 = new Point(i, j + 1);
                swapCandies(p1, p2);
                if (getRemovedCandies().length > 0)
                    answer = false;
                swapCandies(p1, p2);
            }
        for (int i = 0; i < BOARD_SIZE - 1; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {
                Point p1 = new Point(i, j), p2 = new Point(i + 1, j);
                swapCandies(p1, p2);
                if (getRemovedCandies().length > 0)
                    answer = false;
                swapCandies(p1, p2);
            }
        return answer;
    }

    private void resetBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) for (int j = 0; j < BOARD_SIZE; j++) board[i][j] = -1;
    }

    /**
     * process the key even passed by game controller
     */
    public void processKey() {
        if (keyEvent == null || isGameOver())
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
        if (point != null) {

            Point newPoint = new Point(this.cursor);
            newPoint.translate(point.x, point.y);
            moveCursor(newPoint);
        }
        keyEvent = null;
        locked = false;
    }

    /**
     * Processes mouse events created by clicking on candy tiles
     */
    public void processMouse() {
        if (mouseCursor == null || isGameOver())
            return;
        locked = true;
        if (isSelected())
            moveCursor(new Point(mouseCursor));
        else {
            moveCursor(new Point(mouseCursor));
            selectCursor();
        }
        mouseCursor = null;
        locked = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public int[][] getBoard() {
        return board;
    }

    /**
     * Calculates the candies that should be removed
     *
     * @return an array containing position of candies that should be removed
     */
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

    /**
     * moves the cursor to the new position or swap the candy under the current position of the cursor with the new position if the cursor is selected
     *
     * @param newPoint the new position of the cursor
     */
    public void moveCursor(Point newPoint) {
        if (isSelected()) {
            normalizePoint(newPoint);
            if (!newPoint.equals(this.cursor) && Math.abs(cursor.x - newPoint.x) + Math.abs(cursor.y - newPoint.y) == 1) {
                gameBoard.swapCandies(this.cursor, newPoint);
                swapCandies(newPoint);
                Point[] removedCandies = getRemovedCandies();
                if (removedCandies.length == 0) {
                    gameBoard.swapCandies(this.cursor, newPoint);
                    swapCandies(newPoint);
                }
                else {
                    while (removedCandies.length > 0) {
                        gameBoard.eatCandies(removedCandies);
                        score += calculateScore(removedCandies.length);
                        gameBoard.repaint();
                        refillBoard();
                        gameBoard.refillBoard();
                        removedCandies = getRemovedCandies();
                    }
                    if (isLost())
                        gameOver = true;
                }
                selected = false;
            }
        }
        else {
            cursor = newPoint;
            normalizePoint(cursor);
        }
        gameBoard.repaint();
    }

    private int calculateScore(int number_of_candies) {
        return (number_of_candies - 2) * 10;
    }

    /**
     * Refills the board by creating new random candies
     */
    private void refillBoard() {
        for (Point p : getRemovedCandies())
            board[p.x][p.y] = -1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Point lastPos = null;
            for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                if (board[i][j] == -1) {
                    if (lastPos == null) lastPos = new Point(i, j);
                }
                else if (lastPos != null) {
                    board[lastPos.x][lastPos.y] = board[i][j];
                    board[i][j] = -1;
                    lastPos.y--;
                }
            }
            for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                if (board[i][j] == -1) {
                    board[i][j] = rand.nextInt(CANDY_TYPES);
                }
            }
        }
    }

    /**
     * Swaps two candies
     *
     * @param oldPoint position of the first candy
     * @param newPoint position of the second candy
     */
    private void swapCandies(Point oldPoint, Point newPoint) {
        int tmp = board[newPoint.x][newPoint.y];
        board[newPoint.x][newPoint.y] = board[oldPoint.x][oldPoint.y];
        board[oldPoint.x][oldPoint.y] = tmp;
    }

    /**
     * Convenient function for swapping candies
     * @param oldPoint position of the first candy
     */
    private void swapCandies(Point oldPoint) {
        swapCandies(oldPoint, cursor);
    }


    public void selectCursor() {
        selected = true;
        gameBoard.repaint();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

}
