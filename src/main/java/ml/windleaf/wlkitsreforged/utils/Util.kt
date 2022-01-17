package ml.windleaf.wlkitsreforged.utils

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.PluginManager
import ml.windleaf.wlkitsreforged.core.WLKits
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.io.File

class Util {
    companion object {
        fun translateColorCode(s: String?) = s?.replace("&", "§")
        fun withPrefix() = "&b${WLKits.name} » &r"
        fun getPath() = System.getProperty("user.dir") + File.separator
        fun registerEvent(listener: Listener) = WLKits.instance.server.pluginManager.registerEvents(listener, WLKits.instance)
        fun registerCommand(cmd: String, executor: CommandExecutor) = WLKits.instance.getCommand(cmd)?.setExecutor(executor)
        fun getPluginConfig(pluginName: String, root: String) = WLKits.instance.config.get("plugins.${pluginName.lowercase()}.$root")
        fun getPluginMsg(pluginName: String, root: String) = WLKits.message.getString("${pluginName.lowercase()}.$root")
        fun getPluginMsgAs(pluginName: String, root: String) = WLKits.message.get("${pluginName.lowercase()}.$root")
        fun send(p: CommandSender, s: String?) = p.sendMessage(translateColorCode(withPrefix() + s)!!)
        fun isEnabled(pluginName: String) = getPluginConfig(pluginName, "enabled") as Boolean
        fun disabled(p: CommandSender) = send(p, getPluginMsg("main", "disabled"))
        fun getUUID(p: Player) = p.uniqueId.toString()
        fun invalidArgs(p: CommandSender) = send(p, getPluginMsg("main", "invalid-args"))
        fun hasPermission(p: CommandSender, name: String, type: PermissionType) = p.hasPermission("wlkits.${type.string}.$name")

        fun mustPlayer(p: CommandSender): Boolean {
            return if (p is Player && p !is ConsoleCommandSender) true
            else {
                send(p, getPluginMsg("main", "must-player"))
                false
            }
        }

        fun insert(string: String?, insertMap: Map<String, String>): String? {
            var s = string
            for (i in insertMap.keys) s = s?.replace("{$i}", insertMap[i]!!)
            return s
        }

        fun broadcastPlayers(string: String?) {
            for (player in Bukkit.getOnlinePlayers()) send(player, string)
        }

        fun sendHelp(sender: CommandSender, helps: Map<String, String>) {
            for (i in helps.keys) send(sender, "&6$i &f- &a${helps[i]}".replace("|", "&2|&6"))
        }

        fun getWorldByName(name: String): World? {
            for (world in Bukkit.getWorlds()) if (world.toString() == name || world.name == name) return world
            return null
        }

        fun needPermission(p: CommandSender, name: String, type: PermissionType): Boolean {
            if (hasPermission(p, name, type)) return true
            else send(p, getPluginMsg("main", "no-permission"))
            return false
        }

        fun send(p: CommandSender, vararg s: String?) {
            for (i in s) send(p, i)
        }

        fun getPluginByName(name: String): Plugin? {
            for (plugin in PluginManager.pluginList) if (plugin.name == name) return plugin
            return null
        }
    }
}