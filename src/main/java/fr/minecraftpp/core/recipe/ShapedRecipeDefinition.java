package fr.minecraftpp.core.recipe;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A crafting recipe whose ingredients must follow a pattern. Each character of the pattern is an ingredient of the key, a space is an empty slot.
 */
public record ShapedRecipeDefinition(String id, List<String> pattern, Map<Character, Ingredient> key, String result, int count, RecipeCategory category) implements RecipeDefinition
{
	public ShapedRecipeDefinition
	{
		pattern = List.copyOf(pattern);
		// Sorted, so that the generated recipe files do not depend on the iteration order of the given map
		key = Collections.unmodifiableMap(new TreeMap<>(key));
		validate(id, pattern, key, count);
	}

	private static void validate(String id, List<String> pattern, Map<Character, Ingredient> key, int count)
	{
		int width = pattern.isEmpty() ? 0 : pattern.getFirst().length();

		if (pattern.isEmpty() || pattern.size() > 3 || width == 0 || width > 3 || pattern.stream().anyMatch(row -> row.length() != width))
		{
			throw new IllegalArgumentException("Invalid pattern for recipe " + id + ": " + pattern);
		}

		for (String row : pattern)
		{
			for (char symbol : row.toCharArray())
			{
				if (symbol != ' ' && !key.containsKey(symbol))
				{
					throw new IllegalArgumentException("Recipe " + id + " has no ingredient for '" + symbol + "'");
				}
			}
		}

		if (count < 1)
		{
			throw new IllegalArgumentException("Recipe " + id + " must produce at least one item");
		}
	}
}
