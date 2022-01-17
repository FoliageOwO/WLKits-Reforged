package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.PluginManager
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*
import java.util.stream.Collectors

class WLKitsPlugin : Plugin, CommandExecutor, TabCompleter {
    override val name = "WLKitsPlugin"
    override val enabled = true

    override fun load() {
        Util.registerCommand("wlkits", this)
    }

    override fun unload() {
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (Util.needPermission(sender, "wlkits", PermissionType.COMMAND)) {
            if (args.isEmpty()) Util.invalidArgs(sender) else {
                when (args[0]) {
                    "help" -> {
                        val helps = HashMap<String, String>()
                        helps["/wlkits help"] = "查看此帮助"
                        helps["/wlkits reload"] = "重载插件"
                        helps["/wlkits status [pluginName]"] = "查看子插件开启状态"
                        helps["/wlkits info"] = "查看插件信息"
                        Util.sendHelp(sender, helps)
                    }
                    "reload" -> {
                        WLKits.reload()
                        WLKits.pluginManager.reload()
                        Util.send(sender, Util.getPluginMsg("main", "reload"))
                    }
                    "status" -> {
                        if (args.size != 2) Util.invalidArgs(sender) else {
                            val i = HashMap<String, String>()
                            val n = args[1]
                            i["pluginName"] = n
                            val plugin = Util.getPluginByName(n)
                            if (plugin == null) Util.send(sender, Util.insert(Util.getPluginMsg("main", "no-plugin"), i))
                            else {
                                i["status"] = if (plugin.enabled) "&aTRUE" else "&cFALSE"
                                Util.send(sender, Util.insert(Util.getPluginMsg("main", "status"), i))
                            }
                        }
                    }
                    "info" -> {
                        Util.send(sender,
                            "&aWLKits-Reforged &f[v${WLKits.version}]",
                            "&agithub: &ehttps://github.com/WindLeaf233/WLKits-Reforged",
                            "&aplugins: &b(${PluginManager.pluginList.size}) [${
                                PluginManager.pluginList.stream().map { it.name }.collect(Collectors.joining(", "))
                            }]")
                    }
                    else -> Util.invalidArgs(sender)
                }
            }
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        val subCommands = arrayOf("help", "reload", "status", "info")
        if (args.size > 1) return PluginManager.pluginList.stream().map { it.name }.collect(Collectors.toList())
        else if (args.isEmpty()) return listOf(*subCommands)
        else if (args.size > 2) return emptyList()
        return Arrays.stream(subCommands).filter { s: String ->
            s.startsWith(args[0])
        }.collect(Collectors.toList())
    }
}