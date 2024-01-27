package util;

import static main.Game.SCALE;

public class Constants {

    public static  final float GRAVITY = 0.025f * SCALE;
    public static int ANIMATION_SPEED = 25;



    public static class ObjectConstants {
        public static final int SPIKE = 1;
        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (SCALE * SPIKE_HEIGHT_DEFAULT);
    }
    public static class EnemyConstants {
        public static final int GOLEM = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int GOLEM_WIDTH_DEFAULT = 64;
        public static final int GOLEM_HEIGHT_DEFAULT = 64;
        public static final int GOLEM_WIDTH = (int) (GOLEM_WIDTH_DEFAULT * SCALE * 1.2);
        public static final int GOLEM_HEIGHT = (int) (GOLEM_HEIGHT_DEFAULT * SCALE * 1.5);
        public static final int GOLEM_DRAWOFFSET_X = (int)(25 * SCALE);
        public static final int GOLEM_DRAWOFFSET_Y = (int)(40 * SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {

            switch (enemy_type) {
                case GOLEM:
                    switch (enemy_state) {
                        case IDLE:
                            return 9;
                        case RUNNING:
                            return 13;
                        case ATTACK:
                            return 19;
                        case HIT:
                            return 7;
                        case DEAD:
                            return 14;
                    }
            }

            return 0;

        }

        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case GOLEM:
                    return 100;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case GOLEM:
                    return 10;
                default:
                    return 0;
            }

        }


    }
    public static class Enviroment {
        public static final int CITY_IMG_WIDTH_DEFAULT = 320;
        public static final int CITY_IMG_HEIGHT_DEFAULT = 131;
        public static final int CITY_IMG_WIDTH = (int)(CITY_IMG_WIDTH_DEFAULT * 1.75 * SCALE);
        public static final int CITY_IMG_HEIGHT = (int)(CITY_IMG_HEIGHT_DEFAULT * 1.75 * SCALE);

        public static final int FOG_IMG_WIDTH_DEFAULT = 320;
        public static final int FOG_IMG_HEIGHT_DEFAULT = 30;
        public static final int FOG_IMG_WIDTH = (int)(FOG_IMG_WIDTH_DEFAULT * 2 * SCALE);
        public static final int FOG_IMG_HEIGHT = (int)(FOG_IMG_HEIGHT_DEFAULT * 2 * SCALE);


    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 156;
            public static final int B_HEIGHT_DEFAULT = 72;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * SCALE -10);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * SCALE - 10);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 44;
            public static final int SOUND_SIZE = (int)(38 * SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 44;
            public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * SCALE);
        }

        public static class VolumeButton {
            public static final int VOLUME_DEFAULT_WIDTH = 25;
            public static final int VOLUME_DEFAULT_HEIGHT = 40;
            public static final int SLIDER_DEFAULT_WIDTH = 195;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * SCALE);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * SCALE);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * SCALE);

        }

    }
    public static class PlayerConstant {
        public static final int IDLE = 0;
        public static final int ATTACK = 1;
        public static final int RUNNING = 2;
        public static final int JUMP = 3;
        public static final int FALLING = 4;
        public static final int HIT = 5;
        public static final int DEATH = 6;

        public static class Directions {
            public static final int LEFT = 0;
            public static final int UP = 1;
            public static final int RIGHT = 2;
            public static final int DOWN = 3;
        }

        public static int getSpriteAmount(int player_action) {

            switch(player_action) {

                case DEATH:
                    return 33;
                case RUNNING:
                    return 8;
                case IDLE:
                    return 14;
                case JUMP:
                    return 8;
                case FALLING:
                    return 9;
                case HIT:
                    return 6;
                case ATTACK:
                    return 7;
                default:
                    return 1;
            }
        }

    }

}
