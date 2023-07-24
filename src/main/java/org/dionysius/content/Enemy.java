package org.dionysius.content;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.dionysius.game.AnimationFrame;
import org.dionysius.game.Creature;
import org.dionysius.game.Game;

import io.scvis.geometry.Vector2D;

import javax.annotation.Nonnull;

public class Enemy extends Creature {

	private final @Nonnull Stack<Runnable> patterns = new Stack<>();

	public Enemy(@Nonnull Game game, @Nonnull Vector2D position) {
		super(game, position);
		patterns.add(this::idle);
	}

	@Override
	protected void rotate() {
		List<AnimationFrame> images = getAnimations().get(getAnimationPlayed());
		int next = (getAnimationCount() + 1) % images.size();
		if (next < getAnimationCount() && getAnimationPlayed() != ANIMATION_DEATH) {
			Collections.rotate(patterns, 1);
			patterns.peek().run();
		} else {
			setAnimationCount(next);
		}
	}

	public Stack<Runnable> getPattern() {
		return patterns;
	}
}
