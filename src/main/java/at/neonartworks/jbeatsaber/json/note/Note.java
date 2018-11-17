package at.neonartworks.jbeatsaber.json.note;

import at.neonartworks.jbeatsaber.position.HorizontalPosition;
import at.neonartworks.jbeatsaber.position.VerticalPosition;
import at.neonartworks.jbeatsaber.util.Util;

public class Note
{
	private double _time;
	private int _lineIndex;
	private int _lineLayer;
	private int _type;
	private int _cutDirection;

	/**
	 * Converts time (in milliseconds) into the time in beats
	 * 
	 * @param time time in milliseconds
	 * @param bpm  the bpm of the track
	 * @return the position in beats
	 */
	public static double ms2beats(double time, double bpm)
	{

		return (time * bpm) / (1000d * 60d);
	}

	public static double beats2ms(double beat, double bpm)
	{
		return (beat * 60000d) / bpm;
	}

	public Note(double _time, int _lineIndex, int _lineLayer, int _type, int _cutDirection)
	{
		super();
		this._time = _time;
		this._lineIndex = _lineIndex;
		this._lineLayer = _lineLayer;
		this._type = _type;
		this._cutDirection = _cutDirection;
	}

	public Note(double beats, HorizontalPosition index, VerticalPosition layer, NoteType type, CutDirection direction)
	{

		super();
		this._time = beats;
		this._lineIndex = index.getHorizontalPosition();
		this._lineLayer = layer.getVerticalPosition();
		this._type = type.getNoteType();
		this._cutDirection = direction.getCutDirection();

	}

	public void setTime(double _time)
	{
		this._time = _time;
	}

	public void setHorizontalPosition(HorizontalPosition pos)
	{
		this._lineIndex = pos.getHorizontalPosition();
	}

	public void setVerticalPosition(VerticalPosition pos)
	{
		this._lineLayer = pos.getVerticalPosition();
	}

	public void setHorizontalPosition(int _lineIndex)
	{
		this._lineIndex = _lineIndex;
	}

	public void setVerticalPosition(int _lineLayer)
	{
		this._lineLayer = _lineLayer;
	}

	public void setType(NoteType type)
	{
		this._type = type.getNoteType();
	}

	public void setType(int _type)
	{
		this._type = _type;
	}

	public void setCutDirection(CutDirection direction)
	{
		this._cutDirection = direction.getCutDirection();
	}

	public void setCutDirection(int _cutDirection)
	{
		this._cutDirection = _cutDirection;
	}

	public double getTime()
	{
		return _time;
	}

	public int getHorizontalPosition()
	{
		return _lineIndex;
	}

	public int getVerticalPosition()
	{
		return _lineLayer;
	}

	public int getType()
	{
		return _type;
	}

	public int getCutDirection()
	{
		return _cutDirection;
	}

}
