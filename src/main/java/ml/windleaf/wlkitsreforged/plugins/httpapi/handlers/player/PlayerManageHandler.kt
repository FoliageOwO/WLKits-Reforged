package ml.windleaf.wlkitsreforged.plugins.httpapi.handlers.player

import com.sun.net.httpserver.HttpExchange
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.plugins.httpapi.Error.*
import ml.windleaf.wlkitsreforged.plugins.httpapi.Handler
import ml.windleaf.wlkitsreforged.plugins.httpapi.HttpApi
import ml.windleaf.wlkitsreforged.plugins.httpapi.handlers.player.Action.*
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class PlayerManageHandler : Handler() {
    init {
        HttpApi.server.createContext("/api/player/manage", this)
    }

    override fun handle(exchange: HttpExchange) {
        this.exchange = exchange
        val params = getRequestParams()
        if (needToken(params) && rightParams(params, "playerName", "action")) {
            val name = params?.get("playerName") as String
            val player = Bukkit.getPlayer(name)
            val action = getActionByString(params["action"] as String)
            try {
                when (action) {
                    KICK -> if (rightParams(params, "reason") && notNull(player, name)) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                            player?.kickPlayer(Util.translateColorCode(params["reason"] as String))
                        }
                        result(KICK, "kicked player $name")
                    }
                    KILL -> if (notNull(player, name)) {
                        player?.health = 0.0
                        result(KILL, "killed player $name")
                    }
                    BAN -> if (rightParams(params, "reason")) {
                        val reason = Util.translateColorCode(params["reason"] as String)
                        Bukkit.getBanList(BanList.Type.NAME).addBan(name, reason, null, null)
                        Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                            player?.kickPlayer("You are banned from this server.\nReason: $reason")
                        }
                        result(BAN, "banned player $name")
                    }
                    PARDON -> {
                        Bukkit.getBanList(BanList.Type.NAME).pardon(name)
                        result(PARDON, "pardoned player $name")
                    }
                    CLEAR -> if (notNull(player, name)) {
                        player?.inventory?.clear()
                        result(CLEAR, "cleared inventory of player $name")
                    }
                    TP -> if (rightParams(params, "world", "x", "y", "z") && notNull(player, name)) {
                        val world = params["world"] as String
                        val x = params["x"] as Double
                        val y = params["y"] as Double
                        val z = params["z"] as Double

                        Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                            player?.teleport(Location(Util.getWorldByName(world), x, y, z))
                        }
                        result(TP, "teleported player $name to location [world:$world,x:$x,y:$y,z:$z]")
                    }
                    MSG -> if (rightParams(params, "msg") && notNull(player, name)) {
                        player?.sendMessage(Util.translateColorCode(params["msg"] as String)!!)
                        result(MSG, "sent message to player $name success")
                    }
                    NULL -> result(NULL, "", UNKNOWN_ACTION)
                }
            } catch (e: Exception) {
                result(action, e.message!!, EXCEPTION_HAPPENED)
            }
        }
    }

    private fun result(
        action: Action, msg: String, error: Any = ""
    ) = response(content = toJson(hashMapOf("error" to error, "action" to action.string, "msg" to msg)))

    private fun notNull(p: Player?, name: String): Boolean {
        return if (p == null) {
            response(content = toJson(hashMapOf("error" to PLAYER_NOT_ONLINE, "player" to name)))
            false
        } else true
    }

    private fun getActionByString(string: String): Action {
        for (action in Action.values()) if (action.string == string.lowercase()) return action
        return NULL
    }
}