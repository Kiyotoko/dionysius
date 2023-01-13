package org.dionysius.grpc;

import java.util.Collection;
import java.util.Iterator;

import javafx.geometry.Point2D;

public interface Corresponding<T> {
	T associated();

	public static <T> Collection<T> transform(Collection<? extends Corresponding<T>> collection) {
		java.util.Vector<T> transformed = new java.util.Vector<>(collection.size());
		int index = 0;
		for (Iterator<? extends Corresponding<T>> iterator = collection.iterator(); iterator.hasNext(); index++) {
			transformed.add(index, iterator.next().associated());
		}
		return transformed;
	}

	public static Vector transform(Point2D point) {
		return Vector.newBuilder().setX(point.getX()).setY(point.getY()).build();
	}

	public interface ExtendableCorresponding extends Corresponding<Object> {

	}
}
