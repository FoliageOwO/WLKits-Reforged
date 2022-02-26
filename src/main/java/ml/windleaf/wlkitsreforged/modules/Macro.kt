package ml.windleaf.wlkitsreforged.modules

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.internal.PlayerInType
import ml.windleaf.wlkitsreforged.internal.file.JsonData
import ml.windleaf.wlkitsreforged.modules.macro.MacroException
import ml.windleaf.wlkitsreforged.modules.macro.MacroManager
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

@CommandInfo(cmd = "macro", description = "Run the macro", belongTo = Macro::class)
@ModuleInfo(description = "Run the macro", type = LoadType.ON_STARTUP)
class Macro : Module, ModuleCommand {
    private var enabled = false
    override fun getEnabled() = enabled
    companion object {
        var enabled by Delegates.notNull<Boolean>()
        val manager = MacroManager()
        val authorized = JsonData("macro_authorized")
        lateinit var authedList: JSONArray
    }

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        authedList = JSON.toJSON(authorized.getAs<ArrayList<*>>("authed")!!) as JSONArray
        Companion.enabled = enabled
    }

    @Permission("wlkits.cmd.macro")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) Util.invalidArgs(sender) else {
            when (args[0]) {
                "help" -> Util.sendHelp(sender,
                    "/macro help" to "查看此帮助",
                    "/macro list" to "查看所有可用的宏",
                    "/macro run [macro]" to "执行宏",
                    "/macro authed [player]" to "查看玩家是否已经被授权",
                    "/macro authlist" to "查看授权玩家的列表")
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
                                    Util.getModuleMsg(getName(), "success"),
                                    "result" to Util.getModuleMsg(getName(), "no-result")
                                ) else Util.insert(
                                    Util.getModuleMsg(getName(), "success"),
                                    "result" to result
                                )
                            WLKits.debug("Macro run msg: $msg")
                            Util.send(sender, msg)
                        }, {
                            val errorMsg = "${it.javaClass.simpleName}: ${it.message}"
                            Util.send(sender, Util.insert(Util.getModuleMsg(getName(), "error"), "errorMsg" to errorMsg))
                        })
                    }
                }
                "authed" -> {
                    if (args.size != 2) Util.invalidArgs(sender)
                    else {
                        val n = args[1]
                        val player = Bukkit.getPlayer(n)
                        if (player == null) Util.send(sender, Util.insert(Util.getModuleMsg("main", "player-not-found"), "playerName" to n))
                        else Util.send(sender, Util.insert(Util.getModuleMsg(getName(), "authed"), "playerName" to n, "authed" to Util.parseBooleanColor(manager.isAuthorized(player))))
                    }
                }
                "authlist" -> Util.send(sender, authedList.stream().map {
                    val player = Util.getPlayerBy(it as String)
                    val name = if (player == null) "?" else player.getPlayer()?.name
                    "($name) $it"
                }.collect(Collectors.joining("&b,&r ")))
                else -> Util.invalidArgs(sender)
            }
        }
    }
}