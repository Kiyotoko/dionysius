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

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.Kiyotoko.dionysius.game;

import javafx.scene.image.Image;

import java.io.Serializable;

public class FrameSource implements Serializable {
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    private final double duration;

    private transient Image frame;

    public FrameSource(int startX, int startY, int endX, int endY, double duration) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.duration = duration;
    }

    public void load(ImageExtractor extractor) {
        frame = extractor.extract(startX, startY, endX, endY);
    }

    public Image getFrame() {
        return frame;
    }

    public double getDuration() {
        return duration;
    }
}
