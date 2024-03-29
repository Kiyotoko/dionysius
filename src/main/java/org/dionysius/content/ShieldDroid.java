package org.dionysius.content;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import org.dionysius.game.AnimationFrame;
import org.dionysius.game.Creature;
import org.dionysius.game.Game;
import org.dionysius.game.ImageExtractor;

import javax.annotation.Nonnull;
import java.util.List;

public class ShieldDroid extends Enemy {
    public ShieldDroid(@Nonnull Game game, Point2D position) {
        super(game, position);
        getPattern().addAll(List.of(this::attack1, this::walk, this::attack1));
        getHitbox().setHeight(31);
        getHitbox().setWidth(28);

        ImageExtractor idle = new ImageExtractor(new Image("art/creature/shield droid/AnimationIdle.png").getPixelReader(), 3);
        getAnimations().put(ANIMATION_IDLE, List.of(
                new AnimationFrame(idle.extract(0, 0, 90, 31), 20)
        ));

        ImageExtractor walk = new ImageExtractor(new Image("art/creature/shield droid/AnimationWalk.png").getPixelReader(), 3);
        getAnimations().put(ANIMATION_WALK, List.of(
                new AnimationFrame(walk.extract(0, 0, 90, 31), 10),
                new AnimationFrame(walk.extract(0, 32, 90, 62), 10),

                new AnimationFrame(walk.extract(0, 63, 90, 93), 10),
                new AnimationFrame(walk.extract(0, 94, 90, 124), 10),

                new AnimationFrame(walk.extract(0, 125, 90, 155), 10),
                new AnimationFrame(walk.extract(0, 156, 90, 186), 10)
        ));
        
        ImageExtractor attack1 = new ImageExtractor(new Image("art/creature/shield droid/AnimationAttack1.png").getPixelReader(), 3);
        getAnimations().put(ANIMATION_ATTACK_1, List.of(
                new AnimationFrame(attack1.extract(0, 0, 90, 31), 10),
                new AnimationFrame(attack1.extract(0, 32, 90, 62), 10),

                new AnimationFrame(attack1.extract(0, 63, 90, 93), 10),
                new AnimationFrame(attack1.extract(0, 94, 90, 124), 10),

                new AnimationFrame(attack1.extract(0, 125, 90, 155), 10),
                new AnimationFrame(attack1.extract(0, 156, 90, 186), 10),

                new AnimationFrame(attack1.extract(0, 187, 90, 217), 10),
                new AnimationFrame(attack1.extract(0, 218, 90, 248), 10),

                new AnimationFrame(attack1.extract(0, 249, 90, 279), 10),
                new AnimationFrame(attack1.extract(0, 280, 90, 310), 10),

                new AnimationFrame(attack1.extract(0, 311, 90, 341), 10),
                new AnimationFrame(attack1.extract(0, 342, 90, 372), 10),

                new AnimationFrame(attack1.extract(0, 373, 90, 403), 10),
                new AnimationFrame(attack1.extract(0, 404, 90, 434), 10)
        ));

        ImageExtractor death = new ImageExtractor(new Image("art/creature/shield droid/AnimationDeath.png").getPixelReader(), 3);
        getAnimations().put(ANIMATION_DEATH, List.of(
                new AnimationFrame(death.extract(0, 0, 90, 31), 12),
                new AnimationFrame(death.extract(0, 32, 90, 62), 20),

                new AnimationFrame(death.extract(0, 63, 90, 93), 16),
                new AnimationFrame(death.extract(0, 94, 90, 124), 16),

                new AnimationFrame(death.extract(0, 125, 90, 155), 16),
                new AnimationFrame(death.extract(0, 156, 90, 186), 16, Creature::destroy)
        ));

        idle();
    }
}
