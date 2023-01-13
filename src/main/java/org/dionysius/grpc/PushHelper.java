package org.dionysius.grpc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dionysius.game.Action;
import org.dionysius.game.Area;
import org.dionysius.game.Character;
import org.dionysius.game.Game;
import org.dionysius.game.Team;

public class PushHelper implements Corresponding<StatusReply> {
	private final Set<Team> teams = new HashSet<>();
	private final Set<Character> characters = new HashSet<>();
	private final Set<Action> actions = new HashSet<>();
	private final Set<Area> areas = new HashSet<>();

	private final Map<String, String> destroyed = new HashMap<>();

	public PushHelper(Game game) {
		init(game);
	}

	public void init(Game game) {
		teams.addAll(game.getTeams());
		characters.addAll(game.getCharacters());
		actions.addAll(game.getActions());
		areas.addAll(game.getAreas());
	}

	public void clean() {
		teams.clear();
		characters.clear();
		actions.clear();
		areas.clear();

		destroyed.clear();
	}

	public Set<Team> getTeams() {
		return teams;
	}

	public Set<Character> getCharacters() {
		return characters;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public Set<Area> getAreas() {
		return areas;
	}

	public Map<String, String> getDestroyed() {
		return destroyed;
	}

	@Override
	public StatusReply associated() {
		StatusReply reply = StatusReply.newBuilder().addAllTeams(Corresponding.transform(teams))
				.addAllCharacters(Corresponding.transform(getCharacters()))
				.addAllActions(Corresponding.transform(getActions())).addAllAreas(Corresponding.transform(getAreas()))
				.putAllDestroyed(destroyed).build();
		clean();
		return reply;
	}
}
