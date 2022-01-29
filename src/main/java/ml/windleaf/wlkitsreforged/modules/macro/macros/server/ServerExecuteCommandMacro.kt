package ml.windleaf.wlkitsreforged.modules.macro.macros.server

import ml.windleaf.wlkitsreforged.apis.PlayerInType
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.macro.MacroInfo
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit

@MacroInfo(path = "server.execute_command",
    description = "Executes a command on the server, " +
            "this macro can not get any result from executing command," +
            "it just return a state of command executing." +
            "It will execute command by the console if arg `executor` is empty string or player not found, " +
            "else execute it by the player with the name or uuid in arg `executor`",
    args = ["command", "executor"])
class ServerExecuteCommandMacro : MacroEntire<Boolean> {
    private lateinit var command: String
    private var player: PlayerInType? = null
    private lateinit var executor: String

    override fun execute(): Boolean {
        var result = false
        Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
            result = WLKits.instance.server.dispatchCommand(
                when (player == null) {
                    true -> {
                        executor = "[CONSOLE]"
                        Bukkit.getConsoleSender()
                    }
                    false -> {
                        val p = player!!.getOnlinePlayer()
                        if (p == null) {
                            executor = "PLAYER_NOT_FOUND -> [CONSOLE]"
                            Bukkit.getConsoleSender()
                        } else {
                            executor = p.name
                            p
                        }
                    }
                },
                command)
        }
        return result
    }

    override fun parse(map: Map<String, Any>) {
        command = map["command"] as String
        val value = map["executor"] as String
        player = Util.getPlayerBy(value)
    }
}