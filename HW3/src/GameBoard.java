import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CandyTile extends JComponent {
    public static final int CANDY_SIZE = 60;
    private static BufferedImage candyImages[] = new BufferedImage[GameEngine.CANDY_TYPES];

    static {
        try {
            for (int i = 0; i < GameEngine.CANDY_TYPES; i++)
                candyImages[i] = ImageIO.read(new File("/home/amin/Desktop/Courses/Advanced Programming/CE-93-Java/HW3/src/t" + (i + 1) + ".png"));
        } catch (IOException e) {

        }
    }

    int x, y;
    //    int width, height
    int type;

    CandyTile(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        setBounds(x * CANDY_SIZE, y * CANDY_SIZE, CANDY_SIZE + 2, CANDY_SIZE + 2);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(candyImages[type], 0, 0, null);
    }

    public Thread moveToPos(final Point newPos) {
        int dx = newPos.x - getX();
        int dy = newPos.y - getY();
        if (dx != 0)
            dx /= Math.abs(dx);
        if (dy != 0)
            dy /= Math.abs(dy);
        final int finalDy = dy;
        final int finalDx = dx;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                do {
                    setLocation(getX() + finalDx, getY() + finalDy);
                    repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (!(getX() == newPos.x && getY() == newPos.y));
            }
        });
        thread.start();
        return thread;
    }

}

public class GameBoard extends JComponent {
    public static final int PANEL_SIZE = GameEngine.BOARD_SIZE * CandyTile.CANDY_SIZE;
    GameEngine engine;
    CandyTile candyTiles[][];
    ExecutorService executor = Executors.newFixedThreadPool(2);

    GameBoard(final GameController controller, GameEngine engine) {
//        setFocusable(true);
//        requestFocus();
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBounds(0, 0, PANEL_SIZE, PANEL_SIZE);
        this.engine = engine;
        candyTiles = new CandyTile[GameEngine.BOARD_SIZE][GameEngine.BOARD_SIZE];
        for (int i = 0; i < GameEngine.BOARD_SIZE; i++)
            for (int j = 0; j < GameEngine.BOARD_SIZE; j++) {
                candyTiles[i][j] = new CandyTile(i, j, engine.getBoard()[i][j]);
                add(candyTiles[i][j]);
//                System.out.println(candyTiles[i][j]);
            }

    }

    //
//


    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(0, 0, PANEL_SIZE, PANEL_SIZE);
        paintChildren(g);
        paintComponent(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //Drawing candies
        int[][] board = engine.getBoard();
        int boardSize = engine.BOARD_SIZE;
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(255, 0, 0));
        Point cursor = engine.cursor;
        g2d.drawRect(cursor.x * CandyTile.CANDY_SIZE, cursor.y * CandyTile.CANDY_SIZE, CandyTile.CANDY_SIZE, CandyTile.CANDY_SIZE);
        if (engine.isSelected()) {
            g2d.setColor(new Color(255, 0, 0, 50));
            g2d.fillRect(cursor.x * CandyTile.CANDY_SIZE, cursor.y * CandyTile.CANDY_SIZE, CandyTile.CANDY_SIZE, CandyTile.CANDY_SIZE);
        }
    }

    public void moveCandies(Point firstPos, Point secondPos) {
        CandyTile first = candyTiles[firstPos.x][firstPos.y];
        CandyTile second = candyTiles[secondPos.x][secondPos.y];
        candyTiles[secondPos.x][secondPos.y] = first;
        candyTiles[firstPos.x][firstPos.y] = second;
        Point firstLocation = new Point(first.getLocation());
        Thread t1 = first.moveToPos(second.getLocation());
        Thread t2 = second.moveToPos(firstLocation);
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
