package ml.windleaf.wlkitsreforged.plugins.commands.warp

import ml.windleaf.wlkitsreforged.plugins.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class SetwarpCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Warp.enabled) {
            if (Util.mustPlayer(sender)) {
                if (args.isEmpty() || args.size < 2) Util.invalidArgs(sender)
                else {
                    if (args[0].length > 15) Util.send(sender, Util.getPluginMsg("Warp", "max-string"))
                    else {
                        sender as Player
                        var type: Any = args[0]
                        val name = args[1]
                        val i = HashMap<String, String>()
                        i["name"] = name
                        val types = HashMap<String, Warp.WarpType>()
                        for (t: Warp.WarpType in Warp.WarpType.values()) types[t.string] = t

                        i["type"] = type as String
                        if (type !in types.keys) Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "unknown-type"), i))
                        else {
                            type = types[type]!!
                            when (type) {
                                Warp.WarpType.PUBLIC -> {
                                    if (sender.isOp) set(sender, name, type, i)
                                    if (!sender.isOp && Util.getPluginConfig("Warp", "allow-public") as Boolean) set(sender, name, type, i)
                                }
                                Warp.WarpType.PRIVATE -> set(sender, name, type, i)
                            }
                        }
                    }
                }
            }
        } else Util.disabled(sender)
        return true
    }

    private fun set(sender: Player, name: String, type: Warp.WarpType, i: HashMap<String, String>) {
        val kname = if (type == Warp.WarpType.PRIVATE) "${Util.getUUID(sender)}.$name" else name
        if (Warp.warpManager.warps?.getKeys(false)?.contains(kname) == true) {
            Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "already-exists"), i))
        } else {
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
                Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "success"), i))
                if (Util.getPluginConfig("Warp", "broadcast") as Boolean) {
                    i["playerName"] = sender.displayName
                    i["name"] = name
                    for (line in Util.getPluginMsgAs("Warp", "broadcast-lines") as List<*>) Util.broadcastPlayers(Util.insert(line as String, i))
                }
            } catch (e: IOException) {
                Util.send(sender, Util.getPluginMsg("Warp", "fail"))
                e.printStackTrace()
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        val tab = ArrayList<String>()
        tab.add("[private/public] [name]")
        return tab
    }
}