package org.dionysius.content;

import java.util.List;

import org.dionysius.game.AnimatedGraphic;
import org.dionysius.game.Creature;
import org.dionysius.game.Game;
import org.dionysius.game.ImageExtractor;

import io.scvis.geometry.Vector2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Hero extends Creature {

	public Hero(Game game) {
		super(game, new Vector2D(100, 0));
		game.getRender().getChildren().add(getHealth().getMirror().getReflection());

		ImageExtractor moving = new ImageExtractor(new Image("art/creature/hero/AnimationMoving.png").getPixelReader(),
				3);
		List<Image> movements = List.of( //
				moving.extract(0, 0, 56, 27), //
				moving.extract(0, 28, 56, 54), //
				moving.extract(0, 55, 56, 81), //
				moving.extract(0, 82, 56, 108)//
		);
		getAnimations().put(ANIMATION_IDLE, movements);
		getAnimations().put(ANIMATION_WALK, movements);
		getAnimations().put(ANIMATION_RUN, movements);

		ImageExtractor jump = new ImageExtractor(new Image("art/creature/hero/AnimationJump.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_JUMP, List.of( //
				jump.extract(0, 0, 56, 25), //
				jump.extract(0, 26, 56, 49), //
				jump.extract(0, 50, 56, 76), //
				jump.extract(0, 77, 56, 103), //
				jump.extract(0, 106, 56, 133), //
				jump.extract(0, 134, 56, 161), //
				jump.extract(0, 162, 56, 188)//
		));

		ImageExtractor dash = new ImageExtractor(new Image("art/creature/hero/AnimationDash.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_DASH, List.of(//
				dash.extract(28, 0, 90, 27), //
				dash.extract(28, 28, 90, 54), //
				dash.extract(28, 55, 90, 81), //
				dash.extract(28, 82, 90, 108)//
		));

		ImageExtractor attack1 = new ImageExtractor(
				new Image("art/creature/hero/AnimationAttack1.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_ATTACK_1, List.of( //
				attack1.extract(0, 0, 75, 27), //
				attack1.extract(0, 28, 75, 54), //
				attack1.extract(0, 55, 75, 81), //
				attack1.extract(0, 82, 75, 108), //
				attack1.extract(0, 109, 75, 135), //
				attack1.extract(0, 136, 75, 162), //
				attack1.extract(0, 163, 75, 189), //
				attack1.extract(0, 190, 75, 216), //
				attack1.extract(0, 217, 75, 243), //
				attack1.extract(0, 244, 75, 270), //
				attack1.extract(0, 271, 75, 297), //
				attack1.extract(0, 298, 75, 324) //
		));

		ImageExtractor attack2 = new ImageExtractor(
				new Image("art/creature/hero/AnimationAttack2.png").getPixelReader(), 3);
		getAnimations().put(ANIMATION_ATTACK_2, List.of( //
				attack2.extract(0, 0, 75, 27), //
				attack2.extract(0, 28, 75, 54), //
				attack2.extract(0, 55, 75, 81), //
				attack2.extract(0, 82, 75, 108), //
				attack2.extract(0, 109, 75, 135), //
				attack2.extract(0, 136, 75, 162), //
				attack2.extract(0, 163, 75, 189), //
				attack2.extract(0, 190, 75, 216), //
				attack2.extract(0, 217, 75, 243), //
				attack2.extract(0, 244, 75, 270), //
				attack2.extract(0, 271, 75, 297), //
				attack2.extract(0, 298, 75, 324), //
				attack2.extract(0, 325, 75, 351), //
				attack2.extract(0, 352, 75, 378) //
		));

		System.out.println(getAnimations());
	}

	public void applyTo(Scene node) {
		node.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.A)
				if (e.isShiftDown())
					run(DIRECTION_LEFT);
				else
					walk(AnimatedGraphic.DIRECTION_LEFT);
			if (e.getCode() == KeyCode.D)
				if (e.isShiftDown())
					run(DIRECTION_RIGHT);
				else
					walk(AnimatedGraphic.DIRECTION_RIGHT);
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
