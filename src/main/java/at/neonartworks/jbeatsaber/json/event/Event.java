package at.neonartworks.jbeatsaber.json.event;

public class Event
{
	private double _time;
	private int _type;
	private int _value;

	public Event(double _time, int _type, int _value)
	{
		super();
		this._time = _time;
		this._type = _type;
		this._value = _value;
	}

	public double getTime()
	{
		return _time;
	}

	public void setTime(double _time)
	{
		this._time = _time;
	}

	public int getType()
	{
		return _type;
	}

	public void setType(int _type)
	{
		this._type = _type;
	}

	public int getValue()
	{
		return _value;
	}

	public void setValue(int _value)
	{
		this._value = _value;
	}
	
	
	
}
