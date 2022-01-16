package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import java.util.*
import java.util.stream.Collectors

class PlayerTag : Plugin, Listener, CommandExecutor, TabCompleter {
    override var name = "PlayerTag"
    companion object {
        var path: String = WLKits.prefixPath + "playertags.data"
        var playerTags = FileUtil.loadHashMap(path) as HashMap<String, String>
        var enabled = Util.isEnabled("PlayerTag")
    }

    override fun load() {
        Util.registerEvent(this)
        Util.registerCommand("playertag", this)
    }

    override fun unload() {
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (enabled && Util.needPermission(sender, "playertag", PermissionType.COMMAND)) {
            if (args.isEmpty()) Util.invalidArgs(sender) else {
                val i = HashMap<String, String>()
                i["playerName"] = name
                when (args[0]) {
                    "help" -> {
                        val helps = HashMap<String, String>()
                        helps["/playertag help"] = "查看此帮助"
                        helps["/playertag set [player] [tag]"] = "设置一个玩家的称号"
                        helps["/playertag remove [player]"] = "删除一个玩家的称号"
                        helps["/playertag check [player]"] = "查看一个玩家的称号"
                        helps["/playertag reset [player]"] = "重置一个玩家的称号/名称"
                        Util.sendHelp(sender, helps)
                    }
                    "set" -> {
                        if (args.size < 3) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            val tag = args[2]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, i)
                            else {
                                i["tag"] = Util.translateColorCode(tag)!!
                                playerTags[Util.getUUID(player)] = Util.insert(Util.getPluginConfig(name, "format") as String, i)!!
                                player.setDisplayName(Util.translateColorCode(tag)!!)
                                FileUtil.saveHashMap(playerTags, path)
                                Util.send(sender, Util.insert(Util.getPluginMsg(name, "set-success"), i))
                            }
                        }
                    }
                    "remove" -> {
                        if (args.size < 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, i)
                            else {
                                playerTags.remove(Util.getUUID(player))
                                FileUtil.saveHashMap(playerTags, path)
                                Util.send(sender, Util.insert(Util.getPluginMsg(name, "del-success"), i))
                            }
                        }
                    }
                    "check" -> {
                        if (args.size < 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, i)
                            else {
                                val tag = playerTags[Util.getUUID(player)]
                                i["tag"] = tag!!
                                when (tag) {
                                    null -> noTag(sender, i)
                                    "" -> noTag(sender, i)
                                    else -> Util.send(sender, Util.insert(Util.getPluginMsg(name, "get"), i))
                                }
                            }
                        }
                    }
                    "reset" -> {
                        if (args.size < 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, i)
                            else {
                                playerTags.remove(Util.getUUID(player))
                                player.setDisplayName(player.name)
                                FileUtil.saveHashMap(playerTags, path)
                                Util.send(sender, Util.insert(Util.getPluginMsg(name, "reset-success"), i))
                            }
                        }
                    }
                    else -> Util.invalidArgs(sender)
                }
            }
        } else Util.disabled(sender)
        return false
    }

    private fun noTag(sender: CommandSender, i: HashMap<String, String>) = Util.send(sender, Util.insert(Util.getPluginMsg(name, "no-tag"), i))
    private fun noPlayer(sender: CommandSender, i: HashMap<String, String>) = Util.send(sender, Util.insert(Util.getPluginMsg(name, "no-player"), i))

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String?>): List<String> {
        val subCommands = arrayOf("help", "set", "remove", "check", "reset")
        if (args.size > 1) {
            val list = ArrayList<String>()
            for (p in Bukkit.getOnlinePlayers()) list.add(p.name)
            return list
        } else if (args.isEmpty()) return listOf(*subCommands)
        return Arrays.stream(subCommands).filter { s: String ->
            s.startsWith(args[0]!!)
        }.collect(Collectors.toList())
    }
}