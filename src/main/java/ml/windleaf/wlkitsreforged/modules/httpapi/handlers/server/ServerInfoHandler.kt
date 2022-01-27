package ml.windleaf.wlkitsreforged.modules.httpapi.handlers.server

import com.sun.net.httpserver.HttpExchange
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.httpapi.Handler
import ml.windleaf.wlkitsreforged.modules.HttpApi

class ServerInfoHandler : Handler() {
    init {
        HttpApi.server.createContext("/api/server/info", this)
    }

    override fun handle(exchange: HttpExchange) {
        this.exchange = exchange
        val params = getRequestParams()
        if (needToken(params)) {
            val server = WLKits.instance.server
            var tps: Double? = null
            try {
                val nms = server.javaClass.getMethod("getServer").invoke(server).javaClass
                tps = (nms.getField("recentTps").get(nms.getMethod("getServer").invoke(null)) as DoubleArray)[0]
            } catch (ignored: Exception) { }

            val banned = arrayListOf<String>()
            val online = arrayListOf<String>()
            val offline = arrayListOf<String>()
            val ops = arrayListOf<String>()
            server.bannedPlayers.forEach { banned.add(it.name!!) }
            server.onlinePlayers.forEach { online.add(it.name) }
            server.offlinePlayers.forEach { offline.add(it.name!!) }
            server.operators.forEach { ops.add(it.name!!) }

            response(content = toJson(hashMapOf(
                "error" to "",
                "data" to hashMapOf(
                    "ip" to server.ip,
                    "port" to server.port,
                    "bannedPlayers" to banned,
                    "onlinePlayers" to online,
                    "offlinePlayers" to offline,
                    "bannedPlayerNum" to banned.size,
                    "onlinePlayerNum" to online.size,
                    "offlinePlayerNum" to offline.size,
                    "ops" to ops,
                    "opsNum" to ops.size,
                    "bukkitVersion" to server.bukkitVersion,
                    "maxPlayers" to server.maxPlayers,
                    "motd" to server.motd,
                    "name" to server.name,
                    "version" to server.version,
                    "tps" to tps
                )
            )))
        }
    }
}