package fr.minecraftpp.pack;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.minecraftpp.core.set.OreCatalog;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;

/**
 * The files of the generated pack, kept in memory. They are written once, when the mod is initialized, then served to every data and resource reload.
 */
public final class GeneratedPackContents
{
	private final Map<PackType, SortedMap<Identifier, byte[]>> files;

	private GeneratedPackContents(Map<PackType, SortedMap<Identifier, byte[]>> files)
	{
		this.files = files;
	}

	public static GeneratedPackContents write(OreCatalog catalog, List<GeneratedResourceWriter> writers)
	{
		return of(writers.stream().flatMap(writer -> writer.write(catalog).stream()).toList());
	}

	/**
	 * Fails when two files share a location: one would silently hide the other.
	 */
	public static GeneratedPackContents of(List<GeneratedFile> generatedFiles)
	{
		Map<PackType, SortedMap<Identifier, byte[]>> files = new EnumMap<>(PackType.class);

		for (PackType type : PackType.values())
		{
			files.put(type, new TreeMap<>());
		}

		for (GeneratedFile file : generatedFiles)
		{
			byte[] previousContent = files.get(file.type()).putIfAbsent(file.location(), file.content().getBytes(StandardCharsets.UTF_8));

			if (previousContent != null)
			{
				throw new IllegalArgumentException("Two generated files share the location " + file.type().getDirectory() + "/" + file.location().getNamespace() + "/" + file.location().getPath());
			}
		}

		return new GeneratedPackContents(files);
	}

	public Optional<IoSupplier<InputStream>> resource(PackType type, Identifier location)
	{
		return Optional.ofNullable(this.files.get(type).get(location)).map(GeneratedPackContents::opener);
	}

	/**
	 * The resources of a namespace below a directory, subdirectories included, as the vanilla packs list them. An empty directory lists the whole namespace.
	 */
	public Map<Identifier, IoSupplier<InputStream>> resources(PackType type, String namespace, String directory)
	{
		Map<Identifier, IoSupplier<InputStream>> resources = new LinkedHashMap<>();

		this.files.get(type).forEach((location, content) ->
		{
			if (location.getNamespace().equals(namespace) && isBelow(location.getPath(), directory))
			{
				resources.put(location, opener(content));
			}
		});

		return resources;
	}

	public Set<String> namespaces(PackType type)
	{
		return this.files.get(type).keySet().stream().map(Identifier::getNamespace).collect(Collectors.toUnmodifiableSet());
	}

	private static boolean isBelow(String path, String directory)
	{
		if (directory.isEmpty())
		{
			return true;
		}
		else
		{
			return path.startsWith(directory + "/");
		}
	}

	/**
	 * Each call opens a new stream over the content, which is never handed out itself.
	 */
	private static IoSupplier<InputStream> opener(byte[] content)
	{
		return () -> new ByteArrayInputStream(content);
	}
}
