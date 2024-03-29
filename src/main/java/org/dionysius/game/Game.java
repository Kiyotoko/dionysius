package org.dionysius.game;

import java.util.*;

import javafx.animation.Animation;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Camera;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.SubScene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Game extends Scene {

    private final @Nonnull List<Entity> entities = new ArrayList<>();

    private final @Nonnull Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10.0), e -> {
        for (var entity : List.copyOf(entities))
            entity.update();
    }));

    private final @Nonnull ObservableList<Creature> creatures = FXCollections.observableArrayList();
    private final @Nonnull ObjectProperty<Level> level = new SimpleObjectProperty<>();
    private final @Nonnull ObjectProperty<DialogBox> dialog = new SimpleObjectProperty<>();

    private final @Nonnull List<Level> levels = new ArrayList<>();
    private final @Nonnull Camera focusCamera = new ParallelCamera();

    public Game(final @Nonnull Pane root, double width, double height) {
        super(root, width, height);

        Pane overlay = new Pane();
        Pane content = new Pane();

        Pane background = new Pane();
        Pane foreground = new Pane();
        content.getChildren().addAll(background, foreground);

        SubScene subScene = new SubScene(content, width, height);
        subScene.setCamera(focusCamera);

        content.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
                null, null)));

        root.getChildren().addAll(subScene, overlay);

        level.addListener((ObservableValue<? extends Level> observableValue, @Nullable Level oldValue,
                           @Nullable Level newValue) -> {
            if (oldValue != null) {
                for (Tile tile : oldValue.getTileMap()) {
                    background.getChildren().remove(tile.getView());
                }
            }
            if (newValue != null) {
                for (Tile tile : newValue.getTileMap()) {
                    background.getChildren().add(tile.getView());
                }
                subScene.setFill(newValue.getBackgroundColor());
                newValue.getOnLoad().accept(this);
            }
        });
        dialog.addListener((ObservableValue<? extends DialogBox> observableValue, @Nullable DialogBox oldValue,
                            @Nullable DialogBox newValue) -> {
            if (oldValue != null) {
                overlay.getChildren().remove(oldValue);
            }
            if (newValue != null) {
                overlay.getChildren().add(newValue);
            }
        });
        creatures.addListener((ListChangeListener.Change<? extends Creature> change) -> {
            change.next();
            if (change.wasAdded()) {
                for (Creature creature : change.getAddedSubList()) {
                    foreground.getChildren().add(creature.getPane());
                    Platform.runLater(() -> {
                        for (Indicator indicator : creature.getIndicators()) {
                                if (indicator.isClipped()) {
                                    overlay.getChildren().add(indicator.getPane());
                                } else {
                                    foreground.getChildren().add(indicator.getPane());
                                }
                        }
                        creature.reposition();
                    });
                }
            } else if (change.wasRemoved()) {
                for (Creature creature : change.getRemoved()) {
                    foreground.getChildren().remove(creature.getPane());
                    for (Indicator indicator : creature.getIndicators()) {
                        if (indicator.isClipped()) {
                            overlay.getChildren().remove(indicator.getPane());
                        } else {
                            foreground.getChildren().remove(indicator.getPane());
                        }
                    }
                }
            }
        });
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

	@Nonnull
    public List<Entity> getEntities() {
        return entities;
    }

	@Nonnull
    public ObservableList<Creature> getCreatures() {
        return creatures;
    }

	@Nullable
    public Level getLevel() {
        return level.getValue();
    }

    public void setLevel(@Nullable Level level) {
        this.level.setValue(level);
    }

    public void loadNextLevel() {
        if (getLevels().isEmpty()) {
            throw new InternalError("game has no levels. Please add levels first.");
        }

        for (var entity : entities) {
            if (entity instanceof Destroyable) {
                ((Destroyable) entity).destroy();
            }
        }

        int index = getLevel() != null ? getLevels().indexOf(getLevel()) + 1 : 0;
        if (getLevels().size() <= index) {
            throw new InternalError("No levels are left.");
        }
        setLevel(getLevels().get(index));
    }

    @Nonnull
    public List<Level> getLevels() {
        return levels;
    }

    @Nullable
    public DialogBox getDialog() {
        return this.dialog.getValue();
    }

    public void setDialog(@Nullable DialogBox dialog) {
        this.dialog.setValue(dialog);
    }

    @Nonnull
    public Camera getFocusCamera() {
        return focusCamera;
    }
}