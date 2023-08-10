package org.dionysius.content;

import javafx.scene.paint.Color;
import org.dionysius.game.Level;

import java.io.File;

public class DarkForest extends Level {

    private static final TileMapLoader tileMap = new TileMapLoader(new File("src/main/resources/text/tilemap/DarkForest.json"));

    public DarkForest() {
        getTileMap().addAll(tileMap.getLoaded());
        setOnLoad(game -> {
            new DroidZapper(game);
            new ShieldDroid(game);
            new DroidZapper(game);
            new ShieldDroid(game);
            new Hero(game);
        });
        setBackgroundColor(Color.FIREBRICK);
    }
}
