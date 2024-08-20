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

package org.Kiyotoko.dionysius.game;

import java.util.*;

import javafx.animation.Animation;
import javafx.scene.Camera;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.annotation.Nonnull;

public class Game extends Scene {

    private final @Nonnull List<Entity> entities = new ArrayList<>();

    private final @Nonnull Camera focusCamera = new ParallelCamera();
    private final @Nonnull Pane background = new Pane();
    private final @Nonnull Pane foreground = new Pane();
    private final @Nonnull Pane overlay = new Pane();

    public Game(final @Nonnull Pane root, double width, double height) {
        super(root, width, height);

        // Add sub scene, camera and overlay
        SubScene subScene = new SubScene(new Pane(background, foreground), width, height);
        subScene.setCamera(focusCamera);
        root.getChildren().addAll(subScene, overlay);

        // Create and start timeline
        final @Nonnull Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10.0), e -> {
            for (var entity : List.copyOf(entities))
                entity.update();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

	@Nonnull
    public List<Entity> getEntities() {
        return entities;
    }

    @Nonnull
    public Camera getFocusCamera() {
        return focusCamera;
    }

    @Nonnull
    public Pane getBackground() {
        return background;
    }

    @Nonnull
    public Pane getForeground() {
        return foreground;
    }

    @Nonnull
    public Pane getOverlay() {
        return overlay;
    }
}