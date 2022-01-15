package ml.windleaf.wlkitsreforged.plugins.commands.warp

import ml.windleaf.wlkitsreforged.plugins.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WarpCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Warp.enabled) {
            if (Util.mustPlayer(sender)) {
                if (args.isEmpty()) Util.invalidArgs(sender)
                else {
                    sender as Player
                    val name = args[0]
                    val i = HashMap<String, String>()
                    i["name"] = name
                    if (Warp.warpManager.warps?.contains(name) == true) {
                        teleport(sender, name, Warp.WarpType.PUBLIC, i)
                    } else if (Warp.warpManager.warps?.contains("${Util.getUUID(sender)}.$name") == true) {
                        teleport(sender, "${Util.getUUID(sender)}.$name", Warp.WarpType.PRIVATE, i)
                    } else Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "not-found"), i))
                }
            }
        } else Util.disabled(sender)
        return true
    }

    private fun teleport(sender: Player, name: String, type: Warp.WarpType, i: HashMap<String, String>) {
        val world = Warp.warpManager.warps?.getString("$name.world")
        val x = Warp.warpManager.warps?.getDouble("$name.x")
        val y = Warp.warpManager.warps?.getDouble("$name.y")
        val z = Warp.warpManager.warps?.getDouble("$name.z")
        val yaw = Warp.warpManager.warps?.getInt("$name.yaw")
        val pitch = Warp.warpManager.warps?.getInt("$name.pitch")

        val location: Location =
            if (yaw == null || pitch == null) Location(Bukkit.getWorld(world!!), x!!, y!!, z!!)
            else Location(Bukkit.getWorld(world!!), x!!, y!!, z!!, yaw.toFloat(), pitch.toFloat())

        when (type) {
            Warp.WarpType.PUBLIC -> {
                sender.teleport(location)
                Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "tp-success"), i))
            }
            Warp.WarpType.PRIVATE -> {
                val list = listOf(name.split("."))
                val uuid = UUID.fromString(list[0].toString())
                if (sender == Bukkit.getPlayer(uuid)) {
                    sender.teleport(location)
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "tp-success"), i))
                } else Util.send(sender, Util.getPluginMsg("Warp", "tp-private"))
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String?>): List<String>? {
        val tmp: HashMap<String, Warp.WarpType> = Warp.warpManager.getWarps()
        val filter = Arrays.stream<Any>(tmp.keys.toTypedArray()).filter { s: Any ->
            s.toString().startsWith(args[0]!!)
        }.collect(Collectors.toList())
        val warps: MutableList<String> = ArrayList()
        for (name in filter) {
            name as String
            if (tmp[name] == Warp.WarpType.PUBLIC) warps.add(name)
        }
        return warps
    }
}