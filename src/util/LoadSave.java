package util;


import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;


public class LoadSave {

    public static final String PLAYER_ATLAS = "/player/the-knight.png";
    public static final String LEVEL_ATLAS = "/level_design/tiles.png";
    public static final String PLAYING_BG_IMG = "/level_design/layer_1.png";
    public static final String CITY_IMG = "/level_design/layer_2.png";
    public static final String FOG_IMG = "/level_design/layer_3.png";
    public static final String MENU_BUTTONS = "/ui/menu-buttons.png";
    public static final String MENU_BACKGROUND = "/ui/menu.png";
    public static final String MENU_BACKGROUND_IMG = "/ui/Background.png";
    public static final String PAUSED_BACKGROUND = "/ui/paused.png";
    public static final String SOUND_BUTTONS = "/ui/sound-buttons.png";
    public static final String URM_BUTTONS = "/ui/urm-buttons.png";
    public static final String VOLUME_BUTTONS = "/ui/volume_buttons.png";
    public static final String GOLEM_SPRITE = "/player/golem.png";
    public static final String STATUS_BAR = "/ui/health_bar.png";
    public static final String COMPLETED_IMG = "/ui/completed.png";
    public static final String TRAP_IMG =  "/level_design/spikes.png";
    public static final String DEATH_BACKGROUND = "/ui/death.png";
    public static final String OPTIONS_BACKGROUND  = "/ui/options.png";
    public static final String GAME_COMPLETED = "/ui/game_completed.png";




    public static BufferedImage getSpriteAtlas(String filename) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream(filename);

        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] getAllLevels() {
        URL url = LoadSave.class.getResource("/game-levels");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }
            }
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }





}


















