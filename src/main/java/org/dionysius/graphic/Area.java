package org.dionysius.graphic;

import java.util.ArrayList;
import java.util.List;

import org.dionysius.grpc.StatusReply;
import org.dionysius.grpc.Vector;
import org.dionysius.grpc.Switching;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public final class Area extends Group implements Switching<StatusReply.Area> {
	private final Polygon outsides = new Polygon();

	private final List<Polygon> insides = new ArrayList<>();

	public Area() {
		outsides.setFill(Color.WHITE);
		getChildren().add(outsides);
	}

	public boolean grow(int expected) {
		int current = insides.size();
		if (current == expected)
			return false;
		for (int i = current; i < expected; i++) {
			insides.add(new Polygon());
			getChildren().add(insides.get(i));
		}
		for (int i = current; i > expected; i--) {
			insides.remove(i - 1);
			getChildren().remove(insides.get(i));
		}
		return true;
	}

	@Override
	public void switched(org.dionysius.grpc.StatusReply.Area associated) {
		check(associated.getOutsides(), outsides);
		if (grow(associated.getInsidesList().size()))
			for (int i = 0; i < insides.size(); i++)
				check(associated.getInsidesList().get(i), insides.get(i));
		setLayoutX(associated.getPosition().getX());
		setLayoutY(associated.getPosition().getY());
	}

	private static boolean check(org.dionysius.grpc.Polygon net, Polygon cur) {
		if (net.getPointsList().size() * 2 != cur.getPoints().size()) {
			cur.getPoints().clear();
			List<Double> temp = new ArrayList<>();
			for (Vector vector : net.getPointsList()) {
				temp.add(vector.getX());
				temp.add(vector.getY());
			}
			return cur.getPoints().addAll(temp);
		}
		return false;
	}
}
