package fr.minecraftpp.core.recipe;

import java.util.List;

/**
 * A recipe ingredient: any of a list of items, or any item of a tag.
 *
 * @param ids   complete identifiers of the accepted items, or of the tag without its leading {@code #}
 * @param isTag whether {@code ids} holds a single tag
 */
public record Ingredient(List<String> ids, boolean isTag)
{
	public Ingredient
	{
		ids = List.copyOf(ids);

		if (ids.isEmpty() || (isTag && ids.size() != 1))
		{
			throw new IllegalArgumentException("An ingredient needs items or a single tag: " + ids);
		}
	}

	public static Ingredient item(String itemId)
	{
		return new Ingredient(List.of(itemId), false);
	}

	public static Ingredient anyOf(List<String> itemIds)
	{
		return new Ingredient(itemIds, false);
	}

	public static Ingredient tag(String tagId)
	{
		return new Ingredient(List.of(tagId), true);
	}
}
