package org.dionysius;

import org.dionysius.content.DroidZapper;
import org.dionysius.content.Hero;
import org.dionysius.game.Game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class App extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Game game = new Game();

		Hero hero = new Hero(game);
		new DroidZapper(game);

		Scene scene = new Scene(game.getRender(), 600, 400);
		hero.applyTo(scene);
		stage.setScene(scene);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();
	}
}
