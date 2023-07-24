package org.dionysius.game;

import io.scvis.geometry.Vector2D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.annotation.Nonnull;

public class Hitbox {

    private final @Nonnull Creature creature;

    private final DoubleProperty width = new SimpleDoubleProperty();
    private final DoubleProperty height = new SimpleDoubleProperty();

    public Hitbox(@Nonnull Creature creature, double width, double height) {
        this.creature = creature;
        setWidth(width);
        setHeight(height);
    }

    public boolean isInside(Vector2D v) {
        double minX = creature.getPosition().getX() - getWidth() / 2;
        double maxX = creature.getPosition().getX() + getWidth() / 2;
        if (minX < v.getX() && v.getX() < maxX) {
            double minY = creature.getPosition().getY() - getHeight();
            double maxY = creature.getPosition().getY();
            return minY < v.getY() && v.getY() < maxY;
        }
        return false;
    }

    public double getDiameter() {
        return Math.hypot(getWidth(), getHeight());
    }

    @Nonnull
    public Creature getCreature() {
        return creature;
    }

    public void setWidth(double width) {
        this.width.setValue(width);
    }

    public double getWidth() {
        return width.get();
    }

    public void setHeight(double height) {
        this.height.setValue(height);
    }

    public double getHeight() {
        return height.get();
    }
}
