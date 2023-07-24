package org.dionysius.game;

import io.scvis.entity.Entity;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Game extends Scene {

    private final @Nonnull List<Entity> entities = new ArrayList<>();

    private final @Nonnull Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10.0), e -> {
        for (int index = 0; index < entities.size(); index++)
            entities.get(index).update(1.0);
    }));

    private final @Nonnull ObservableList<Creature> creatures = FXCollections.observableArrayList();
    private final @Nonnull ObjectProperty<Level> environment = new SimpleObjectProperty<>();
    private final @Nonnull ObjectProperty<DialogBox> dialog = new SimpleObjectProperty<>();

    public Game(final @Nonnull Pane root, double width, double height) {
        super(root, width, height);

        environment.addListener((ObservableValue<? extends Level> observableValue, @Nullable Level oldValue,
                                 @Nullable Level newValue) -> {
            if (oldValue != null) {
                root.getChildren().remove(oldValue);
            }
            if (newValue != null) {
                root.getChildren().add(newValue);
                root.setBackground(new Background(new BackgroundFill(newValue.getBackgroundColor(),
                        null, null)));
            }
        });
        dialog.addListener((ObservableValue<? extends DialogBox> observableValue, @Nullable DialogBox oldValue,
                            @Nullable DialogBox newValue) -> {
            if (oldValue != null) {
                root.getChildren().remove(oldValue);
            }
            if (newValue != null) {
                root.getChildren().add(newValue);
            }
        });
        creatures.addListener((Change<? extends Creature> change) -> {
            change.next();
            if (change.wasAdded()) {
                for (Creature creature : change.getAddedSubList()) {
                    root.getChildren().add(creature.getPane());
                    for (Indicator indicator : creature.getIndicators()) {
                        root.getChildren().add(indicator.getPane());
                    }
                    Platform.runLater(creature::reposition);
                }
            } else if (change.wasRemoved()) {
                for (Creature creature : change.getRemoved()) {
                    root.getChildren().remove(creature.getPane());
                    for (Indicator indicator : creature.getIndicators()) {
                        root.getChildren().remove(indicator.getPane());
                    }
                }
            }
            System.out.println(change);
        });
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

	@Nonnull
    public List<Entity> getEntities() {
        return entities;
    }

	@Nonnull
    public Timeline getTimeline() {
        return timeline;
    }

	@Nonnull
    public ObservableList<Creature> getCreatures() {
        return creatures;
    }

	@Nullable
    public Level getEnvironment() {
        return environment.getValue();
    }

    public void setEnvironment(@Nullable Level environment) {
        this.environment.setValue(environment);
    }

	@Nullable
    public DialogBox getDialog() {
        return this.dialog.getValue();
    }

    public void setDialog(@Nullable DialogBox dialog) {
        this.dialog.setValue(dialog);
    }
}