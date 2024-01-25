package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import util.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static util.Constants.ANIMATION_SPEED;
import static util.Constants.EnemyConstants.DEAD;
import static util.Constants.GRAVITY;
import static util.Constants.PlayerConstant.*;
import static util.Constants.PlayerConstant.Directions.*;
import static util.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animation;
    private boolean moving = false;
    private boolean attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset = 40 * SCALE;
    private float yDrawOffset = 35 * SCALE;


    private float jumpSpeed = -2.1f * SCALE;
    private float fallSpeedAfterCollision = 0.5f * SCALE;

    //STATUS BAR
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * SCALE);
    private int statusBarHeight = (int) (58 * SCALE);
    private int statusBarX = (int) (10 * SCALE);
    private int statusBarY = (int) (10 * SCALE);

    private int healthBarWidth = (int) (150 * SCALE);
    private int healthBarHeight = (int) (4 * SCALE);
    private int healthBarXStart = (int) (34 * SCALE);
    private int healthBarYStart = (int) (14 * SCALE);


    private int healthWidth = healthBarWidth;

    // AttackBox
    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    private int tileY = 0;



    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 50;
        this.currentHealth = maxHealth;
        this.walkSpeed = SCALE * 1.0f;
        loadAnimations();
        initHitbox(17,30);
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        //offset
        attackBox = new Rectangle2D.Float(x, y, (int) (45 * SCALE), (int) (20 * SCALE));
        resetAttackBox();
    }

    public void update() {
        updateHealthBar();

        if (currentHealth <= 0) {
            if (state != DEATH) {
                state = DEATH;
                animationTick = 0;
                animationIndex = 0;
                playing.setPLayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);

                // Check if player died in air
                if (!isEntityOnFloor(hitbox, lvlData)) {
                    inAir = true;
                    airSpeed = 0;
                }
            } else if (animationIndex == getSpriteAmount(DEAD) + 23 && animationTick >= ANIMATION_SPEED - 1) {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else {
                updateAnimationTick();

                // Fall if in air
                if (inAir) {
                    if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                        hitbox.y += airSpeed;
                        airSpeed += GRAVITY;
                    } else {
                        inAir = false;
                    }
                }

            }

            return;
        }


        updateAttackBox();

        if (state == HIT) {
            if (animationIndex <= getSpriteAmount(state) - 3) {
                pushBack(pushBackDir, lvlData, 1.55f);
            }
            updatePushBackDrawOffset();
        } else {
            updatePosition();
        }

        if (moving) {
            checkSpikesTouched();
            tileY = (int) (hitbox.y / TILES_SIZE);
        }

        if (attacking) {
            checkAttack();
        }

        updateAnimationTick();

        setAnimation();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }


    private void checkAttack() {
        if (attackChecked || animationIndex != 3) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if(right || left) {
            if (flipW == 1) {
                attackBox.x = hitbox.x + hitbox.width + (int) (SCALE * -5);
            } else {
                attackBox.x = hitbox.x - hitbox.width - (int) (SCALE * 22);
            }
        } else if (right) {
            attackBox.x = hitbox.x + hitbox.width + (int) (SCALE * -5);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int) (SCALE * 22);
        }

        attackBox.y = hitbox.y + (SCALE * 5);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animation[state][animationIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset) , width * flipW, height, null);
//      drawHitbox(g, lvlOffset);
//      drawAttackBox(g, lvlOffset);
      drawUI(g);
    }


    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }


    private void updateAnimationTick() {

        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(state)) {
                animationIndex = 0;
                attacking = false;
                attackChecked = false;
                if (state == HIT) {
                    newState(IDLE);
                    airSpeed = 0f;
                    if (!isFloor(hitbox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }


    public void setAnimation() {

        int startAnimation = state;

        if (state == HIT)
            return;

        if (moving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }

        if (attacking) {
            state = ATTACK;
            if (startAnimation != ATTACK) {
                animationIndex = 2;
                animationTick = 0;
                return;
            }
        }

        if (startAnimation != state) {
            resetAnimationTick();
        }

    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    public void updatePosition() {
        moving = false;

        if(jump) {
            jump();
        }

        if (!inAir) {
            if ((!left & !right) || (right & left)) {
                return;
            }
        }

        float xSpeed = 0;

        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }

        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir) {
            if(!isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPosition(xSpeed);
            } else {
                hitbox.y = getEntityYPositionUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                    updateXPosition(xSpeed);
                }
            }
        } else {
            updateXPosition(xSpeed);
        }
        moving = true;
    }

    private void jump() {
        if (inAir) {
            return;
        }
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPosition(float xSpeed) {
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = getEntityXPositionNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value) {
        if (value < 0) {
            if (state == HIT) {
                return;
            } else {
                newState(HIT);
            }
        }

        currentHealth += value;
        currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
    }

    public void changeHealth(int value, Enemy e) {
        if (state == HIT) {
            return;
        }
        changeHealth(value);
        pushBackOffsetDir = UP;
        pushDrawOffset = 0;

        if (e.getHitbox().x < hitbox.x) {
            pushBackDir = RIGHT;
        } else {
            pushBackDir = LEFT;
        }
    }



    public void kill() {
        currentHealth = 0;
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animation = new BufferedImage[7][33];
        for (int j = 0; j < animation.length; j++) {
            for (int i = 0; i < animation[j].length; i++) {
                animation[j][i] = img.getSubimage(i * 64, j * 63, 64, 64);
            }
        }

        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
            if (!isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
    }

    public void resetDirectionBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    public boolean getLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirectionBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        airSpeed = 0f;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        resetAttackBox();


        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    private void resetAttackBox() {
        if (flipW == 1) {
            attackBox.x = hitbox.x + hitbox.width + (int) (SCALE * -5);
        } else {
            attackBox.x = hitbox.x - hitbox.width - (int) (SCALE * 22);
        }
    }



}



















