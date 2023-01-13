package org.dionysius.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
@JsonDeserialize
@JacksonXmlRootElement(localName = "map-pack")
public class MapPack {
	@JsonProperty("areas")
	public List<Area> areas;
	@JsonProperty("teams")
	public List<Team> teams;

	@JsonCreator
	public MapPack(@JsonProperty("areas") List<Area> areas, @JsonProperty("teams") List<Team> teams) {
		this.areas = areas;
		this.teams = teams;
	}

	public static void export(MapPack pack, String location) throws IOException {
		File file = new File(location);
		if (!file.exists())
			file.createNewFile();
		try (FileWriter writer = new FileWriter(file)) {
			writer.append(new XmlMapper().writeValueAsString(pack));
			writer.flush();
		}
	}

	public static MapPack load(String location) throws IOException {
		File file = new File(location);
		if (!file.exists())
			file.createNewFile();
		try (InputStream stream = new FileInputStream(location)) {
			return new XmlMapper().readValue(stream, MapPack.class);
		}
	}

	public List<Area> getAreas() {
		return areas;
	}

	public List<Team> getTeams() {
		return teams;
	}
}
