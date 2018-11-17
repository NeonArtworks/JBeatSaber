package jbeatsaber.test;

import at.neonartworks.jbeatsaber.core.JBeatSaber;
import at.neonartworks.jbeatsaber.json.difficulty.Difficulty;
import at.neonartworks.jbeatsaber.json.environment.Environment;
import at.neonartworks.jbeatsaber.json.level.Level;

public class JBeatsSaberTest
{

	public static void main(String[] args)
	{
		JBeatSaber map = new JBeatSaber(140d, "TestSong", "subsub", "NeonArtworks", "test.jpg", Environment.NICE,
				"W:\\[Music]\\RUDE - Eternal Youth.mp3", 0, 10, 12, 10, 0, 0, "S:\\_1_steam\\_0_games\\steamapps\\common\\Beat Saber\\CustomSongs\\output");

		Level easyLevel = map.createLevel(Difficulty.EASY);
		map.populateLevel(easyLevel);
		map.saveMaps();

	}

}
