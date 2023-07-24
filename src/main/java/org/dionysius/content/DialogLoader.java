package org.dionysius.content;

import com.google.gson.Gson;
import org.dionysius.game.DialogBox;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class DialogLoader {

    public static class DialogSource implements Serializable {
        private final List<Integer> connections = new ArrayList<>();
        private final String text;
        private final String preview;

        public DialogSource(String text, String preview, Integer... connections) {
            this.text = text;
            this.preview = preview;
            getConnections().addAll(List.of(connections));
        }

        public List<Integer> getConnections() {
            return connections;
        }

        public String getText() {
            return text;
        }

        public String getPreview() {
            return preview;
        }
    }

    public static class BundleSource implements Serializable {
        private final ArrayList<DialogSource> dialogs = new ArrayList<>();

        public BundleSource(List<DialogSource> dialogs) {
            this.dialogs.addAll(dialogs);
        }

        public ArrayList<DialogSource> getDialogs() {
            return dialogs;
        }
    }

    private static final Gson gson = new Gson();

    private final List<DialogBox.Dialog> loaded = new ArrayList<>();

    public DialogLoader(File file) {
        try (FileReader reader = new FileReader(file)) {
            DialogLoader.BundleSource bundle = gson.fromJson(reader, DialogLoader.BundleSource.class);
            Objects.requireNonNull(bundle);
            loaded.addAll(buildBundle(bundle));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static DialogBox.Dialog buildDialog(DialogSource source) {
        return new DialogBox.Dialog(source.getText(), source.getPreview(), source.getConnections());
    }

    private static List<DialogBox.Dialog> buildBundle(DialogLoader.BundleSource source) {
        List<DialogBox.Dialog> build = new ArrayList<>();
        for (DialogSource entry : source.getDialogs()) {
            build.add(buildDialog(entry));
        }
        return build;
    }

    public List<DialogBox.Dialog> getLoaded() {
        return loaded;
    }
}
