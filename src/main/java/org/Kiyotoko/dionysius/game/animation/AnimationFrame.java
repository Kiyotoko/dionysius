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

package org.Kiyotoko.dionysius.game.animation;

import java.util.function.Consumer;

import javafx.scene.image.Image;
import org.Kiyotoko.dionysius.game.creature.Creature;

public class AnimationFrame {

	private Image image;
	private double duration;
	private Consumer<Creature> onPlayed;

	public AnimationFrame(Image image, double duration, Consumer<Creature> onPlayed) {
		this.image = image;
		this.duration = duration;
		this.onPlayed = onPlayed;
	}

	public AnimationFrame(Image image, double duration) {
		this.image = image;
		this.duration = duration;
		this.onPlayed = this::onPlayed;
	}

	protected void onPlayed(Creature graphic) {

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

	public void setOnPlayed(Consumer<Creature> onPlayed) {
		this.onPlayed = onPlayed;
	}

	public Consumer<Creature> getOnPlayed() {
		return onPlayed;
	}
}
