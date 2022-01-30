package ml.windleaf.wlkitsreforged.modules.commands.tpa

import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleTabCompleter
import ml.windleaf.wlkitsreforged.modules.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@CommandInfo(cmd = "tpa", description = "Send a tpa request to player", belongTo = Tpa::class)
class TpaCommand : ModuleCommand, ModuleTabCompleter {
    @MustPlayer
    @Permission("wlkits.cmd.tpa")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        sender as Player
        if (args.isEmpty()) {
            Util.send(sender, Util.getPluginMsg("Tpa", "help"))
        } else {
            val player = Bukkit.getPlayer(args[0])
            if (player === sender) {
                Util.send(sender, Util.getPluginMsg("Tpa", "self-tpa"))
            } else {
                if (player == null) Util.send(sender, Util.insert(Util.getPluginMsg("main", "player-not-found"), "playerName" to args[0]))
                else {
                    Tpa.tpaLogs[player] = sender
                    val receiverLines = Util.getPluginMsgAs("Tpa", "receiver-lines") as List<*>
                    val senderLines = Util.getPluginMsgAs("Tpa", "sender-lines") as List<*>
                    val rc = "receiver" to player.displayName
                    val sd = "sender" to sender.displayName
                    for (rl in receiverLines) Util.send(player, Util.insert(rl as String, rc, sd))
                    for (sl in senderLines) Util.send(sender, Util.insert(sl as String, rc, sd))
                }
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<String>): List<String> {
        val tmp: MutableList<String> = ArrayList()
        for (p in Bukkit.getOnlinePlayers()) tmp.add(p.name)
        val filter = Arrays.stream<Any>(tmp.toTypedArray()).filter { s: Any ->
            s.toString().startsWith(args[0])
        }.collect(Collectors.toList())
        val players: MutableList<String> = ArrayList()
        for (i in filter) players.add(i.toString())
        return players
    }
}