package entities;

import static util.Constants.EnemyConstants.*;
import static util.HelpMethods.isFloor;

import gamestates.Playing;


public class Golem extends Enemy {

	// AttackBox

	public Golem(float x, float y) {
		super(x, y, GOLEM_WIDTH, GOLEM_HEIGHT, GOLEM);
		initHitbox(20,35);
		initAttackBox();
	}


	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBox();
	}


	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate) {
			firstUpdateCheck(lvlData);
		}

		if (inAir) {
			inAirChecks(lvlData, playing);
		} else {
			switch (state) {
				case IDLE:
					if (isFloor(hitbox, lvlData)) {
						newState(RUNNING);
					} else {
						inAir = true;
					}
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, playing.getPlayer())) {
						turnTowardsPlayer(playing.getPlayer());
						if (isPlayerCloseForAttack(playing.getPlayer())) {
							newState(ATTACK);
						}
					}
					move(lvlData);

					break;
				case ATTACK:
					if (animationIndex == 0) {
						attackChecked = false;
					}
					if (animationIndex == 12 && !attackChecked) {
						checkPlayerHit(attackBox, playing.getPlayer());
					}
					break;
				case HIT:
					if (animationIndex <= GetSpriteAmount(enemyType, state) - 2) {
						pushBack(pushBackDir, lvlData, 2f);
					}
					updatePushBackDrawOffset();
					break;
			}
		}
	}
}