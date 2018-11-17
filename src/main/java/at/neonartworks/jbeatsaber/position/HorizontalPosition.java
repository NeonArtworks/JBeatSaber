package at.neonartworks.jbeatsaber.position;

import java.util.Random;

public enum HorizontalPosition
{
	FARLEFT(0), LEFT(1), RIGHT(2), FARRIGHT(3);

	private int horizontalPosition;

	private HorizontalPosition(int pos)
	{
		this.horizontalPosition = pos;
	}

	public int getHorizontalPosition()
	{
		return horizontalPosition;
	}

	public static HorizontalPosition getByID(int id)
	{
		for (HorizontalPosition pos : values())
		{
			if (pos.getHorizontalPosition() == id)
			{
				return pos;
			}
		}
		return HorizontalPosition.FARLEFT;
	}

	public static HorizontalPosition getRandom()
	{
		Random random = new Random();
		return getByID(random.nextInt(4));

	}

}
