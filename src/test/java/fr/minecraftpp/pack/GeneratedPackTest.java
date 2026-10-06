package fr.minecraftpp.pack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;

class GeneratedPackTest
{
	private static final PackLocationInfo LOCATION = new PackLocationInfo("test_pack", Component.literal("Test pack"), PackSource.BUILT_IN, Optional.empty());

	private static final GeneratedFile ITEM_TAG = new GeneratedFile(PackType.SERVER_DATA, Identifier.fromNamespaceAndPath("minecraftpp", "tags/item/xyzium_repair_materials.json"), "{\"values\":[\"minecraftpp:xyzium\"]}");
	private static final GeneratedFile NESTED_ITEM_TAG = new GeneratedFile(PackType.SERVER_DATA, Identifier.fromNamespaceAndPath("minecraftpp", "tags/item/tools/xyzium.json"), "{\"values\":[]}");
	private static final GeneratedFile SIBLING_DIRECTORY_TAG = new GeneratedFile(PackType.SERVER_DATA, Identifier.fromNamespaceAndPath("minecraftpp", "tags/item_sets/xyzium.json"), "{\"values\":[]}");
	private static final GeneratedFile VANILLA_RECIPE = new GeneratedFile(PackType.SERVER_DATA, Identifier.fromNamespaceAndPath("minecraft", "recipe/torch.json"), "{}");
	private static final GeneratedFile ITEM_MODEL = new GeneratedFile(PackType.CLIENT_RESOURCES, Identifier.fromNamespaceAndPath("minecraftpp", "items/xyzium.json"), "{}");

	private static GeneratedPack pack;

	/**
	 * The pack reads the format of the running game, which must be detected first.
	 */
	@BeforeAll
	static void createPack()
	{
		SharedConstants.tryDetectVersion();
		pack = new GeneratedPack(LOCATION, PackType.SERVER_DATA, Component.literal("Test"), GeneratedPackContents.of(List.of(ITEM_TAG, NESTED_ITEM_TAG, SIBLING_DIRECTORY_TAG, VANILLA_RECIPE, ITEM_MODEL)));
	}

	@Test
	void servesTheWrittenContent() throws IOException
	{
		assertEquals(ITEM_TAG.content(), read(pack.getResource(PackType.SERVER_DATA, ITEM_TAG.location())));
	}

	@Test
	void servesAFileOnlyWithItsPackType()
	{
		assertNull(pack.getResource(PackType.CLIENT_RESOURCES, ITEM_TAG.location()));
		assertNull(pack.getResource(PackType.SERVER_DATA, ITEM_MODEL.location()));
	}

	@Test
	void listsTheFilesBelowADirectoryWithItsSubdirectories()
	{
		List<Identifier> listed = new ArrayList<>();

		pack.listResources(PackType.SERVER_DATA, "minecraftpp", "tags/item", (location, opener) -> listed.add(location));

		assertEquals(List.of(NESTED_ITEM_TAG.location(), ITEM_TAG.location()), listed);
	}

	@Test
	void listsAWholeNamespaceForAnEmptyDirectory()
	{
		List<Identifier> listed = new ArrayList<>();

		pack.listResources(PackType.SERVER_DATA, "minecraft", "", (location, opener) -> listed.add(location));

		assertEquals(List.of(VANILLA_RECIPE.location()), listed);
	}

	@Test
	void declaresTheNamespacesOfEachPackType()
	{
		assertEquals(Set.of("minecraft", "minecraftpp"), pack.getNamespaces(PackType.SERVER_DATA));
		assertEquals(Set.of("minecraftpp"), pack.getNamespaces(PackType.CLIENT_RESOURCES));
	}

	@Test
	void declaresThePackFormatOfTheRunningGame()
	{
		PackMetadataSection metadata = pack.getMetadataSection(PackMetadataSection.SERVER_TYPE);

		assertNotNull(metadata);
		assertEquals(SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).minorRange(), metadata.supportedFormats());
	}

	@Test
	void refusesTwoFilesAtTheSameLocation()
	{
		GeneratedFile otherContent = new GeneratedFile(ITEM_TAG.type(), ITEM_TAG.location(), "{\"values\":[]}");

		assertThrows(IllegalArgumentException.class, () -> GeneratedPackContents.of(List.of(ITEM_TAG, otherContent)));
	}

	private static String read(IoSupplier<InputStream> opener) throws IOException
	{
		try (InputStream stream = opener.get())
		{
			return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
		}
	}
}
