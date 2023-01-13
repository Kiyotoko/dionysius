package org.dionysius.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Physical {
	@JsonProperty("position")
	protected Vector position = Vector.ZERO;
	@JsonIgnore
	private transient String id;
	@JsonIgnore
	private transient String type;

	public String getId() {
		if (id == null)
			id = Integer.toHexString(hashCode());
		return id;
	}

	public String getType() {
		if (type == null)
			type = getClass().getSimpleName();
		return type;
	}

	@JsonIgnore
	public String getContainer() {
		return getType();
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}
}
