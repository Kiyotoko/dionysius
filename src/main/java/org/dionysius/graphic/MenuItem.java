package org.dionysius.graphic;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class MenuItem<T extends Node> extends StackPane {
	private final T top;

	public MenuItem(T top) {
		this.top = top;
		Rectangle bottom = new Rectangle(150, 30);
		bottom.setFill(Color.gray(.125));
		bottom.setArcWidth(16);
		bottom.setArcHeight(16);
		getChildren().addAll(bottom, top);
	}

	public T getTop() {
		return top;
	}

	public static class MenuButton extends MenuItem<Label> {
		public MenuButton(String text, EventHandler<MouseEvent> handler) {
			super(new Label(text.toUpperCase()));
			getTop().setFont(Font.font("Ubuntu", 16));
			getTop().setTextFill(Color.CRIMSON);
			addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
		}
	}

	/**
	 * @see https://dx.dragan.ba/javafx-textfield-custom-css/
	 */
	public static class MenuInput extends MenuItem<TextField> {
		public MenuInput(String promt) {
			super(new TextField());
			getTop().maxWidthProperty().bind(new SimpleDoubleProperty(130));
			getTop().maxHeightProperty().bind(new SimpleDoubleProperty(30));
			getTop().setPromptText(promt.toUpperCase());
			getTop().setFont(Font.font("Ubuntu", 16));
			getTop().setFocusTraversable(false);
			getTop().setStyle(//
					"-fx-background-color:#00000000;" //
							+ "-fx-background-insets: 0, 0 0 1 0;"//
							+ "-fx-background-radius: 0;" //
							+ "-fx-text-fill: #DC143C;"//
			);
		}

		public String getInput() {
			return getTop().getText();
		}
	}
}
