package fr.minecraftpp.pack.asset;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.minecraftpp.content.material.MaterialFactory;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.material.MaterialDefinition;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Writes the equipment asset each armor material names, which draws the armor on its wearer, as 1.12 did: the generic texture of the material tinted with the color of the set, then, for the second texture, an overlay drawn as is.
 *
 * Only the adult layers exist: the textures of 1.12 have no baby variant.
 */
public final class EquipmentAssetWriter implements GeneratedResourceWriter
{
	private static final List<String> LAYER_TYPES = List.of("humanoid", "humanoid_leggings");
	private static final int TEXTURE_WITH_OVERLAY = 2;

	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().filter(set -> set.material().isPresent()).map(EquipmentAssetWriter::equipmentAsset).toList();
	}

	private static GeneratedFile equipmentAsset(OreSetDefinition set)
	{
		Map<String, List<EquipmentAssetJson.Layer>> layers = new LinkedHashMap<>();

		for (String layerType : LAYER_TYPES)
		{
			layers.put(layerType, layers(set, set.material().orElseThrow()));
		}

		return GeneratedFile.asset("equipment/" + MaterialFactory.equipmentAsset(set).identifier().getPath() + ".json", new EquipmentAssetJson(layers));
	}

	/**
	 * Each layer type uses the texture of the same name in its own directory, {@code textures/entity/equipment/<layer type>/}.
	 */
	private static List<EquipmentAssetJson.Layer> layers(OreSetDefinition set, MaterialDefinition material)
	{
		String texture = "generic_" + material.textureId();
		List<EquipmentAssetJson.Layer> layers = new ArrayList<>();
		layers.add(new EquipmentAssetJson.Layer(ContentIds.full(texture), new EquipmentAssetJson.Dyeable(set.item().color().asInt())));

		if (material.textureId() == TEXTURE_WITH_OVERLAY)
		{
			layers.add(new EquipmentAssetJson.Layer(ContentIds.full(texture + "_overlay"), null));
		}

		return layers;
	}
}
