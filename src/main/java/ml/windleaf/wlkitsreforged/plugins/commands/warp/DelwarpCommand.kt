package ml.windleaf.wlkitsreforged.plugins.commands.warp

import ml.windleaf.wlkitsreforged.plugins.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap

class DelwarpCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Warp.enabled) {
            return if (args.isEmpty()) {
                Util.invalidArgs(sender)
                false
            } else {
                val name = args[0]
                val i = HashMap<String, String>()
                i["name"] = name
                i["playerName"] = sender.name
                if (Warp.warpManager.warps?.contains(name) == true) {
                    delete(sender, name, Warp.WarpType.PUBLIC, i)
                } else if (sender is Player) {
                    if (Warp.warpManager.warps?.contains("${Util.getUUID(sender)}.$name") == true) {
                        delete(sender, "${Util.getUUID(sender)}.$name", Warp.WarpType.PRIVATE, i)
                    }
                } else Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "not-found"), i))
                true
            }
        } else Util.disabled(sender)
        return true
    }

    private fun delete(sender: CommandSender, name: String, type: Warp.WarpType, i: HashMap<String, String>) {
        when (type) {
            Warp.WarpType.PUBLIC -> {
                if (sender.isOp || Util.getPluginConfig("Warp", "allow-public") as Boolean) {
                    Warp.warpManager.warps?.set(name, null)
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "del-success"), i))
                    if (Util.getPluginConfig("Warp", "broadcast") as Boolean)
                        Util.broadcastPlayers(Util.insert(Util.getPluginMsg("Warp", "del-broadcast"), i))
                } else Util.send(sender, Util.getPluginMsg("Warp", "del-public"))
            }
            Warp.WarpType.PRIVATE -> {
                val list = listOf(name.split("."))
                val uuid = UUID.fromString(list[0].toString())
                if (sender == Bukkit.getPlayer(uuid)) {
                    Warp.warpManager.warps?.set(name, null)
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "del-success"), i))
                } else Util.send(sender, Util.getPluginMsg("Warp", "del-private"))
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String?>): List<String> {
        val tmp: HashMap<String, Warp.WarpType> = Warp.warpManager.getWarps()
        val filter = Arrays.stream<Any>(tmp.keys.toTypedArray()).filter { s: Any ->
            s.toString().startsWith(args[0]!!)
        }.collect(Collectors.toList())
        val warps: MutableList<String> = ArrayList()
        for (name in filter) {
            name as String
            if (sender is Player) if (tmp[name] == Warp.WarpType.PRIVATE && !name.startsWith(Util.getUUID(sender))) continue
            warps.add(name)
        }
        return warps
    }
}