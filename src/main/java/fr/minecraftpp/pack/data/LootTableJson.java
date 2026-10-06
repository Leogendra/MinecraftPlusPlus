package fr.minecraftpp.pack.data;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * The shapes of a block loot table file, limited to what the generated blocks need. A null field is left out of the file.
 */
public record LootTableJson(String type, List<LootTableJson.Pool> pools, @SerializedName("random_sequence") String randomSequence)
{
	public static LootTableJson block(String randomSequence, Pool pool)
	{
		return new LootTableJson("minecraft:block", List.of(pool), randomSequence);
	}

	public record Pool(float rolls, @SerializedName("bonus_rolls") float bonusRolls, List<Condition> conditions, List<Entry> entries)
	{
		public static Pool single(List<Condition> conditions, Entry entry)
		{
			return new Pool(1.0F, 0.0F, conditions, List.of(entry));
		}
	}

	public record Entry(String type, String name, List<Entry> children, List<Condition> conditions, List<Function> functions)
	{
		public static Entry item(String name)
		{
			return new Entry("minecraft:item", name, null, null, null);
		}

		public static Entry item(String name, List<Condition> conditions, List<Function> functions)
		{
			return new Entry("minecraft:item", name, null, conditions, functions);
		}

		/**
		 * The first child whose conditions pass is dropped.
		 */
		public static Entry alternatives(List<Entry> children)
		{
			return new Entry("minecraft:alternatives", null, children, null, null);
		}
	}

	public record Condition(String condition, ToolPredicate predicate)
	{
		public static Condition survivesExplosion()
		{
			return new Condition("minecraft:survives_explosion", null);
		}

		public static Condition silkTouch()
		{
			EnchantmentPredicate silkTouch = new EnchantmentPredicate("minecraft:silk_touch", new Levels(1));

			return new Condition("minecraft:match_tool", new ToolPredicate(Map.of("minecraft:enchantments", List.of(silkTouch))));
		}
	}

	public record ToolPredicate(Map<String, List<EnchantmentPredicate>> predicates)
	{
	}

	public record EnchantmentPredicate(String enchantments, Levels levels)
	{
	}

	public record Levels(int min)
	{
	}

	public record Function(String function, Count count, Boolean add, String enchantment, String formula)
	{
		public static Function setCount(int minimum, int maximum)
		{
			return new Function("minecraft:set_count", new Count("minecraft:uniform", minimum, maximum), false, null, null);
		}

		/**
		 * The vanilla ore fortune bonus: each fortune level may multiply the drop.
		 */
		public static Function fortuneOreBonus()
		{
			return new Function("minecraft:apply_bonus", null, null, "minecraft:fortune", "minecraft:ore_drops");
		}

		public static Function explosionDecay()
		{
			return new Function("minecraft:explosion_decay", null, null, null, null);
		}
	}

	public record Count(String type, float min, float max)
	{
	}
}
