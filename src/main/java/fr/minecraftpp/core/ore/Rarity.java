package fr.minecraftpp.core.ore;

/**
 * The six rarity levels of the 1.12 version: the four vanilla ones plus FAMILIAR and LEGENDARY.
 *
 * The color is the RGB value of the 1.12 text formatting code of each level, so that the item name keeps its 1.12 color (decision D4).
 */
public enum Rarity
{
	COMMON(0xFFFFFF, "Common"), FAMILIAR(0x55FF55, "Familiar"), UNCOMMON(0xFFFF55, "Uncommon"), RARE(0x55FFFF, "Rare"), EPIC(0xFF55FF, "Epic"), LEGENDARY(0xAA0000, "Legendary");

	private final int nameColor;
	private final String displayName;

	Rarity(int nameColor, String displayName)
	{
		this.nameColor = nameColor;
		this.displayName = displayName;
	}

	public int getNameColor()
	{
		return this.nameColor;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public Rarity next()
	{
		if (this == LEGENDARY)
		{
			return LEGENDARY;
		}
		else
		{
			return Rarity.values()[this.ordinal() + 1];
		}
	}
}
