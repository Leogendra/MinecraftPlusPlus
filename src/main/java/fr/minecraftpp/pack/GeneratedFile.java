package fr.minecraftpp.pack;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

/**
 * A text file of the generated pack, such as {@code data/minecraftpp/tags/item/xyzium_repair_materials.json}: the pack type gives the {@code data} or {@code assets} root, the location the namespace and the path below it.
 */
public record GeneratedFile(PackType type, Identifier location, String content)
{
}
