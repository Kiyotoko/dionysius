package org.dionysius.game;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.dionysius.game.Indicator.BarIndicator;

import javafx.scene.paint.Color;

public class Health {

	@Nonnull
	private final Creature creature;

	@Nonnull
	private final DoubleProperty val;
	@Nonnull
	private final DoubleProperty max;

	public Health(@Nonnull Creature creature, double max) {

		this.creature = creature;
		this.val = new SimpleDoubleProperty(max);
		val.addListener((observable, oldValue, newValue) -> {
			if (newValue.doubleValue() <= 0) {
				creature.death();
			}
		});
		this.max = new SimpleDoubleProperty(max);
	}

	@Nonnull
	public Creature getCreature() {
		return creature;
	}

	private static double capp(double min, double max, double value) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	@Nonnull
	public DoubleProperty valueProperty() {
		return val;
	}

	public void setValue(@Nullable Double value) {
		val.set(capp(0.0, max.get(), value == null ? 0.0 : value));
	}

	public double getValue() {
		return val.get();
	}

	@Nonnull
	public DoubleProperty maxProperty() {
		return max;
	}

	public void setMax(double max) {
		this.max.set(max);
	}

	public double getMax() {
		return max.get();
	}

	private BarIndicator indicator;

	@Nonnull
	public BarIndicator asIndicator() {
		if (indicator == null) {
			indicator = new BarIndicator(creature, val, max);
			indicator.setColor(Color.RED);
			indicator.getPane().setVisible(false);
			val.addListener(invalidation -> indicator.getPane().setVisible(true));
		}
		return indicator;
	}

	public void setIndicator(BarIndicator indicator) {
		this.indicator = indicator;
	}

	public BarIndicator getIndicator() {
		return indicator;
	}
}
