package org.dionysius;

import javafx.scene.layout.Pane;
import org.dionysius.content.*;
import org.dionysius.game.Game;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {
	@Override
	public void start(Stage stage) {
		Game game = new Game(new Pane(), 600.0, 400.0);
		game.getLevels().addAll(List.of(new FieldOfTheDead(), new DarkForest()));
		game.loadNextLevel();

		stage.setScene(game);
		stage.setResizable(false);
		stage.show();
	}
}
