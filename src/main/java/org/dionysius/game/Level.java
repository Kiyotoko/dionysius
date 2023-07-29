package org.dionysius.game;

import io.scvis.geometry.Vector2D;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Level {

	public static class Tile {
		private final @Nonnull ImageView view = new ImageView();

		private @Nonnull Vector2D position = Vector2D.ZERO;

		public Tile(@Nonnull Image image, int x, int y) {
			view.setImage(image);
			setPosition(new Vector2D(x, y));
		}

		public Tile(@Nonnull Image image) {
			view.setImage(image);
		}

		@Nonnull
		public ImageView getView() {
			return view;
		}

		public void setPosition(@Nonnull Vector2D position) {
			this.position = position;
			view.setLayoutX((int) position.getX());
			view.setLayoutY((int) position.getY());
		}

		@Nonnull
		public Vector2D getPosition() {
			return position;
		}
	}

	public static final @Nonnull Consumer<Game> NONE = (Game game) -> {};

	@Nonnull
	private final List<Tile> tileMap = new ArrayList<>();

	private Consumer<Game> onLoad = NONE;

	@Nonnull
	private Color backgroundColor = Color.TRANSPARENT;

	public Level() {

	}

	@Nonnull
	public List<Tile> getTileMap() {
		return tileMap;
	}

	public Consumer<Game> getOnLoad() {
		return onLoad;
	}

	public void setOnLoad(Consumer<Game> onLoad) {
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
