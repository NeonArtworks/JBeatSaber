package at.neonartworks.jbeatsaber.position;

import java.util.Random;

public enum VerticalPosition
{
	BOTTOM(0), MIDDLE(1), TOP(2);

	private int verticalPosition;

	VerticalPosition(int pos)
	{
		this.verticalPosition = pos;
	}

	public int getVerticalPosition()
	{
		return verticalPosition;
	}

	public static VerticalPosition getByID(int id)
	{
		for (VerticalPosition pos : values())
		{
			if (pos.getVerticalPosition() == id)
			{
				return pos;
			}
		}
		return VerticalPosition.BOTTOM;
	}

	public static VerticalPosition getRandom()
	{
		Random random = new Random();
		return getByID(random.nextInt(4));

	}

}
