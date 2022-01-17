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
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*
import java.util.stream.Collectors

class PlayerTag : Plugin, Listener, CommandExecutor, TabCompleter {
    override val name = "PlayerTag"
    override val enabled = Util.isEnabled(name)
    companion object {
        var path: String = WLKits.prefixPath + "playertags.data"
        var playerTags = FileUtil.loadHashMap(path) as HashMap<String, String>
        var enabled = Util.isEnabled("PlayerTag")
        var scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard!!
    }

    override fun load() {
        Util.registerEvent(this)
        Util.registerCommand("playertag", this)
        for (p in Bukkit.getOnlinePlayers()) setDisplayName(p)
    }

    override fun unload() {
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (enabled && Util.needPermission(sender, "playertag", PermissionType.COMMAND)) {
            if (args.isEmpty()) Util.invalidArgs(sender) else {
                val i = HashMap<String, String>()
                when (args[0]) {
                    "help" -> {
                        val helps = HashMap<String, String>()
                        helps["/playertag help"] = "查看此帮助"
                        helps["/playertag set [player] [tag]"] = "设置一个玩家的称号"
                        helps["/playertag get [player]"] = "查看一个玩家的称号"
                        helps["/playertag reset [player]"] = "重置一个玩家的称号/名称"
                        Util.sendHelp(sender, helps)
                    }
                    "set" -> {
                        if (args.size != 3) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            i["playerName"] = n
                            val tag = args[2]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, i)
                            else {
                                val coloredTag = Util.translateColorCode(tag)!!
                                i["tag"] = coloredTag
                                val formatted = Util.insert(Util.getPluginConfig(name, "format") as String, i)!!
                                playerTags[Util.getUUID(player)] = coloredTag
                                val displayName = formatted + player.name
                                player.setDisplayName(displayName)
                                setNameTag(player, formatted)
                                FileUtil.saveHashMap(playerTags, path)
                                Util.send(sender, Util.insert(Util.getPluginMsg(name, "set-success"), i))
                            }
                        }
                    }
                    "get" -> {
                        if (args.size != 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            i["playerName"] = n
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, i)
                            else {
                                when (val tag = playerTags[Util.getUUID(player)]) {
                                    null -> noTag(sender, i)
                                    "" -> noTag(sender, i)
                                    else -> {
                                        i["tag"] = tag
                                        Util.send(sender, Util.insert(Util.getPluginMsg(name, "get"), i))
                                    }
                                }
                            }
                        }
                    }
                    "reset" -> {
                        if (args.size != 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]
                            i["playerName"] = n!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, i)
                            else {
                                playerTags.remove(Util.getUUID(player))
                                player.setDisplayName(player.name)
                                resetNameTag(player)
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
        val subCommands = arrayOf("help", "set", "get", "reset")
        if (args.size > 1) {
            val list = ArrayList<String>()
            for (p in Bukkit.getOnlinePlayers()) list.add(p.name)
            return list
        } else if (args.isEmpty()) return listOf(*subCommands)
        else if (args.size > 2) return emptyList()
        return Arrays.stream(subCommands).filter { s: String ->
            s.startsWith(args[0]!!)
        }.collect(Collectors.toList())
    }

    @EventHandler
    fun event(e: PlayerJoinEvent) {
        if (enabled) setDisplayName(e.player)
    }

    private fun setDisplayName(p: Player) {
        val uuid = Util.getUUID(p)
        if (playerTags.containsKey(uuid)) {
            val tag = playerTags[uuid]!!
            val displayName = Util.insert(Util.getPluginConfig(name, "format") as String, hashMapOf("tag" to tag))!! + p.name
            p.setDisplayName(displayName)
        }
    }

    private fun setNameTag(p: Player, tag: String) {
        var team = scoreboard.getTeam(p.hashCode().toString())
        if (team == null) team = scoreboard.registerNewTeam(p.hashCode().toString())
        team.prefix = tag
        team.addEntry(p.name)
    }

    private fun resetNameTag(p: Player) = scoreboard.getTeam(p.hashCode().toString())?.unregister()
}