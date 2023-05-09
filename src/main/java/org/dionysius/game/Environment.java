package org.dionysius.game;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Environment {

	public static final byte DAYTIME_MORNING = 0;
	public static final byte DAYTIME_NOON = 1;
	public static final byte DAYTIME_EVENING = 2;
	public static final byte DAYTIME_NIGHT = 3;

	private Pane render = new Pane();

	private ObservableList<StaticGraphic> graphics = FXCollections.observableArrayList();

	private Color backgroundColor = Color.TRANSPARENT;

	public Environment() {
		graphics.addListener((Change<? extends StaticGraphic> change) -> {
			change.next();
			if (change.wasAdded())
				for (int i = 0; i < change.getAddedSize(); i++)
					render.getChildren().add(change.getAddedSubList().get(i).getMirror().getReflection());
			if (change.wasRemoved())
				for (int i = 0; i < change.getAddedSize(); i++)
					render.getChildren().add(change.getRemoved().get(i).getMirror().getReflection());
			System.out.println(change);
		});
	}

	public Pane getRender() {
		return render;
	}

	public ObservableList<StaticGraphic> getGraphics() {
		return graphics;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
