package org.dionysius.game;

import org.dionysius.grpc.Corresponding;
import org.dionysius.grpc.PushHelper;
import org.dionysius.grpc.StatusReply;

public abstract class Action extends Entity implements Corresponding<StatusReply.Action> {
	private final Character character;

	public Action(Character character) {
		super(character.getGame());
		this.character = character;
		getGame().getActions().add(this);
	}

	@Override
	public abstract void update(double deltaT);

	@Override
	public void destroy() {
		super.destroy();
		getGame().getActions().remove(this);
	}

	@Override
	public void changed() {
		for (PushHelper helper : getPushHelpers()) {
			helper.getActions().add(this);
		}
	}

	@Override
	public String getContainer() {
		return "Action";
	}

	private int livetime;

	public boolean hasLivetime() {
		return !(livetime > 0);
	}

	public int getLivetime() {
		return livetime;
	}

	public void setLivetime(int livetime) {
		this.livetime = livetime;
	}

	public Character getCharacter() {
		return character;
	}

	public static class Arrow extends Action {
		public Arrow(Character character) {
			super(character);
		}

		@Override
		public void update(double deltaT) {
			if (!move()) {
				destroy();
				return;
			}
			for (int i = 0; i < getGame().getEntities().size(); i++) {
				Entity entity = getGame().getEntities().get(i);
				if (entity instanceof Character) {
					Character character = (Character) entity;
					if (character.getTeam() != getCharacter().getTeam())
						if (entity.getBounds().contains(getPosition().subtract(entity.getPosition()))) {
							character.setHitPoints(character.getHitPoints() - .5);
							destroy();
							return;
						}
				}
			}
		}
	}

	@Override
	public org.dionysius.grpc.StatusReply.Action associated() {
		return StatusReply.Action.newBuilder().setId(getId()).setType(getType())
				.setPosition(Corresponding.transform(getPosition())).build();
	}
}
