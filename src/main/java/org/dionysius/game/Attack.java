package org.dionysius.game;

import java.util.function.Consumer;
import javafx.geometry.Point2D;

public class Attack {

	private double damage;
	private double range;
	private double angle;

	public Attack(double damage, double range, double angle) {
		this.damage = damage;
		this.range = range;
		this.angle = angle;
	}

	public Attack() {

	}

	public Consumer<Creature> asConsumer() {
		return creature -> creature.hit(this);
	}

	public boolean canHit(double direction, Point2D from, Point2D to) {
		return isInRange(from, to) && isInAngle(direction * 90, from, to);
	}

	public boolean isInRange(Point2D from, Point2D to) {
		return from.distance(to) <= range;
	}

	public boolean isInAngle(double attack, Point2D from, Point2D to) {
		return Math.abs(attack - Math.toDegrees(from.angle(to))) <= angle;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
}
