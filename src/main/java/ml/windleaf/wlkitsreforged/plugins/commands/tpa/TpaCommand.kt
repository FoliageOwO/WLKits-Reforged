package ml.windleaf.wlkitsreforged.plugins.commands.tpa

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TpaCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Tpa.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "tpa", PermissionType.COMMAND)) {
                sender as Player
                if (args.isEmpty()) {
                    Util.send(sender, Util.getPluginMsg("Tpa", "help"))
                } else {
                    val player = Bukkit.getPlayer(args[0])
                    if (player === sender) {
                        Util.send(sender, Util.getPluginMsg("Tpa", "self-tpa"))
                    } else {
                        if (player == null) {
                            val i = HashMap<String, String>()
                            i["playerName"] = args[0]
                            Util.send(sender, Util.insert(Util.getPluginMsg("Tpa", "not-found"), i))
                        } else {
                            Tpa.tpaLogs[player] = sender
                            val receiverLines = Util.getPluginMsgAs("Tpa", "receiver-lines") as List<*>
                            val senderLines = Util.getPluginMsgAs("Tpa", "sender-lines") as List<*>
                            val i = HashMap<String, String>()
                            i["receiver"] = player.displayName
                            i["sender"] = sender.displayName
                            for (rl in receiverLines) Util.send(player, Util.insert(rl as String, i))
                            for (sl in senderLines) Util.send(sender, Util.insert(sl as String, i))
                        }
                    }
                }
            }
        } else Util.disabled(sender)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String?>): List<String>? {
        val tmp: MutableList<String> = ArrayList()
        for (p in Bukkit.getOnlinePlayers()) tmp.add(p.name)
        val filter = Arrays.stream<Any>(tmp.toTypedArray()).filter { s: Any ->
            s.toString().startsWith(args[0]!!)
        }.collect(Collectors.toList())
        val players: MutableList<String> = ArrayList()
        for (i in filter) players.add(i.toString())
        return players
    }
}