package fr.minecraftpp.core.config;

/**
 * A seed file does not follow its format. The message tells the player what is wrong.
 */
public class MalformedSeedException extends Exception
{
	public MalformedSeedException(String message)
	{
		super(message);
	}
}
