package org.dionysius.game;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import io.scvis.geometry.Vector2D;
import io.scvis.observable.InvalidationListener;
import io.scvis.observable.InvalidationListener.InvalidationEvent;
import io.scvis.observable.Observable;
import io.scvis.proto.Mirror;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StaticGraphic implements Observable<StaticGraphic> {

	private final ImageView view = new ImageView();

	private final Mirror<StaticGraphic, ImageView> mirror = new Mirror<StaticGraphic, ImageView>() {
		@Override
		public void update(StaticGraphic reference) {
			getReflection().setLayoutX((int) reference.getPosition().getX() - getReflection().getFitWidth() / 2);
			getReflection()
					.setLayoutY((int) -reference.getPosition().getY() - getReflection().getFitHeight() / 2 + 300);
		}
	};

	public StaticGraphic(Image image, int x, int y) {
		addInvalidationListener(observable -> mirror.update(this));
		mirror.setSource(this);
		mirror.setReflection(view);
		view.setImage(image);
		setPosition(new Vector2D(x, y));
		invalidated();
	}

	private final Set<InvalidationListener<StaticGraphic>> listeners = new HashSet<>();

	protected void invalidated() {
		final InvalidationEvent<StaticGraphic> event = new InvalidationEvent<>(this);
		for (InvalidationListener<StaticGraphic> listener : listeners) {
			listener.invalidated(event);
		}
	}

	@Override
	public void addInvalidationListener(InvalidationListener<StaticGraphic> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeInvalidationListener(InvalidationListener<StaticGraphic> listener) {
		listeners.add(listener);
	}

	public Mirror<StaticGraphic, ImageView> getMirror() {
		return mirror;
	}

	@Nonnull
	private Vector2D position = Vector2D.ZERO;

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(@Nonnull Vector2D position) {
		this.position = position;
	}
}
