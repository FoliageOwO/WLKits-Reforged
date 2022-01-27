package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.*
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*
import java.util.stream.Collectors

class WLKitsPlugin : Plugin, CommandExecutor, TabCompleter {
    private var enabled = false
    override fun getName() = "WLKitsPlugin"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(name)
    }

    override fun unload() = Unit
    override fun registers() = Util.registerCommand("wlkits", this)!!

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (enabled) {
            if (Util.needPermission(sender, "wlkits", PermissionType.COMMAND)) {
                if (args.isEmpty()) Util.invalidArgs(sender) else {
                    when (args[0]) {
                        "help" -> Util.sendHelp(sender,
                            "/wlkits help" to "查看此帮助",
                            "/wlkits reload" to "重载插件",
                            "/wlkits status [pluginName]" to "查看子插件开启状态",
                            "/wlkits info" to "查看插件信息")
                        "reload" -> {
                            PluginManager.reload()
                            Util.send(sender, Util.getPluginMsg("main", "reload"))
                        }
                        "status" -> {
                            if (args.size != 2) Util.invalidArgs(sender) else {
                                val n = args[1]
                                val plugin = Util.getPluginByName(n)
                                if (plugin == null) Util.send(sender, Util.insert(Util.getPluginMsg("main", "no-plugin"), "pluginName" to n))
                                else Util.send(sender, Util.insert(Util.getPluginMsg("main", "status"), "pluginName" to n, "status" to if (plugin.enabled) "&aTRUE" else "&cFALSE"))
                            }
                        }
                        "info" -> {
                            Util.send(sender,
                                "&a${WLKits.name} &fv${WLKits.version} &arunning on bukkit &c${Bukkit.getBukkitVersion()} &6[using Reflector: ${WLKits.reflector.nms}]",
                                "&agithub: &ehttps://github.com/WindLeaf233/WLKits-Reforged",
                                "&aplugins: &b(${PluginManager.pluginList.size}) [${
                                    PluginManager.pluginList.stream().map { if (it.enabled) "&a${it.name}&r" else "&c${it.name}&r" }.collect(Collectors.joining(", "))
                                }]",
                                "&adebugMode: ${if (WLKits.debug) "&aTRUE" else "&cFALSE"}")
                        }
                        else -> Util.invalidArgs(sender)
                    }
                }
            }
        } else Util.disabled(sender)
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        val subCommands = arrayOf("help", "reload", "status", "info")
        if (args.size > 1) return PluginManager.pluginList.stream().map {
            it.name
        }.collect(Collectors.toList()).filter { s: String -> s.startsWith(args[1]) }
        else if (args.isEmpty()) return listOf(*subCommands).filter { s: String -> s.startsWith(args[0]) }
        else if (args.size > 2) return emptyList()
        return Arrays.stream(subCommands).filter { s: String ->
            s.startsWith(args[0])
        }.collect(Collectors.toList())
    }
}