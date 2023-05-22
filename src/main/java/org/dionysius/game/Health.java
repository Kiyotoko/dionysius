package org.dionysius.game;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.dionysius.game.Indicator.BarIndicator;

import io.scvis.observable.Property;
import javafx.scene.paint.Color;

public class Health {

	@Nonnull
	private final Creature creature;

	@Nonnull
	private final Property<Double> val;
	@Nonnull
	private final Property<Double> max;

	public Health(@Nonnull Creature creature, double max) {

		this.creature = creature;
		this.val = new Property<>(max);
		val.addChangeListener(e -> {
			if (e.getNew() <= 0)
				creature.death();
		});
		this.max = new Property<>(max);
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
	public Property<Double> valueProperty() {
		return val;
	}

	public void setValue(@Nullable Double value) {
		val.set(capp(0.0, max.get(), value == null ? 0.0 : value));
	}

	public double getValue() {
		return val.get();
	}

	@Nonnull
	public Property<Double> maxProperty() {
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
