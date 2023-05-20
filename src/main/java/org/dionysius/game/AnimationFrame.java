package org.dionysius.game;

import java.util.function.Consumer;

import javafx.scene.image.Image;

public class AnimationFrame<T extends AnimatedGraphic> {

	private Image image;
	private double duration;
	private Consumer<T> onPlayed;

	public AnimationFrame(Image image, double duration, Consumer<T> onPlayed) {
		this.image = image;
		this.duration = duration;
		this.onPlayed = onPlayed;
	}

	public AnimationFrame(Image image, double duration) {
		this.image = image;
		this.duration = duration;
		this.onPlayed = this::onPlayed;
	}

	protected void onPlayed(T graphic) {

	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getDuration() {
		return duration;
	}

	public void setOnPlayed(Consumer<T> onPlayed) {
		this.onPlayed = onPlayed;
	}

	public Consumer<T> getOnPlayed() {
		return onPlayed;
	}
}
