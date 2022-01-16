package ml.windleaf.wlkitsreforged.plugins.commands.home

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Home
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Home.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "home", PermissionType.COMMAND)) {
                val player = sender as Player
                if (Home.homes.containsKey(Util.getUUID(player))) {
                    val locationString: String = Home.homes[Util.getUUID(player)]!!
                    val array = locationString.split("\\s+".toRegex()).toTypedArray()
                    val world = Util.getWorldByName(array[0])
                    val x = array[1].toDouble()
                    val y = array[2].toDouble()
                    val z = array[3].toDouble()
                    if (array.size == 6) {
                        val yaw = array[4].toFloat()
                        val pitch = array[5].toFloat()
                        player.teleport(Location(world, x, y, z, yaw, pitch))
                    } else player.teleport(Location(world, x, y, z))
                    Util.send(player, Util.getPluginMsg("Home", "back-home"))
                } else Util.send(player, Util.getPluginMsg("Home", "no-home"))
            }
        } else Util.disabled(sender)
        return true
    }
}