package entities;

import gamestates.Playing;

import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static util.Constants.ANIMATION_SPEED;
import static util.Constants.EnemyConstants.*;
import static util.Constants.GRAVITY;
import static util.Constants.PlayerConstant.Directions.*;
import static util.HelpMethods.*;

public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkDirection = LEFT;
    protected int tileY;
    protected float attackDistance = TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = SCALE * 0.10f;
        ANIMATION_SPEED = 10;
    }

    protected  void updateAttackBox() {
        if (walkDirection == RIGHT) {
            attackBox.x = hitbox.x + hitbox.width + (int) (SCALE * 0.9);
        } else if (walkDirection == LEFT) {
            attackBox.x = hitbox.x - hitbox.width - (int) (SCALE * 11);
        }

        attackBox.y = hitbox.y + (SCALE * 10);
    }

    protected void initAttackBox() {
        //offset
        attackBox = new Rectangle2D.Float(x, y, (int) (60 * SCALE), (int) (25 * SCALE));
    }


    protected void firstUpdateCheck(int[][] lvlData) {
        if (!isEntityOnFloor(hitbox,lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void inAirChecks(int[][] lvlData, Playing playing) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouched(this);
        }
    }


    protected void updateInAir(int[][] lvlData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = getEntityYPositionUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int)(hitbox.y / TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDirection == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }
        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (isFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }
        changeWalkDirection();
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x) {
            walkDirection = RIGHT;
        } else {
            walkDirection = LEFT;
        }
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int)(player.getHitbox().y / TILES_SIZE);
        if(playerTileY == tileY) {
            if(isPlayerInRange(player)) {
                if(isSightClear(lvlData, hitbox, player.hitbox, tileY)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }


    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            newState(DEAD);
        }
        else {
            newState(HIT);
            if (walkDirection == LEFT) {
                pushBackDir = RIGHT;
            } else {
                pushBackDir = LEFT;
            }
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox)) {
            player.changeHealth(-GetEnemyDmg(enemyType), this);
        }
        attackChecked = true;

    }


    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(enemyType, state)) {
                animationIndex = 0;

                switch (state) {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }


    protected void changeWalkDirection() {
        if (walkDirection == LEFT) {
            walkDirection = RIGHT;
        } else {
            walkDirection = LEFT;
        }
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;

        pushDrawOffset = 0;
    }

    public int flipX() {
        if (walkDirection == LEFT) {
            return width;
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (walkDirection == LEFT) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean isActive() {
        return active;
    }

    public float getPushDrawOffset() {
        return pushDrawOffset;
    }

}
