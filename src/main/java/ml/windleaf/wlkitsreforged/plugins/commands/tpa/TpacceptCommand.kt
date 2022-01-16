package ml.windleaf.wlkitsreforged.plugins.commands.tpa

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpacceptCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Tpa.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "tpa", PermissionType.COMMAND)) {
                val toPlayer: Player? = Tpa.tpaLogs[sender]
                if (toPlayer != null) {
                    toPlayer.teleport((sender as Player?)!!)
                    Tpa.tpaLogs.remove(sender)
                    Util.send(sender, Util.getPluginMsg("Tpa", "accept"))
                    Util.send(toPlayer, Util.getPluginMsg("Tpa", "accept"))
                } else Util.send(sender, Util.getPluginMsg("Tpa", "no-request"))
            }
        } else Util.disabled(sender)
        return true
    }
}