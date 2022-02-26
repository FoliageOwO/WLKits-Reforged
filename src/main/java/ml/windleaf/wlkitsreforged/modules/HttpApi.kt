package ml.windleaf.wlkitsreforged.modules

import com.sun.net.httpserver.HttpServer
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.modules.httpapi.handlers.macro.MacroListHandler
import ml.windleaf.wlkitsreforged.modules.httpapi.handlers.macro.MacroRunHandler
import ml.windleaf.wlkitsreforged.utils.Util
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import kotlin.properties.Delegates

@ModuleInfo(description = "Opens HTTP API to manage server", type = LoadType.ON_STARTUP)
class HttpApi : Module {
    private var enabled = false
    override fun getEnabled() = enabled
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
        port = Util.getModuleConfig("HttpApi", "port") as Int
        pool = Util.getModuleConfig("HttpApi", "pool") as Int
        if (enabled) {
            val configToken = Util.getModuleConfig("HttpApi", "token") as String
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
        server.stop(0)
        WLKits.log("&aHTTP API Server stopped.")
    }
}