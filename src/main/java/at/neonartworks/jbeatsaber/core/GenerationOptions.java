package at.neonartworks.jbeatsaber.core;

public class GenerationOptions
{
	private int timeBetweenNotesInMs = 100;
	private static GenerationOptions instance;

	public static GenerationOptions getInstance()
	{
		if (instance == null)
		{
			instance = new GenerationOptions();
		}
		return instance;
	}

	public int getTimeBetweenNotesInMs()
	{
		return timeBetweenNotesInMs;
	}

	public void setTimeBetweenNotesInMs(int timeBetweenNotesInMs)
	{
		this.timeBetweenNotesInMs = timeBetweenNotesInMs;
	}

}
