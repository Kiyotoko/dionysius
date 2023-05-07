package org.dionysius.game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageExtractor {

	private final PixelReader reader;

	private int scale;

	public ImageExtractor(PixelReader reader, int scale) {
		this.reader = reader;
		this.scale = scale;
	}

	public Image extract(final int stx, final int sty, final int endx, final int endy) {
		int width = (endx - stx) * scale;
		int height = (endy - sty) * scale;

		WritableImage extracted = new WritableImage(width, height);
		PixelWriter writer = extracted.getPixelWriter();

		for (int y = sty; y < endy; y++) {
			for (int x = stx; x < endx; x++) {
				int argb = reader.getArgb(x, y);
				for (int wy = 0; wy < scale; wy++) {
					for (int wx = 0; wx < scale; wx++) {
						writer.setArgb((x - stx) * scale + wx, (y - sty) * scale + wy, argb);
					}
				}
			}
		}

		return extracted;
	}

}
