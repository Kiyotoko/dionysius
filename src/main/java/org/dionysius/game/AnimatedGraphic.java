package org.dionysius.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import io.scvis.geometry.Border2D;
import io.scvis.geometry.Kinetic2D;
import io.scvis.geometry.Vector2D;
import io.scvis.observable.InvalidationListener;
import io.scvis.observable.InvalidationListener.InvalidationEvent;
import io.scvis.observable.Observable;
import io.scvis.proto.Mirror;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AnimatedGraphic extends Kinetic2D implements Observable<AnimatedGraphic> {

	public static final byte ANIMATION_IDLE = 0;

	public static final byte DIRECTION_LEFT = -1;
	public static final byte DIRECTION_RIGHT = 1;

	private final Game game;

	private final ImageView view = new ImageView();
	private final Map<Byte, List<Image>> animations = new HashMap<>();

	private final Mirror<AnimatedGraphic, Pane> mirror = new Mirror<AnimatedGraphic, Pane>(this, new Pane(view)) {
		@Override
		public void update(AnimatedGraphic reference) {
			getReflection().setLayoutX(reference.getPosition().getX() - getReflection().getWidth() / 2);
			getReflection().setLayoutY(-reference.getPosition().getY() - getReflection().getHeight() + 350);
		}
	};

	public AnimatedGraphic(Game game, @Nonnull Border2D local, @Nonnull Vector2D position) {
		super(local, position, 0.0, Vector2D.ZERO, Vector2D.ZERO);
		this.game = game;
		game.getEntities().add(this);
		addInvalidationListener(observable -> mirror.update(AnimatedGraphic.this));
		setAnimationPlayed(ANIMATION_IDLE);
	}

	private double animationTime = 0;

	@Override
	public void update(double deltaT) {
		super.update(deltaT);
		animationTime += deltaT;
		if (animationTime > 10) {
			upsert();
			animationTime -= 10;
		}
	}

	private final Set<InvalidationListener<AnimatedGraphic>> listeners = new HashSet<>();

	protected void invalidated() {
		final InvalidationEvent<AnimatedGraphic> event = new InvalidationEvent<>(this);
		for (InvalidationListener<AnimatedGraphic> listener : listeners) {
			listener.invalidated(event);
		}
	}

	@Override
	public void addInvalidationListener(InvalidationListener<AnimatedGraphic> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeInvalidationListener(InvalidationListener<AnimatedGraphic> listener) {
		listeners.remove(listener);
	}

	public void upsert() {
		List<Image> images = animations.get(animationPlayed);
		int next = (getAnimationCount() + 1) % images.size();
		if (next < animationCount && getAnimationPlayed() != ANIMATION_IDLE)
			setAnimationPlayed(ANIMATION_IDLE);
		else
			setAnimationCount(next);
	}

	private byte direction = DIRECTION_RIGHT;

	public void flip(byte direction) {
		if (this.direction != direction) {
			setPosition(getPosition().add(new Vector2D(view.getFitWidth() * direction / -2.0, 0)));
			view.setScaleX(direction);
			this.direction = direction;
		}
	}

	public void idle() {
		setDestination(Vector2D.ZERO);
		setAnimationPlayed(ANIMATION_IDLE);
	}

	public void setDirection(byte direction) {
		this.direction = direction;
	}

	public byte getDirection() {
		return direction;
	}

	private static double clamp(double min, double max, double value) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	@Override
	public void accelerate(double deltaT) {

	}

	@Override
	public void velocitate(double deltaT) {
		setVelocity(new Vector2D(clamp(0, 100, getVelocity().getX()), clamp(-10, 10, getVelocity().getY() - deltaT)));
	}

	@Override
	public void displacement(double deltaT) {
		setPosition(new Vector2D(clamp(0, 500, getPosition().getX() + getDestination().getX() + getVelocity().getX()),
				clamp(0, 200, getPosition().getY() + getVelocity().getY())));
		invalidated();
	}

	public Game getGame() {
		return game;
	}

	public ImageView getView() {
		return view;
	}

	public Map<Byte, List<Image>> getAnimations() {
		return animations;
	}

	public Mirror<AnimatedGraphic, Pane> getMirror() {
		return mirror;
	}

	private byte animationPlayed;
	private int animationCount;

	public void setAnimationPlayed(byte animationPlayed) {
		if (this.animationPlayed != animationPlayed) {
			this.animationPlayed = animationPlayed;
			this.animationCount = 0;
		}
	}

	public byte getAnimationPlayed() {
		return animationPlayed;
	}

	public boolean isInAnimationIdle() {
		return animationPlayed == ANIMATION_IDLE;
	}

	public void setAnimationCount(int animationCount) {
		if (this.animationCount != animationCount) {
			this.animationCount = animationCount;
			view.setImage(animations.get(animationPlayed).get(animationCount));
		}
	}

	public int getAnimationCount() {
		return animationCount;
	}
}
