package org.dionysius.content;

import com.google.gson.Gson;
import javafx.scene.image.Image;
import org.dionysius.game.ImageExtractor;
import org.dionysius.game.Level;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class TileMapLoader {

    public static class TileSource implements Serializable {

        private final int startX, startY, endX, endY;

        private final int positionX, positionY;

        public TileSource(int startX, int startY, int endX, int endY, int positionX, int positionY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.positionX = positionX;
            this.positionY = positionY;
        }

        private transient Image tile;

        public void load(ImageExtractor extractor) {
            tile = extractor.extract(startX, startY, endX, endY);
        }

        public Image getTile() {
            return tile;
        }

        public int getPositionX() {
            return positionX;
        }

        public int getPositionY() {
            return positionY;
        }
    }

    public static class BundleSource implements Serializable {
        public final String path;

        private final List<TileSource> tiles = new ArrayList<>();

        public BundleSource(String path, TileSource... tiles) {
            this.path = path;
            getTiles().addAll(List.of(tiles));
        }

        public List<TileSource> getTiles() {
            return tiles;
        }

        public void preload() {
            for (TileSource tile : tiles) {
                tile.load(getExtractor());
            }
        }

        private transient ImageExtractor extractor;

        public ImageExtractor getExtractor() {
            if (extractor == null)
                extractor = new ImageExtractor(new Image(path).getPixelReader(), 3);
            return extractor;
        }
    }

    private static final Gson gson = new Gson();

    private final List<Level.Tile> loaded = new ArrayList<>();

    public TileMapLoader(File file) {
        try (FileReader reader = new FileReader(file)) {
            TileMapLoader.BundleSource bundle = gson.fromJson(reader, TileMapLoader.BundleSource.class);
            Objects.requireNonNull(bundle);
            loaded.addAll(buildBundle(bundle));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Level.Tile buildTile(TileSource source) {
        return new Level.Tile(source.getTile(), source.getPositionX(), source.getPositionY());
    }

    private static List<Level.Tile> buildBundle(BundleSource source) {
        List<Level.Tile> build = new ArrayList<>();
        source.preload();
        for (TileSource entry : source.getTiles()) {
            build.add(buildTile(entry));
        }
        return build;
    }

    public List<Level.Tile> getLoaded() {
        return loaded;
    }
}
