package org.dionysius.game;

import org.dionysius.grpc.PushHelper;

public final class Player extends Character {
	public Player(Game game) {
		super(game);
		getGame().getPushHelpers().put(this, new PushHelper(getGame()));
	}
}
