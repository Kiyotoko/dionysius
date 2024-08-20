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

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.Kiyotoko.dionysius.game.creature;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Indicator {

	private final @Nonnull Creature creature;
	private final @Nonnull Pane pane = new Pane();

	private @Nonnull Point2D position = Point2D.ZERO;
	private @Nullable Point2D clip = null;

	protected Indicator(@Nonnull Creature creature) {
		this.creature = creature;
	}

	public static class BarIndicator extends Indicator {

		public static final Pair<Double, Double> SMALL_SIZE = new Pair<>(70.0, 7.0);
		public static final Pair<Double, Double> MEDIUM_SIZE = new Pair<>(120.0, 12.0);
		public static final Pair<Double, Double> LARGE_SIZE = new Pair<>(170.0, 17.0);
		public static final Pair<Double, Double> ULTRA_SIZE = new Pair<>(450.0, 30.0);

		private Pair<Double, Double> size = SMALL_SIZE;
		private Color color = Color.LIGHTCORAL;

		private final Rectangle fill = new Rectangle(getWidth(), getHeight());
		private final Rectangle edge = new Rectangle(getWidth(), getHeight(), Color.grayRgb(240, 0.15));

		public BarIndicator(Creature creature, DoubleProperty val, DoubleProperty max) {
			super(creature);
			setColor(color);
			edge.setStroke(Color.gray(0.15));

			val.addListener(
					(event) -> fill.setWidth(val.doubleValue() / max.doubleValue() * getWidth()));
			getPane().getChildren().addAll(fill, edge);
		}

		public BarIndicator(Creature creature, DoubleProperty property, double max, Point2D clipp) {
			super(creature);
			setColor(color);
			edge.setStroke(Color.gray(0.15));

			property.addListener((observable, newValue, oldValue) -> fill.setWidth(newValue.doubleValue() / max * getWidth()));
			getPane().getChildren().addAll(fill, edge);
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
			return getSize().getKey();
		}

		public double getHeight() {
			return getSize().getValue();
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
			fill.setFill(color);
		}
	}

	public static class PositionIndicator extends Indicator {

		protected PositionIndicator(Creature creature) {
			super(creature);

			getPane().getChildren()
					.add(new Circle(5.0, new Color(Math.random(), Math.random(), Math.random(), 1.0)));
		}
	}

	@Nonnull
	public Creature getCreature() {
		return creature;
	}

	@Nonnull
	public Pane getPane() {
		return pane;
	}

	public void setPosition(@Nonnull Point2D position) {
		this.position = position;
	}

	@Nonnull
	public Point2D getPosition() {
		return position;
	}


	public boolean isClipped() {
		return clip != null;
	}

	public void setClip(Point2D clip) {
		this.clip = clip;
		getPane().setLayoutX(clip.getX());
		getPane().setLayoutY(clip.getY());
		getPane().setVisible(true);
	}

	@Nullable
	public Point2D getClip() {
		return clip;
	}

}
