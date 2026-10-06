package fr.minecraftpp.core.variant;

import java.util.Objects;

import fr.minecraftpp.core.set.ContentSlot;

/**
 * A vanilla item that a generated content replaces: wherever the vanilla item is accepted, the generated one is accepted too.
 *
 * @param vanillaItemId identifier of the vanilla item, such as {@code minecraft:iron_ingot}
 * @param slot          the generated content that becomes a variant of it
 */
public record VanillaVariant(String vanillaItemId, ContentSlot slot)
{
	public VanillaVariant
	{
		Objects.requireNonNull(vanillaItemId, "vanillaItemId");
		Objects.requireNonNull(slot, "slot");
	}
}
