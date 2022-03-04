package ml.windleaf.wlkitsreforged.modules.categories.player.tpa.commands

import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.categories.player.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandInfo(cmd = "tpaccept", description = "Accept the tpa request", aliases = ["tpac"], belongTo = Tpa::class)
class TpacceptCommand : ModuleCommand {
    @MustPlayer
    @Permission("wlkits.cmd.tpa")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        val toPlayer: Player? = Tpa.tpaLogs[sender]
        if (toPlayer != null) {
            toPlayer.teleport((sender as Player?)!!)
            Tpa.tpaLogs.remove(sender)
            Util.send(sender, Util.getModuleMsg("Tpa", "accept"))
            Util.send(toPlayer, Util.getModuleMsg("Tpa", "accept"))
        } else Util.send(sender, Util.getModuleMsg("Tpa", "no-request"))
    }
}