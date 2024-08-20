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

package org.Kiyotoko.dionysius;

import javafx.scene.Node;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static double clamp(double min, double max, double value) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    public static void center(Node node, double x, double y) {
        node.setLayoutX(x - node.getBoundsInLocal().getWidth() * 0.5);
        node.setLayoutY(y - node.getBoundsInLocal().getHeight() * 0.5);
    }
}
