package at.neonartworks.jbeatsaber.json.difficulty;

public class DifficultyLevel
{

	private String difficulty;
	private int difficultyRank;
	private String audioPath;
	private String jsonPath;
	private int offset = 0;
	private int oldOffset = 0;

	public DifficultyLevel(Difficulty difficulty, String audioPath)
	{
		this.difficulty = difficulty.getDifficultyString();
		this.difficultyRank = difficulty.getDifficultyRank();
		this.audioPath = audioPath;
		this.jsonPath = difficulty.getJsonPath();
		this.offset = 0;
		this.oldOffset = 0;

	}

	public DifficultyLevel(Difficulty difficulty, String audioPath, int offset, int oldOffset)
	{
		this.difficulty = difficulty.getDifficultyString();
		this.difficultyRank = difficulty.getDifficultyRank();
		this.audioPath = audioPath;
		this.jsonPath = difficulty.getJsonPath();
		this.offset = offset;
		this.oldOffset = oldOffset;

	}

	public DifficultyLevel(String difficulty, int difficultyRank, String audioPath, String jsonPath, int offset,
			int oldOffset)
	{
		super();
		this.difficulty = difficulty;
		this.difficultyRank = difficultyRank;
		this.audioPath = audioPath;
		this.jsonPath = jsonPath;
		this.offset = offset;
		this.oldOffset = oldOffset;
	}

	public String getDifficulty()
	{
		return difficulty;
	}

	public void setDifficulty(String difficulty)
	{
		this.difficulty = difficulty;
	}

	public int getDifficultyRank()
	{
		return difficultyRank;
	}

	public void setDifficultyRank(int difficultyRank)
	{
		this.difficultyRank = difficultyRank;
	}

	public String getAudioPath()
	{
		return audioPath;
	}

	public void setAudioPath(String audioPath)
	{
		this.audioPath = audioPath;
	}

	public String getJsonPath()
	{
		return jsonPath;
	}

	public void setJsonPath(String jsonPath)
	{
		this.jsonPath = jsonPath;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getOldOffset()
	{
		return oldOffset;
	}

	public void setOldOffset(int oldOffset)
	{
		this.oldOffset = oldOffset;
	}

}
