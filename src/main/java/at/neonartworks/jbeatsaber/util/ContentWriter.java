package at.neonartworks.jbeatsaber.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.neonartworks.jbeatsaber.json.info.Info;
import at.neonartworks.jbeatsaber.json.level.Level;

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
