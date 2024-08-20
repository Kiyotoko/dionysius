/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.Kiyotoko.dionysius.game.creature;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import org.Kiyotoko.dionysius.Utils;
import org.Kiyotoko.dionysius.game.Entity;
import org.Kiyotoko.dionysius.game.Game;
import org.Kiyotoko.dionysius.game.animation.Animation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Creature implements Entity, Destroyable {

    private final @Nonnull Game game;
    private final @Nonnull Properties properties;

    // Graphics
    private final @Nonnull ImageView view = new ImageView();
    private final @Nonnull Health health = new Health(this, 20.0);
    private final @Nonnull Polygon hitBox = new Polygon();
    private final @Nonnull List<Animation> animations = new ArrayList<>();

    private @Nonnull Point2D velocity = Point2D.ZERO;
    private @Nonnull Point2D position = Point2D.ZERO;
    private Animation animationPlayed;

    public Creature(@Nonnull Game game, @Nonnull Properties properties) {
        this.game = game;
        this.properties = properties;
        for (var animation : getProperties().getAnimations()) {
            animations.add(new Animation(getView(), animation));
        }

        getGame().getForeground().getChildren().addAll(getView(), getHitBox());
        getGame().getEntities().add(this);
    }

    @Override
    public void update() {
        velocity();
        move();
        animate();
    }

    @Override
    public void destroy() {
        getGame().getForeground().getChildren().remove(getView());
        getGame().getEntities().remove(this);
    }

    public void velocity() {
        setVelocity(new Point2D(Utils.clamp(-35, 35, getVelocity().getX() + getVelocity().getX()),
                Utils.clamp(-35, 35, getVelocity().getY() - 1)));
    }

    public void move() {
        setPosition(new Point2D(Utils.clamp(0, 600, getPosition().getX()),
                Utils.clamp(0, 250, getPosition().getY() + getVelocity().getY())));
    }

    public void animate() {
        if (animationPlayed != null)
            animationPlayed.update();
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Nonnull
    public Properties getProperties() {
        return properties;
    }

    @Nonnull
    public ImageView getView() {
        return view;
    }

    @Nonnull
    public Health getHealth() {
        return health;
    }

    @Nonnull
    public Polygon getHitBox() {
        return hitBox;
    }

    @Nonnull
    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(@Nonnull Point2D velocity) {
        this.velocity = velocity;
    }

    @Nonnull
    public Point2D getPosition() {
        return position;
    }

    public void setPosition(@Nonnull Point2D position) {
        if (!position.equals(getPosition())) {
            this.position = position;
        }
    }

    public Animation getAnimationPlayed() {
        return animationPlayed;
    }

    public void setAnimationPlayed(int animation) {
        this.animationPlayed = animations.get(animation);
    }

    public static class Properties {
        private final List<Animation.Properties> animations = new ArrayList<>();

        public void preload() {
            for (var animation : animations) {
                animation.preload();
            }
        }

        public List<Animation.Properties> getAnimations() {
            return animations;
        }
    }
}
