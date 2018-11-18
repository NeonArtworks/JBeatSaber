package at.neonartworks.jbeatsaber.json.obstacle;

import at.neonartworks.jbeatsaber.position.HorizontalPosition;

public class Obstacle
{
	private double _time;
	private int _lineIndex;
	private int _type;
	private double _duration;
	private int _width;

	public Obstacle(double _time, int _lineIndex, int _type, double _duration, int _width)
	{
		super();
		this._time = _time;
		this._lineIndex = _lineIndex;
		this._type = _type;
		this._duration = _duration;
		this._width = _width;
	}

	public Obstacle(double _time, HorizontalPosition pos, ObstacleType type, double duration, ObstacleWidth width)
	{
		super();
		this._time = _time;
		this._lineIndex = pos.getHorizontalPosition();
		this._type = type.getObstacleType();
		this._width = width.getObstacleWidth();
		this._duration = duration;
	}

	public double getTime()
	{
		return _time;
	}

	public void setTime(double _time)
	{
		this._time = _time;
	}

	public int getHorizontalPosition()
	{
		return _lineIndex;
	}

	public void setHorizontalPosition(HorizontalPosition pos)
	{
		this._lineIndex = pos.getHorizontalPosition();
	}

	public void setHorizontalPosition(int _lineIndex)
	{
		this._lineIndex = _lineIndex;
	}

	public int getType()
	{
		return _type;
	}

	public void setType(ObstacleType type)
	{
		this._type = type.getObstacleType();
	}

	public void setType(int _type)
	{
		this._type = _type;
	}

	public double getDuration()
	{
		return _duration;
	}

	public void setDuration(double _duration)
	{
		this._duration = _duration;
	}

	public int getWidth()
	{
		return _width;
	}

	public void setWidth(ObstacleWidth width)
	{
		this._width = width.getObstacleWidth();
	}

	public void setWidth(int _width)
	{
		this._width = _width;
	}

}
