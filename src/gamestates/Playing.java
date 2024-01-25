package gamestates;

import entities.EnemyManager;

import entities.Player;
import level_design.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameCompletedOverlay;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import util.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static main.Game.*;
import static util.Constants.Enviroment.*;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private ObjectManager objectManager;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int)(0.3 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.6 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg;
    private BufferedImage cityImg, fogImg;

    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean gameCompleted;
    private boolean playerDying;

    public Playing(Game game) {
        super(game);
        initializeClasses();

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        cityImg = LoadSave.getSpriteAtlas(LoadSave.CITY_IMG);
        fogImg = LoadSave.getSpriteAtlas(LoadSave.FOG_IMG);

        calcLvlOffset();
        loadStartLvl();
    }

    public void loadNextLvl() {
        levelManager.setLevelIndex(levelManager.getLvlIndex() + 1);
        levelManager.loadNextLvl();
        player.setSpawn(levelManager.getCurrentLvl().getPlayerSpawn());
        resetAll();
    }

    private void loadStartLvl() {
        enemyManager.loadEnemies(levelManager.getCurrentLvl());
        objectManager.loadObjects(levelManager.getCurrentLvl());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLvl().getLvlOffset();
    }

    private void initializeClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (90*SCALE), (int) (90*SCALE), this);
        player.loadLvlData(levelManager.getCurrentLvl().getLvlData());
        player.setSpawn(levelManager.getCurrentLvl().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } else if (gameCompleted) {
            gameCompletedOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLvl().getLvlData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLvl().getLvlData());
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int differance = playerX - xLvlOffset;

        if (differance > rightBorder) {
            xLvlOffset += differance - rightBorder;
        } else if (differance < leftBorder) {
            xLvlOffset += differance - leftBorder;
        }

        xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        drawCity(g);

        levelManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);


        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0, GAME_WIDTH, GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        } else if (gameCompleted) {
            gameCompletedOverlay.draw(g);
        }
    }

    private void drawCity(Graphics g) {

        for (int i = 0; i < 4; i++) {
            g.drawImage(cityImg, 0 + i * CITY_IMG_WIDTH - (int)(xLvlOffset * 0.3), (int) (145 * SCALE), CITY_IMG_WIDTH, CITY_IMG_HEIGHT, null);
        }

        for (int i = 0; i < 4; i++) {
            g.drawImage(fogImg, 0 + i * FOG_IMG_WIDTH - (int)(xLvlOffset * 0.7), (int) (315 * SCALE), FOG_IMG_WIDTH, FOG_IMG_HEIGHT, null);
        }

        for (int i = 0; i < 4; i++) {
            g.drawImage(fogImg, 0 + i * FOG_IMG_WIDTH - (int)(xLvlOffset * 0.7), (int) (315 * SCALE), FOG_IMG_WIDTH, FOG_IMG_HEIGHT, null);
        }
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }


    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;

        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();

    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }
    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver) {
            gameOverOverlay.mousePressed(e);
        } else if (paused) {
            pauseOverlay.mousePressed(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mousePressed(e);
        } else if (gameCompleted) {
            gameCompletedOverlay.mousePressed(e);
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver) {
            gameOverOverlay.mouseReleased(e);
        } else if (paused) {
            pauseOverlay.mouseReleased(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mouseReleased(e);
        } else if (gameCompleted) {
            gameCompletedOverlay.mouseReleased(e);
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver) {
            gameOverOverlay.mouseMoved(e);
        } else if (paused) {
            pauseOverlay.mouseMoved(e);
        } else if (lvlCompleted) {
            levelCompletedOverlay.mouseMoved(e);
        } else if (gameCompleted) {
            gameCompletedOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_P:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver && !gameCompleted && !lvlCompleted) {
            if (paused) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    public void setLevelCompleted(boolean levelCompleted) {
        game.getAudioPlayer().lvlCompleted();
        if (levelManager.getLvlIndex() + 1 >= levelManager.getAmountOfLvls()) {
            // No more levels
            gameCompleted = true;
            levelManager.setLevelIndex(0);
            levelManager.loadNextLvl();
            resetAll();
            return;
        }
        this.lvlCompleted = levelCompleted;
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }
    public void unpauseGame() {
        paused = false;
    }
    public Player getPlayer() {
        return player;
    }
    public ObjectManager getObjectManager() {
        return objectManager;
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    public LevelManager getLevelManager() {
        return levelManager;
    }
    public void setPLayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
