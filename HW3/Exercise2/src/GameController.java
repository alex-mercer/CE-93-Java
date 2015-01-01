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

    public Thread getGameloop() {
        return gameloop;
    }

    Thread gameloop;

    boolean should_close = false;

    boolean paused = false;

    private boolean downKey;

    public void init(GameBoard panel, GameEngine engine) {
        this.panel = panel;
        this.engine = engine;
    }

    public void start() {
        gameloop = new Thread(new Runnable() {

            @Override
            public void run() {
                do {
                    try {
                        engine.processKey();
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

    private void gameUpdate() {
//        try {
//            engine.update();
//            if (downKey) {
//                for (int i = 0; i < 6; i++) {
//                    engine.update();
//                }
//            }
//        } catch (GameOverException e) {
//            running = false;
//            panel.showGameOverMessage();
//        }
    }

    private void gameRender() {
        panel.repaint();
    }

    public boolean isShould_close() {
        return should_close;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            GameBoard.paused = !GameBoard.paused;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                    "CandyCrush", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                gameloop.interrupt();
                should_close = true;
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
        // TODO Auto-generated method stub

    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

}