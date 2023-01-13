package org.dionysius;

import org.dionysius.graphic.Menu;

import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
//		StageScene scene = new StageScene(new BorderPane());
//		scene.setBody(new Menu(new BorderPane()));
		stage.setScene(new Menu(new BorderPane()));
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
}
