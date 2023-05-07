package org.dionysius.game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Environment {

	public static final byte DAYTIME_MORNING = 0;
	public static final byte DAYTIME_NOON = 1;
	public static final byte DAYTIME_EVENING = 2;
	public static final byte DAYTIME_NIGHT = 3;

	private Pane render = new Pane();

	private ObservableSet<StaticGraphic> graphics = FXCollections.observableSet();

	private Color backgroundColor = Color.TRANSPARENT;

	public Environment() {
		graphics.addListener((Change<? extends StaticGraphic> change) -> {
			if (change.wasAdded())
				render.getChildren().add(change.getElementAdded().getMirror().getReflection());
			if (change.wasRemoved())
				render.getChildren().remove(change.getElementRemoved().getMirror().getReflection());
			System.out.println(change);
		});
	}

	public Pane getRender() {
		return render;
	}

	public ObservableSet<StaticGraphic> getGraphics() {
		return graphics;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
