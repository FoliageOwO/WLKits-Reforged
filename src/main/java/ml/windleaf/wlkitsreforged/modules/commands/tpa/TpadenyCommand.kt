package ml.windleaf.wlkitsreforged.modules.commands.tpa

import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandInfo(cmd = "tpadeny", description = "Deny the tpa request", aliases = ["tpad"], belongTo = Tpa::class)
class TpadenyCommand : ModuleCommand {
    @MustPlayer
    @Permission("wlkits.cmd.tpa")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        val player: Player? = Tpa.tpaLogs[sender]
        if (player != null) {
            Tpa.tpaLogs.remove(sender)
            Util.send(sender, Util.getModuleMsg("Tpa", "deny"))
            Util.send(player, Util.getModuleMsg("Tpa", "deny"))
        } else Util.send(sender, Util.getModuleMsg("Tpa", "no-request"))
    }
}