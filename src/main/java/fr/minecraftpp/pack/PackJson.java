package fr.minecraftpp.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Writes the typed records of the generated files as JSON, with the Gson library shipped with Minecraft.
 */
public final class PackJson
{
	/**
	 * HTML escaping is disabled so that texts such as death messages stay readable in the generated files.
	 */
	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

	private PackJson()
	{
	}

	public static String toJson(Object value)
	{
		return GSON.toJson(value);
	}
}
