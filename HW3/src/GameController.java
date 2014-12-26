import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements KeyListener {

    private static final int FPS = 30;

    GameBoard panel;

    GameEngine engine;

    Thread gameloop;

    boolean running = false;

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
                        Thread.sleep(1000 / FPS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            GameBoard.paused = !GameBoard.paused;
            return;
        }
        if (engine.locked) {
            return;
        }
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