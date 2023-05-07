package org.dionysius.game;

import java.util.ArrayList;
import java.util.List;

import org.dionysius.content.Sykarus;

import io.scvis.game.Entity;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Game {

	private Pane render = new Pane();

	private final List<Entity> entities = new ArrayList<>();

	private final ObservableSet<Creature> creatures = FXCollections.observableSet();

	public Game() {
		Environment envi = Sykarus.SYKARUS_DAY;
		render.getChildren().add(envi.getRender());
		render.setBackground(new Background(new BackgroundFill(envi.getBackgroundColor(), null, null)));

		creatures.addListener((Change<? extends Creature> change) -> {
			if (change.wasAdded())
				render.getChildren().add(change.getElementAdded().getMirror().getReflection());
			if (change.wasRemoved())
				render.getChildren().remove(change.getElementRemoved().getMirror().getReflection());
			System.out.println(change);
		});

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10.0), e -> {
			for (int index = 0; index < entities.size(); index++)
				entities.get(index).update(1.0);
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	public Pane getRender() {
		return render;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public ObservableSet<Creature> getCreatures() {
		return creatures;
	}
}