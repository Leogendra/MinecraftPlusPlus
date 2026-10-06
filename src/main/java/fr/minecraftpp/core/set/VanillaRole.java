package fr.minecraftpp.core.set;

/**
 * The vanilla functions that the constraint solver hands out to the generated sets.
 *
 * The declaration order is the order in which the 1.12 version applied the roles and listed them in /mppinfo.
 */
public enum VanillaRole
{
	BLUE_DYE("bluedye"), REDSTONE("redstone"), CURRENCY("currency"), FUEL("fuel"), BEACON("beacon"), ENCHANTING_CURRENCY("enchant"), COAL("coal"), IRON("iron"), GOLD("gold"), DIAMOND("diamond");

	private final String infoName;

	VanillaRole(String infoName)
	{
		this.infoName = infoName;
	}

	/**
	 * The name shown by /mppinfo.
	 */
	public String getInfoName()
	{
		return this.infoName;
	}
}
