package ml.windleaf.wlkitsreforged.plugins.commands.home

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Home
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DelhomeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Home.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "home", PermissionType.COMMAND)) {
                val player = sender as Player
                if (Home.homes.containsKey(Util.getUUID(player))) {
                    Home.homes.remove(Util.getUUID(player))
                    FileUtil.saveHashMap(Home.homes, Home.path)
                    Util.send(player, Util.getPluginMsg("Home", "delete"))
                } else Util.send(player, Util.getPluginMsg("Home", "no-home"))
            }
        } else Util.disabled(sender)
        return true
    }
}