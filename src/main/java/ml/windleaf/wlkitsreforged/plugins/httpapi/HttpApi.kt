package ml.windleaf.wlkitsreforged.plugins.httpapi

import com.sun.net.httpserver.HttpServer
import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.plugins.httpapi.handlers.player.PlayerInfoHandler
import ml.windleaf.wlkitsreforged.plugins.httpapi.handlers.player.PlayerManageHandler
import ml.windleaf.wlkitsreforged.plugins.httpapi.handlers.server.ServerExecuteCommandHandler
import ml.windleaf.wlkitsreforged.plugins.httpapi.handlers.server.ServerInfoHandler
import ml.windleaf.wlkitsreforged.utils.Util
import java.net.InetSocketAddress
import java.util.concurrent.Executors

class HttpApi : Plugin {
    override val name = "HttpApi"
    override val enabled = Util.isEnabled(name)
    override val type = LoadType.ON_STARTUP
    companion object {
        val port = Util.getPluginConfig("HttpApi", "port") as Int
        val pool = Util.getPluginConfig("HttpApi", "pool") as Int
        lateinit var token: String
        lateinit var server: HttpServer
    }

    override fun load() {
        if (enabled) {
            val configToken = Util.getPluginConfig("HttpApi", "token") as String
            token = if (configToken == "") Util.generateRandomToken() else configToken

            server = HttpServer.create(InetSocketAddress(port), 0)
            server.executor = Executors.newFixedThreadPool(pool)

            PlayerInfoHandler()
            PlayerManageHandler()
            ServerInfoHandler()
            ServerExecuteCommandHandler()

            server.start()
            WLKits.log("&aHTTP API Server: &b${server.address}")
            WLKits.log("&aToken: $token")
        }
    }

    override fun unload() {
        if (enabled) server.stop(0)
    }
}