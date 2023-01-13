package org.dionysius.game;

import java.util.Collection;

import org.dionysius.game.Area.Bounds;
import org.dionysius.grpc.Destroyable;
import org.dionysius.grpc.Observable;
import org.dionysius.grpc.PushHelper;

import javafx.geometry.Point2D;

public abstract class Entity extends Physical implements Observable, Destroyable {
	private final Game game;

	private double theta, magnitude;

	protected Entity(Game game) {
		this.game = game;
		game.getEntities().add(this);
	}

	public abstract void update(double deltaT);

	public boolean move() {
		if (magnitude > 0) {
			Point2D next = getPosition().add(getDestination());
			if (getGame().available(next)) {
				setPosition(new Vector(next.getX(), next.getY()));
				changed();
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		getGame().getEntities().remove(this);
		for (PushHelper helper : getPushHelpers()) {
			helper.getDestroyed().put(getContainer(), getId());
		}
	}

	public Game getGame() {
		return game;
	}

	private Bounds bounds = Bounds.NONE;

	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	public Bounds getBounds() {
		return bounds;
	}

	public Vector getDestination() {
		return new Vector(Math.sin(theta) * magnitude, Math.cos(theta) * magnitude);
	}

	public void setDestination(Point2D destination) {
		theta = Math.atan2(destination.getX(), destination.getY());
		magnitude = Math.min(Math.abs(destination.magnitude()), 1);
	}

	@Override
	public Collection<PushHelper> getPushHelpers() {
		return getGame().getPushHelpers().values();
	}
}
