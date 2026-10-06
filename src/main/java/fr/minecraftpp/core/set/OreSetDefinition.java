package fr.minecraftpp.core.set;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import fr.minecraftpp.core.ore.Rarity;

/**
 * Everything the seed decided about one of the seven generated sets.
 *
 * @param index position of the set in the catalog, from 0 to 6
 * @param name  generated name, lowercase letters only, used to build the identifiers and display names
 */
public record OreSetDefinition(int index, String name, SetType type, Rarity rarity, Set<VanillaRole> roles, OreGeneration generation, ItemTraits item, StorageBlockTraits block, OreTraits ore, Optional<MaterialDefinition> material)
{
	public OreSetDefinition
	{
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(rarity, "rarity");
		Objects.requireNonNull(generation, "generation");
		Objects.requireNonNull(item, "item");
		Objects.requireNonNull(block, "block");
		Objects.requireNonNull(ore, "ore");
		Objects.requireNonNull(material, "material");

		if (name == null || !name.matches("[a-z]+"))
		{
			throw new IllegalArgumentException("set name must be lowercase letters: " + name);
		}

		if (type.hasMaterial() != material.isPresent())
		{
			throw new IllegalArgumentException(type + " set " + name + (material.isPresent() ? " must not" : " must") + " have a material");
		}

		roles = Collections.unmodifiableSet(roles.isEmpty() ? EnumSet.noneOf(VanillaRole.class) : EnumSet.copyOf(roles));
	}

	public boolean hasRole(VanillaRole role)
	{
		return this.roles.contains(role);
	}
}
