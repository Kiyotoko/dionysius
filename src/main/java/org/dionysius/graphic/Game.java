package org.dionysius.graphic;

import java.util.HashMap;
import java.util.Map;

import org.dionysius.graphic.Action.Arrow;
import org.dionysius.grpc.Creator;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableMap;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;

public class Game extends Scene {
	private final HashMap<String, Creator<?>> creators = new HashMap<>(Map.of("Team", () -> new Team(), "Character",
			() -> new Character(this), "Area", () -> new Area(), "Arrow", () -> new Arrow()));

	private final Map<String, Map<String, ?>> flat = new HashMap<>();

	private final ObservableMap<String, Team> teams = FXCollections.observableHashMap();
	private final ObservableMap<String, Character> characters = FXCollections.observableHashMap();
	private final ObservableMap<String, Action> actions = FXCollections.observableHashMap();
	private final ObservableMap<String, Area> areas = FXCollections.observableHashMap();

	private final ObjectProperty<Player> player = new SimpleObjectProperty<>();

	private final Group constant = new Group(), entity = new Group();

	public Game(Group root) {
		super(root, 1024, 768 - 24, true, SceneAntialiasing.BALANCED);
		flat.putAll(Map.of("Character", characters, "Action", actions, "Area", areas));
		characters.addListener(getChangeListener(entity));
		actions.addListener(getChangeListener(entity));
		areas.addListener(getChangeListener(constant));
		
		root.getChildren().addAll(constant, entity);
		AnimationTimer timer = new AnimationTimer() {
			final long[] frameTimes = new long[100];
			int frameTimeIndex = 0;
			boolean arrayFilled = false;

			@Override
			public void handle(long now) {
				frameTimes[frameTimeIndex] = now;
				frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
				if (frameTimeIndex == 0) {
					arrayFilled = true;
				}
				if (arrayFilled) {
					player.getValue().view();
				}
			}
		};
		timer.start();

		setFill(Color.BLACK);
	}

	private MapChangeListener<String, Node> getChangeListener(Group group) {
		return (Change<? extends String, ? extends Node> change) -> {
			if (change.wasAdded())
				group.getChildren().add(change.getValueAdded());
			if (change.wasRemoved())
				group.getChildren().remove(change.getValueRemoved());
		};
	}

	@SuppressWarnings("unchecked")
	public <T> T save(java.util.Map<String, T> map, String id, String type) {
		T val;
		if (!map.containsKey(id)) {
			val = (T) creators.get(type).create();
			map.put(id, val);
		} else {
			val = map.get(id);
		}
		return val;
	}

	public ObservableMap<String, Team> getTeams() {
		return teams;
	}

	public Map<String, Character> getCharacters() {
		return characters;
	}

	public Map<String, Action> getActions() {
		return actions;
	}

	public ObservableMap<String, Area> getAreas() {
		return areas;
	}

	public ObjectProperty<Player> getPlayer() {
		return player;
	}

	public Map<String, Map<String, ?>> getFlat() {
		return flat;
	}
}
