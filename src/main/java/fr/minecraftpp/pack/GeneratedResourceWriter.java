package fr.minecraftpp.pack;

import java.util.List;

import fr.minecraftpp.core.set.OreCatalog;

/**
 * Writes one kind of generated resource (tags, recipes, models...) for the sets of the game. A new kind of resource is a new writer, without changes to the others.
 */
public interface GeneratedResourceWriter
{
	List<GeneratedFile> write(OreCatalog catalog);
}
