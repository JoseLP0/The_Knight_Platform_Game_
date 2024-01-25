package gamestates;

import main.Game;
import ui.AudioOptions;
import ui.PausedButton;
import ui.UrmButton;
import util.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static util.Constants.UI.URMButtons.URM_SIZE;

public class GameOptions extends State implements Statemethods {

    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int backgroundX;
    private int backgroundY;
    private int backgroundW;
    private int backgroundH;
    private UrmButton menuB;

    public GameOptions(Game game) {
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    private void loadButton() {
        int menuX = (int) (466 * Game.SCALE);
        int menuY = (int) (368 * Game.SCALE);

        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadImgs() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgroundImg = LoadSave.getSpriteAtlas(LoadSave.OPTIONS_BACKGROUND  );

        backgroundW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
        backgroundH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundW / 2;
        backgroundY = (int) (33 * Game.SCALE + 15);
    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, backgroundX, backgroundY, backgroundW, backgroundH, null);

        menuB.draw(g);
        audioOptions.draw(g);

    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
            }
        } else {
            audioOptions.mouseReleased(e);
        }

        menuB.resetBooleans();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);

        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Gamestate.state = Gamestate.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private boolean isIn(MouseEvent e, PausedButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}
