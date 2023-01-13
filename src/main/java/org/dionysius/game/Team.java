package org.dionysius.game;

import java.util.ArrayList;
import java.util.List;

import org.dionysius.grpc.StatusReply;
import org.dionysius.grpc.Corresponding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Team implements Corresponding<StatusReply.Team> {
	@JsonProperty("targets")
	public List<String> targets;
	@JsonProperty("members")
	public List<Character> members;
	@JsonProperty("spawnpoint")
	public Vector spawnpoint;
	@JsonProperty("color")
	public String color;

	public Team() {
		targets = new ArrayList<>();
		members = new ArrayList<>();
		setColor(String.format("0x%02x%02x%02x%02x", (int) Math.round(125 + Math.random() * 125),
				(int) Math.round(125 + Math.random() * 125), (int) Math.round(125 + Math.random() * 125), 255));
	}

	@JsonIgnore
	private transient String id;

	public String getId() {
		if (id == null)
			id = Integer.toHexString(hashCode());
		return id;
	}

	public List<String> getTargets() {
		return targets;
	}

	public List<Character> getMembers() {
		return members;
	}

	public Vector getSpawnpoint() {
		return spawnpoint;
	}

	public void setSpawnpoint(Vector spawnpoint) {
		this.spawnpoint = spawnpoint;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public StatusReply.Team associated() {
		return StatusReply.Team.newBuilder().setId(getId()).addAllTarget(targets).setColor(color).build();
	}
}
