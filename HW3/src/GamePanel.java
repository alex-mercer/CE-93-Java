import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    public static final int CANDY_SIZE = 60;
    public static final int PANEL_SIZE = GameEngine.BOARD_SIZE * CANDY_SIZE;
    BufferedImage candyImages[] = new BufferedImage[GameEngine.CANDY_TYPES];
    GameEngine engine;

    GamePanel(final GameController controller, GameEngine engine) {
        setFocusable(true);
        requestFocus();
        setLayout(null);
        addKeyListener(controller);
        this.engine = engine;
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        try {
            for (int i = 0; i < GameEngine.CANDY_TYPES; i++)
                candyImages[i] = ImageIO.read(new File("/home/amin/Desktop/Courses/Advanced Programming/CE-93-Java/HW3/src/t" + (i + 1) + ".png"));
        } catch (IOException e) {

        }
    }

    //
//
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(0, 0, PANEL_SIZE, PANEL_SIZE);
        //Drawing candies!
        int[][] board = engine.getBoard();
        int boardSize = engine.BOARD_SIZE;
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                g2d.drawImage(candyImages[board[i][j]], i * CANDY_SIZE, j * CANDY_SIZE, null);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(255, 0, 0));
        Point cursor = engine.cursor;
        g2d.drawRect((int) cursor.getX() * CANDY_SIZE, (int) cursor.getY() * CANDY_SIZE, CANDY_SIZE, CANDY_SIZE);
        if(engine.isSelected())
        {
            g2d.setColor(new Color(255, 0, 0,50));
            g2d.fillRect((int) cursor.getX() * CANDY_SIZE, (int) cursor.getY() * CANDY_SIZE, CANDY_SIZE, CANDY_SIZE);
        }
    }
}
