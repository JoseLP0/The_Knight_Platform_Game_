package level_design;

import entities.Golem;
import main.Game;
import objects.Spike;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static util.Constants.EnemyConstants.GOLEM;
import static util.Constants.ObjectConstants.SPIKE;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Golem> golems = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        loadLevel();
        calcLvlOffsets();
    }

    private void loadLevel() {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
        }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50) {
            lvlData[y][x] = 0;
        } else {
            lvlData[y][x] = redValue;
        }
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case GOLEM -> golems.add(new Golem(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
        }
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }



    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
    public ArrayList<Golem> getGolems() {
        return golems;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }


}
