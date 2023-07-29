package org.dionysius.game;

import io.scvis.entity.Destroyable;
import io.scvis.entity.Entity;
import io.scvis.entity.Kinetic;
import io.scvis.geometry.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Creature implements Kinetic, Destroyable {

    public static final byte DIRECTION_LEFT = -1;
    public static final byte DIRECTION_RIGHT = 1;

    public static final byte ANIMATION_IDLE = 0;
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

    private final @Nonnull Game game;
    private final @Nonnull Health health = new Health(this, 20.0);
    private final @Nonnull Hitbox hitbox = new Hitbox(this, 20, 20);
    private final @Nonnull ImageView view = new ImageView();
    private final @Nonnull Pane pane = new Pane(view);
    private final @Nonnull List<Indicator> indicators = new ArrayList<>(
            List.of(health.asIndicator(), new Indicator.PositionIndicator(this)));

    public Creature(@Nonnull Game game, @Nonnull Vector2D position) {
        this.game = game;
        this.position = position;
        game.getEntities().add(this);
        game.getCreatures().add(this);
        // reposition();
    }

    @Nonnull
    public static Vector2D transform(double x, double y, double width, double height) {
        return new Vector2D(x - width * 0.5, 350.0 - y
                - height
        );
    }

    private static double clamp(double min, double max, double value) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    @Override
    public void update(double deltaT) {
        Kinetic.super.update(deltaT);
        animate(deltaT);
    }

    @Override
    public void destroy() {
        game.getEntities().remove(this);
        game.getCreatures().remove(this);
    }

    private byte direction = DIRECTION_RIGHT;

    public void flip(byte direction) {
        if (this.direction != direction) {
            view.setScaleX(direction);
            this.direction = direction;
        }
    }

    public void turn() {
        if (direction == DIRECTION_LEFT) {
            flip(DIRECTION_RIGHT);
        } else {
            flip(DIRECTION_LEFT);
        }
    }

    public byte getDirection() {
        return direction;
    }

    public void setDirection(byte direction) {
        this.direction = direction;
    }

    @Override
    public void accelerate(double deltaT) {
    }

    @Override
    public void velocitate(double deltaT) {
        setVelocity(new Vector2D(clamp(-35, 35, getVelocity().getX() + getVelocity().getX()),
                clamp(-35, 35, getVelocity().getY() + getDestination().getY() - deltaT)));
    }

    @Override
    public void displacement(double deltaT) {
        setPosition(new Vector2D(clamp(0, 600, getPosition().getX() + getDestination().getX()),
                clamp(0, 250, getPosition().getY() + getVelocity().getY())));
    }

    //#####################################################
    //#
    //#     Animations
    //#
    //#####################################################
    private final @Nonnull Map<Byte, List<AnimationFrame>> animations = new HashMap<>();
    private byte animationPlayed = -1;
    private int animationCount = -1;
    private double animationTime = 0;

    public void animate(double deltaT) {
        animationTime += deltaT;
        if (animationTime > getAnimationDuration()) {
            rotate();
            animationTime = 0;
        }
    }

    protected void rotate() {
        List<AnimationFrame> images = animations.get(animationPlayed);
        int next = (getAnimationCount() + 1) % images.size();
        if (next < animationCount && getAnimationPlayed() != ANIMATION_IDLE) {
            setAnimationPlayed(ANIMATION_IDLE);
        } else {
            setAnimationCount(next);
        }
    }

    private @Nonnull Vector2D velocity = Vector2D.ZERO;
    private @Nonnull Vector2D position;
    private @Nonnull Vector2D destination = Vector2D.ZERO;

    @Nonnull
    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(@Nonnull Vector2D velocity) {
        this.velocity = velocity;
    }

    @Nonnull
    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(@Nonnull Vector2D position) {
       if (!position.equals(getPosition())) {
            this.position = position;
            reposition();
       }
    }

    public void reposition() {
        Vector2D transformed = transform(position.getX(), position.getY(), pane.getWidth(), pane.getHeight());
        pane.setLayoutX(transformed.getX());
        pane.setLayoutY(transformed.getY());
        for (Indicator indicator : getIndicators()) {
            indicator.setPosition(position);
        }
    }

    @Nonnull
    public Vector2D getDestination() {
        return destination;
    }

    public void setDestination(@Nonnull Vector2D destination) {
        this.destination = destination;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Nonnull
    public ImageView getView() {
        return view;
    }

    @Nonnull
    public Map<Byte, List<AnimationFrame>> getAnimations() {
        return animations;
    }

    public byte getAnimationPlayed() {
        return animationPlayed;
    }

    public void setAnimationPlayed(byte animationPlayed) {
        if (!animations.containsKey(animationPlayed))
            return;
        if (this.animationPlayed != animationPlayed) {
            this.animationPlayed = animationPlayed;
            setAnimationCount(0);
        }
    }

    public int getAnimationCount() {
        return animationCount;
    }

    public void setAnimationCount(int animationCount) {
        this.animationCount = animationCount;
        Image next = getAnimationImage();
        if (view.getImage() != next) {
            view.setImage(next);
            getAnimationOnPlayed().accept(this);
        }
    }

    public Image getAnimationImage() {
        return animations.get(animationPlayed).get(animationCount).getImage();
    }

    public double getAnimationDuration() {
        return animations.get(animationPlayed).get(animationCount).getDuration();
    }

    public Consumer<Creature> getAnimationOnPlayed() {
        return animations.get(animationPlayed).get(animationCount).getOnPlayed();
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
                    }
                    if (creature.isBlocking()) {
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

    public void interact() {
        for (int i = 0; i < getGame().getEntities().size(); i++) {
            Entity entity = getGame().getEntities().get(i);
            if (entity instanceof Interactable) {
                if (entity instanceof Creature) {
                    Creature creature = (Creature) entity;
                    if (getPosition().distance(creature.getPosition()) < creature.getHitbox().getDiameter()) {
                        ((Interactable) entity).interact();
                    }
                } else {
                    ((Interactable) entity).interact();
                }
            }
        }
    }

    public void idle() {
        setDestination(Vector2D.ZERO);
        setAnimationPlayed(ANIMATION_IDLE);
    }

    public void walk() {
        if (hasHigherPriority(ANIMATION_WALK)) {
            setDestination(new Vector2D(getDirection(), 0));
            if (getAnimations().containsKey(ANIMATION_WALK))
                setAnimationPlayed(ANIMATION_WALK);
        }
    }

    public void run() {
        if (hasHigherPriority(ANIMATION_RUN)) {
            setDestination(new Vector2D(2.0 * getDirection(), 0));
            if (getAnimations().containsKey(ANIMATION_RUN))
                setAnimationPlayed(ANIMATION_RUN);
        }
    }

    public void halt()  {
        setDestination(Vector2D.ZERO);
    }

    public void dash() {
        if (hasHigherPriority(ANIMATION_DASH)) {
            setPosition(getPosition().add(125.0 * getDirection(), 0));
            if (getAnimations().containsKey(ANIMATION_DASH))
                setAnimationPlayed(ANIMATION_DASH);
        }
    }

    public void jump() {
        if (hasHigherPriority(ANIMATION_JUMP)) {
            setVelocity(new Vector2D(0, 20));
            if (getAnimations().containsKey(ANIMATION_JUMP))
                setAnimationPlayed(ANIMATION_JUMP);
        }
    }

    public void attack1() {
        if (hasHigherPriority(ANIMATION_ATTACK_1)) {
            if (getAnimations().containsKey(ANIMATION_ATTACK_1))
                setAnimationPlayed(ANIMATION_ATTACK_1);
        }
    }

    public void attack2() {
        if (hasHigherPriority(ANIMATION_ATTACK_2)) {
            if (getAnimations().containsKey(ANIMATION_ATTACK_2))
                setAnimationPlayed(ANIMATION_ATTACK_2);
        }
    }

    public void attack3() {
        if (hasHigherPriority(ANIMATION_ATTACK_3)) {
            if (getAnimations().containsKey(ANIMATION_ATTACK_3))
                setAnimationPlayed(ANIMATION_ATTACK_3);
        }
    }

    public void block() {
        if (hasHigherPriority(ANIMATION_BLOCK)) {
            if (getAnimations().containsKey(ANIMATION_BLOCK))
                setAnimationPlayed(ANIMATION_BLOCK);
        }
    }

    public void roll() {
        if (hasHigherPriority(ANIMATION_ROLL)) {
            setDestination(new Vector2D(.5 * getDirection(), 0));
            if (getAnimations().containsKey(ANIMATION_ROLL))
                setAnimationPlayed(ANIMATION_BLOCK);
        }
    }

    public void evade() {
        if (hasHigherPriority(ANIMATION_EVADE)) {
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

    public boolean isInAnimationIdle() {
        return getAnimationPlayed() == ANIMATION_IDLE;
    }

    public boolean hasHigherPriority(byte animation) {
        return animation > animationPlayed;
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

    @Nonnull
    public List<Indicator> getIndicators() {
        return indicators;
    }

    @Nonnull
    public Health getHealth() {
        return health;
    }

    @Nonnull
    public Hitbox getHitbox() {
        return hitbox;
    }

    @Nonnull
    public Pane getPane() {
        return pane;
    }
}
