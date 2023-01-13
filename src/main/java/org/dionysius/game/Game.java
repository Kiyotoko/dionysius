package org.dionysius.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dionysius.grpc.PushHelper;

import javafx.geometry.Point2D;

public class Game {
	private final Clock clock = new Clock(new Runnable() {
		@Override
		public void run() {
			for (int e = 0; e < entities.size(); e++) {
				entities.get(e).update(1);
			}
		}
	}, 5);

	private final Map<Player, PushHelper> pushHelpers = new HashMap<>();

	private final List<Entity> entities = new ArrayList<>();
	private final List<Team> teams = new ArrayList<>();
	private final List<Character> characters = new ArrayList<>();
	private final List<Action> actions = new ArrayList<>();
	private final List<Area> areas = new ArrayList<>();

	public Game() {
		MapPack pack = null;
		try {
			pack = MapPack.load("src/main/resources/default.xml");
		} catch (IOException e) {
			pack = new MapPack(areas, teams);
			e.printStackTrace();
		}

		this.teams.addAll(pack.getTeams());
		this.areas.addAll(pack.getAreas());
		for (int i = 0; i < 2; i++) {
			new NPC(this);
		}
	}

	public boolean available(Point2D point) {
		for (Area area : areas)
			if (area.contains(point))
				return true;
		return false;
	}

	public Clock getClock() {
		return clock;
	}

	public Map<Player, PushHelper> getPushHelpers() {
		return pushHelpers;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Team getRandomTeam() {
		return Area.pick(teams, (a, b) -> (int) (a.getMembers().size() - b.getMembers().size()));
	}

	public List<Team> getTeams() {
		return teams;
	}

	public List<Character> getCharacters() {
		return characters;
	}

	public List<Action> getActions() {
		return actions;
	}

	public List<Area> getAreas() {
		return areas;
	}
}