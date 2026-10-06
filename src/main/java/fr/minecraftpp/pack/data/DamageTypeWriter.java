package fr.minecraftpp.pack.data;

import java.util.List;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import net.minecraft.resources.Identifier;

/**
 * Writes a damage type for each storage block that hurts the entities walking on it (decision D8), so that its death message names the block, as in 1.12. It costs hunger and scales with the difficulty like the vanilla hot floor of the magma block.
 */
public final class DamageTypeWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().filter(set -> set.block().walkDamage() > 0).map(DamageTypeWriter::damageType).toList();
	}

	/**
	 * The message identifier of the damage type: its death messages are {@code death.attack.<message id>} and {@code death.attack.<message id>.player}.
	 */
	public static String messageId(OreSetDefinition set)
	{
		return ContentIds.NAMESPACE + "." + ContentIds.walkDamage(set);
	}

	public static Identifier damageTypeId(OreSetDefinition set)
	{
		return Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, ContentIds.walkDamage(set));
	}

	private static GeneratedFile damageType(OreSetDefinition set)
	{
		return GeneratedFile.data(damageTypeId(set).withPath(path -> "damage_type/" + path + ".json"), new DamageTypeJson(messageId(set), 0.1F, "when_caused_by_living_non_player"));
	}
}
