package org.dionysius.graphic;

import org.dionysius.graphic.MenuItem.MenuButton;
import org.dionysius.graphic.MenuItem.MenuInput;
import org.dionysius.grpc.DemonClient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Menu extends Scene {
	public Menu(BorderPane root) {
		super(root, 1024, 768 - 24);

		VBox host = new VBox(8);
		MenuButton hosting = new MenuButton("start".toUpperCase(), e -> {
			DemonClient client = DemonClient.singleplayer(8080);
			((Stage) getWindow()).setScene(client.getGame());
			client.start();
		});
		host.getChildren().addAll(hosting);
		host.setPadding(new Insets(32));

		VBox join = new VBox(8);
		MenuInput hoster = new MenuInput("address");
		MenuButton joining = new MenuButton("join".toUpperCase(), e -> {
			String ip = hoster.getInput();
			if (!ip.isBlank()) {
				DemonClient client = DemonClient.multiplayer(ip, 8080);
				((Stage) getWindow()).setScene(client.getGame());
				client.start();
			}
		});
		join.getChildren().addAll(hoster, joining);
		join.setPadding(new Insets(32));

		HBox box = new HBox(8);
		box.setPadding(new Insets(32));
		MenuButton singleplayer = new MenuButton("singleplayer".toUpperCase(), e -> root.setCenter(host));
		MenuButton multiplayer = new MenuButton("multiplayer".toUpperCase(), e -> root.setCenter(join));
		box.setBackground(new Background(new BackgroundFill(Color.gray(0.375), null, null)));
		box.getChildren().addAll(singleplayer, multiplayer);

		root.setBackground(new Background(new BackgroundFill(Color.gray(0.25), null, null)));
		root.setTop(box);
		box.setAlignment(Pos.TOP_LEFT);
	}
}
