package org.dionysius.content;

import com.google.gson.Gson;
import javafx.scene.image.Image;
import org.dionysius.game.AnimationFrame;
import org.dionysius.game.ImageExtractor;

import java.io.*;
import java.util.*;

public class AnimationLoader {

    public static class FrameSource implements Serializable{
        private final int startX, startY, endX, endY;

        private final double duration;

        public FrameSource(int startX, int startY, int endX, int endY, double duration) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.duration = duration;
        }

        private transient Image frame;

        public void load(AnimationSource source) {
            frame = source.getExtractor().extract(startX, startY, endX, endY);
        }

        public Image getFrame() {
            return frame;
        }

        public double getDuration() {
            return duration;
        }
    }

    public static class AnimationSource implements Serializable {

        public final String path;

        private final List<FrameSource> frames = new ArrayList<>();

        public AnimationSource(String path, FrameSource... frames) {
            this.path = path;
            this.frames.addAll(List.of(frames));
        }

        public void preload() {
            for (FrameSource frame : frames) {
                frame.load(this);
            }
        }

        private transient ImageExtractor extractor;

        public ImageExtractor getExtractor() {
            if (extractor == null)
                extractor = new ImageExtractor(new Image(path).getPixelReader(), 3);
            return extractor;
        }

        public List<FrameSource> getFrames() {
            return frames;
        }
    }

    public static class BundleSource implements Serializable {
       private final Map<Byte, AnimationSource> animations = new WeakHashMap<>();

        public BundleSource(Map<Byte, AnimationSource> animations) {
            this.animations.putAll(animations);
        }

        public Map<Byte, AnimationSource> getAnimations() {
            return animations;
        }
    }

    private static final Gson gson = new Gson();

    private final Map<Byte, List<AnimationFrame>> loaded = new WeakHashMap<>();

    public AnimationLoader(File file) {
        try (FileReader reader = new FileReader(file)) {
            BundleSource bundle = gson.fromJson(reader, BundleSource.class);
            Objects.requireNonNull(bundle);
            loaded.putAll(buildBundle(bundle));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static AnimationFrame buildFrame(FrameSource source) {
        return new AnimationFrame(source.getFrame(), source.getDuration());
    }

    private static Map<Byte, List<AnimationFrame>> buildBundle(BundleSource source) {
        Map<Byte, List<AnimationFrame>> build = new WeakHashMap<>();
        for (Map.Entry<Byte, AnimationSource> entry : source.getAnimations().entrySet()) {
            List<AnimationFrame> frames = new ArrayList<>();
            entry.getValue().preload();
            for (FrameSource frame : entry.getValue().getFrames()) {
                frames.add(buildFrame(frame));
            }
            build.put(entry.getKey(), frames);
        }
        return build;
    }

    public Map<Byte, List<AnimationFrame>> getLoaded() {
        return loaded;
    }
}
