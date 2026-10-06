package fr.minecraftpp.core.set;

/**
 * The three kinds of generated sets. A material set adds tools and armor to a simple set, a metal set adds nuggets to a material set.
 */
public enum SetType
{
	SIMPLE("Simple"), MATERIAL("Material"), METAL("Metal");

	private final String displayName;

	SetType(String displayName)
	{
		this.displayName = displayName;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public boolean hasMaterial()
	{
		return this != SIMPLE;
	}
}
