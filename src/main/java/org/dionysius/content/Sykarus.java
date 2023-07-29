package org.dionysius.content;

import java.io.File;
import java.util.Set;

import org.dionysius.game.Level;
import org.dionysius.game.ImageExtractor;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Sykarus extends Level {

	private static final TileMapLoader tileMap = new TileMapLoader(new File("src/main/resources/text/tilemap/Sykarus.json"));

	public Sykarus() {
		getTileMap().addAll(tileMap.getLoaded());
		setOnLoad(game -> {
			new DroidZapper(game);
			new Merchant(game);
			new ShieldDroid(game);
			new Hero(game);
		});
		setBackgroundColor(Color.WHEAT);
	}
}
