package org.dionysius.game;

import javafx.scene.paint.Color;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Level {

	public static final @Nonnull Consumer<Game> NONE = (Game game) -> {};


	private final @Nonnull List<Tile> tileMap = new ArrayList<>();

	private @Nonnull Consumer<Game> onLoad = NONE;
	private @Nonnull Color backgroundColor = Color.TRANSPARENT;


	@Nonnull
	public List<Tile> getTileMap() {
		return tileMap;
	}

	@Nonnull
	public Consumer<Game> getOnLoad() {
		return onLoad;
	}

	public void setOnLoad(@Nonnull Consumer<Game> onLoad) {
		this.onLoad = onLoad;
	}

	@Nonnull
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(@Nonnull Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
