package ml.windleaf.wlkitsreforged.plugins.commands.home

import ml.windleaf.wlkitsreforged.plugins.Home
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SethomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Home.enabled) {
            if (Util.mustPlayer(sender)) {
                val player = sender as Player
                if (Home.homes.containsKey(Util.getUUID(player))) Util.send(player, Util.getPluginMsg("Home", "redo"))
                else {
                    val sb = StringBuilder()
                    val location = player.location
                    sb.append(location.world)
                        .append(" ").append(location.x)
                        .append(" ").append(location.y)
                        .append(" ").append(location.z)
                    if (Util.getPluginConfig("Home", "set-more") as Boolean)
                        sb.append(" ").append(location.yaw).append(" ").append(location.pitch)
                    Home.homes[Util.getUUID(player)] = sb.toString()
                    FileUtil.saveHashMap(Home.homes, Home.path)
                    Util.send(player, Util.getPluginMsg("Home", "success"))
                }
            }
        } else Util.disabled(sender)
        return false
    }
}