package ml.windleaf.wlkitsreforged.plugins.commands.warp

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException
import java.util.*

class SetwarpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Warp.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "warp", PermissionType.COMMAND)) {
                if (args.isEmpty() || args.size < 2) Util.invalidArgs(sender)
                else {
                    if (args[0].length > 15) Util.send(sender, Util.getPluginMsg("Warp", "max-string"))
                    else {
                        sender as Player
                        var type: Any = args[0]
                        val name = args[1]
                        val types = HashMap<String, Warp.WarpType>()
                        for (t: Warp.WarpType in Warp.WarpType.values()) types[t.string] = t
                        val n = "name" to name
                        val t = "type" to type.toString()
                        if (type !in types.keys) Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "unknown-type"), n, t))
                        else {
                            type = types[type]!!
                            when (type) {
                                Warp.WarpType.PUBLIC -> {
                                    if (sender.isOp) set(sender, name, type, n, t)
                                    else if (!sender.isOp && Util.getPluginConfig("Warp", "allow-public") as Boolean) {
                                        set(sender, name, type, n, t)
                                    } else Util.send(sender, Util.getPluginMsg("Warp", "cannot-public"))
                                }
                                Warp.WarpType.PRIVATE -> set(sender, name, type, n, t)
                            }
                        }
                    }
                }
            }
        } else Util.disabled(sender)
        return true
    }

    private fun set(sender: Player, name: String, type: Warp.WarpType, vararg pairs: Pair<String, String>) {
        val kname = if (type == Warp.WarpType.PRIVATE) "${Util.getUUID(sender)}|$name" else name
        if (Warp.warpManager.getWarps().keys.contains(kname)) Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "already-exists"), *pairs))
        else {
            val list = Warp.warpManager.warps?.getStringList("list")!!
            list.add(kname)
            Warp.warpManager.warps?.set("list", list)
            val sname = if (type == Warp.WarpType.PRIVATE) "private.$kname" else "public.$kname"
            val location = sender.location
            Warp.warpManager.warps?.set("$sname.x", location.x)
            Warp.warpManager.warps?.set("$sname.y", location.y)
            Warp.warpManager.warps?.set("$sname.z", location.z)
            Warp.warpManager.warps?.set("$sname.world", location.world?.name)
            if (Util.getPluginConfig("Warp", "set-more") as Boolean) {
                Warp.warpManager.warps?.set("$sname.yaw", location.yaw)
                Warp.warpManager.warps?.set("$sname.pitch", location.pitch)
            }
            try {
                Warp.warpManager.warps?.save(Warp.warpManager.file!!)
                Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "success"), *pairs))
                if (Util.getPluginConfig("Warp", "broadcast") as Boolean) {
                    val p = "playerName" to sender.displayName
                    val n = "name" to name
                    if (type == Warp.WarpType.PUBLIC)
                        for (line in Util.getPluginMsgAs("Warp", "broadcast-lines") as List<*>) Util.broadcastPlayers(Util.insert(line as String, p, n))
                }
            } catch (e: IOException) {
                Util.send(sender, Util.getPluginMsg("Warp", "fail"))
                e.printStackTrace()
            }
        }
    }
}