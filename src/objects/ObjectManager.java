package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


import entities.Enemy;
import entities.Player;
import gamestates.Playing;
import util.LoadSave;
import level_design.Level;

import static util.Constants.ObjectConstants.SPIKE_HEIGHT;
import static util.Constants.ObjectConstants.SPIKE_WIDTH;


public class ObjectManager {

    private Playing playing;
    private BufferedImage spikeImg;
    private Level currentLevel;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurrentLvl();
        loadImgs();
    }

    public void checkSpikesTouched(Player p) {
        for (Spike s : currentLevel.getSpikes()) {
            if (s.getHitbox().intersects(p.getHitbox())) {
                p.kill();
            }
        }
    }

    public void checkSpikesTouched(Enemy e) {
        for (Spike s : currentLevel.getSpikes()) {
            if (s.getHitbox().intersects(e.getHitbox())) {
                e.hurt(200);
            }
        }
    }

    public void loadObjects(Level newLevel) {
        currentLevel = newLevel;
    }


    private void loadImgs() {
        spikeImg = LoadSave.getSpriteAtlas(LoadSave.TRAP_IMG);
    }

    public void update(int[][] lvlData, Player player) {
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawTraps(g, xLvlOffset);
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : currentLevel.getSpikes()) {
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getYDrawOffset()), (int) (SPIKE_WIDTH * 1.19), (int) (SPIKE_HEIGHT * 1.19), null);
        }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLvl());
    }
}
