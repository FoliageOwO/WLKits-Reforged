package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
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
import org.bukkit.scoreboard.Scoreboard
import java.util.*
import java.util.stream.Collectors

class PlayerTag : Plugin, Listener, CommandExecutor, TabCompleter {
    override val name = "PlayerTag"
    override val enabled = Util.isEnabled(name)
    override val type = LoadType.ON_LOAD_WORLD
    companion object {
        var path: String = WLKits.prefixPath + "playertags.data"
        var playerTags = FileUtil.loadHashMap(path) as HashMap<String, String>
        var enabled = Util.isEnabled("PlayerTag")
        lateinit var scoreboard: Scoreboard
    }

    override fun load() {
        scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard!!
        Util.registerEvent(this)
        Util.registerCommand("playertag", this)
        for (p in Bukkit.getOnlinePlayers()) setDisplayName(p)
    }

    override fun unload() {
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (enabled && Util.needPermission(sender, "playertag", PermissionType.COMMAND)) {
            if (args.isEmpty()) Util.invalidArgs(sender) else {
                when (args[0]) {
                    "help" -> Util.sendHelp(sender,
                            "/playertag help" to "查看此帮助",
                            "/playertag set [player] [tag]" to "设置一个玩家的称号",
                            "/playertag get [player]" to "查看一个玩家的称号",
                            "/playertag reset [player]" to "重置一个玩家的称号/名称")
                    "set" -> {
                        if (args.size != 3) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            val tag = args[2]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, "playerName" to n, "tag" to tag)
                            else {
                                val coloredTag = Util.translateColorCode(tag)!!
                                val formatted = Util.insert(Util.getPluginConfig(name, "format") as String, "playerName" to n, "tag" to coloredTag)!!
                                playerTags[Util.getUUID(player)] = coloredTag
                                val displayName = formatted + player.name
                                player.setDisplayName(displayName)
                                setNameTag(player, formatted)
                                FileUtil.saveHashMap(playerTags, path)
                                Util.send(sender, Util.insert(Util.getPluginMsg(name, "set-success"), "playerName" to n, "tag" to coloredTag))
                            }
                        }
                    }
                    "get" -> {
                        if (args.size != 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, "playerName" to n)
                            else {
                                when (val tag = playerTags[Util.getUUID(player)]) {
                                    null -> noTag(sender, "playerName" to n)
                                    "" -> noTag(sender, "playerName" to n)
                                    else -> Util.send(sender, Util.insert(Util.getPluginMsg(name, "get"), "playerName" to n, "tag" to tag))
                                }
                            }
                        }
                    }
                    "reset" -> {
                        if (args.size != 2) Util.invalidArgs(sender)
                        else {
                            val n = args[1]!!
                            val player = Bukkit.getPlayer(n)
                            if (player == null) noPlayer(sender, "playerName" to n)
                            else {
                                playerTags.remove(Util.getUUID(player))
                                player.setDisplayName(player.name)
                                resetNameTag(player)
                                FileUtil.saveHashMap(playerTags, path)
                                Util.send(sender, Util.insert(Util.getPluginMsg(name, "reset-success"), "playerName" to n))
                            }
                        }
                    }
                    else -> Util.invalidArgs(sender)
                }
            }
        } else Util.disabled(sender)
        return false
    }

    private fun noTag(sender: CommandSender, vararg pairs: Pair<String, String>) = Util.send(sender, Util.insert(Util.getPluginMsg(name, "no-tag"), *pairs))
    private fun noPlayer(sender: CommandSender, vararg pairs: Pair<String, String>) = Util.send(sender, Util.insert(Util.getPluginMsg(name, "no-player"), *pairs))

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
            val displayName = Util.insert(Util.getPluginConfig(name, "format") as String, "tag" to tag)!! + p.name
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