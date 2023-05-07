package org.dionysius.game;

import javax.annotation.Nullable;

import io.scvis.observable.Property;
import io.scvis.proto.Mirror;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Health extends Property<Double> {

	private final Creature creature;

	private final Rectangle indicator = new Rectangle(100, 12, Color.RED);

	private final Mirror<Health, Pane> mirror = new Mirror<>(this, new Pane(indicator)) {
		@Override
		public void update(Health reference) {
			indicator.setWidth((int) max);
		}
	};

	public Health(Creature creature, double max) {
		super(max);
		this.creature = creature;
		this.min = 0;
		this.max = max;

		mirror.getReflection().setPadding(new Insets(6));
	}

	public Creature getCreature() {
		return creature;
	}

	public Mirror<Health, Pane> getMirror() {
		return mirror;
	}

	private double min;
	private double max;

	private static double capp(double min, double max, double value) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	@Override
	public void set(@Nullable Double value) {
		super.set(capp(min, max, value == null ? 0.0 : value));
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMin() {
		return min;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMax() {
		return max;
	}
}
