package org.dionysius.graphic;

import java.util.Map;

import org.dionysius.grpc.ActivationRequest;
import org.dionysius.grpc.JoinReply;
import org.dionysius.grpc.JoinRequest;
import org.dionysius.grpc.LeaveRequest;
import org.dionysius.grpc.MovementRequest;
import org.dionysius.grpc.StatusReply;
import org.dionysius.grpc.StatusRequest;
import org.dionysius.grpc.Corresponding;
import org.dionysius.grpc.DemonClient;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Camera;
import javafx.scene.ParallelCamera;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class Player extends Character {
	private final DemonClient client;
	private final Camera camera;

	public Player(DemonClient client) {
		super(client.getGame());
		this.client = client;
		join("" + System.getProperty("user.name"), "");

		Game game = client.getGame();
		camera = new ParallelCamera();
		camera.setClip(new Rectangle(200, 200));
		camera.layoutXProperty().bind(layoutXProperty().subtract(1024 / 2));
		camera.layoutYProperty().bind(layoutYProperty().subtract((768 - 24) / 2));
		getMouseHandler().forEach((k, v) -> game.addEventHandler(k, v));
		game.setCamera(camera);
		game.getPlayer().set(this);
	}

	private String token = "";

	public void join(String n, String p) {
		try {
			JoinReply reply = client.getBlockingStub()
					.join(JoinRequest.newBuilder().setUsername(n).setPassword(p).build());
			token = reply.getToken();
			getGame().getCharacters().put(reply.getId(), this);
		} catch (Exception ex) {
		}
	}

	public void leave() {
		try {
			client.getBlockingStub().leave(LeaveRequest.newBuilder().setToken(token).build());
		} catch (Exception ex) {
		}
	}

	public void move() {
		try {
			client.getBlockingStub().move(MovementRequest.newBuilder().setToken(token)
					.setTarget(Corresponding.transform(new Point2D(mX, mY))).build());
		} catch (Exception ex) {
		}
	}

	public void activate() {
		try {
			client.getBlockingStub().activate(ActivationRequest.newBuilder().setToken(token).setSlot(slot)
					.setTarget(Corresponding.transform(new Point2D(aX, aY))).build());
		} catch (Exception ex) {

		}
	}

	public void view() {
		try {
			Game game = getGame();
			StatusReply reply = client.getBlockingStub().status(StatusRequest.newBuilder().setToken(token).build());
			for (StatusReply.Team team : reply.getTeamsList())
				game.save(game.getTeams(), team.getId(), "Team").switched(team);
			for (StatusReply.Character character : reply.getCharactersList())
				game.save(game.getCharacters(), character.getId(), "Character").switched(character);
			for (StatusReply.Action action : reply.getActionsList())
				game.save(game.getActions(), action.getId(), action.getType()).switched(action);
			for (StatusReply.Area area : reply.getAreasList())
				game.save(game.getAreas(), area.getId(), "Area").switched(area);
			for (Map.Entry<String, String> entry : reply.getDestroyedMap().entrySet())
				game.getFlat().get(entry.getKey()).remove(entry.getValue());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private transient double mX = 0, mY = 0, aX = 0, aY = 0;
	private transient int slot = 1;

	public Map<EventType<MouseEvent>, EventHandler<MouseEvent>> getMouseHandler() {
		return Map.of(MouseEvent.MOUSE_CLICKED, e -> {
			aX = e.getSceneX() + camera.getLayoutX() - getLayoutX();
			aY = e.getSceneY() + camera.getLayoutY() - getLayoutY();
			activate();
		});
	}

	public Map<EventType<KeyEvent>, EventHandler<KeyEvent>> getKeyHandler() {
		return Map.of(KeyEvent.KEY_PRESSED, e -> {
			int speed = 1;
			switch (e.getCode()) {
			case W:
				mY = -speed;
				break;
			case A:
				mX = -speed;
				break;
			case S:
				mY = speed;
				break;
			case D:
				mX = speed;
				break;
			case DIGIT1:
				slot = 0;
				break;
			case DIGIT2:
				slot = 1;
				break;
			default:
				break;
			}
			move();
		}, KeyEvent.KEY_RELEASED, e -> {
			switch (e.getCode()) {
			case W:
				mY = 0;
				break;
			case A:
				mX = 0;
				break;
			case S:
				mY = 0;
				break;
			case D:
				mX = 0;
				break;
			default:
				break;
			}
			move();
		});
	}
}
