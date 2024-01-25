package ui;


import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import util.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameCompletedOverlay {
    private Playing playing;
    private BufferedImage img;
    private MenuButton quit;
    private int imgX, imgY, imgW, imgH;

    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() {
        quit = new MenuButton(Game.GAME_WIDTH / 2, (int) (254 * Game.SCALE), 2, Gamestate.MENU);
    }

    private void createImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.GAME_COMPLETED);
        imgW = (int) (img.getWidth() * Game.SCALE);
        imgH = (int) (img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (140 * Game.SCALE);

    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);

        quit.draw(g);
    }

    public void update() {
        quit.update();
    }

    private boolean isIn(MenuButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        quit.setMouseOver(false);

        if (isIn(quit, e)) {
            quit.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(quit, e)) {
            if (quit.isMousePressed()) {
                playing.resetAll();
                playing.resetGameCompleted();
                playing.setGamestate(Gamestate.MENU);

            }
        }

        quit.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(quit, e)) {
            quit.setMousePressed(true);
        }
    }
}
