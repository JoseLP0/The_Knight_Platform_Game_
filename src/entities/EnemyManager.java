package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import level_design.Level;
import util.LoadSave;

import static util.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] golemArray;
	private Level currentLevel;


	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		this.currentLevel = level;
	}


	public void update(int[][] lvlData) {
		boolean isAnyActive = false;
		for (Golem s : currentLevel.getGolems()) {
			if (s.isActive()) {
				s.update(lvlData, playing);
				isAnyActive = true;
			}
		}
		if (!isAnyActive) {
			playing.setLevelCompleted(true);
		}
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawGolem(g, xLvlOffset);
	}

	private void drawGolem(Graphics g, int xLvlOffset) {
		for (Golem s : currentLevel.getGolems()) {
			if (s.isActive()) {
				g.drawImage(golemArray[s.getState()][s.getAniIndex()], (int) s.getHitbox().x - xLvlOffset - GOLEM_DRAWOFFSET_X + s.flipX(), (int) s.getHitbox().y - GOLEM_DRAWOFFSET_Y + (int) s.getPushDrawOffset(), GOLEM_WIDTH * s.flipW(), GOLEM_HEIGHT, null);
//				s.drawHitbox(g, xLvlOffset);
//				s.drawAttackBox(g, xLvlOffset);
			}
		}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Golem s : currentLevel.getGolems()) {
			if (s.isActive()) {
				if (s.getState() != DEAD && s.getState() != HIT) {
					if (attackBox.intersects(s.getHitbox())) {
						s.hurt(10);
						return;
					}
				}
			}
		}
	}


	private void loadEnemyImgs() {
		golemArray = getImgArr(LoadSave.getSpriteAtlas(LoadSave.GOLEM_SPRITE), 20, 5, GOLEM_WIDTH_DEFAULT, GOLEM_HEIGHT_DEFAULT);
	}

	private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
		BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
		for (int j = 0; j < tempArr.length; j++) {
			for (int i = 0; i < tempArr[j].length; i++) {
				tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
			}
		}
		return tempArr;
	}


	public void resetAllEnemies() {
		for (Golem s : currentLevel.getGolems()) {
			s.resetEnemy();
		}
	}

}
