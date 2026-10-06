package fr.minecraftpp.pack.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * The shapes of the ore features of the world generation: the configured feature says what a vein is made of, the placed feature where and how often it appears. A null field is left out of the file.
 */
public final class OreFeatureJson
{
	private OreFeatureJson()
	{
	}

	public record Configured(String type, OreConfig config)
	{
	}

	/**
	 * @param size the number of blocks of a vein
	 */
	public record OreConfig(@SerializedName("discard_chance_on_air_exposure") float discardChanceOnAirExposure, int size, List<Target> targets)
	{
	}

	/**
	 * The ore state placed where the vein meets a block of the replaceable tag.
	 */
	public record Target(BlockStateJson state, RuleTest target)
	{
	}

	public record BlockStateJson(@SerializedName("Name") String name)
	{
	}

	public record RuleTest(@SerializedName("predicate_type") String predicateType, String tag)
	{
	}

	public record Placed(String feature, List<Placement> placement)
	{
	}

	public record Placement(String type, Integer count, HeightProvider height)
	{
		public static Placement count(int count)
		{
			return new Placement("minecraft:count", count, null);
		}

		public static Placement inSquare()
		{
			return new Placement("minecraft:in_square", null, null);
		}

		public static Placement uniformHeight(int minInclusive, int maxInclusive)
		{
			return new Placement("minecraft:height_range", null, new HeightProvider("minecraft:uniform", new Anchor(minInclusive), new Anchor(maxInclusive)));
		}

		public static Placement biome()
		{
			return new Placement("minecraft:biome", null, null);
		}
	}

	public record HeightProvider(String type, @SerializedName("min_inclusive") Anchor minInclusive, @SerializedName("max_inclusive") Anchor maxInclusive)
	{
	}

	public record Anchor(int absolute)
	{
	}
}
