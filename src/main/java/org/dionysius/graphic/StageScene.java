package org.dionysius.graphic;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StageScene extends Scene {
	private final ObjectProperty<Node> header = new SimpleObjectProperty<>();
	private final ObjectProperty<Node> body = new SimpleObjectProperty<>();

	private transient double offsetX, offsetY;

	public StageScene(BorderPane root) {
		super(root, 1024, 768);
		BorderPane pane = new BorderPane();
		pane.prefWidthProperty().bind(widthProperty());
		pane.prefHeightProperty().bind(new SimpleDoubleProperty(24));
		pane.setBackground(new Background(new BackgroundFill(Color.gray(0.1), null, null)));
		pane.setOnMousePressed(e -> {
			Stage stage = (Stage) getWindow();
			offsetX = e.getScreenX() - stage.getX();
			offsetY = e.getScreenY() - stage.getY();
		});
		pane.setOnMouseDragged(e -> {
			Stage stage = (Stage) getWindow();
			stage.setX(e.getScreenX() - offsetX);
			stage.setY(e.getScreenY() - offsetY);
		});

		Label title = new Label("Demon");
		title.setFont(Font.font("Ubuntu", FontWeight.NORMAL, FontPosture.REGULAR, 16));
		title.setTextFill(Color.WHITE);
		title.setPadding(new Insets(6));
		pane.setLeft(title);

		HBox box = new HBox(4);
		box.setPadding(new Insets(6));
		Circle min = new Circle(12, Color.gray(.25));
		min.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			((Stage) getWindow()).setIconified(true);
		});
		Circle close = new Circle(12, Color.rgb(192, 0, 0));
		close.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			((Stage) getWindow()).close();
		});
		box.getChildren().addAll(min, close);
		pane.setRight(box);

		header.addListener((observable, o, n) -> {
			root.setTop(n);
		});
		body.addListener((observable, o, n) -> {
			root.getChildren().remove(o);
			if (header.get() != null)
				n.layoutYProperty().bind(pane.heightProperty());
			root.getChildren().add(n);
		});
		setHeader(pane);
	}

	public void setHeader(Node node) {
		this.header.set(node);
	}

	public Node getHeader() {
		return header.get();
	}

	public void setBody(Node node) {
		this.body.set(node);
	}

	public Node getBody() {
		return body.get();
	}
}
