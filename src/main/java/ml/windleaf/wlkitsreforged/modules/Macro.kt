package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.internal.JsonData
import ml.windleaf.wlkitsreforged.modules.macro.MacroException
import ml.windleaf.wlkitsreforged.modules.macro.MacroManager
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.stream.Collectors
import kotlin.properties.Delegates

class Macro : Module, CommandExecutor {
    private var enabled = false
    override fun getName() = "Macro"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    companion object {
        var enabled by Delegates.notNull<Boolean>()
        val manager = MacroManager()
        val authorized = JsonData("macro_authorized")
        lateinit var authedList: ArrayList<String>
    }

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        authedList = authorized.getAs("authed")!!
        Companion.enabled = enabled
    }

    override fun unload() = Unit
    override fun registers() {
        Util.registerCommand("macro", this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (enabled && Util.needPermission(sender, "macro", PermissionType.COMMAND)) {
            if (args.isEmpty()) Util.invalidArgs(sender) else {
                when (args[0]) {
                    "help" -> Util.sendHelp(sender,
                        "/macro help" to "查看此帮助",
                        "/macro list" to "查看所有可用的宏",
                        "/macro run [macro]" to "执行宏",
                        "/macro authed [player]" to "查看玩家是否已经被授权")
                    "list" -> Util.send(sender,
                        "[${manager.getMacros().stream().map { "&a${it.getMacroInfo()?.path}&r" }.collect(Collectors.joining(", "))}]")
                    "run" -> {
                        if (args.size < 2) Util.invalidArgs(sender)
                        else {
                            Util.catch(MacroException::class.java, {
                                WLKits.debug("Macro args: ${args.joinToString(" ")}")
                                val macro = manager.parseMacroCommand(args)
                                WLKits.debug("Successfully got macro: $macro")
                                val result = manager.executeMacro(macro, sender)
                                WLKits.debug("Macro run result: $result")
                                val msg =
                                    if (result == Unit.toString()) Util.insert(
                                        Util.getPluginMsg(getName(), "success"),
                                        "result" to Util.getPluginMsg(getName(), "no-result")
                                    ) else Util.insert(
                                        Util.getPluginMsg(getName(), "success"),
                                        "result" to result
                                    )
                                WLKits.debug("Macro run msg: $msg")
                                Util.send(sender, msg)
                            }, {
                                val errorMsg = "${it.javaClass.simpleName}: ${it.message}"
                                Util.send(sender, Util.insert(Util.getPluginMsg(getName(), "error"), "errorMsg" to errorMsg))
                            })
                        }
                    }
                    "authed" -> {
                        if (args.size != 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]
                            val player = Bukkit.getPlayer(n)
                            if (player == null) Util.send(sender, Util.insert(Util.getPluginMsg("main", "player-not-found"), "playerName" to n))
                            else Util.send(sender, Util.insert(Util.getPluginMsg(getName(), "authed"), "playerName" to n, "authed" to Util.parseBooleanColor(manager.isAuthorized(player))))
                        }
                    }
                    else -> Util.invalidArgs(sender)
                }
            }
        }
        return true
    }
}