package ml.windleaf.wlkitsreforged.modules.commands.warp

import com.alibaba.fastjson.JSONObject
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.modules.Warp
import ml.windleaf.wlkitsreforged.modules.enums.WarpType
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors

class DelwarpCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Warp.enabled && Util.needPermission(sender, "warp", PermissionType.COMMAND)) {
            if (args.isEmpty()) Util.invalidArgs(sender)
            else {
                val name = args[0]
                val uuid = if (sender is Player) Util.getUUID(sender) else ""
                val n = "name" to name
                val pn = "playerName" to sender.name
                if (Warp.existsWarp(uuid, name, WarpType.PUBLIC)) {
                    delete(sender, name, WarpType.PUBLIC, n, pn)
                } else if (Warp.existsWarp(uuid, name, WarpType.PRIVATE)) {
                    delete(sender, name, WarpType.PRIVATE, n, pn)
                } else Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "not-found"), n, pn))
            }
        } else Util.disabled(sender)
        return true
    }

    private fun delete(sender: CommandSender, name: String, type: WarpType, vararg pairs: Pair<String, String>) {
        val uuid = if (sender is Player) Util.getUUID(sender) else ""
        when (type) {
            WarpType.PUBLIC -> {
                if (sender.isOp || Util.getPluginConfig("Warp", "allow-public") as Boolean) {
                    Warp.list.remove(name)
                    Warp.update()

                    Warp.publics.removeIf { (it as JSONObject)["name"] == name }
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "del-success"), *pairs))
                    if (Util.getPluginConfig("Warp", "broadcast") as Boolean)
                        Util.broadcastPlayers(Util.insert(Util.getPluginMsg("Warp", "del-broadcast"), *pairs))
                    Warp.update()
                } else Util.send(sender, Util.getPluginMsg("Warp", "cannot-public"))
            }
            WarpType.PRIVATE -> {
                val jsonObj = Warp.getWarpByName(name, WarpType.PRIVATE)!!
                if (jsonObj["owner"] == uuid) {
                    Warp.list.remove(if (type == WarpType.PRIVATE) "$uuid|$name" else name)
                    Warp.update()

                    Warp.privates.remove(jsonObj)
                    Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "del-success"), *pairs))
                    Warp.update()
                } else Util.send(sender, Util.getPluginMsg("Warp", "del-private"))
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
            if (sender is Player) {
                if (Util.getPluginConfig("Warp", "allow-public") as Boolean || sender.isOp) warps.add(name)
                if (tmp[name] == WarpType.PRIVATE && name.startsWith(Util.getUUID(sender))) warps.add(name.split("|")[1])
            }
        }
        return warps
    }
}