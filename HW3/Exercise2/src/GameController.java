import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements KeyListener {

    private static final int FPS = 30;

    GameBoard panel;

    GameEngine engine;
    JFrame frame;

    public GameController(JFrame frame) {
        this.frame = frame;
    }

    Thread gameloop;

    public void init(GameBoard panel, GameEngine engine) {
        this.panel = panel;
        this.engine = engine;
    }

    /**
     * Starts the main game loop
     */
    public void start() {
        gameloop = new Thread(new Runnable() {

            @Override
            public void run() {
                do {
                    try {
                        engine.processKey();
                        engine.processMouse();
                        if (engine.isGameOver()) {
                            Thread.sleep(2000);
                            frame.dispose();
                            if (JOptionPane.showConfirmDialog(null, "You lost\nYour score:" + engine.getScore() + "\nDo you want to play again?",
                                    "CandyCrush", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                CocoCrush.createAndShowGui();
                            }
                            break;
                        }
                        Thread.sleep(1000 / FPS);
                    } catch (InterruptedException e) {
                        //good bye
                        break;
                    }
                } while (true);
            }
        });

        gameloop.start();
    }

    /**
     * Handles key press if it's related to game logic it's passed to game engine otherwise handled directly
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            GameBoard.paused = !GameBoard.paused;
            panel.repaint();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                    "CandyCrush", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                gameloop.interrupt();
                frame.dispose();
            }
        }
        if (engine.locked) {
            return;
        }
        if (!GameBoard.paused)
            engine.keyEvent = e;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

}