package fr.minecraftpp.core.set;

import java.util.List;

/**
 * The seven sets generated from a Minecraft++ seed.
 */
public record OreCatalog(long seed, List<OreSetDefinition> sets)
{
	public OreCatalog
	{
		sets = List.copyOf(sets);
	}

	/**
	 * The sets that have a role, in catalog order. Several sets can share the beacon and fuel roles.
	 */
	public List<OreSetDefinition> setsWithRole(VanillaRole role)
	{
		return this.sets.stream().filter(set -> set.hasRole(role)).toList();
	}
}
