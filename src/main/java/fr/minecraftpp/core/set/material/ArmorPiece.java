package fr.minecraftpp.core.set.material;

/**
 * The four armor pieces, in the 1.12 order of the armor statistics arrays.
 */
public enum ArmorPiece
{
	HELMET(11), CHESTPLATE(16), LEGGINGS(15), BOOTS(13);

	private final int baseDurability;

	ArmorPiece(int baseDurability)
	{
		this.baseDurability = baseDurability;
	}

	/**
	 * Durability of the piece per point of the material durability factor, as in vanilla.
	 */
	public int getBaseDurability()
	{
		return this.baseDurability;
	}
}
