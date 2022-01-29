package ml.windleaf.wlkitsreforged.data

import ml.windleaf.wlkitsreforged.internal.JsonData
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.OfflinePlayer

/**
 * The offline player internal, only for the offline players
 */
@Suppress("UNUSED")
data class OfflinePlayerData(val p: OfflinePlayer,
                             override val T: Class<*> = OfflinePlayer::class.java) : Data {
    /**
     * The name of player
     */
    @DataField
    val name = p.name

    /**
     * The uuid string of player
     */
    @DataField
    val uuid = p.uniqueId.toString()

    /**
     * Boolean of whether the player is online
     */
    @DataField
    val isOnline = p.isOnline

    /**
     * Boolean of whether the player has been banned
     */
    @DataField
    val isBanned = p.isBanned

    /**
     * Boolean of whether the player has been whitelisted
     */
    @DataField
    val isWhitelisted = p.isWhitelisted

    /**
     * Boolean of whether the player is op
     */
    @DataField
    val isOp = p.isOp

    /**
     * The first played date string of player, format with `yyyy-MM-dd HH:mm:ss`
     *
     * @see Util.getDate
     */
    @DataField
    val firstPlayedDate = Util.getDate(p.firstPlayed)

    /**
     * The last played date string of player, format with `yyyy-MM-dd HH:mm:ss`
     *
     * @see Util.getDate
     */
    @DataField
    val lastPlayedDate = Util.getDate(p.lastPlayed)

    override fun toString() = JsonData.toJson(this)
}
