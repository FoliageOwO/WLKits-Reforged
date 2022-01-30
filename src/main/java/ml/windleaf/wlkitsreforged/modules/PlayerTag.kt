package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleTabCompleter
import ml.windleaf.wlkitsreforged.internal.file.JsonData
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Scoreboard
import java.util.*
import java.util.stream.Collectors
import kotlin.properties.Delegates

@CommandInfo(cmd = "playertag", description = "Manage title of player", aliases = ["ptag"], belongTo = PlayerTag::class)
@ModuleInfo(description = "Manage title of player", type = LoadType.ON_LOAD_WORLD)
class PlayerTag : Module, Listener, ModuleCommand, ModuleTabCompleter {
    private var enabled = false
    override fun getEnabled() = enabled
    companion object {
        val playerTags = JsonData("playertags")
        var enabled by Delegates.notNull<Boolean>()
        lateinit var scoreboard: Scoreboard
    }

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard!!
        Companion.enabled = enabled
        Bukkit.getOnlinePlayers().forEach { setDisplayName(it) }
    }

    @Permission("wlkits.cmd.playertag")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
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
                        val n = args[1]
                        val tag = args[2]
                        val player = Bukkit.getPlayer(n)
                        if (player == null) noPlayer(sender, "playerName" to n, "tag" to tag)
                        else {
                            val coloredTag = Util.translateColorCode("$tag&r")!!
                            val formatted = Util.insert(Util.getPluginConfig(getName(), "format") as String, "playerName" to n, "tag" to coloredTag)!!
                            playerTags[Util.getUUID(player)!!] = coloredTag
                            val displayName = formatted + player.name
                            player.setDisplayName(displayName)
                            setNameTag(player, formatted)
                            playerTags.saveData()
                            Util.send(sender, Util.insert(Util.getPluginMsg(getName(), "set-success"), "playerName" to n, "tag" to coloredTag))
                        }
                    }
                }
                "get" -> {
                    if (args.size != 2) Util.invalidArgs(sender)
                    else {
                        val n = args[1]
                        val player = Bukkit.getPlayer(n)
                        if (player == null) noPlayer(sender, "playerName" to n)
                        else {
                            when (val tag = playerTags[Util.getUUID(player)!!] as String?) {
                                null -> noTag(sender, "playerName" to n)
                                "" -> noTag(sender, "playerName" to n)
                                else -> Util.send(sender, Util.insert(Util.getPluginMsg(getName(), "get"), "playerName" to n, "tag" to tag))
                            }
                        }
                    }
                }
                "reset" -> {
                    if (args.size != 2) Util.invalidArgs(sender)
                    else {
                        val n = args[1]
                        val player = Bukkit.getPlayer(n)
                        if (player == null) noPlayer(sender, "playerName" to n)
                        else {
                            playerTags.remove(Util.getUUID(player)!!)
                            player.setDisplayName(player.name)
                            resetNameTag(player)
                            playerTags.saveData()
                            Util.send(sender, Util.insert(Util.getPluginMsg(getName(), "reset-success"), "playerName" to n))
                        }
                    }
                }
                else -> Util.invalidArgs(sender)
            }
        }
    }

    private fun noTag(sender: CommandSender, vararg pairs: Pair<String, String>) = Util.send(sender, Util.insert(Util.getPluginMsg(getName(), "no-tag"), *pairs))
    private fun noPlayer(sender: CommandSender, vararg pairs: Pair<String, String>) = Util.send(sender, Util.insert(Util.getPluginMsg("main", "player-not-found"), *pairs))

    override fun onTabComplete(sender: CommandSender, args: Array<String>): List<String> {
        val subCommands = arrayOf("help", "set", "get", "reset")
        if (args.size > 1) {
            val list = ArrayList<String>()
            Bukkit.getOnlinePlayers().forEach { list.add(it.name) }
            return list.filter { s: String -> s.startsWith(args[1]) }
        } else if (args.isEmpty()) return listOf(*subCommands)
        else if (args.size > 2) return emptyList()
        return Arrays.stream(subCommands).filter { s: String ->
            s.startsWith(args[0])
        }.collect(Collectors.toList())
    }

    @EventHandler
    fun event(e: PlayerJoinEvent) {
        if (enabled) setDisplayName(e.player)
    }

    private fun setDisplayName(p: Player) {
        val uuid = Util.getUUID(p)!!
        if (playerTags.contains(uuid)) {
            val tag = playerTags[uuid]!! as String
            val displayName = Util.insert(Util.getPluginConfig(getName(), "format") as String, "tag" to tag)!! + p.name
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