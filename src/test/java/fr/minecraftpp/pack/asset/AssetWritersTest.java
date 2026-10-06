package fr.minecraftpp.pack.asset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.minecraftpp.core.GoldenFixture;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.pack.GeneratedPackContents;
import fr.minecraftpp.pack.GeneratedPackFixture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

/**
 * Seed 42: kal is a material set with block texture 2, ore texture 4 and color {R: 200, G: 11, B: 242}.
 */
class AssetWritersTest
{
	private static final GeneratedPackContents SEED_42 = GeneratedPackFixture.write(42);

	@Test
	void filesMatchTheReferences()
	{
		for (String path : List.of("blockstates/kal_block.json", "models/block/deepslate_kal_ore.json", "models/item/kal_pickaxe.json", "items/kal_pickaxe.json", "items/kal_ore.json"))
		{
			Identifier location = Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, path);

			assertEquals(GeneratedPackFixture.reference(PackType.CLIENT_RESOURCES, location), GeneratedPackFixture.file(SEED_42, PackType.CLIENT_RESOURCES, location), path);
		}
	}

	/**
	 * 7 sets of 3 blocks, 7 main items, the nugget of the only metal set, and 9 tools and armor pieces for each of the 3 equipped sets.
	 */
	@Test
	void everyItemHasADefinition()
	{
		long definitions = GeneratedPackFixture.files(SEED_42, PackType.CLIENT_RESOURCES).keySet().stream().filter(location -> location.getPath().startsWith("items/")).count();

		assertEquals(21 + 7 + 1 + 27, definitions);
	}

	@Test
	void everyReferencedModelAndTextureExists()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			Map<Identifier, JsonElement> files = GeneratedPackFixture.files(GeneratedPackFixture.write(seed), PackType.CLIENT_RESOURCES);

			files.forEach((location, json) ->
			{
				for (String model : referencedModels(location, json))
				{
					assertTrue(modelExists(model, files), location + " points to the missing model " + model);
				}

				for (String texture : referencedTextures(location, json))
				{
					assertTrue(staticResourceExists(texture, "textures", ".png"), location + " points to the missing texture " + texture);
				}
			});
		}
	}

	private static List<String> referencedModels(Identifier location, JsonElement json)
	{
		JsonObject object = json.getAsJsonObject();

		if (location.getPath().startsWith("blockstates/"))
		{
			return object.getAsJsonObject("variants").entrySet().stream().map(variant -> variant.getValue().getAsJsonObject().get("model").getAsString()).toList();
		}
		else if (location.getPath().startsWith("items/"))
		{
			return List.of(object.getAsJsonObject("model").get("model").getAsString());
		}
		else if (location.getPath().startsWith("models/"))
		{
			return List.of(object.get("parent").getAsString());
		}
		else
		{
			return List.of();
		}
	}

	private static List<String> referencedTextures(Identifier location, JsonElement json)
	{
		if (location.getPath().startsWith("models/"))
		{
			return json.getAsJsonObject().getAsJsonObject("textures").entrySet().stream().map(texture -> texture.getValue().getAsString()).filter(texture -> texture.startsWith(ContentIds.NAMESPACE + ":")).toList();
		}
		else
		{
			return List.of();
		}
	}

	/**
	 * A model of the mod is generated or shipped in the jar; the vanilla models are trusted.
	 */
	private static boolean modelExists(String model, Map<Identifier, JsonElement> generatedFiles)
	{
		Identifier identifier = Identifier.parse(model);

		if (!identifier.getNamespace().equals(ContentIds.NAMESPACE))
		{
			return true;
		}
		else
		{
			return generatedFiles.containsKey(identifier.withPath(path -> "models/" + path + ".json")) || staticResourceExists(model, "models", ".json");
		}
	}

	private static boolean staticResourceExists(String resource, String directory, String extension)
	{
		Identifier identifier = Identifier.parse(resource);

		return AssetWritersTest.class.getResource("/assets/" + identifier.getNamespace() + "/" + directory + "/" + identifier.getPath() + extension) != null;
	}
}
