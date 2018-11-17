package at.neonartworks.jbeatsaber.json.difficulty;

public enum Difficulty
{

	EXPERT("Expert", 4, "Expert.json"), HARD("Hard", 3, "Hard.json"), NORMAL("Normal", 2, "Normal.json"),
	EASY("Easy", 1, "Easy.json");

	private String difficultyString;
	private int difficultyRank;
	private String jsonPath;

	Difficulty(String diff, int rank, String jsonPath)
	{
		this.difficultyString = diff;
		this.difficultyRank = rank;
		this.jsonPath = jsonPath;
	}

	public String getDifficultyString()
	{
		return difficultyString;
	}

	public int getDifficultyRank()
	{
		return difficultyRank;
	}

	public String getJsonPath()
	{
		return this.jsonPath;
	}

}
