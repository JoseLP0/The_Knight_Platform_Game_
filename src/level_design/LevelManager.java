package level_design;

import main.Game;
import util.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_SIZE;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importLevelSprites();
        levels = new ArrayList<>();
        buildAllLvls();
    }

    public void loadNextLvl() {
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }


    private void buildAllLvls() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage img : allLevels) {
            levels.add(new Level(img));
        }
    }

    public void importLevelSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[52];
        for (int j =0; j < 4; j++) {
            for (int i = 0; i < 13; i++) {
                int index = j * 13 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }


    public void draw(Graphics g, int lvlOffset) {
        for (int j= 0; j < Game.TILES_HEIGHT; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], TILES_SIZE * i - lvlOffset, TILES_SIZE * j, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    public void update() {
    }
    public Level getCurrentLvl() {
        return levels.get(lvlIndex);
    }
    public int getAmountOfLvls() {
        return levels.size();
    }
    public int getLvlIndex() {
        return lvlIndex;
    }
    public void setLevelIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }


}
