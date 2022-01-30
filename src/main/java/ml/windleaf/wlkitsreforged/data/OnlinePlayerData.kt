package ml.windleaf.wlkitsreforged.data

import ml.windleaf.wlkitsreforged.internal.file.JsonData
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.GameMode
import org.bukkit.entity.Player

/**
 * The online player internal, only for online players
 */
@Suppress("UNUSED")
data class OnlinePlayerData(val p: Player,
                            override val T: Class<*> = Player::class.java) : Data {
    /**
     * The name of player
     */
    @DataField
    val name = p.name

    /**
     * The display name of player
     */
    @DataField
    val displayName = p.displayName

    /**
     * The uuid string of player
     */
    @DataField
    val uuid = Util.getUUID(p)

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
     * The connection ip string of player
     */
    @DataField
    val address = p.address.toString()

    /**
     * The experience level of player
     */
    @DataField
    val exp = p.exp

    /**
     * The delay of player
     */
    @DataField
    val ping = p.ping

    /**
     * The locale of player
     */
    @DataField
    val locale = p.locale

    /**
     * The location internal of player
     *
     * @see LocationData
     */
    @DataField
    val location = LocationData(p.location)

    /**
     * The current health of player
     */
    @DataField
    val health = p.health

    /**
     * The current food level of player
     */
    @DataField
    val food = p.foodLevel

    /**
     * The gamemode name of player
     *
     * @see GameMode
     */
    @DataField
    val gamemode = p.gameMode.name

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