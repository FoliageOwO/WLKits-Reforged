package ml.windleaf.wlkitsreforged.modules.commands.home

import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.modules.Home
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SethomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Home.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "home", PermissionType.COMMAND)) {
                val player = sender as Player
                if (Home.homes.contains(Util.getUUID(player)!!)) Util.send(player, Util.getPluginMsg("Home", "redo"))
                else {
                    val sb = StringBuilder()
                    val location = player.location
                    sb.append(location.world?.name)
                        .append(" ").append(location.x)
                        .append(" ").append(location.y)
                        .append(" ").append(location.z)
                    if (Util.getPluginConfig("Home", "set-more") as Boolean)
                        sb.append(" ").append(location.yaw).append(" ").append(location.pitch)
                    Home.homes[Util.getUUID(player)!!] = sb.toString()
                    Home.homes.saveData()
                    Util.send(player, Util.getPluginMsg("Home", "success"))
                }
            }
        } else Util.disabled(sender)
        return false
    }
}