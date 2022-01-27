package ml.windleaf.wlkitsreforged.modules.httpapi.handlers.player

import com.sun.net.httpserver.HttpExchange
import ml.windleaf.wlkitsreforged.modules.httpapi.Error.*
import ml.windleaf.wlkitsreforged.modules.httpapi.Handler
import ml.windleaf.wlkitsreforged.modules.HttpApi
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.text.SimpleDateFormat
import java.util.*

class PlayerInfoHandler : Handler() {
    init {
        HttpApi.server.createContext("/api/player/info", this)
    }

    override fun handle(exchange: HttpExchange) {
        this.exchange = exchange
        val params = getRequestParams()
        if (needToken(params) && rightParams(params, "playerName")) {
            val name = params?.get("playerName") as String
            val onlinePlayer = Bukkit.getPlayer(name)
            var offlinePlayer: OfflinePlayer? = null
            for (p in Bukkit.getOfflinePlayers()) if (p.name == name) offlinePlayer = p

            if (onlinePlayer == null && offlinePlayer == null) response(content = toJson(hashMapOf("error" to NO_SUCH_PLAYER, "player" to name)))
            if (onlinePlayer != null) {
                response(content = toJson(hashMapOf(
                    "error" to "",
                    "data" to hashMapOf(
                        "name" to onlinePlayer.name,
                        "displayName" to onlinePlayer.displayName,
                        "uuid" to Util.getUUID(onlinePlayer),
                        "isOnline" to onlinePlayer.isOnline,
                        "isWhitelisted" to onlinePlayer.isWhitelisted,
                        "isOp" to onlinePlayer.isOp,
                        "address" to onlinePlayer.address.toString(),
                        "exp" to onlinePlayer.exp,
                        "ping" to onlinePlayer.ping,
                        "locale" to onlinePlayer.locale,
                        "location" to hashMapOf(
                            "x" to onlinePlayer.location.x,
                            "y" to onlinePlayer.location.y,
                            "z" to onlinePlayer.location.z
                        ),
                        "health" to onlinePlayer.health,
                        "food" to onlinePlayer.foodLevel,
                        "firstPlayedStamp" to onlinePlayer.firstPlayed,
                        "firstPlayedDate" to getDate(onlinePlayer.firstPlayed),
                        "lastPlayedStamp" to onlinePlayer.lastPlayed,
                        "lastPlayedDate" to getDate(onlinePlayer.lastPlayed)
                    )
                )))
            }
            if (offlinePlayer != null) {
                response(content = toJson(hashMapOf(
                    "error" to "",
                    "data" to hashMapOf(
                        "name" to offlinePlayer.name,
                        "uuid" to offlinePlayer.uniqueId.toString(),
                        "isBanned" to offlinePlayer.isBanned,
                        "isOnline" to offlinePlayer.isOnline,
                        "isWhitelisted" to offlinePlayer.isWhitelisted,
                        "isOp" to offlinePlayer.isOp,
                        "firstOnlineStamp" to offlinePlayer.firstPlayed,
                        "firstOnlineDate" to getDate(offlinePlayer.firstPlayed),
                        "lastOnlineStamp" to offlinePlayer.lastPlayed,
                        "lastOnlineDate" to getDate(offlinePlayer.lastPlayed)
                    )
                )))
            }
        }
    }

    private fun getDate(long: Long) = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(long))
}