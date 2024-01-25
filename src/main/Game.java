package main;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import util.LoadSave;

import java.awt.*;

public class Game implements Runnable {
    private GameWindow window;
    private GamePanel gamePanel;
    private Thread thread;
    private final int FPS_SET = 120;
    private final int UPD_SET = 200;

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;

    public final static int TILES_DEFAULT_SIZE = 38;
    public final static float SCALE  = 1.2f;
    public final static int TILES_WIDTH = 26;
    public final static int TILES_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE*SCALE);
    public final static int GAME_WIDTH = TILES_SIZE*TILES_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE*TILES_HEIGHT;

    public Game() {
        initializeClasses();

        gamePanel = new GamePanel(this);
        window = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initializeClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
    }

    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        switch(Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PlAYING:
                playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g){
        switch(Gamestate.state) {
            case MENU:
                menu.draw(g);
                break;
            case PlAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;
        }
    }


    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPD_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
           long currentTime = System.nanoTime();

           deltaU += (currentTime-previousTime) / timePerUpdate;
           deltaF += (currentTime-previousTime) / timePerFrame;
           previousTime = currentTime;

           if (deltaU >= 1) {
               update();
               updates++;
               deltaU--;
           }

           if (deltaF >= 1) {
               gamePanel.repaint();
               deltaF--;
               frames++;
           }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PlAYING) {
            playing.getPlayer().resetDirectionBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
    public GameOptions getGameOptions() {
        return gameOptions;
    }
    public AudioOptions getAudioOptions() {
        return audioOptions;
    }
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
