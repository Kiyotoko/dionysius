package org.dionysius.game;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import io.scvis.game.Entity;
import io.scvis.geometry.Vector2D;

public class Creature extends AnimatedGraphic {

	public static final byte ANIMATION_WALK = 10;
	public static final byte ANIMATION_RUN = 11;
	public static final byte ANIMATION_JUMP = 12;
	public static final byte ANIMATION_ATTACK_1 = 20;
	public static final byte ANIMATION_ATTACK_2 = 21;
	public static final byte ANIMATION_ATTACK_3 = 22;
	public static final byte ANIMATION_BLOCK = 30;
	public static final byte ANIMATION_ROLL = 31;
	public static final byte ANIMATION_EVADE = 32;
	public static final byte ANIMATION_DASH = 40;
	public static final byte ANIMATION_DEATH = Byte.MAX_VALUE;

	private final Health health = new Health(this, 20.0);

	private final List<Indicator> indicators = new ArrayList<>(
			List.of(health.asIndicator(), new Indicator.PositionIndicator(this)));

	public Creature(Game game, @Nonnull Vector2D position) {
		super(game, position);
		game.getCreatures().add(this);
	}

	@Override
	public void destroy() {
		super.destroy();
		getGame().getCreatures().remove(this);
	}

	public void damage(double value) {
		getHealth().setValue(getHealth().getValue() - value);
	}

	public void hit(Attack attack) {
		for (int i = 0; i < getGame().getEntities().size(); i++) {
			Entity entity = getGame().getEntities().get(i);
			if (entity instanceof Creature) {
				Creature creature = (Creature) entity;
				if (creature != this && attack.canHit(getDirection(), getPosition(), creature.getPosition())) {
					if (creature.isRolling()) {
						continue;
					} else if (creature.isBlocking()) {
						creature.damage(attack.getDamage() * 0.2);
						damage(attack.getDamage() * 0.2);
					} else if (creature.isEvading()) {
						creature.damage(attack.getDamage() * 1.2);
					} else {
						creature.damage(attack.getDamage());
					}
				}
			}
		}
	}

	public void walk() {
		if (hasHighterPriority(ANIMATION_WALK)) {
			setDestination(new Vector2D(getDirection(), 0));
			if (getAnimations().containsKey(ANIMATION_WALK))
				setAnimationPlayed(ANIMATION_WALK);
		}
	}

	public void run() {
		if (hasHighterPriority(ANIMATION_RUN)) {
			setDestination(new Vector2D(2.0 * getDirection(), 0));
			if (getAnimations().containsKey(ANIMATION_RUN))
				setAnimationPlayed(ANIMATION_RUN);
		}
	}

	public void dash() {
		if (hasHighterPriority(ANIMATION_DASH)) {
			setPosition(getPosition().add(125.0 * getDirection(), 0));
			if (getAnimations().containsKey(ANIMATION_DASH))
				setAnimationPlayed(ANIMATION_DASH);
		}
	}

	public void jump() {
		if (hasHighterPriority(ANIMATION_JUMP)) {
			setVelocity(new Vector2D(0, 20));
			if (getAnimations().containsKey(ANIMATION_JUMP))
				setAnimationPlayed(ANIMATION_JUMP);
		}
	}

	public void attack1() {
		if (hasHighterPriority(ANIMATION_ATTACK_1)) {
			if (getAnimations().containsKey(ANIMATION_ATTACK_1))
				setAnimationPlayed(ANIMATION_ATTACK_1);
		}
	}

	public void attack2() {
		if (hasHighterPriority(ANIMATION_ATTACK_2)) {
			if (getAnimations().containsKey(ANIMATION_ATTACK_2))
				setAnimationPlayed(ANIMATION_ATTACK_2);
		}
	}

	public void attack3() {
		if (hasHighterPriority(ANIMATION_ATTACK_3)) {
			if (getAnimations().containsKey(ANIMATION_ATTACK_3))
				setAnimationPlayed(ANIMATION_ATTACK_3);
		}
	}

	public void block() {
		if (hasHighterPriority(ANIMATION_BLOCK)) {
			if (getAnimations().containsKey(ANIMATION_BLOCK))
				setAnimationPlayed(ANIMATION_BLOCK);
		}
	}

	public void roll() {
		if (hasHighterPriority(ANIMATION_ROLL)) {
			setDestination(new Vector2D(.5 * getDirection(), 0));
			if (getAnimations().containsKey(ANIMATION_ROLL))
				setAnimationPlayed(ANIMATION_BLOCK);
		}
	}

	public void evade() {
		if (hasHighterPriority(ANIMATION_EVADE)) {
			setDestination(new Vector2D(-3 * getDirection(), 0));
			if (getAnimations().containsKey(ANIMATION_EVADE))
				setAnimationPlayed(ANIMATION_EVADE);
		}
	}

	public void death() {
		if (getAnimations().containsKey(ANIMATION_DEATH))
			setAnimationPlayed(ANIMATION_DEATH);
		else {
			destroy();
		}
	}

	public boolean isBlocking() {
		return getAnimationPlayed() == ANIMATION_BLOCK;
	}

	public boolean isRolling() {
		return getAnimationPlayed() == ANIMATION_ROLL;
	}

	public boolean isEvading() {
		return getAnimationPlayed() == ANIMATION_EVADE;
	}

	public List<Indicator> getIndicators() {
		return indicators;
	}

	public Health getHealth() {
		return health;
	}
}
