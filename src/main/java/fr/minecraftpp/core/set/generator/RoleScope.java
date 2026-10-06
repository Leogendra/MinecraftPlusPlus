package fr.minecraftpp.core.set.generator;

/**
 * The vanilla roles handed out to the sets.
 */
public enum RoleScope
{
	/**
	 * The roles of 1.12 only: a seed gives exactly the 1.12 sets. The tests use it to compare the generator with the 1.12 outputs.
	 */
	MINECRAFT_1_12,

	/**
	 * The 1.12 roles, plus copper (decision D6), given to the only ore left without a vanilla role. The game uses it.
	 */
	WITH_COPPER
}
