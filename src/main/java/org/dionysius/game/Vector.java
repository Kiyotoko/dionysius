package org.dionysius.game;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javafx.geometry.Point2D;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
@JsonDeserialize
public class Vector extends Point2D {
	public static final Vector ZERO = new Vector(0.0, 0.0);

	@JsonCreator
	public Vector(@JsonProperty("x") double x, @JsonProperty("y") double y) {
		super(x, y);
	}
}
