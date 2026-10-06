package fr.minecraftpp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * The core package holds the business logic and must stay independent from Minecraft and Fabric (decision D2: checked by reading the imports instead of ArchUnit).
 */
class CoreArchitectureTest
{
	private static final Path CORE_SOURCES = Path.of("src", "main", "java", "fr", "minecraftpp", "core");
	private static final List<String> FORBIDDEN_PACKAGES = List.of("net.minecraft", "net.fabricmc", "com.mojang");

	@Test
	void coreDoesNotDependOnMinecraftOrFabric() throws IOException
	{
		assertTrue(Files.isDirectory(CORE_SOURCES), "core sources not found from " + Path.of("").toAbsolutePath());

		try (Stream<Path> sources = Files.walk(CORE_SOURCES))
		{
			List<String> violations = sources.filter(path -> path.toString().endsWith(".java")).flatMap(CoreArchitectureTest::forbiddenImports).toList();

			assertEquals(List.of(), violations);
		}
	}

	private static Stream<String> forbiddenImports(Path source)
	{
		try
		{
			return Files.readAllLines(source).stream().map(String::trim).filter(line -> line.startsWith("import ")).filter(CoreArchitectureTest::isForbidden).map(line -> source.getFileName() + ": " + line);
		}
		catch (IOException exception)
		{
			throw new IllegalStateException("Cannot read " + source, exception);
		}
	}

	private static boolean isForbidden(String importLine)
	{
		String imported = importLine.replace("import static ", "").replace("import ", "");
		return FORBIDDEN_PACKAGES.stream().anyMatch(imported::startsWith);
	}
}
