package org.dionysius.graphic;

import org.dionysius.grpc.StatusReply;
import org.dionysius.grpc.Switching;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class Character extends Group implements Switching<StatusReply.Character> {
	private final Game game;

	private final StringProperty name = new SimpleStringProperty();

	Circle bound = new Circle(10);

	public Character(Game game) {
		this.game = game;
		Label label = new Label();
		label.layoutXProperty().bind(label.widthProperty().divide(2).multiply(-1));
		label.layoutYProperty().bind(label.heightProperty().multiply(-1));
		label.textProperty().bind(name);
		getChildren().addAll(bound, new Circle(4), label);
	}

	public Game getGame() {
		return game;
	}

	public StringProperty getName() {
		return name;
	}

	@Override
	public void switched(org.dionysius.grpc.StatusReply.Character associated) {
		StatusReply.Character reply = (StatusReply.Character) associated;
		setLayoutX(reply.getPosition().getX());
		setLayoutY(reply.getPosition().getY());
		name.set(associated.getName());
		bound.setFill(game.getTeams().get(associated.getTeam()).getColor().get());
	}
}
