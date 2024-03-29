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
        private final List<String> connections = new ArrayList<>();
        private final String text;
        private final String preview;
        private final String key;

        public DialogSource(String text, String preview, String key, String... connections) {
            this.text = text;
            this.preview = preview;
            this.key = key;
            getConnections().addAll(List.of(connections));
        }

        public List<String> getConnections() {
            return connections;
        }

        public String getText() {
            return text;
        }

        public String getPreview() {
            return preview;
        }

        public String getKey() {
            return key;
        }
    }

    public static class BundleSource implements Serializable {
        private final ArrayList<DialogSource> dialogs = new ArrayList<>();

        public BundleSource(List<DialogSource> dialogs) {
            this.dialogs.addAll(dialogs);
        }

        public List<DialogSource> getDialogs() {
            return dialogs;
        }
    }

    private static final Gson gson = new Gson();

    private final Map<String, DialogBox.Dialog> loaded = new HashMap<>();

    public DialogLoader(File file) {
        try (FileReader reader = new FileReader(file)) {
            DialogLoader.BundleSource bundle = gson.fromJson(reader, DialogLoader.BundleSource.class);
            Objects.requireNonNull(bundle);
            loaded.putAll(buildBundle(bundle));
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    private static DialogBox.Dialog buildDialog(DialogSource source) {
        return new DialogBox.Dialog(source.getText(), source.getPreview(), source.getConnections());
    }

    private static Map<String, DialogBox.Dialog> buildBundle(DialogLoader.BundleSource source) {
        Map<String, DialogBox.Dialog> build = new HashMap<>();
        for (DialogSource entry : source.getDialogs()) {
            build.put(entry.getKey(), buildDialog(entry));
        }
        return build;
    }

    public Map<String, DialogBox.Dialog> getLoaded() {
        return loaded;
    }
}
