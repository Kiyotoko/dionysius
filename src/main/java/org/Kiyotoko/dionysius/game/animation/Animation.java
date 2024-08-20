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

package org.Kiyotoko.dionysius.game.animation;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.Kiyotoko.dionysius.game.FrameSource;
import org.Kiyotoko.dionysius.game.ImageExtractor;
import org.Kiyotoko.dionysius.game.Entity;

import java.util.ArrayList;
import java.util.List;

public class Animation implements Entity {
    private final Properties properties;
    private final ImageView view;

    public Animation(ImageView view, Properties properties) {
        this.view = view;
        this.properties = properties;
    }

    private int time;
    private int showed;

    @Override
    public void update() {
        var frame = getProperties().getFrames().get(showed);
        if (time++ > frame.getDuration()) {
            if (++showed >= getProperties().getFrames().size()) {
                showed = 0;
            }
            getView().setImage(getProperties().getFrames().get(showed).getFrame());
            time = 0;
        }
    }

    public static class Properties {
        protected final List<FrameSource> frames = new ArrayList<>();
        protected final String path;
        protected final int priority;
        private transient ImageExtractor extractor;

        public Properties(String path, int priority) {
            this.path = path;
            this.priority = priority;
        }

        public void preload() {
            for (FrameSource frame : frames) {
                frame.load(getExtractor());
            }
        }

        public ImageExtractor getExtractor() {
            if (extractor == null)
                extractor = new ImageExtractor(new Image(path).getPixelReader(), 3);
            return extractor;
        }

        public List<FrameSource> getFrames() {
            return frames;
        }
    }

    public ImageView getView() {
        return view;
    }

    public Properties getProperties() {
        return properties;
    }
}
