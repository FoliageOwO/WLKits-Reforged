package ml.windleaf.wlkitsreforged.utils

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.PluginManager
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.core.reflect.versions.V1_16_R3
import ml.windleaf.wlkitsreforged.core.reflect.versions.Versions
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.io.File
import java.util.*

class Util {
    companion object {
        fun translateColorCode(s: String?) = s?.replace("&", "§")
        fun withPrefix() = "&b${WLKits.name} >> &r" /* "&b${WLKits.name} » &r" */
        fun getPath() = System.getProperty("user.dir") + File.separator
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
        fun generateRandomToken() = UUID.randomUUID().toString().replace("-", "")
        fun broadcastPlayers(string: String?) = Bukkit.getOnlinePlayers().forEach { send(it, string) }
        fun sendHelp(s: CommandSender, vararg pairs: Pair<String, String>) = pairs.forEach { send(s, "&6${it.first} &f- &a${it.second}".replace("|", "&2|&6")) }
        fun send(p: CommandSender, vararg s: String?) = s.forEach { send(p, it) }
        fun getPluginByName(name: String): Plugin? = PluginManager.pluginList.firstOrNull { it.name == name }
        fun getWorldByName(name: String): World? = Bukkit.getWorlds().firstOrNull { it.toString() == name || it.name == name }

        fun registerEvent(listener: Listener) {
            WLKits.debug(null, "register listener: $listener")
            WLKits.instance.server.pluginManager.registerEvents(listener, WLKits.instance)
        }

        fun mustPlayer(p: CommandSender): Boolean {
            return if (p is Player && p !is ConsoleCommandSender) true
            else {
                send(p, getPluginMsg("main", "must-player"))
                false
            }
        }

        fun insert(s: String?, vararg pairs: Pair<String, String>): String? {
            var result = s
            pairs.forEach { result = result?.replace("{${it.first}}", it.second) }
            return result
        }

        fun needPermission(p: CommandSender, name: String, type: PermissionType): Boolean {
            if (hasPermission(p, name, type)) return true
            else send(p, getPluginMsg("main", "no-permission"))
            return false
        }

        fun getReflector(): Reflector {
            val versions = Bukkit.getBukkitVersion().split(".")
            val major = versions[0]
            val minor = versions[1]
            var nmsVersion = "v$major@$minor@R".replace('@', '_')

            for (i in 1..9) {
                val versionTest = nmsVersion + i
                try {
                    Class.forName("org.bukkit.craftbukkit.$versionTest.inventory.CraftItemStack")
                    nmsVersion += i
                    break
                } catch (ignored: ClassNotFoundException) { }
            }

            val result: Reflector = try {
                Versions.valueOf(nmsVersion.uppercase()).reflector
            } catch (e: IllegalArgumentException) { V1_16_R3() }

            WLKits.debug(ref = result, "NMS: $nmsVersion -> $result")
            return result
        }
    }
}