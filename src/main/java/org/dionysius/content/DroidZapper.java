package org.dionysius.content;

import java.util.List;

import org.dionysius.game.Creature;
import org.dionysius.game.Game;
import org.dionysius.game.ImageExtractor;

import io.scvis.geometry.Vector2D;
import javafx.scene.image.Image;

public class DroidZapper extends Creature {

	public DroidZapper(Game game) {
		super(game, new Vector2D(300, 0));

		ImageExtractor idle = new ImageExtractor(new Image("art/creature/zapper/AnimationIdle.png").getPixelReader(),
				3);
		getAnimations().put(ANIMATION_IDLE, List.of(//
				idle.extract(0, 0, 35, 41), //
				idle.extract(0, 42, 35, 82), //
				idle.extract(0, 83, 35, 123), //
				idle.extract(0, 124, 35, 164), //
				idle.extract(0, 165, 35, 205), //
				idle.extract(0, 206, 35, 246)//
		));
		flip(DIRECTION_LEFT);
		idle();
	}
}
