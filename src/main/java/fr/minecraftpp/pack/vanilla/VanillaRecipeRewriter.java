package fr.minecraftpp.pack.vanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.TagIds;
import fr.minecraftpp.core.variant.VariantCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import fr.minecraftpp.pack.data.TagJson;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

/**
 * Overrides the vanilla recipes whose ingredients have generated variants, so that the variants are accepted. A recipe whose result has variants is left vanilla, as in 1.12: nine generated nuggets would otherwise make both the generated ingot and the vanilla one.
 */
public final class VanillaRecipeRewriter implements GeneratedResourceWriter
{
	private static final String RECIPE_DIRECTORY = "recipe";

	private final VanillaData vanillaData;

	public VanillaRecipeRewriter(VanillaData vanillaData)
	{
		this.vanillaData = vanillaData;
	}

	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		VariantCatalog variants = VariantCatalog.of(catalog);
		VariantIngredients ingredients = new VariantIngredients(variants, this.vanillaData.itemTagMembers());
		List<GeneratedFile> files = new ArrayList<>();

		this.vanillaData.files(RECIPE_DIRECTORY).forEach((location, recipe) -> rewrite(recipe.getAsJsonObject(), variants, ingredients).ifPresent(rewritten -> files.add(GeneratedFile.data(location, rewritten))));

		for (String vanillaTag : ingredients.rewrittenTags())
		{
			files.add(tagOfTag(vanillaTag, ingredients));
		}

		return files;
	}

	/**
	 * The rewritten recipe, or nothing when the recipe stays vanilla: it has no result, its result has variants, or none of its ingredients has.
	 */
	private static Optional<JsonElement> rewrite(JsonObject recipe, VariantCatalog variants, VariantIngredients ingredients)
	{
		Optional<String> result = resultId(recipe);

		if (result.isEmpty() || variants.hasVariants(result.get()))
		{
			return Optional.empty();
		}
		else
		{
			JsonElement rewritten = ingredients.rewrite(recipe);
			return rewritten.equals(recipe) ? Optional.empty() : Optional.of(rewritten);
		}
	}

	private static Optional<String> resultId(JsonObject recipe)
	{
		JsonElement result = recipe.get("result");

		if (result == null)
		{
			return Optional.empty();
		}
		else if (result.isJsonObject())
		{
			return Optional.of(result.getAsJsonObject().get("id").getAsString());
		}
		else
		{
			return Optional.of(result.getAsString());
		}
	}

	private static GeneratedFile tagOfTag(String vanillaTag, VariantIngredients ingredients)
	{
		List<String> values = new ArrayList<>();
		values.add("#" + vanillaTag);
		values.addAll(ingredients.variantsOfTag(vanillaTag));

		return new TagJson(values).toFile(TagKey.create(Registries.ITEM, Identifier.parse(TagIds.variantsOfTag(vanillaTag))));
	}
}
