package fr.minecraftpp.pack.asset;

import java.util.Map;

/**
 * A block state definition. The generated blocks have no property: their single variant, named by the empty string, points to their model.
 */
public record BlockStateJson(Map<String, BlockStateJson.Variant> variants)
{
	public static BlockStateJson singleModel(String model)
	{
		return new BlockStateJson(Map.of("", new Variant(model)));
	}

	public record Variant(String model)
	{
	}
}
