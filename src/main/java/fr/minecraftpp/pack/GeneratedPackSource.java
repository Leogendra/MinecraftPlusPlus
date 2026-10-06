package fr.minecraftpp.pack;

import java.util.Optional;
import java.util.function.Consumer;

import fr.minecraftpp.core.set.ContentIds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

/**
 * Offers the generated pack of one type to a pack repository. The pack is required and placed on top: it is always active, and its files override the vanilla ones.
 */
public final class GeneratedPackSource implements RepositorySource
{
	public static final String PACK_ID = ContentIds.NAMESPACE + "_generated";

	private static final PackLocationInfo LOCATION = new PackLocationInfo(PACK_ID, Component.translatable("pack.minecraftpp.generated.title"), PackSource.BUILT_IN, Optional.empty());
	private static final Component DESCRIPTION = Component.translatable("pack.minecraftpp.generated.description");
	private static final PackSelectionConfig SELECTION = new PackSelectionConfig(true, Pack.Position.TOP, false);

	private final PackType type;
	private final GeneratedPackContents contents;

	public GeneratedPackSource(PackType type, GeneratedPackContents contents)
	{
		this.type = type;
		this.contents = contents;
	}

	@Override
	public void loadPacks(Consumer<Pack> result)
	{
		Pack pack = Pack.readMetaAndCreate(LOCATION, new FixedResources(new GeneratedPack(LOCATION, this.type, DESCRIPTION, this.contents)), this.type, SELECTION);

		if (pack == null)
		{
			throw new IllegalStateException("Minecraft cannot read the metadata of the generated pack " + PACK_ID);
		}
		else
		{
			result.accept(pack);
		}
	}

	/**
	 * The same in-memory pack answers the metadata reading and the full opening.
	 */
	private record FixedResources(PackResources resources) implements Pack.ResourcesSupplier
	{
		@Override
		public PackResources openPrimary(PackLocationInfo location)
		{
			return this.resources;
		}

		@Override
		public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata)
		{
			return this.resources;
		}
	}
}
