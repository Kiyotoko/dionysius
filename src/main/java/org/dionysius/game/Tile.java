package org.dionysius.game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.annotation.Nonnull;

public class Tile {
    private final @Nonnull ImageView view = new ImageView();

    private @Nonnull Point2D position = Point2D.ZERO;

    public Tile(@Nonnull Image image, int x, int y) {
        view.setImage(image);
        setPosition(new Point2D(x, y));
    }

    public Tile(@Nonnull Image image) {
        view.setImage(image);
    }

    @Nonnull
    public ImageView getView() {
        return view;
    }

    public void setPosition(@Nonnull Point2D position) {
        this.position = position;
        view.setLayoutX((int) position.getX());
        view.setLayoutY((int) position.getY());
    }

    @Nonnull
    public Point2D getPosition() {
        return position;
    }
}
