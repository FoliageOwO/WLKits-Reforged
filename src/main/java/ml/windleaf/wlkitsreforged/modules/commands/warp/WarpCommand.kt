package ml.windleaf.wlkitsreforged.modules.commands.warp

import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.modules.Warp
import ml.windleaf.wlkitsreforged.modules.enums.WarpType
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.math.BigDecimal
import java.util.*
import java.util.stream.Collectors

class WarpCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Warp.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "warp", PermissionType.COMMAND)) {
                if (args.isEmpty()) Util.invalidArgs(sender)
                else {
                    sender as Player
                    val name = args[0]
                    val uuid = Util.getUUID(sender)
                    if (Warp.existsWarp(uuid!!, name, WarpType.PUBLIC)) {
                        teleport(sender, name, WarpType.PUBLIC, "name" to name)
                    } else if (Warp.existsWarp(uuid, name, WarpType.PRIVATE)) {
                        teleport(sender, name, WarpType.PRIVATE, "name" to name)
                    } else Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "not-found"), "name" to name))
                }
            }
        } else Util.disabled(sender)
        return true
    }

    private fun teleport(sender: Player, name: String, type: WarpType, pair: Pair<String, String>) {
        val uuid = Util.getUUID(sender)
        val jsonObj = Warp.getWarpByName(name, type)!!

        val world = jsonObj["world"] as String
        val x = (jsonObj["x"] as BigDecimal).toDouble()
        val y = (jsonObj["y"] as BigDecimal).toDouble()
        val z = (jsonObj["z"] as BigDecimal).toDouble()
        val yaw = jsonObj["yaw"] as Int?
        val pitch = jsonObj["pitch"] as Int?

        val location: Location =
            if (yaw == null || pitch == null) Location(Util.getWorldByName(world), x, y, z)
            else Location(Util.getWorldByName(world), x, y, z, yaw.toFloat(), pitch.toFloat())

        when (type) {
            WarpType.PUBLIC -> {
                sender.teleport(location)
                Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "tp-success"), pair))
            }
            WarpType.PRIVATE -> {
                if (Util.getUUID(sender) == uuid) {
                    sender.teleport(location)
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "tp-success"), pair))
                } else Util.send(sender, Util.getPluginMsg("Warp", "tp-private"))
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String?>): List<String> {
        val tmp = Warp.getWarps()
        val filter = Arrays.stream<Any>(tmp.keys.toTypedArray()).filter { s: Any ->
            s.toString().startsWith(args[0]!!)
        }.collect(Collectors.toList())
        val warps: MutableList<String> = ArrayList()
        for (name in filter) {
            name as String
            if (tmp[name] == WarpType.PUBLIC) warps.add(name)
            if (sender is Player && tmp[name] == WarpType.PRIVATE) {
                val s = name.split("|")
                val uuid = s[0]
                if (Util.getUUID(sender) == uuid) warps.add(s[1])
            }
        }
        return warps
    }
}