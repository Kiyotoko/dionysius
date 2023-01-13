package org.dionysius.graphic;

import org.dionysius.grpc.StatusReply;
import org.dionysius.grpc.Switching;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class Action extends Group implements Switching<StatusReply.Action> {

	@Override
	public void switched(org.dionysius.grpc.StatusReply.Action associated) {
		setLayoutX(associated.getPosition().getX());
		setLayoutY(associated.getPosition().getY());
	}

	public static class Arrow extends Action {
		public Arrow() {
			getChildren().add(new Circle(2));
		}
	}

}
