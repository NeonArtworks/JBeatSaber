package at.neonartworks.jbeatsaber.json.info;

import java.util.ArrayList;
import java.util.List;

import at.neonartworks.jbeatsaber.json.difficulty.DifficultyLevel;
import at.neonartworks.jbeatsaber.json.environment.Environment;

public class Info
{
	private String songName;
	private String songSubName;
	private String authorName;
	private double beatsPerMinute;
	private int previewStartTime;
	private int previewDuration;
	private String coverImagePath;
	private String environmentName;
	private List<DifficultyLevel> difficultyLevels;

	public Info(String songName, String songSubName, String authorName, double beatsPerMinute, int previewStartTime,
			int previewDuration, String coverImagePath, Environment environmentName,
			List<DifficultyLevel> difficultyLevels)
	{
		super();
		this.songName = songName;
		this.songSubName = songSubName;
		this.authorName = authorName;
		this.beatsPerMinute = beatsPerMinute;
		this.previewStartTime = previewStartTime;
		this.previewDuration = previewDuration;
		this.coverImagePath = coverImagePath;
		this.environmentName = environmentName.getEnvironmentName();
		this.difficultyLevels = difficultyLevels;
	}

	public Info(String songName, String songSubName, String authorName, double beatsPerMinute, int previewStartTime,
			int previewDuration, String coverImagePath, Environment environmentName)
	{
		super();
		this.songName = songName;
		this.songSubName = songSubName;
		this.authorName = authorName;
		this.beatsPerMinute = beatsPerMinute;
		this.previewStartTime = previewStartTime;
		this.previewDuration = previewDuration;
		this.coverImagePath = coverImagePath;
		this.environmentName = environmentName.getEnvironmentName();
		this.difficultyLevels = new ArrayList<DifficultyLevel>();
	}

	public void addDifficulty(DifficultyLevel diffLevel)
	{
		this.difficultyLevels.add(diffLevel);
	}

	public void removeDifficulty(DifficultyLevel diffLevel)
	{
		this.difficultyLevels.remove(diffLevel);
	}

	public String getSongName()
	{
		return songName;
	}

	public void setSongName(String songName)
	{
		this.songName = songName;
	}

	public String getSongSubName()
	{
		return songSubName;
	}

	public void setSongSubName(String songSubName)
	{
		this.songSubName = songSubName;
	}

	public String getAuthorName()
	{
		return authorName;
	}

	public void setAuthorName(String authorName)
	{
		this.authorName = authorName;
	}

	public double getBeatsPerMinute()
	{
		return beatsPerMinute;
	}

	public void setBeatsPerMinute(double beatsPerMinute)
	{
		this.beatsPerMinute = beatsPerMinute;
	}

	public int getPreviewStartTime()
	{
		return previewStartTime;
	}

	public void setPreviewStartTime(int previewStartTime)
	{
		this.previewStartTime = previewStartTime;
	}

	public int getPreviewDuration()
	{
		return previewDuration;
	}

	public void setPreviewDuration(int previewDuration)
	{
		this.previewDuration = previewDuration;
	}

	public String getCoverImagePath()
	{
		return coverImagePath;
	}

	public void setCoverImagePath(String coverImagePath)
	{
		this.coverImagePath = coverImagePath;
	}

	public String getEnvironmentName()
	{
		return environmentName;
	}

	public void setEnvironmentName(String environmentName)
	{
		this.environmentName = environmentName;
	}

	public List<DifficultyLevel> getDifficultyLevels()
	{
		return difficultyLevels;
	}

	public void setDifficultyLevels(List<DifficultyLevel> difficultyLevels)
	{
		this.difficultyLevels = difficultyLevels;
	}

}
