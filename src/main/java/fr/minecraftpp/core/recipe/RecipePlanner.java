package fr.minecraftpp.core.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.minecraftpp.core.recipe.SmeltingRecipeDefinition.CookingMethod;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;

/**
 * Computes the recipes of the generated content, with the quantities and experience of 1.12.
 */
public final class RecipePlanner
{
	private static final int BASE_AMOUNT_OF_BLUE_DYE = 6;
	private static final int BASE_AMOUNT_OF_REDSTONE = 4;
	private static final float EXPERIENCE_PER_RARITY_LEVEL = 0.15F;
	private static final float NUGGET_SMELTING_EXPERIENCE = 0.1F;
	private static final String STICK = "minecraft:stick";

	private RecipePlanner()
	{
	}

	public static List<RecipeDefinition> plan(OreCatalog catalog)
	{
		List<RecipeDefinition> recipes = new ArrayList<>();

		for (OreSetDefinition set : catalog.sets())
		{
			addStorageRecipes(recipes, set);
			addOreSmeltingRecipes(recipes, set);
			addRoleRecipes(recipes, set);

			if (set.type().hasMaterial())
			{
				addEquipmentRecipes(recipes, set);
			}

			if (set.type() == SetType.METAL)
			{
				addNuggetRecipes(recipes, set);
			}
		}

		return recipes;
	}

	private static void addStorageRecipes(List<RecipeDefinition> recipes, OreSetDefinition set)
	{
		String item = ContentIds.item(set);
		String block = ContentIds.storageBlock(set);

		recipes.add(new ShapedRecipeDefinition(block, RecipePatterns.COMPACT, Map.of(RecipePatterns.MATERIAL, Ingredient.item(ContentIds.full(item))), ContentIds.full(block), 1, RecipeCategory.BUILDING));
		recipes.add(new ShapelessRecipeDefinition(item + "_from_" + block, List.of(Ingredient.item(ContentIds.full(block))), ContentIds.full(item), 9, RecipeCategory.MISC));
	}

	/**
	 * Both ores smelt into the item. The experience grows with the rarity, as in 1.12; blasting is the vanilla shortcut for ores.
	 */
	private static void addOreSmeltingRecipes(List<RecipeDefinition> recipes, OreSetDefinition set)
	{
		String item = ContentIds.item(set);
		float experience = (set.rarity().ordinal() + 1) * EXPERIENCE_PER_RARITY_LEVEL;

		for (String ore : List.of(ContentIds.ore(set), ContentIds.deepslateOre(set)))
		{
			recipes.add(new SmeltingRecipeDefinition(item + "_from_smelting_" + ore, CookingMethod.SMELTING, Ingredient.item(ContentIds.full(ore)), ContentIds.full(item), experience, RecipeCategory.MISC));
			recipes.add(new SmeltingRecipeDefinition(item + "_from_blasting_" + ore, CookingMethod.BLASTING, Ingredient.item(ContentIds.full(ore)), ContentIds.full(item), experience, RecipeCategory.MISC));
		}
	}

	/**
	 * The blue dye and redstone sets craft their vanilla product. The quantity is divided by the average number of items an ore drops, with an integer division as in 1.12.
	 */
	private static void addRoleRecipes(List<RecipeDefinition> recipes, OreSetDefinition set)
	{
		String item = ContentIds.item(set);
		int averageDrop = set.ore().averageQuantityDropped();

		if (set.hasRole(VanillaRole.BLUE_DYE))
		{
			recipes.add(new ShapelessRecipeDefinition("blue_dye_from_" + item, List.of(Ingredient.item(ContentIds.full(item))), "minecraft:blue_dye", BASE_AMOUNT_OF_BLUE_DYE / averageDrop, RecipeCategory.MISC));
		}

		if (set.hasRole(VanillaRole.REDSTONE))
		{
			recipes.add(new ShapelessRecipeDefinition("redstone_from_" + item, List.of(Ingredient.item(ContentIds.full(item))), "minecraft:redstone", BASE_AMOUNT_OF_REDSTONE / averageDrop, RecipeCategory.MISC));
		}
	}

	private static void addEquipmentRecipes(List<RecipeDefinition> recipes, OreSetDefinition set)
	{
		Ingredient material = Ingredient.item(ContentIds.full(ContentIds.item(set)));
		Ingredient stick = Ingredient.item(STICK);

		for (ToolType toolType : ToolType.values())
		{
			String tool = ContentIds.tool(set, toolType);
			recipes.add(new ShapedRecipeDefinition(tool, RecipePatterns.tool(toolType), Map.of(RecipePatterns.MATERIAL, material, RecipePatterns.STICK, stick), ContentIds.full(tool), 1, RecipeCategory.EQUIPMENT));
		}

		for (ArmorPiece piece : ArmorPiece.values())
		{
			String armor = ContentIds.armor(set, piece);
			recipes.add(new ShapedRecipeDefinition(armor, RecipePatterns.armor(piece), Map.of(RecipePatterns.MATERIAL, material), ContentIds.full(armor), 1, RecipeCategory.EQUIPMENT));
		}
	}

	/**
	 * Nine nuggets make an ingot and back. The tools and armor smelt into a nugget, like the vanilla iron and gold equipment.
	 */
	private static void addNuggetRecipes(List<RecipeDefinition> recipes, OreSetDefinition set)
	{
		String ingot = ContentIds.item(set);
		String nugget = ContentIds.nugget(set);

		recipes.add(new ShapedRecipeDefinition(ingot + "_from_nuggets", RecipePatterns.COMPACT, Map.of(RecipePatterns.MATERIAL, Ingredient.item(ContentIds.full(nugget))), ContentIds.full(ingot), 1, RecipeCategory.MISC));
		recipes.add(new ShapelessRecipeDefinition(nugget, List.of(Ingredient.item(ContentIds.full(ingot))), ContentIds.full(nugget), 9, RecipeCategory.MISC));

		Ingredient equipment = Ingredient.anyOf(equipmentIds(set));
		recipes.add(new SmeltingRecipeDefinition(nugget + "_from_smelting", CookingMethod.SMELTING, equipment, ContentIds.full(nugget), NUGGET_SMELTING_EXPERIENCE, RecipeCategory.MISC));
		recipes.add(new SmeltingRecipeDefinition(nugget + "_from_blasting", CookingMethod.BLASTING, equipment, ContentIds.full(nugget), NUGGET_SMELTING_EXPERIENCE, RecipeCategory.MISC));
	}

	private static List<String> equipmentIds(OreSetDefinition set)
	{
		List<String> ids = new ArrayList<>();

		for (ToolType toolType : ToolType.values())
		{
			ids.add(ContentIds.full(ContentIds.tool(set, toolType)));
		}

		for (ArmorPiece piece : ArmorPiece.values())
		{
			ids.add(ContentIds.full(ContentIds.armor(set, piece)));
		}

		return ids;
	}
}
