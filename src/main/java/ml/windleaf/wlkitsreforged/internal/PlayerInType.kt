package ml.windleaf.wlkitsreforged.internal

import ml.windleaf.wlkitsreforged.core.enums.PlayerType
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.permissions.ServerOperator

/**
 * The PlayerInType class is for parse the player easily
 */
class PlayerInType(val type: TypeEnum, val player: ServerOperator, val playerType: PlayerType, val value: String = "") {
    companion object {
        /**
         * Gets the PlayerInType instance by uuid or name value
         *
         * @param value the uuid or name value
         * @return the PlayerInType instance, null if the player not found
         * @see TypeEnum
         * @see Util.getPlayerBy
         */
        @Deprecated("Common way to get",
            ReplaceWith("Util.getPlayerBy(value)", imports = ["ml.windleaf.wlkitsreforged.utils.Util"]))
        fun getByValue(value: String): PlayerInType? {
            return null
        }
    }

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
     * All value types
     */
    enum class TypeEnum {
        UUID, NAME;
    }
}