package ui;

import gamestates.Gamestate;
import gamestates.Playing;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.*;
import static util.Constants.UI.URMButtons.*;
import static util.LoadSave.*;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;



    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (425 * SCALE);
        int nextX = (int) (500 * SCALE);
        int y = (int) (245 * SCALE);
        next = new UrmButton(nextX, y, URM_DEFAULT_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_DEFAULT_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = getSpriteAtlas(COMPLETED_IMG);
        bgW = (int) (img.getWidth() * SCALE * 1.1);
        bgH = (int) (img.getHeight() * SCALE * 1.1);
        bgX = GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (105 * SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update() {
        next.update();
        menu.update();
    }

    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e)) {
            menu.setMouseOver(true);
        } else if (isIn(next, e)) {
            next.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
            }
        }
        else if (isIn(next, e)) {
            if (next.isMousePressed()) {
                playing.loadNextLvl();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        }

        menu.resetBooleans();
        next.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            menu.setMousePressed(true);
        } else if (isIn(next, e)) {
            next.setMousePressed(true);
        }
    }

}
