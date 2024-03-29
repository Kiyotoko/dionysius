package org.dionysius.content;

import javafx.geometry.Point2D;
import org.dionysius.game.Level;

import java.io.File;

public class DarkForest extends Level {

    private static final TileMapLoader tileMap = new TileMapLoader(new File("src/main/resources/text/tilemap/DarkForest.json"));

    public DarkForest() {
        getTileMap().addAll(tileMap.getLoaded());
        setOnLoad(game -> {
            new DroidZapper(game, new Point2D(300, 0));
            new ShieldDroid(game, new Point2D(250, 0));
            new DroidZapper(game, new Point2D(350, 0));
            new ShieldDroid(game, new Point2D(400, 0));
            new Hero(game);
        });
    }
}
