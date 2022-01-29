package ml.windleaf.wlkitsreforged.modules.enums

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import ml.windleaf.wlkitsreforged.core.enums.PlayerType
import ml.windleaf.wlkitsreforged.core.enums.PlayerType.*
import ml.windleaf.wlkitsreforged.modules.macro.macros.player.PlayerManageMacro
import org.bukkit.Bukkit
import org.bukkit.BanList
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.Location

/**
 * The player action enum
 *
 * @param string the action string for api
 * @param onlyFor the player type that can run this action
 * @see PlayerType
 * @see PlayerManageMacro
 */
enum class PlayerAction(val string: String, val run: (args: Array<Any>) -> Unit, vararg val onlyFor: PlayerType) {
    /**
     * [NULL] action, used when the action is not found
     *
     * args:
     *  - 0 -> [String] the action name
     */
    NULL("null",
        { args ->
            throw IllegalArgumentException("action `${args[0]}` not found")
        }, ONLINE, OFFLINE),

    /**
     * [KICK] action, used to kick a player
     *
     * args:
     *  - [OfflinePlayer] the player
     *  - [String] reason
     */
    KICK("kick",
        { args ->
            val p = args[0] as OfflinePlayer
            if (checkOnlyOnline(KICK, p)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                    (p as Player).kickPlayer(Util.translateColorCode(args[1] as String))
                }
            }
        },
        ONLINE),

    /**
     * [KILL] action, used to kill a player
     *
     * args:
     *  - [OfflinePlayer] the online player
     */
    KILL("kill",
        { args ->
            val p = args[0] as OfflinePlayer
            if (checkOnlyOnline(KILL, p)) {
                val x = args[0] as Player
                x.damage(x.health)
            }
        },
        ONLINE),

    /**
     * [BAN] action, used to ban a player
     *
     * args:
     *  - [OfflinePlayer] the player
     *  - [String] reason
     */
    BAN("ban",
        { args ->
            val p = args[0] as OfflinePlayer
            val reason = Util.translateColorCode(args[1] as String)
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.name!!, reason, null, null)
            if (p.isOnline) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                    (p as Player).kickPlayer("You are banned from this server.\nReason: $reason")
                }
            }
        },
        ONLINE, OFFLINE),

    /**
     * [PARDON] action, used to unban a player
     *
     * args:
     *  - [OfflinePlayer] the player
     */
    PARDON("pardon",
        { args ->
            Bukkit.getBanList(BanList.Type.NAME).pardon((args[0] as OfflinePlayer).name!!)
        },
        ONLINE, OFFLINE),

    /**
     * [CLEAR] action, used to clear the inventory of a player
     *
     * args:
     *  - [OfflinePlayer] the online player
     */
    CLEAR("clear",
        { args ->
            val p = args[0] as OfflinePlayer
            if (checkOnlyOnline(CLEAR, p)) {
                (p as Player).inventory.clear()
            }
        },
        ONLINE),

    /**
     * [GIVE] action, used to give a player an item
     *
     * args:
     *  - [OfflinePlayer] the online player
     *  - [ItemStack] the item
     */
    GIVE("give",
        { args ->
            val p = args[0] as OfflinePlayer
            if (checkOnlyOnline(GIVE, p)) {
                (p as Player).inventory.addItem(args[1] as ItemStack)
            }
        },
        ONLINE),

    /**
     * [TP_LOCATION] action, used to teleport a player to a location
     *
     * args:
     *  - [OfflinePlayer] the online player
     *  - [Location] the location
     */
    TP_LOCATION("tp_location",
        { args ->
            val p = args[0] as OfflinePlayer
            if (checkOnlyOnline(TP_LOCATION, p)) {
                (p as Player).teleport(args[1] as Location)
            }
        },
        ONLINE),

    /**
     * [TP_PLAYER] action, used to teleport a player to another player
     *
     * args:
     *  - [OfflinePlayer] the online player
     *  - [OfflinePlayer] the target online player
     */
    TP_PLAYER("tp_player",
        { args ->
            val p = args[0] as OfflinePlayer
            val t = args[1] as OfflinePlayer
            if (checkOnlyOnline(TP_PLAYER, p, t)) {
                (p as Player).teleport(t as Player)
            }
        },
        ONLINE),

    /**
     * [MSG] action, used to send a message to a player
     *
     * args:
     *  - [OfflinePlayer] the online player
     *  - [String] the message
     */
    MSG("msg",
        { args ->
            val p = args[0] as OfflinePlayer
            if (checkOnlyOnline(MSG, p)) {
                (p as Player).sendMessage(Util.translateColorCode(args[1] as String)!!)
            }
        },
        ONLINE);

    companion object {
        fun checkOnlyOnline(
            instance: PlayerAction, vararg targets: OfflinePlayer)
        = if (!instance.onlyFor.contains(OFFLINE) && targets.any { !it.isOnline }) {
            throw IllegalArgumentException("action `${instance.string}` can only be run when player is online player")
        } else true
    }
}