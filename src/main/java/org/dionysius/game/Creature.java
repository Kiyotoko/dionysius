package org.dionysius.game;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import io.scvis.game.Entity;
import io.scvis.geometry.Vector2D;

public class Creature extends AnimatedGraphic {

	public static final byte ANIMATION_WALK = 10;
	public static final byte ANIMATION_RUN = 11;
	public static final byte ANIMATION_DASH = 12;
	public static final byte ANIMATION_JUMP = 13;
	public static final byte ANIMATION_ATTACK_1 = 20;
	public static final byte ANIMATION_ATTACK_2 = 21;
	public static final byte ANIMATION_ATTACK_3 = 22;
	public static final byte ANIMATION_BLOCK = 30;
	public static final byte ANIMATION_ROLL = 31;
	public static final byte ANIMATION_EVADE = 32;
	public static final byte ANIMATION_DEATH = Byte.MAX_VALUE;

	private final Health health = new Health(this, 20.0);

	private final List<Indicator> indicators = new ArrayList<>(
			List.of(health.asIndicator(), new Indicator.PositionIndicator(this)));

	public Creature(Game game, @Nonnull Vector2D position) {
		super(game, position);
		game.getCreatures().add(this);
		health.addChangeListener(e -> {
			if (e.getNew() <= 0)
				death();
		});
	}

	@Override
	public void destroy() {
		super.destroy();
		getGame().getCreatures().remove(this);
	}

	public void damage(double value) {
		getHealth().set(getHealth().get() - value);
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

	public void walk(byte direction) {
		if (isInAnimationIdle()) {
			setDestination(new Vector2D(direction, 0));
			if (getAnimations().containsKey(ANIMATION_WALK))
				setAnimationPlayed(ANIMATION_WALK);
			flip(direction);
		}
	}

	public void run(byte direction) {
		if (isInAnimationIdle()) {
			setDestination(new Vector2D(2 * direction, 0));
			if (getAnimations().containsKey(ANIMATION_RUN))
				setAnimationPlayed(ANIMATION_RUN);
			flip(direction);
		}
	}

	public void dash() {
		if (isInAnimationIdle()) {
			setPosition(getPosition().add(125 * getDirection(), 0));
			if (getAnimations().containsKey(ANIMATION_DASH))
				setAnimationPlayed(ANIMATION_DASH);
		}
	}

	public void jump() {
		if (isInAnimationIdle()) {
			setVelocity(new Vector2D(0, 20));
			if (getAnimations().containsKey(ANIMATION_JUMP))
				setAnimationPlayed(ANIMATION_JUMP);
		}
	}

	public void attack1() {
		if (isInAnimationIdle()) {
			hit(new Attack(4, 110, 90));
			if (getAnimations().containsKey(ANIMATION_ATTACK_1))
				setAnimationPlayed(ANIMATION_ATTACK_1);
		}
	}

	public void attack2() {
		if (isInAnimationIdle()) {
			hit(new Attack(3, 70, 125));
			if (getAnimations().containsKey(ANIMATION_ATTACK_2))
				setAnimationPlayed(ANIMATION_ATTACK_2);
		}
	}

	public void attack3() {
		if (isInAnimationIdle()) {
			hit(new Attack(8, 120, 90));
			if (getAnimations().containsKey(ANIMATION_ATTACK_3))
				setAnimationPlayed(ANIMATION_ATTACK_3);
		}
	}

	public void block() {
		if (isInAnimationIdle()) {
			if (getAnimations().containsKey(ANIMATION_BLOCK))
				setAnimationPlayed(ANIMATION_BLOCK);
		}
	}

	public void roll(byte direction) {
		if (isInAnimationIdle()) {
			setDestination(new Vector2D(.5 * direction, 0));
			if (getAnimations().containsKey(ANIMATION_ROLL))
				setAnimationPlayed(ANIMATION_BLOCK);
			flip(direction);
		}
	}

	public void evade() {
		if (isInAnimationIdle()) {
			setDestination(new Vector2D(-3 * getDirection(), 0));
			if (getAnimations().containsKey(ANIMATION_EVADE))
				setAnimationPlayed(ANIMATION_EVADE);
		}
	}

	public void death() {
		if (getAnimations().containsKey(ANIMATION_EVADE))
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
