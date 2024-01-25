package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static util.Constants.PlayerConstant.Directions.*;
import static util.HelpMethods.canMoveHere;

public abstract class Entity {

    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rectangle2D.Float hitbox;
    protected int animationTick;
    protected int animationIndex;

    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;
    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit) {
                pushBackOffsetDir = DOWN;
            }

        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0) {
                pushDrawOffset = 0;
            }
        }
    }

    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if (pushBackDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if (canMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed * speedMulti;
        }
    }

    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float( x, y, (int)(width * SCALE), (int)(height * SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getState() {
        return state;
    }

    public int getAniIndex() {
        return animationIndex;
    }


    protected void newState(int state) {
        this.state = state;
        animationTick = 0;
        animationIndex = 0;
    }

}
