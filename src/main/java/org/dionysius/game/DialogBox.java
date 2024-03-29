package org.dionysius.game;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.dionysius.content.Hero;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;

public class DialogBox extends VBox {

    public static class Dialog implements Serializable {

        private transient @Nonnull BiConsumer<Dialog, Hero> onAction = (d, h) -> { };

        private final @Nonnull String text;

        private final @Nonnull String preview;

        private final @Nonnull List<String> connections = new ArrayList<>();

        public Dialog(@Nonnull String text, @Nonnull String preview, Collection<String> connections) {
            this.text = text;
            this.preview = preview;
            getConnections().addAll(connections);
        }

        public void setOnAction(@Nonnull BiConsumer<Dialog, Hero> onAction) {
            this.onAction = onAction;
        }

        @Nonnull
        public BiConsumer<Dialog, Hero> getOnAction() {
            return onAction;
        }

        @Nonnull
        public List<String> getConnections() {
            return connections;
        }

        @Nonnull
        public String getText() {
            return text;
        }

        @Nonnull
        public String getPreview() {
            return preview;
        }
    }

    public final @Nonnull Map<String, Dialog> dialog = new HashMap<>();

    private final @Nonnull Label text = new Label("[ TEXT IS MISSING ]");

    private final @Nonnull List<Label> options = new ArrayList<>();

    public DialogBox() {
        setBackground(new Background(new BackgroundFill(Color.gray(0.1, 0.6), CornerRadii.EMPTY,
                new Insets(40))));

        text.setWrapText(true);
        text.setMnemonicParsing(true);
        text.setMaxWidth(300);
        text.setFont(Font.font(14));

        for (int i = 0; i < 3; i++) {
            Label build = new Label();
            Label graphic = new Label("" + (i+1));
            graphic.setFont(Font.font(14));
            build.setGraphic(graphic);
            build.setFont(Font.font(12));
            options.add(build);
        }
        getChildren().add(text);
        getChildren().addAll(options);
    }

    private String element;

    public void show() {
        element = "0";
        Dialog curr = getCurrent();
        text.setText(curr.getText());
        for (int i = 0; i < 3; i++) {
            if (i < curr.getConnections().size()) {
                options.get(i).setText(dialog.get(curr.getConnections().get(i)).getPreview());
                options.get(i).setVisible(true);
            } else options.get(i).setVisible(false);
        }
    }

    public void start(Hero hero) {
        Dialog curr = getCurrent();
        curr.getOnAction().accept(curr, hero);
        text.setText(curr.getText());
        for (int i = 0; i < 3; i++) {
            if (i < curr.getConnections().size()) {
                options.get(i).setText(dialog.get(curr.getConnections().get(i)).getPreview());
                options.get(i).setVisible(true);
            } else options.get(i).setVisible(false);
        }
    }

    public void next(Hero hero, int index) {
        Dialog curr = getCurrent();
        if (index < curr.getConnections().size()) {
            element = curr.getConnections().get(index);
            start(hero);
        }
    }

    public Dialog getCurrent() {
        return getDialogs().get(element);
    }

    @Nonnull
    public Map<String, Dialog> getDialogs() {
        return dialog;
    }
}
