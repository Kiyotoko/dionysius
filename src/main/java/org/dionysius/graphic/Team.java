package org.dionysius.graphic;

import org.dionysius.grpc.StatusReply;
import org.dionysius.grpc.Switching;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class Team implements Switching<StatusReply.Team> {
	private final ObjectProperty<Color> color = new SimpleObjectProperty<>();

	public ObjectProperty<Color> getColor() {
		return color;
	}

	@Override
	public void switched(org.dionysius.grpc.StatusReply.Team associated) {
		color.set(Color.web(associated.getColor()));
	}
}
