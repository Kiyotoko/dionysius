package org.dionysius.game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.annotation.Nonnull;

public class ImageExtractor {

	private final @Nonnull PixelReader reader;

	private int scale;

	public ImageExtractor(@Nonnull PixelReader reader, int scale) {
		this.reader = reader;
		this.scale = scale;
	}

	@Nonnull
	public Image extract(final int stX, final int stY, final int endX, final int endY) {
		int width = (endX - stX) * scale;
		int height = (endY - stY) * scale;

		WritableImage extracted = new WritableImage(width, height);
		PixelWriter writer = extracted.getPixelWriter();

		for (int y = stY; y < endY; y++) {
			for (int x = stX; x < endX; x++) {
				int argb = reader.getArgb(x, y);
				for (int wy = 0; wy < scale; wy++) {
					for (int wx = 0; wx < scale; wx++) {
						writer.setArgb((x - stX) * scale + wx, (y - stY) * scale + wy, argb);
					}
				}
			}
		}

		return extracted;
	}

	@Nonnull
	public PixelReader getReader() {
		return reader;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getScale() {
		return scale;
	}
}
