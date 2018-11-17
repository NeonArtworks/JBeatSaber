package at.neonartworks.jbeatsaber.json.obstacle;

import java.util.Random;

import at.neonartworks.jbeatsaber.json.note.NoteType;

public enum ObstacleType
{
	WALL(0), CEILING(1);

	private int type;

	ObstacleType(int type)
	{
		this.type = type;
	}

	public int getObstacleType()
	{
		return this.type;
	}
	
	public static ObstacleType getByID(int id)
	{
		for (ObstacleType pos : values())
		{
			if (pos.getObstacleType() == id)
			{
				return pos;
			}
		}
		return ObstacleType.WALL;
	}

	public static ObstacleType getRandom()
	{
		Random random = new Random();
		return getByID(random.nextInt(2));

	}
	
}
