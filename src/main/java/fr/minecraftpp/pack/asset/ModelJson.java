package fr.minecraftpp.pack.asset;

import java.util.Map;
import java.util.TreeMap;

/**
 * A block or item model that inherits its shape from a parent model and only names its textures.
 */
public record ModelJson(String parent, Map<String, String> textures)
{
	/**
	 * The textures are sorted by key, so that a set always produces the same file.
	 */
	public ModelJson
	{
		textures = new TreeMap<>(textures);
	}
}
