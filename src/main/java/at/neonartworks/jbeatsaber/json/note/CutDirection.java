package at.neonartworks.jbeatsaber.json.note;

import java.util.Random;

import at.neonartworks.jbeatsaber.position.HorizontalPosition;

public enum CutDirection
{
	UP(0), DOWN(1), LEFT(2), RIGHT(3), UP_LEFT(4), UP_RIGHT(5), DOWN_LEFT(6), DOWN_RIGHT(7), NO_DIRECTION(7);

	private int cutDirection;

	CutDirection(int dir)
	{
		this.cutDirection = dir;
	}

	public int getCutDirection()
	{
		return cutDirection;
	}

	public static CutDirection getByID(int id)
	{
		for (CutDirection pos : values())
		{
			if (pos.getCutDirection() == id)
			{
				return pos;
			}
		}
		return CutDirection.UP;
	}

	public static CutDirection getRandom()
	{
		Random random = new Random();
		return getByID(random.nextInt(8));

	}

}
