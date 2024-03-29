package org.dionysius.content;

import java.util.*;

import javafx.geometry.Point2D;
import org.dionysius.game.AnimationFrame;
import org.dionysius.game.Creature;
import org.dionysius.game.Game;

import javax.annotation.Nonnull;

public class Enemy extends Creature {

	private final @Nonnull Deque<Runnable> patterns = new ArrayDeque<>();

	public Enemy(@Nonnull Game game, @Nonnull Point2D position) {
		super(game, position);
		patterns.add(this::idle);
	}

	@Override
	protected void rotate() {
		List<AnimationFrame> images = getAnimations().get(getAnimationPlayed());
		int next = (getAnimationCount() + 1) % images.size();
		if (next < getAnimationCount() && getAnimationPlayed() != ANIMATION_DEATH) {
			var first = patterns.removeFirst();
			patterns.addLast(first);
			first.run();
		} else {
			setAnimationCount(next);
		}
	}

	public Deque<Runnable> getPattern() {
		return patterns;
	}
}
