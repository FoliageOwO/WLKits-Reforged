package ml.windleaf.wlkitsreforged.others

import ml.windleaf.wlkitsreforged.core.enums.PlayerType
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.permissions.ServerOperator

/**
 * The PlayerInType class is for parse the player easily
 */
class PlayerInType(val type: TypeEnum, val player: ServerOperator, val playerType: PlayerType, val value: String = "") {
    /**
     * Gets the online player
     *
     * @return the online player, null if the player is offline player
     */
    fun getOnlinePlayer(): Player? {
        if (playerType == PlayerType.OFFLINE) return null
        return player as Player
    }

    /**
     * Gets the offline player
     *
     * @return the offline player, null if the player is online player
     */
    fun getOfflinePlayer(): OfflinePlayer? {
        if (playerType == PlayerType.ONLINE) return null
        return player as OfflinePlayer
    }

    /**
     * Gets the player, without judge online or offline
     *
     * @return the player, an online player or an offline player
     */
    fun getPlayer(): OfflinePlayer? = this.getOnlinePlayer() ?: this.getOfflinePlayer()

    /**
     * All value types
     */
    enum class TypeEnum {
        UUID, NAME;
    }
}