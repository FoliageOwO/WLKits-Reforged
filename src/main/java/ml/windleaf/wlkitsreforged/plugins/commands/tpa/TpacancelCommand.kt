package ml.windleaf.wlkitsreforged.plugins.commands.tpa

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpacancelCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Tpa.enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "tpa", PermissionType.COMMAND)) {
                val toPlayer: Player? = Tpa.tpaLogs[sender]
                val player = getKeyByValue(Tpa.tpaLogs, toPlayer)
                if (toPlayer != null && player != null && player == sender) {
                    Tpa.tpaLogs.remove(sender)
                    Util.send(sender, Util.getPluginMsg("Tpa", "cancel"))
                } else Util.send(sender, Util.getPluginMsg("Tpa", "no-request"))
            }
        } else Util.disabled(sender)
        return true
    }

    private fun getKeyByValue(map: Map<Player, Player>, value: Player?): Player? {
        for (i in map.keys) if (map[i] == value || Util.getUUID(map[i]!!) == value?.let { Util.getUUID(it) }) return i
        return null
    }
}