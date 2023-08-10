package org.dionysius.content;

import java.io.File;

import org.dionysius.game.Level;

public class FieldOfTheDead extends Level {

	private static final TileMapLoader tileMap = new TileMapLoader(new File("src/main/resources/text/tilemap/FieldOfTheDead.json"));

	public FieldOfTheDead() {
		getTileMap().addAll(tileMap.getLoaded());
		setOnLoad(game -> {
			new Merchant(game);
			new Hero(game);
		});
	}
}
