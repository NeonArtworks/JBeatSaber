package at.neonartworks.jbeatsaber.json.level;

import at.neonartworks.jbeatsaber.json.difficulty.Difficulty;
import at.neonartworks.jbeatsaber.json.event.Event;
import at.neonartworks.jbeatsaber.json.note.Note;
import at.neonartworks.jbeatsaber.json.obstacle.Obstacle;

public class Level
{

	private Track level;
	private Difficulty difficutly;

	public Level(Track level, Difficulty difficutly)
	{
		super();
		this.level = level;
		this.difficutly = difficutly;
	}

	public void addNote(Note n)
	{
		this.level.addNote(n);
	}

	public void removeNote(Note n)
	{
		this.level.removeNote(n);
	}

	public void removeNoteByIndex(int index)
	{
		this.level.removeNoteByIndex(index);
	}

	public void addObstacle(Obstacle obstacle)
	{
		this.level.addObstacle(obstacle);
	}

	public void removeObstacle(Obstacle obstacle)
	{
		this.level.removeObstacle(obstacle);
	}

	public void removeObstacleByIndex(int index)
	{
		this.level.removeObstacleByIndex(index);
	}

	public void addEvent(Event e)
	{
		this.level.addEvent(e);
	}

	public void removeEvent(Event e)
	{
		this.level.removeEvent(e);
	}

	public void removeEventByIndex(int index)
	{
		this.level.removeEventByIndex(index);
	}

	public Track getTrack()
	{
		return level;
	}

	public void setTrack(Track level)
	{
		this.level = level;
	}

	public Difficulty getDifficutly()
	{
		return difficutly;
	}

	public void setDifficutly(Difficulty difficutly)
	{
		this.difficutly = difficutly;
	}

}
