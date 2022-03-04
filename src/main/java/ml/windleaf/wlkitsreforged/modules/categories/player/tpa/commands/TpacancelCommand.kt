package ml.windleaf.wlkitsreforged.modules.categories.player.tpa.commands

import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.categories.player.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandInfo(cmd = "tpacancel", description = "Cancel the tpa request", belongTo = Tpa::class)
class TpacancelCommand : ModuleCommand {
    @MustPlayer
    @Permission("wlkits.cmd.tpa")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        val toPlayer: Player? = Tpa.tpaLogs[sender]
        val player = getKeyByValue(Tpa.tpaLogs, toPlayer)
        if (toPlayer != null && player != null && player == sender) {
            Tpa.tpaLogs.remove(sender)
            Util.send(sender, Util.getModuleMsg("Tpa", "cancel"))
        } else Util.send(sender, Util.getModuleMsg("Tpa", "no-request"))
    }

    private fun getKeyByValue(map: Map<Player, Player>, value: Player?): Player? {
        for (i in map.keys) if (map[i] == value || Util.getUUID(map[i]!!) == value?.let { Util.getUUID(it) }) return i
        return null
    }
}