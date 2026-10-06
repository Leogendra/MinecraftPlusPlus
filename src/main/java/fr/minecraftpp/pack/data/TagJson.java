package fr.minecraftpp.pack.data;

import java.util.List;

import fr.minecraftpp.pack.GeneratedFile;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

/**
 * A tag file: the identifiers of its entries, a tag entry starting with {@code #}. A vanilla tag written here is merged with the vanilla entries, not replaced.
 */
public record TagJson(List<String> values)
{
	/**
	 * The file of the tag in the data pack, such as {@code data/minecraftpp/tags/item/xyzium_repair_materials.json} for an item tag.
	 */
	public GeneratedFile toFile(TagKey<?> key)
	{
		Identifier location = key.location().withPath(path -> Registries.tagsDirPath(key.registry()) + "/" + path + ".json");

		return GeneratedFile.data(location, this);
	}
}
