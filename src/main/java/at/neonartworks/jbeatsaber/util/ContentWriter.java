package at.neonartworks.jbeatsaber.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import at.neonartworks.jbeatsaber.json.difficulty.Difficulty;
import at.neonartworks.jbeatsaber.json.info.Info;
import at.neonartworks.jbeatsaber.json.level.Level;
import at.neonartworks.jbeatsaber.json.level.Track;

public class ContentWriter
{
	private final static GsonBuilder gsonB = new GsonBuilder();
	private static final Gson gson;
	static
	{
		// System.out.println(configFile); DEBUG

		gsonB.setPrettyPrinting();
		gson = gsonB.create();
	}

	public Level readJson(File f)
	{
		JsonReader reader = null;
		try
		{
			reader = new JsonReader(new FileReader(f));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Track da = gson.fromJson(reader, Track.class);
		Level data = new Level(da, Difficulty.EASY);
		return data;
	}

	public void writeInfo(Info info, String path)
	{

		String jsonOut = gson.toJson(info);
		Path p = new File(path + "/" + "info.json").toPath();

		try
		{
			BufferedWriter writer = Files.newBufferedWriter(p, StandardCharsets.UTF_8);
			writer.append(jsonOut);

			writer.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void writeTrack(Level level, String path)
	{
		String jsonOut = gson.toJson(level.getTrack());

		Path p = new File(path + "/" + level.getDifficutly().getJsonPath()).toPath();
		try
		{
			BufferedWriter writer = Files.newBufferedWriter(p, StandardCharsets.UTF_8);
			writer.append(jsonOut);

			writer.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
