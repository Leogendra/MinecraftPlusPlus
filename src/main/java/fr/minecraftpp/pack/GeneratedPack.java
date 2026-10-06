package fr.minecraftpp.pack;

import java.io.InputStream;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceMetadata;

/**
 * Serves the generated files to Minecraft as a pack of one type: data for the server, resources for the client.
 */
public final class GeneratedPack implements PackResources
{
	private final PackLocationInfo location;
	private final GeneratedPackContents contents;
	private final ResourceMetadata metadata;

	/**
	 * The pack declares the format of the running game, so it is never reported as made for another version.
	 */
	public GeneratedPack(PackLocationInfo location, PackType type, Component description, GeneratedPackContents contents)
	{
		this.location = location;
		this.contents = contents;
		this.metadata = ResourceMetadata.of(PackMetadataSection.forPackType(type), new PackMetadataSection(description, SharedConstants.getCurrentVersion().packVersion(type).minorRange()));
	}

	/**
	 * The pack has no root file: its metadata are built in memory and it has no icon.
	 */
	@Override
	public @Nullable IoSupplier<InputStream> getRootResource(String... path)
	{
		return null;
	}

	@Override
	public @Nullable IoSupplier<InputStream> getResource(PackType type, Identifier location)
	{
		return this.contents.resource(type, location).orElse(null);
	}

	@Override
	public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output)
	{
		this.contents.resources(type, namespace, directory).forEach(output);
	}

	@Override
	public Set<String> getNamespaces(PackType type)
	{
		return this.contents.namespaces(type);
	}

	@Override
	public <T> @Nullable T getMetadataSection(MetadataSectionType<T> metadataType)
	{
		return this.metadata.getSection(metadataType).orElse(null);
	}

	@Override
	public PackLocationInfo location()
	{
		return this.location;
	}

	/**
	 * Nothing to release: the files stay in memory for the next reloads.
	 */
	@Override
	public void close()
	{
	}
}
