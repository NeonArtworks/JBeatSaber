package at.neonartworks.jbeatsaber.util;

public class Util
{
	public static String getVersion()
	{
		return "1.5.0";
	}

	/**
	 * Rounds a value to a some decimal placesF
	 * 
	 * @param value  the value to round
	 * @param places the decimalplaces of the new double
	 * @return
	 */
	public static double round(double value, int places)
	{
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}
