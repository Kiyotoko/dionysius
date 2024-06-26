package org.dionysius.content;

import java.util.List;

import javafx.geometry.Point2D;
import org.dionysius.game.*;

import javafx.scene.image.Image;

public class DroidZapper extends Enemy {

	public DroidZapper(Game game, Point2D position) {
		super(game, position);
		getPattern().addAll(List.of(this::attack1, this::walk, this::turn));
		getHitbox().setWidth(35);
		getHitbox().setHeight(41);

		ImageExtractor idle = new ImageExtractor(new Image("art/creature/zapper/AnimationIdle.png").getPixelReader(),
				3);
		getAnimations().put(ANIMATION_IDLE, List.of(//
				new AnimationFrame(idle.extract(0, 0, 35, 41), 10), //
				new AnimationFrame(idle.extract(0, 42, 35, 82), 10), //
				new AnimationFrame(idle.extract(0, 83, 35, 123), 10), //
				new AnimationFrame(idle.extract(0, 124, 35, 164), 10), //
				new AnimationFrame(idle.extract(0, 165, 35, 205), 10), //
				new AnimationFrame(idle.extract(0, 206, 35, 246), 10)//
		));

		ImageExtractor moving = new ImageExtractor(
				new Image("art/creature/zapper/AnimationMoving.png").getPixelReader(), 3);
		List<AnimationFrame> movements = List.of(//
				new AnimationFrame(moving.extract(0, 0, 58, 41), 20), //
				new AnimationFrame(moving.extract(0, 42, 58, 82), 20), //
				new AnimationFrame(moving.extract(0, 83, 58, 123), 20), //
				new AnimationFrame(moving.extract(0, 124, 58, 164), 20), //
				new AnimationFrame(moving.extract(0, 165, 58, 205), 20), //
				new AnimationFrame(moving.extract(0, 206, 58, 246), 20)//
		);
		getAnimations().put(ANIMATION_WALK, movements);
		getAnimations().put(ANIMATION_RUN, movements);

		ImageExtractor attack1 = new ImageExtractor(
				new Image("art/creature/zapper/AnimationAttack1.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_ATTACK_1, List.of(//
				new AnimationFrame(attack1.extract(0, 0, 58, 41), 10), //
				new AnimationFrame(attack1.extract(0, 42, 58, 82), 10), //
				new AnimationFrame(attack1.extract(0, 83, 58, 123), 10), //
				new AnimationFrame(attack1.extract(0, 124, 58, 164), 10), //
				new AnimationFrame(attack1.extract(0, 165, 58, 205), 10, new Attack(3, 80, 90).asConsumer()), //
				new AnimationFrame(attack1.extract(0, 206, 58, 246), 10), //
				new AnimationFrame(attack1.extract(0, 247, 58, 287), 10), //
				new AnimationFrame(attack1.extract(0, 288, 58, 328), 10, new Attack(2, 80, 90).asConsumer()), //
				new AnimationFrame(attack1.extract(0, 329, 58, 369), 10), //
				new AnimationFrame(attack1.extract(0, 370, 58, 410), 10, new Attack(1, 80, 90).asConsumer())//
		));

		ImageExtractor death = new ImageExtractor(new Image("art/creature/zapper/AnimationDeath.png").getPixelReader(),
				3);
		getAnimations().put(ANIMATION_DEATH, List.of(//
				new AnimationFrame(death.extract(0, 0, 58, 41), 10), //
				new AnimationFrame(death.extract(0, 42, 58, 82), 14), //
				new AnimationFrame(death.extract(0, 83, 58, 123), 12), //
				new AnimationFrame(death.extract(0, 124, 58, 164), 12), //
				new AnimationFrame(death.extract(0, 165, 58, 205), 12), //
				new AnimationFrame(death.extract(0, 206, 58, 246), 12), //
				new AnimationFrame(death.extract(0, 247, 58, 287), 12), //
				new AnimationFrame(death.extract(0, 288, 58, 328), 20, Creature::destroy)//
		));

		flip(DIRECTION_LEFT);
		idle();
	}
}
