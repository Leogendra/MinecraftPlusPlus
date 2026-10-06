package fr.minecraftpp.core.set;

/**
 * Naming rule of the generated tags, without namespace. The tags themselves are written in the generated pack.
 */
public final class TagIds
{
	private TagIds()
	{
	}

	/**
	 * The items that repair the tools and armor of a set: its main item.
	 */
	public static String repairMaterials(OreSetDefinition set)
	{
		return set.name() + "_repair_materials";
	}
}
