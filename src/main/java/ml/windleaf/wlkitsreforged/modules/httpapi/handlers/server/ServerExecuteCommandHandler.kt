package ml.windleaf.wlkitsreforged.modules.httpapi.handlers.server

import com.sun.net.httpserver.HttpExchange
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.httpapi.Error.*
import ml.windleaf.wlkitsreforged.modules.httpapi.Handler
import ml.windleaf.wlkitsreforged.modules.HttpApi
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class ServerExecuteCommandHandler : Handler() {
    init {
        HttpApi.server.createContext("/api/server/execute", this)
    }

    override fun handle(exchange: HttpExchange) {
        this.exchange = exchange
        val server = WLKits.instance.server
        val params = getRequestParams()
        if (needToken(params) && rightParams(params, "command", "executor")) {
            val command = params?.get("command")!! as String
            val executor: Any = params["executor"] as String
            val ex: CommandSender? = if (executor == "") Bukkit.getConsoleSender() else Bukkit.getPlayer(executor as String)
            if (ex == null) response(content = toJson(hashMapOf("error" to NO_SUCH_PLAYER, "player" to executor)))
            else {
                try {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                        val result = server.dispatchCommand(ex, command)
                        response(content = toJson(hashMapOf(
                            "error" to "",
                            "command" to command,
                            "executor" to ex.name,
                            "success" to result
                        )))
                    }
                } catch (e: Exception) {
                    response(content = toJson(hashMapOf(
                        "error" to COMMAND_EXCEPTION,
                        "command" to command,
                        "executor" to ex.name,
                        "success" to false,
                        "exception" to e.message
                    )))
                }
            }
        }
    }
}