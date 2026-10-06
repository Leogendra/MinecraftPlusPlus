package fr.minecraftpp.pack.data;

import com.google.gson.annotations.SerializedName;

/**
 * A damage type file.
 *
 * @param exhaustion the hunger the damage costs
 * @param scaling    when the damage grows with the difficulty
 */
public record DamageTypeJson(@SerializedName("message_id") String messageId, float exhaustion, String scaling)
{
}
