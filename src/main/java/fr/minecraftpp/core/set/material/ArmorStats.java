package fr.minecraftpp.core.set.material;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import fr.minecraftpp.core.set.Bounds;

/**
 * Statistics of the four armor pieces of a material.
 *
 * @param durabilityFactor multiplied by the base durability of each piece, as in vanilla
 * @param defense          armor points of each piece
 */
public record ArmorStats(int durabilityFactor, Map<ArmorPiece, Integer> defense, float toughness)
{
	public ArmorStats
	{
		Bounds.check("armor durability factor", durabilityFactor, 1, Integer.MAX_VALUE);
		Bounds.check("armor toughness", toughness, 0, Float.MAX_VALUE);

		EnumMap<ArmorPiece, Integer> copy = new EnumMap<>(ArmorPiece.class);
		copy.putAll(defense);

		if (copy.size() != ArmorPiece.values().length)
		{
			throw new IllegalArgumentException("Every armor piece needs a defense: " + copy.keySet());
		}

		defense = Collections.unmodifiableMap(copy);
	}

	public int defense(ArmorPiece piece)
	{
		return this.defense.get(piece);
	}

	public int durability(ArmorPiece piece)
	{
		return piece.getBaseDurability() * this.durabilityFactor;
	}
}
