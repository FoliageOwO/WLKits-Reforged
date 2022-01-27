package ml.windleaf.wlkitsreforged.modules.commands.warp

import com.alibaba.fastjson.JSONObject
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.modules.Warp
import ml.windleaf.wlkitsreforged.modules.enums.WarpType
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException

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
                        val types = HashMap<String, WarpType>()
                        for (t: WarpType in WarpType.values()) types[t.string] = t
                        val n = "name" to name
                        val t = "type" to type.toString()
                        if (type !in types.keys) Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "unknown-type"), n, t))
                        else {
                            type = types[type]!!
                            when (type) {
                                WarpType.PUBLIC -> {
                                    if (sender.isOp) set(sender, name, type, n, t)
                                    else if (!sender.isOp && Util.getPluginConfig("Warp", "allow-public") as Boolean) {
                                        set(sender, name, type, n, t)
                                    } else Util.send(sender, Util.getPluginMsg("Warp", "cannot-public"))
                                }
                                WarpType.PRIVATE -> set(sender, name, type, n, t)
                            }
                        }
                    }
                }
            }
        } else Util.disabled(sender)
        return true
    }

    private fun set(sender: Player, name: String, type: WarpType, vararg pairs: Pair<String, String>) {
        val uuid = Util.getUUID(sender)
        if (Warp.existsWarp(uuid, name, type)) Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "already-exists"), *pairs))
        else {
            Warp.list.add(if (type == WarpType.PRIVATE) "$uuid|$name" else name)
            Warp.update()

            val loc = sender.location
            val map = hashMapOf(
                "name" to name,
                "x" to loc.x,
                "y" to loc.y,
                "z" to loc.z,
                "world" to loc.world?.name,
            ) as HashMap<String, Any>
            if (Util.getPluginConfig("Warp", "set-more") as Boolean) {
                map["yaw"] = loc.yaw
                map["pitch"] = loc.pitch
            }

            when (type) {
                WarpType.PRIVATE -> {
                    map["owner"] = uuid
                    Warp.privates.fluentAdd(JSONObject(map))
                }
                WarpType.PUBLIC -> {
                    Warp.publics.fluentAdd(JSONObject(map))
                }
            }

            try {
                Warp.update()
                Util.send(sender, Util.insert(Util.getPluginMsg("Warp", "success"), *pairs))
                if (Util.getPluginConfig("Warp", "broadcast") as Boolean) {
                    val p = "playerName" to sender.displayName
                    val n = "name" to name
                    if (type == WarpType.PUBLIC)
                        for (line in Util.getPluginMsgAs("Warp", "broadcast-lines") as List<*>) Util.broadcastPlayers(Util.insert(line as String, p, n))
                }
            } catch (e: IOException) {
                Util.send(sender, Util.getPluginMsg("Warp", "fail"))
                e.printStackTrace()
            }
        }
    }
}