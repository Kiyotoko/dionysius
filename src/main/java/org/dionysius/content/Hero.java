package org.dionysius.content;

import java.util.List;
import java.util.function.Consumer;

import org.dionysius.game.AnimatedGraphic;
import org.dionysius.game.AnimationFrame;
import org.dionysius.game.Attack;
import org.dionysius.game.Creature;
import org.dionysius.game.Game;
import org.dionysius.game.ImageExtractor;
import org.dionysius.game.Indicator.BarIndicator;

import io.scvis.geometry.Vector2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Hero extends Creature {

	@SuppressWarnings("unchecked")
	public Hero(Game game) {
		super(game, new Vector2D(100, 0));
		getHealth().setMax(60.0);
		getHealth().setValue(60.0);
		getHealth().getIndicator().setClipp(new Vector2D(10, 10));
		getHealth().getIndicator().setSize(BarIndicator.MEDIUM_SIZE);

		ImageExtractor moving = new ImageExtractor(new Image("art/creature/hero/AnimationMoving.png").getPixelReader(),
				3);
		List<AnimationFrame<AnimatedGraphic>> movements = List.of( //
				new AnimationFrame<>(moving.extract(0, 0, 56, 27), 10), //
				new AnimationFrame<>(moving.extract(0, 28, 56, 54), 10), //
				new AnimationFrame<>(moving.extract(0, 55, 56, 81), 10), //
				new AnimationFrame<>(moving.extract(0, 82, 56, 108), 10)//
		);
		getAnimations().put(ANIMATION_IDLE, movements);
		getAnimations().put(ANIMATION_WALK, movements);
		getAnimations().put(ANIMATION_RUN, movements);

		ImageExtractor jump = new ImageExtractor(new Image("art/creature/hero/AnimationJump.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_JUMP, List.of( //
				new AnimationFrame<>(jump.extract(0, 0, 56, 25), 10), //
				new AnimationFrame<>(jump.extract(0, 26, 56, 49), 10), //
				new AnimationFrame<>(jump.extract(0, 50, 56, 76), 10), //
				new AnimationFrame<>(jump.extract(0, 77, 56, 103), 10), //
				new AnimationFrame<>(jump.extract(0, 106, 56, 133), 10), //
				new AnimationFrame<>(jump.extract(0, 134, 56, 161), 10), //
				new AnimationFrame<>(jump.extract(0, 162, 56, 188), 10)//
		));

		ImageExtractor dash = new ImageExtractor(new Image("art/creature/hero/AnimationDash.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_DASH, List.of(//
				new AnimationFrame<>(dash.extract(28, 0, 90, 27), 10), //
				new AnimationFrame<>(dash.extract(28, 28, 90, 54), 10), //
				new AnimationFrame<>(dash.extract(28, 55, 90, 81), 10), //
				new AnimationFrame<>(dash.extract(28, 82, 90, 108), 10)//
		));

		Consumer<Creature> near = new Attack(1, 80, 90).asConsumer();
		Consumer<Creature> range = new Attack(2, 130, 90).asConsumer();
		ImageExtractor attack1 = new ImageExtractor(
				new Image("art/creature/hero/AnimationAttack1.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_ATTACK_1, (List<AnimationFrame<AnimatedGraphic>>) (List<?>) List.of( //
				new AnimationFrame<>(attack1.extract(0, 0, 75, 27), 10), //
				new AnimationFrame<>(attack1.extract(0, 28, 75, 54), 10), //
				new AnimationFrame<>(attack1.extract(0, 55, 75, 81), 10, near), //
				new AnimationFrame<>(attack1.extract(0, 82, 75, 108), 10, near), //
				new AnimationFrame<>(attack1.extract(0, 109, 75, 135), 10, near), //
				new AnimationFrame<>(attack1.extract(0, 136, 75, 162), 10, near), //
				new AnimationFrame<>(attack1.extract(0, 163, 75, 189), 10), //
				new AnimationFrame<>(attack1.extract(0, 190, 75, 216), 10), //
				new AnimationFrame<>(attack1.extract(0, 217, 75, 243), 10), //
				new AnimationFrame<>(attack1.extract(0, 244, 75, 270), 10), //
				new AnimationFrame<>(attack1.extract(0, 271, 75, 297), 10, range), //
				new AnimationFrame<>(attack1.extract(0, 298, 75, 324), 10, range) //
		));

		Consumer<Creature> charge = new Attack(6, 130, 90).asConsumer();
		ImageExtractor attack2 = new ImageExtractor(
				new Image("art/creature/hero/AnimationAttack2.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_ATTACK_2, (List<AnimationFrame<AnimatedGraphic>>) (List<?>) List.of( //
				new AnimationFrame<>(attack2.extract(0, 0, 75, 27), 10), //
				new AnimationFrame<>(attack2.extract(0, 28, 75, 54), 10), //
				new AnimationFrame<>(attack2.extract(0, 55, 75, 81), 10), //
				new AnimationFrame<>(attack2.extract(0, 82, 75, 108), 10), //
				new AnimationFrame<>(attack2.extract(0, 109, 75, 135), 10, charge), //
				new AnimationFrame<>(attack2.extract(0, 136, 75, 162), 10), //
				new AnimationFrame<>(attack2.extract(0, 163, 75, 189), 10), //
				new AnimationFrame<>(attack2.extract(0, 190, 75, 216), 10), //
				new AnimationFrame<>(attack2.extract(0, 217, 75, 243), 10), //
				new AnimationFrame<>(attack2.extract(0, 244, 75, 270), 10), //
				new AnimationFrame<>(attack2.extract(0, 271, 75, 297), 10, charge), //
				new AnimationFrame<>(attack2.extract(0, 298, 75, 324), 10), //
				new AnimationFrame<>(attack2.extract(0, 325, 75, 351), 10), //
				new AnimationFrame<>(attack2.extract(0, 352, 75, 378), 10) //
		));
		idle();
		System.out.println(getAnimations());
	}

	public void applyTo(Scene node) {
		node.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.A) {
				flip(DIRECTION_LEFT);
				if (e.isShiftDown())
					run();
				else
					walk();
			}
			if (e.getCode() == KeyCode.D) {
				flip(DIRECTION_RIGHT);
				if (e.isShiftDown())
					run();
				else
					walk();
			}
			if (e.getCode() == KeyCode.SPACE)
				jump();
			if (e.getCode() == KeyCode.SHIFT)
				dash();
			if (e.getCode() == KeyCode.E)
				attack1();
			if (e.getCode() == KeyCode.F)
				attack2();
		});
		node.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D)
				idle();
		});
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
		});
	}
}
