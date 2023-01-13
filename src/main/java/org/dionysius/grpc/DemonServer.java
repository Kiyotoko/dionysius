package org.dionysius.grpc;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.dionysius.game.Game;
import org.dionysius.game.Item;
import org.dionysius.game.Player;
import org.dionysius.grpc.DionysiusGrpc.DionysiusImplBase;

import com.google.common.hash.Hashing;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import javafx.geometry.Point2D;

public class DemonServer {
	private static final Logger logger = Logger.getLogger(DemonServer.class.getName());

	private final Server server;

	DemonServer(int port) {
		server = ServerBuilder.forPort(port).addService(new DionysiusService()).build();
	}

	public void start() throws Exception {
		server.start();
		game.getClock().start();
		logger.info("Server started, listening on " + server.getPort());
	}

	public void stop() throws Exception {
		server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		game.getClock().markAsDone();
		logger.info("Server shutdown");
	}

	private final Game game = new Game();

	private class DionysiusService extends DionysiusImplBase {
		private final HashMap<String, Player> players = new HashMap<>();

		@Override
		public void join(JoinRequest request, StreamObserver<JoinReply> responseObserver) {
			Player player = new Player(game);
			player.setName(request.getUsername());
			String token = Hashing.adler32().hashString("" + Math.random(), Charset.defaultCharset()).toString();
			players.put(token, player);
			responseObserver.onNext(JoinReply.newBuilder().setId(player.getId()).setToken(token).build());
			responseObserver.onCompleted();
		}

		@Override
		public void leave(LeaveRequest request, StreamObserver<ChangeReply> responseObserver) {
			Player player = players.get(request.getToken());
			if (player == null) {
				responseObserver.onError(new StatusException(Status.NOT_FOUND));
			} else {
				players.remove(request.getToken());
				player.destroy();
				responseObserver.onNext(ChangeReply.newBuilder().build());
			}
			responseObserver.onCompleted();
		}

		@Override
		public void status(StatusRequest request, StreamObserver<StatusReply> responseObserver) {
			Player player = players.get(request.getToken());
			if (player == null) {
				responseObserver.onError(new StatusException(Status.NOT_FOUND));
			} else {
				responseObserver.onNext(game.getPushHelpers().get(player).associated());
			}
			responseObserver.onCompleted();
		}

		@Override
		public void move(MovementRequest request, StreamObserver<ChangeReply> responseObserver) {
			Player player = players.get(request.getToken());
			if (player == null) {
				responseObserver.onError(new StatusException(Status.NOT_FOUND));
			} else {
				player.setDestination(new Point2D(request.getTarget().getX(), request.getTarget().getY()));
				responseObserver.onNext(ChangeReply.newBuilder().build());
			}
			responseObserver.onCompleted();
		}

		@Override
		public void interact(InteractionRequest request, StreamObserver<ChangeReply> responseObserver) {
			responseObserver.onNext(ChangeReply.newBuilder().build());
			responseObserver.onCompleted();
		}

		@Override
		public void activate(ActivationRequest request, StreamObserver<ChangeReply> responseObserver) {
			Player player = players.get(request.getToken());
			if (player == null) {
				responseObserver.onError(new StatusException(Status.NOT_FOUND));
			} else {
				int slot = request.getSlot();
				List<Item> items = player.getItems();
				if (items.size() > slot) {
					if (items.get(slot).canActivated())
						items.get(slot).activate(new Point2D(request.getTarget().getX(), request.getTarget().getY()));
					responseObserver.onNext(ChangeReply.newBuilder().build());
				}
			}
			responseObserver.onCompleted();
		}

	}
}
