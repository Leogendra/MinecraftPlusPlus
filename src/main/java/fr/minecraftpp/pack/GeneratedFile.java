package fr.minecraftpp.pack;

import fr.minecraftpp.core.set.ContentIds;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

/**
 * A text file of the generated pack, such as {@code data/minecraftpp/tags/item/xyzium_repair_materials.json}: the pack type gives the {@code data} or {@code assets} root, the location the namespace and the path below it.
 */
public record GeneratedFile(PackType type, Identifier location, String content)
{
	/**
	 * A JSON file of the client resources, in the mod namespace.
	 *
	 * @param path the path below {@code assets/minecraftpp/}, such as {@code items/xyzium.json}
	 */
	public static GeneratedFile asset(String path, Object json)
	{
		return new GeneratedFile(PackType.CLIENT_RESOURCES, Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, path), PackJson.toJson(json));
	}

	/**
	 * A JSON file of the server data, in any namespace: the vanilla tags are extended from the {@code minecraft} namespace.
	 *
	 * @param location the namespace and the path below {@code data/<namespace>/}, such as {@code minecraftpp:recipe/xyzium_block.json}
	 */
	public static GeneratedFile data(Identifier location, Object json)
	{
		return new GeneratedFile(PackType.SERVER_DATA, location, PackJson.toJson(json));
	}
}
