package ml.windleaf.wlkitsreforged.plugins.commands.warp

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Warp
import ml.windleaf.wlkitsreforged.utils.Util
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
        if (Warp.enabled && Util.needPermission(sender, "warp", PermissionType.COMMAND)) {
            if (args.isEmpty()) Util.invalidArgs(sender)
            else {
                val name = args[0]
                val n = "name" to name
                val pn = "playerName" to sender.name
                if (Warp.warpManager.getWarps().keys.contains(name)/* && '|' !in name.toCharArray()*/) {
                    delete(sender, name, Warp.WarpType.PUBLIC, n, pn)
                } else if (sender is Player && Warp.warpManager.getWarps().keys.contains("${Util.getUUID(sender)}|$name")) {
                    delete(sender, "${Util.getUUID(sender)}|$name", Warp.WarpType.PRIVATE, n, pn)
                } else Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "not-found"), n, pn))
            }
        } else Util.disabled(sender)
        return true
    }

    private fun delete(sender: CommandSender, name: String, type: Warp.WarpType, vararg pairs: Pair<String, String>) {
        when (type) {
            Warp.WarpType.PUBLIC -> {
                if (sender.isOp || Util.getPluginConfig("Warp", "allow-public") as Boolean) {
                    Warp.warpManager.warps?.set("public.$name", null)
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "del-success"), *pairs))
                    if (Util.getPluginConfig("Warp", "broadcast") as Boolean)
                        Util.broadcastPlayers(Util.insert(Util.getPluginMsg("Warp", "del-broadcast"), *pairs))
                    val list = Warp.warpManager.warps?.getStringList("list")!!
                    list.remove(name)
                    Warp.warpManager.warps?.set("list", list)
                } else Util.send(sender, Util.getPluginMsg("Warp", "cannot-public"))
            }
            Warp.WarpType.PRIVATE -> {
                val p = if ('|' in name.toCharArray()) name else (if (sender is Player) "${Util.getUUID(sender)}|$name" else name)
                val l = p.split("|")
                val uuid = l[0]
                if ((sender is Player && Util.getUUID(sender) == uuid) || sender !is Player) {
                    Warp.warpManager.warps?.set("private.$p", null)
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "del-success"), *pairs))
                    val list = Warp.warpManager.warps?.getStringList("list")!!
                    list.remove(p)
                    Warp.warpManager.warps?.set("list", list)
                } else Util.send(sender, Util.getPluginMsg("Warp", "del-private"))
            }
        }
        Warp.warpManager.warps?.save(Warp.warpManager.file!!)
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String?>): List<String> {
        val tmp: HashMap<String, Warp.WarpType> = Warp.warpManager.getWarps()
        val filter = Arrays.stream<Any>(tmp.keys.toTypedArray()).filter { s: Any ->
            s.toString().startsWith(args[0]!!)
        }.collect(Collectors.toList())
        val warps: MutableList<String> = ArrayList()
        for (name in filter) {
            name as String
            if (sender is Player) {
                if (Util.getPluginConfig("Warp", "allow-public") as Boolean || sender.isOp) warps.add(name)
                if (tmp[name] == Warp.WarpType.PRIVATE && name.startsWith(Util.getUUID(sender))) warps.add(name.split("|")[1])
            }
        }
        return warps
    }
}