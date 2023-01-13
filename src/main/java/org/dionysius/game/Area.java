package org.dionysius.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.dionysius.grpc.Corresponding;
import org.dionysius.grpc.StatusReply;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javafx.geometry.Point2D;

public final class Area extends Physical implements Corresponding<StatusReply.Area> {
	@JsonProperty("outsides")
	private Polygon outsides;
	@JsonProperty("insides")
	private List<Polygon> insides;

	@JsonCreator
	public Area(@JsonProperty("outsides") Polygon outsides, @JsonProperty("insides") List<Polygon> insides,
			@JsonProperty("position") Vector position) {
		this.outsides = outsides;
		this.insides = insides;
		setPosition(position);
	}

	public Area(Polygon outsides, List<Polygon> insides) {
		this.outsides = outsides;
		this.insides = insides;
	}

	public Area(Vector... points) {
		this(Polygon.of(points), new ArrayList<>());
	}

	public static <T> T pick(List<T> list, Comparator<T> comparator) {
		if (list.isEmpty())
			return null;
		List<T> temp = new ArrayList<>(list);
		temp.sort(comparator);
		return temp.get(0);
	}

	private static boolean contains(List<Vector> polygon, Point2D point) {
		boolean inside = false;
		for (int i = 0; i < polygon.size(); i++) {
			int j = (i + 1) % polygon.size();
			if (crosses(polygon.get(i), polygon.get(j), point))
				inside = !inside;
		}
		return inside;
	}

	private static boolean crosses(Point2D a, Point2D b, Point2D point) {
		if (point.getY() == a.getY() && a.getY() == b.getY())
			if (a.getX() <= point.getX() && point.getX() <= b.getX()
					|| b.getX() <= point.getX() && point.getX() <= a.getX())
				return true;
			else
				return false;
		if (point.getY() == a.getY() && point.getX() == b.getX())
			return true;
		if (a.getY() > b.getY()) {
			var t = a;
			a = b;
			b = t;
		}
		if (point.getY() <= a.getY() || point.getY() > b.getY())
			return false;
		double x = (a.getX() - point.getX()) * (b.getY() - point.getY())
				- (a.getY() - point.getY()) * (b.getX() - point.getX());
		if (x < 0)
			return false;
		else
			return true;
	}

	public boolean contains(final Point2D point) {
		Point2D temp = point.subtract(getPosition());
		if (outsides.contains(temp)) {
			for (Polygon polygon : getInsides())
				if (polygon.contains(temp))
					return false;
			return true;
		}
		return false;
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonSerialize
	@JsonDeserialize
	public static interface Bounds {
		public static final Bounds NONE = p -> false;

		boolean contains(Point2D point);
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonSerialize
	@JsonDeserialize
	public static class Polygon implements Bounds, Corresponding<org.dionysius.grpc.Polygon> {
		@JsonProperty("points")
		private List<Vector> points;
		@JsonProperty("minX")
		private double minX;
		@JsonProperty("minY")
		private double minY;
		@JsonProperty("maxX")
		private double maxX;
		@JsonProperty("maxY")
		private double maxY;

		@JsonCreator
		public Polygon(@JsonProperty("points") List<Vector> points, @JsonProperty("minX") double minX,
				@JsonProperty("minY") double minY, @JsonProperty("maxX") double maxX,
				@JsonProperty("maxY") double maxY) {
			this.points = points;
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
		}

		protected Polygon(List<Vector> points) {
			this.points = points;
			minX = pick(points, (a, b) -> (int) (a.getX() - b.getX())).getX();
			minY = pick(points, (a, b) -> (int) (a.getY() - b.getY())).getY();
			maxX = pick(points, (a, b) -> (int) (b.getX() - a.getX())).getX();
			maxY = pick(points, (a, b) -> (int) (b.getY() - a.getY())).getY();
		}

		public static final Polygon of(Vector... vec) {
			return new Polygon(List.of(vec));
		}

		public boolean contains(Point2D point) {
			if (point.getX() < minX || point.getX() > maxX)
				return false;
			if (point.getY() < minY || point.getY() > maxY)
				return false;
			return Area.contains(points, point);
		}

		@Override
		public org.dionysius.grpc.Polygon associated() {
			org.dionysius.grpc.Polygon.Builder builder = org.dionysius.grpc.Polygon.newBuilder();
			for (Point2D point : points)
				builder.addPoints(Corresponding.transform(point));
			return builder.build();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj instanceof Polygon)
				return ((Polygon) obj).points.equals(points);
			return false;
		}
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonSerialize
	@JsonDeserialize
	public static class Circle implements Bounds {
		@JsonProperty("radius")
		public final double radius;

		public Circle(double radius) {
			this.radius = radius;
		}

		@Override
		public boolean contains(Point2D point) {
			return radius > point.magnitude();
		}
	}

	public Polygon getOutsides() {
		return outsides;
	}

	public List<Polygon> getInsides() {
		return insides;
	}

	@Override
	public StatusReply.Area associated() {
		return StatusReply.Area.newBuilder().setId(getId()).setOutsides(outsides.associated())
				.addAllInsides(Corresponding.transform(insides)).setPosition(Corresponding.transform(getPosition()))
				.build();
	}
}
