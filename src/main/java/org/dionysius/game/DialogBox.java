package org.dionysius.game;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.dionysius.content.Hero;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;

public class DialogBox extends Pane {

    public static class Dialog {

        public static final @Nonnull BiConsumer<Dialog, Hero> NONE = (d, h) -> { };

        private @Nonnull BiConsumer<Dialog, Hero> onStart = NONE;
        private @Nonnull BiConsumer<Dialog, Hero> onFinish = NONE;

        private @Nonnull String text;

        private @Nonnull String preview;

        private final @Nonnull List<Integer> connections = new ArrayList<>();

        public Dialog(@Nonnull String text, @Nonnull String preview, Collection<Integer> connections) {
            this.text = text;
            this.preview = preview;
            getConnections().addAll(connections);
        }

        public void setOnStart(@Nonnull BiConsumer<Dialog, Hero> onStart) {
            this.onStart = onStart;
        }

        @Nonnull
        public BiConsumer<Dialog, Hero> getOnStart() {
            return onStart;
        }

        public void setOnFinish(@Nonnull BiConsumer<Dialog, Hero> onFinish) {
            this.onFinish = onFinish;
        }

        @Nonnull
        public BiConsumer<Dialog, Hero> getOnFinish() {
            return onFinish;
        }

        @Nonnull
        public List<Integer> getConnections() {
            return connections;
        }

        public void setText(@Nonnull String text) {
            this.text = text;
        }

        @Nonnull
        public String getText() {
            return text;
        }

        public void setPreview(@Nonnull String preview) {
            this.preview = preview;
        }

        @Nonnull
        public String getPreview() {
            return preview;
        }
    }

    public final @Nonnull Vector<Dialog> dialog = new Vector<>();

    private final @Nonnull Label text = new Label("[ TEXT IS MISSING ]");
    {
        text.setWrapText(true);
        text.setMnemonicParsing(true);
        text.setMaxWidth(300);
    }

    public DialogBox() {
        setBackground(new Background(new BackgroundFill(Color.gray(0.1, 0.6), CornerRadii.EMPTY, new Insets(40))));
        getChildren().add(text);
    }

    int element;

    public void show() {
        element = 0;
        Dialog curr = getCurrent();
        text.setText(getScript(curr));
    }

    public void start(Hero hero) {
        Dialog curr = getCurrent();
        curr.getOnStart().accept(curr, hero);
        text.setText(getScript(curr));
    }

    public void next(Hero hero, int index) {
        Dialog curr = getCurrent();
        if (index < curr.getConnections().size()) {
            finish(hero);
            element = curr.getConnections().get(index);
            start(hero);
        }
    }


    @Nonnull
    public String getScript(Dialog dialog) {
        return dialog.getText() + getPreviews(dialog);
    }

    @Nonnull
    public String getPreviews(Dialog dialog) {
        final @Nonnull StringBuilder build = new StringBuilder();
        for (int index = 0; index < dialog.getConnections().size(); index++)
            build.append("\n").append(index+1).append("\t").append(getDialogs().get(dialog.getConnections().get(index)).getPreview());
        return build.toString();
    }

    public void finish(Hero hero) {
        getCurrent().getOnFinish().accept(getCurrent(), hero);
    }

    public Dialog getCurrent() {
        return getDialogs().get(element);
    }

    @Nonnull
    public Vector<Dialog> getDialogs() {
        return dialog;
    }

    @Nonnull
    public Label getText() {
        return text;
    }
}
