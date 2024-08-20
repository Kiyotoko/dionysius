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

import com.google.gson.Gson;
import javafx.scene.layout.Pane;
import org.Kiyotoko.dionysius.game.Game;

import javafx.application.Application;
import javafx.stage.Stage;
import org.Kiyotoko.dionysius.game.creature.Creature;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class App extends Application {
	@Override
	public void start(Stage stage) {
		Game game = new Game(new Pane(), 600.0, 400.0);
		try (InputStream stream = App.class.getResourceAsStream(Path.of("text", "animation", "Hero.json").toString())) {
			if (stream == null) throw new FileNotFoundException();
			var properties = new Gson().fromJson(new String(stream.readAllBytes()), Creature.Properties.class);
			properties.preload();
			var creature = new Creature(game, properties);
			creature.setAnimationPlayed(0);
		} catch (IOException ex) {
			throw new InternalError(ex);
		}

		stage.setScene(game);
		stage.setResizable(false);
		stage.show();
	}
}
