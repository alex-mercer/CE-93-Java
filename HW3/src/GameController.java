import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements KeyListener {

    private static final int FPS = 30;

    GamePanel panel;

    GameEngine engine;

    Thread gameloop;

    boolean running = false;

    boolean paused = false;

    private boolean downKey;

    public void init(GamePanel panel, GameEngine engine) {
        this.panel = panel;
        this.engine = engine;
    }

    public void start() {
        gameloop = new Thread(new Runnable() {

            @Override
            public void run() {
                running = true;
                while (running) {
                    if (paused == false) {
                        gameUpdate();
                        gameRender();
                    }

                    try {
                        Thread.sleep(1000 / FPS);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
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

    @Override
    public void keyPressed(KeyEvent e) {
        Point point = new Point(0, 0);
        switch (e.getKeyCode()) {
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
                engine.selectCursor();
        }
        engine.moveCursor(point);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                downKey = false;
                break;
        }
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