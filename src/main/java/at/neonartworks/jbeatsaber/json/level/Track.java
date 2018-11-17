package at.neonartworks.jbeatsaber.json.level;

import java.util.ArrayList;
import java.util.List;

import at.neonartworks.jbeatsaber.json.event.Event;
import at.neonartworks.jbeatsaber.json.note.Note;
import at.neonartworks.jbeatsaber.json.obstacle.Obstacle;
import at.neonartworks.jbeatsaber.util.Util;

public class Track
{

	private String _version;
	private double _beatsPerMinute;
	private int _beatsPerBar;
	private int _noteJumpSpeed;
	private int _shuffle;
	private double _shufflePeriod;
	private List<Event> _events;
	private List<Obstacle> _obstacles;
	private List<Note> _notes;

	public Track(double _beatsPerMinute, int _beatsPerBar, int _noteJumpSpeed, int _shuffle, double _shufflePeriod,
			List<Event> _events, List<Obstacle> _obstacles, List<Note> _notes)
	{
		super();
		this._version = Util.getVersion();
		this._beatsPerMinute = _beatsPerMinute;
		this._beatsPerBar = _beatsPerBar;
		this._noteJumpSpeed = _noteJumpSpeed;
		this._shuffle = _shuffle;
		this._shufflePeriod = _shufflePeriod;
		this._events = _events;
		this._obstacles = _obstacles;
		this._notes = _notes;
	}

	public Track(double _beatsPerMinute, int _beatsPerBar, int _noteJumpSpeed, int _shuffle, double _shufflePeriod)
	{
		super();
		this._version = Util.getVersion();
		this._beatsPerMinute = _beatsPerMinute;
		this._beatsPerBar = _beatsPerBar;
		this._noteJumpSpeed = _noteJumpSpeed;
		this._shuffle = _shuffle;
		this._shufflePeriod = _shufflePeriod;
		this._notes = new ArrayList<Note>();
		this._obstacles = new ArrayList<Obstacle>();
		this._events = new ArrayList<Event>();
	}

	public void addNote(Note n)
	{
		this._notes.add(n);
	}

	public void removeNote(Note n)
	{
		this._notes.remove(n);
	}

	public void removeNoteByIndex(int index)
	{
		this._notes.remove(index);
	}

	public void addObstacle(Obstacle obstacle)
	{
		this._obstacles.add(obstacle);
	}

	public void removeObstacle(Obstacle obstacle)
	{
		this._obstacles.remove(obstacle);
	}

	public void removeObstacleByIndex(int index)
	{
		this._obstacles.remove(index);
	}

	public void addEvent(Event e)
	{
		this._events.add(e);
	}

	public void removeEvent(Event e)
	{
		this._events.remove(e);
	}

	public void removeEventByIndex(int index)
	{
		this._events.remove(index);
	}

	public String getVersion()
	{
		return _version;
	}

	public double getBeatsPerMinute()
	{
		return _beatsPerMinute;
	}

	public int getBeatsPerBar()
	{
		return _beatsPerBar;
	}

	public int getNoteJumpSpeed()
	{
		return _noteJumpSpeed;
	}

	public int getShuffle()
	{
		return _shuffle;
	}

	public double getShufflePeriod()
	{
		return _shufflePeriod;
	}

	public List<Event> getEvents()
	{
		return _events;
	}

	public List<Obstacle> getObstacles()
	{
		return _obstacles;
	}

	public List<Note> getNotes()
	{
		return _notes;
	}

	public void setVersion(String _version)
	{
		this._version = _version;
	}

	public void setBeatsPerMinute(double _beatsPerMinute)
	{
		this._beatsPerMinute = _beatsPerMinute;
	}

	public void setBeatsPerBar(int _beatsPerBar)
	{
		this._beatsPerBar = _beatsPerBar;
	}

	public void setNoteJumpSpeed(int _noteJumpSpeed)
	{
		this._noteJumpSpeed = _noteJumpSpeed;
	}

	public void setShuffle(int _shuffle)
	{
		this._shuffle = _shuffle;
	}

	public void setShufflePeriod(double _shufflePeriod)
	{
		this._shufflePeriod = _shufflePeriod;
	}

	public void setEvents(List<Event> _events)
	{
		this._events = _events;
	}

	public void setObstacles(List<Obstacle> _obstacles)
	{
		this._obstacles = _obstacles;
	}

	public void setNotes(List<Note> _notes)
	{
		this._notes = _notes;
	}

}
