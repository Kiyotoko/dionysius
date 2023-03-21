package org.dionysius.graphic;

import org.dionysius.grpc.StatusReply;
import org.dionysius.grpc.Switching;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
		label.setTextFill(Color.WHITE);
		label.setFont(Font.font("Ubuntu", FontWeight.BOLD, 13));
		bound.setStrokeDashOffset(3);
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
		setLayoutX((int) reply.getPosition().getX());
		setLayoutY((int) reply.getPosition().getY());
		name.set(associated.getName());
		bound.setStroke(game.getTeams().get(associated.getTeam()).getColor().get());
	}
}
