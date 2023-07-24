package org.dionysius.game;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.annotation.Nonnull;

public class Level extends Pane {

	public static final byte DAYTIME_MORNING = 0;
	public static final byte DAYTIME_NOON = 1;
	public static final byte DAYTIME_EVENING = 2;
	public static final byte DAYTIME_NIGHT = 3;

	public static  ImageView create(Image image, Number posX, Number posY) {
		ImageView view = new ImageView(image);
		view.setLayoutX(posX.intValue());
		view.setLayoutY(posY.intValue());
		return view;
	}

	@Nonnull
	private final ObservableList<ImageView> graphics = FXCollections.observableArrayList();

	@Nonnull
	private Color backgroundColor = Color.TRANSPARENT;

	public Level() {
		graphics.addListener((Change<? extends ImageView> change) -> {
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
	public ObservableList<ImageView> getGraphics() {
		return graphics;
	}

	@Nonnull
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(@Nonnull Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
