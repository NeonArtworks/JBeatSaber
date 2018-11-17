package at.neonartworks.jbeatsaber.json.note;

import java.util.Random;

public enum NoteType
{

	RED(0), BLUE(1), BOMB(2);

	private int noteType;

	private NoteType(int type)
	{
		this.noteType = type;
	}

	public int getNoteType()
	{
		return noteType;
	}
	
	public static NoteType getByID(int id)
	{
		for (NoteType pos : values())
		{
			if (pos.getNoteType() == id)
			{
				return pos;
			}
		}
		return NoteType.BLUE;
	}

	public static NoteType getRandom()
	{
		Random random = new Random();
		return getByID(random.nextInt(3));

	}
	
}
