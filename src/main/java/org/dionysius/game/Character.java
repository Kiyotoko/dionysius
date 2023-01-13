package org.dionysius.game;

import java.util.ArrayList;
import java.util.List;

import org.dionysius.grpc.StatusReply;
import org.dionysius.game.Area.Circle;
import org.dionysius.game.Item.Bow;
import org.dionysius.game.Item.Sword;
import org.dionysius.grpc.Corresponding;
import org.dionysius.grpc.Observable;
import org.dionysius.grpc.PushHelper;

public abstract class Character extends Entity implements Corresponding<StatusReply.Character>, Observable {
	private final List<Item> items = new ArrayList<>(List.of(new Sword(this), new Bow(this)));

	private final Team team;

	private double hitPoints = 4;
	private String name;

	protected Character(Game game) {
		super(game);
		this.team = game.getRandomTeam();
		getGame().getCharacters().add(this);
		getTeam().getMembers().add(this);
		setPosition(team.getSpawnpoint());
		setBounds(new Circle(10));
	}

	@Override
	public void update(double deltaT) {
		move();
		for (Item item : getItems()) {
			item.cooldown();
		}
	}

	@Override
	public void changed() {
		for (PushHelper helper : getPushHelpers()) {
			helper.getCharacters().add(this);
		}
	}

	@Override
	public void destroy() {
		getGame().getCharacters().remove(this);
		super.destroy();
	}

	public Team getTeam() {
		return team;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setHitPoints(double hitPoints) {
		this.hitPoints = hitPoints;
		if (hitPoints > 0) {
			changed();
		} else {
			destroy();
		}
	}

	public double getHitPoints() {
		return hitPoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getContainer() {
		return "Character";
	}

	@Override
	public StatusReply.Character associated() {
		return org.dionysius.grpc.StatusReply.Character.newBuilder().setId(getId()).setName(name).setTeam(team.getId())
				.setHitPoints(hitPoints).setPosition(Corresponding.transform(getPosition())).build();
	}
}
