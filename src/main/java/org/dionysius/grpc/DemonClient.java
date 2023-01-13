package org.dionysius.grpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.dionysius.graphic.Game;
import org.dionysius.graphic.Player;
import org.dionysius.grpc.DionysiusGrpc.DionysiusBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class DemonClient {
	private static final Logger logger = Logger.getLogger(DemonClient.class.getName());

	private final ManagedChannel channel;
	private final DionysiusBlockingStub blockingStub;

	private final BorderPane overview = new BorderPane();

	private final Game game = new Game(new Group());

	DemonClient(String target, int port) {
		channel = ManagedChannelBuilder.forAddress(target, port).usePlaintext().build();
		blockingStub = DionysiusGrpc.newBlockingStub(channel);
		logger.info("Client started");
	}

	public static DemonClient singleplayer(int port) {
		DemonServer server = new DemonServer(port);
		DemonClient client = new DemonClient("localhost", port);
		try {
			server.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				server.stop();
				client.stop();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}));
		return client;
	}

	public static DemonClient multiplayer(String target, int port) {
		DemonClient client = new DemonClient(target, port);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				client.stop();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}));
		return client;
	}

	public void start() {
		Player player = new Player(this);
		player.getKeyHandler().forEach((EventType<KeyEvent> k, EventHandler<KeyEvent> v) -> {
			game.addEventHandler(k, v);
		});
		logger.info("Logged in");
	}

	public void stop() throws Exception {
		game.getPlayer().get().leave();
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		logger.info("Client shutdown");
	}

	public DionysiusBlockingStub getBlockingStub() {
		return blockingStub;
	}

	public BorderPane getOverview() {
		return overview;
	}

	public Game getGame() {
		return game;
	}
}
