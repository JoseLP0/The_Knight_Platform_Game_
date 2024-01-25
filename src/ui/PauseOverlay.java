package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import util.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static util.Constants.UI.URMButtons.*;


public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private AudioOptions audioOptions;
    private UrmButton menuButton, replayButton,unpauseButton;


    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();
    }


    private void createUrmButtons() {
        int menuX = (int)(403 * Game.SCALE);
        int replayX = (int)(467 * Game.SCALE);
        int unpauseX = (int)(532 * Game.SCALE);
        int buttonY = (int)(365 * Game.SCALE);

        menuButton = new UrmButton(menuX, buttonY, URM_SIZE, URM_SIZE,2);
        replayButton = new UrmButton(replayX, buttonY, URM_SIZE, URM_SIZE,1);
        unpauseButton = new UrmButton(unpauseX, buttonY, URM_SIZE, URM_SIZE,0);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSED_BACKGROUND);
        backgroundWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
        backgroundHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = Game.GAME_HEIGHT / 2 - backgroundHeight / 2 - 20;
    }

    public void update() {
        //URM BUTTON
        menuButton.update();
        replayButton.update();
        unpauseButton.update();

        audioOptions.update();

    }


    public void draw(Graphics g) {
        //BACKGROUND
        g.drawImage(backgroundImg, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        //URM BUTTONS
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);


        audioOptions.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMousePressed(true);
        } else if (isIn(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
                playing.unpauseGame();
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseButton)) {
            if (unpauseButton.isMousePressed())
                playing.unpauseGame();
        } else {
            audioOptions.mouseReleased(e);
        }

        menuButton.resetBooleans();
        replayButton.resetBooleans();
        unpauseButton.resetBooleans();

    }

    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMouseOver(true);
        } else if (isIn(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    private boolean isIn(MouseEvent e, PausedButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}
