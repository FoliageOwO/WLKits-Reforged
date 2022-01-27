package ml.windleaf.wlkitsreforged.modules.commands.tpa

import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.modules.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpadenyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Tpa.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "tpa", PermissionType.COMMAND)) {
                val player: Player? = Tpa.tpaLogs[sender]
                if (player != null) {
                    Tpa.tpaLogs.remove(sender)
                    Util.send(sender, Util.getPluginMsg("Tpa", "deny"))
                    Util.send(player, Util.getPluginMsg("Tpa", "deny"))
                } else Util.send(sender, Util.getPluginMsg("Tpa", "no-request"))
            }
        } else Util.disabled(sender)
        return true
    }
}