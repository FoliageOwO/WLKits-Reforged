package ml.windleaf.wlkitsreforged.modules

import com.sun.net.httpserver.HttpServer
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.httpapi.handlers.macro.MacroListHandler
import ml.windleaf.wlkitsreforged.modules.httpapi.handlers.macro.MacroRunHandler
import ml.windleaf.wlkitsreforged.utils.Util
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import kotlin.properties.Delegates

class HttpApi : Module {
    private var enabled = false
    override fun getName() = "HttpApi"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    companion object {
        var port by Delegates.notNull<Int>()
        var pool by Delegates.notNull<Int>()
        lateinit var token: String
        lateinit var server: HttpServer
    }

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        port = Util.getPluginConfig("HttpApi", "port") as Int
        pool = Util.getPluginConfig("HttpApi", "pool") as Int
        if (enabled) {
            val configToken = Util.getPluginConfig("HttpApi", "token") as String
            token = if (configToken == "") Util.generateRandomToken() else configToken

            server = HttpServer.create(InetSocketAddress(port), 0)
            server.executor = Executors.newFixedThreadPool(pool)

            MacroRunHandler()
            MacroListHandler()

            server.start()
            WLKits.log("&aHTTP API Server started: &b${server.address}")
            WLKits.log("&aToken: $token")
        }
    }

    override fun unload() {
        if (enabled) {
            server.stop(0)
            WLKits.log("&aHTTP API Server stopped.")
        }
    }

    override fun registers() = Unit
}