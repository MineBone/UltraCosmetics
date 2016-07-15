package be.isach.ultracosmetics.api;

import org.bukkit.OfflinePlayer;

/**
 * The Interface EconomyWrapper.
 */
public interface EconomyWrapper {

	/**
	 * Withdraw money from the player.
	 *
	 * @param type the type
	 * @param paramOfflinePlayer the param offline player
	 * @param paramDouble the param double
	 * @return true, if successful
	 */
	public boolean withdrawPlayer(EconomyType type, OfflinePlayer paramOfflinePlayer, double paramDouble);
	
	/**
	 * Deposit to the player.
	 *
	 * @param type the type
	 * @param paramOfflinePlayer the param offline player
	 * @param paramDouble the param double
	 * @return true, if successful
	 */
	public boolean depositPlayer(EconomyType type, OfflinePlayer paramOfflinePlayer, double paramDouble);
	
	/**
	 * Gets the balance of the player.
	 *
	 * @param type the type
	 * @param paramOfflinePlayer the param offline player
	 * @return the balance
	 */
	public double getBalance(EconomyType type, OfflinePlayer paramOfflinePlayer);
	
}
