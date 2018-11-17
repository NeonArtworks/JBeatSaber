package at.neonartworks.jbeatsaber.json.environment;

public enum Environment
{

	DEFAULT("DefaultEnvironment"), NICE("NiceEnvironment");

	private String envir;

	private Environment(String name)
	{
		this.envir = name;
	}

	public String getEnvironmentName()
	{
		// TODO Auto-generated method stub
		return this.envir;
	}

}
