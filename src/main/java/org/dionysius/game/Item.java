package org.dionysius.game;

import org.dionysius.game.Action.Arrow;
import org.dionysius.grpc.Creator;

import javafx.geometry.Point2D;

public abstract class Item {
	private final Character character;

	public Item(Character character) {
		this.character = character;
	}

	public Character getCharacter() {
		return character;
	}

	private int cooldown;

	public abstract void activate(Point2D target);

	public boolean canActivated() {
		return !(cooldown > 0);
	}

	public void cooldown() {
		cooldown--;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getCooldown() {
		return cooldown;
	}

	public static class Sword extends Item {
		public Sword(Character character) {
			super(character);
		}

		@Override
		public void activate(Point2D target) {

		}
	}

	public static class Bow extends Item {
		private final Creator<Arrow> creator = () -> new Arrow(getCharacter());

		public Bow(Character character) {
			super(character);
		}

		@Override
		public void activate(Point2D target) {
			Arrow arrow = creator.create();
			arrow.setPosition(getCharacter().getPosition());
			arrow.setDestination(target);
			setCooldown(20);
		}
	}
}
