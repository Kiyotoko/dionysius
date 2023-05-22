package org.dionysius.game;

import io.scvis.geometry.Vector2D;
import io.scvis.observable.Property;
import io.scvis.proto.Mirror;
import io.scvis.proto.Pair;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public abstract class Indicator extends Mirror<Creature, Pane> {

	protected Indicator(Creature creature) {
		super(creature, new Pane());
	}

	public static class BarIndicator extends Indicator {

		public static final Pair<Double, Double> SMALL_SIZE = new Pair<Double, Double>(70.0, 7.0);
		public static final Pair<Double, Double> MEDIUM_SIZE = new Pair<Double, Double>(120.0, 12.0);
		public static final Pair<Double, Double> LARGE_SIZE = new Pair<Double, Double>(170.0, 17.0);
		public static final Pair<Double, Double> ULTRA_SIZE = new Pair<Double, Double>(450.0, 30.0);

		private Pair<Double, Double> size = SMALL_SIZE;
		private Color color = Color.LIGHTCORAL;
		private Vector2D clipp = null;

		private final Rectangle fill = new Rectangle(getWidth(), getHeight());
		private final Rectangle edge = new Rectangle(getWidth(), getHeight(), Color.grayRgb(240, 0.15));

		public BarIndicator(Creature creature, Property<? extends Number> val, Property<? extends Number> max) {
			super(creature);
			setColor(color);
			edge.setStroke(Color.gray(0.15));

			creature.addInvalidationListener((event) -> {
				if (!isClipped())
					update(creature);
			});
			val.addChangeListener(
					(event) -> fill.setWidth(val.get().doubleValue() / max.get().doubleValue() * getWidth()));
			getReflection().getChildren().addAll(fill, edge);
		}

		public BarIndicator(Creature creature, Property<? extends Number> property, double max, Vector2D clipp) {
			super(creature);
			setColor(color);
			edge.setStroke(Color.gray(0.15));

			property.addChangeListener((event) -> fill.setWidth(property.get().doubleValue() / max * getWidth()));
			getReflection().getChildren().addAll(fill, edge);
		}

		@Override
		public void update(Creature reference) {
			Vector2D transformed = Creature.transform(reference.getPosition().getX(), reference.getPosition().getY(),
					getWidth(), getHeight());
			getReflection().setLayoutX(transformed.getX());
			getReflection().setLayoutY(transformed.getY());
		}

		public Pair<Double, Double> getSize() {
			return size;
		}

		public void setSize(Pair<Double, Double> size) {
			this.size = size;
			fill.setWidth(getWidth());
			fill.setHeight(getHeight());
			edge.setWidth(getWidth());
			edge.setHeight(getHeight());
		}

		public double getWidth() {
			return getSize().getLeft();
		}

		public double getHeight() {
			return getSize().getRight();
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
			fill.setFill(color);
		}

		public boolean isClipped() {
			return clipp != null;
		}

		public void setClipp(Vector2D clipp) {
			this.clipp = clipp;
			getReflection().setLayoutX(clipp.getX());
			getReflection().setLayoutY(clipp.getY());
		}

		public Vector2D getClipp() {
			return clipp;
		}
	}

	public static class PositionIndicator extends Indicator {

		protected PositionIndicator(Creature creature) {
			super(creature);

			creature.addInvalidationListener((event) -> update(creature));
			getReflection().getChildren()
					.add(new Circle(5.0, new Color(Math.random(), Math.random(), Math.random(), 1.0)));
		}

		@Override
		public void update(Creature reference) {
			Vector2D transformed = Creature.transform(reference.getPosition().getX(), reference.getPosition().getY(),
					0.0, 0.0);
			getReflection().setLayoutX(transformed.getX());
			getReflection().setLayoutY(transformed.getY());
		}

	}
}
