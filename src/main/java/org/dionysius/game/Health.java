package org.dionysius.game;

import javax.annotation.Nullable;

import org.dionysius.game.Indicator.BarIndicator;

import io.scvis.observable.Property;
import javafx.scene.paint.Color;

public class Health extends Property<Double> {

	private final Creature creature;

	public Health(Creature creature, double max) {
		super(max);
		this.creature = creature;
		this.min = 0;
		this.max = max;
	}

	public Creature getCreature() {
		return creature;
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

	private BarIndicator indicator;

	public BarIndicator asIndicator() {
		if (indicator == null) {
			indicator = new BarIndicator(creature, this, max);
			indicator.setColor(Color.RED);
		}
		return indicator;
	}

	public void setIndicator(BarIndicator indicator) {
		this.indicator = indicator;
	}

	public BarIndicator getIndicator() {
		return indicator;
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
