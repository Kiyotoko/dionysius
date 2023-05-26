package org.dionysius.content;

import java.util.ArrayList;
import java.util.List;

import org.dionysius.game.AnimatedGraphic;
import org.dionysius.game.AnimationFrame;
import org.dionysius.game.Creature;
import org.dionysius.game.Game;

import io.scvis.geometry.Vector2D;

public class Enemy extends Creature {

	private final List<Runnable> patterns = new ArrayList<>();

	public Enemy(Game game, Vector2D position) {
		super(game, position);
		patterns.add(this::idle);
	}

	private int last;

	@Override
	protected void rotate() {
		List<AnimationFrame<AnimatedGraphic>> images = getAnimations().get(getAnimationPlayed());
		int next = (getAnimationCount() + 1) % images.size();
		if (next < getAnimationCount() && getAnimationPlayed() != ANIMATION_DEATH) {
			patterns.get(last++ % patterns.size()).run();
			System.out.println(last % patterns.size());
		} else {
			setAnimationCount(next);
		}
	}

	public List<Runnable> getPattern() {
		return patterns;
	}
}
