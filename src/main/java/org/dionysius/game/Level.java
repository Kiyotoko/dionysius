package org.dionysius.game;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Level extends Pane {

	public static final Consumer<Game> NONE = (Game game) -> {};

	public static  ImageView create(Image image, Number posX, Number posY) {
		ImageView view = new ImageView(image);
		view.setLayoutX(posX.intValue());
		view.setLayoutY(posY.intValue());
		return view;
	}

	@Nonnull
	private final ObservableList<ImageView> tileSets = FXCollections.observableArrayList();

	private Consumer<Game> onLoad = NONE;

	@Nonnull
	private Color backgroundColor = Color.TRANSPARENT;

	public Level() {
		tileSets.addListener((Change<? extends ImageView> change) -> {
			change.next();
			if (change.wasAdded())
				for (int i = 0; i < change.getAddedSize(); i++)
					getChildren().add(change.getAddedSubList().get(i));
			if (change.wasRemoved())
				for (int i = 0; i < change.getRemovedSize(); i++)
					getChildren().remove(change.getRemoved().get(i));
			System.out.println(change);
		});
	}

	@Nonnull
	public ObservableList<ImageView> getTileSets() {
		return tileSets;
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
