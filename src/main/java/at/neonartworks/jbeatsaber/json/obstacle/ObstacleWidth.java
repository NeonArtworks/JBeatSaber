package at.neonartworks.jbeatsaber.json.obstacle;

import java.util.Random;

public enum ObstacleWidth
{

	ONE_LINE(1), TWO_LINES(2), THREE_LINES(3), FOUR_LINES(4);

	private int width;

	ObstacleWidth(int width)
	{
		this.width = width;
	}

	public int getObstacleWidth()
	{
		return this.width;
	}
	
	public static ObstacleWidth getByID(int id)
	{
		for (ObstacleWidth pos : values())
		{
			if (pos.getObstacleWidth() == id)
			{
				return pos;
			}
		}
		return ObstacleWidth.ONE_LINE;
	}

	public static ObstacleWidth getRandom()
	{
		Random random = new Random();
		return getByID(random.nextInt(5));

	}
	
}
