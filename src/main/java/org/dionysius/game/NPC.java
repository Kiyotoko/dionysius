package org.dionysius.game;

import javafx.geometry.Point2D;

public class NPC extends Character {
	public NPC(Game game) {
		super(game);
		setName("Unit " + getId());
	}

	@Override
	public void update(double deltaT) {
		super.update(deltaT);
		setAutoCommand(deltaT);
	}

	private double rotation = 0;

	private void setAutoCommand(double deltaT) {
		var temp = rotation;
		for (double r = 0 /*TODO -Math.PI / 2*/; r < Math.PI; r += Math.PI / 2) {
			rotation += r;
			if (isFree()) {
				setDestination();
				break;
			}
			rotation = temp;
		}
	}

	private void setDestination() {
		setDestination(new Point2D(20 * Math.cos(rotation), 20 * Math.sin(rotation)));
	}

	private boolean isFree() {
		return getGame().available(getPosition().add(10 * Math.cos(rotation), 10 * Math.sin(rotation)));
	}
}
