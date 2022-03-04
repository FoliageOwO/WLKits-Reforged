package ml.windleaf.wlkitsreforged.modules.categories.player.warp.commands

import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.modules.annotations.Permission
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.categories.player.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender

@CommandInfo(cmd = "warplist", description = "Show the list of warps", belongTo = Warp::class)
class WarplistCommand : ModuleCommand {
    @Permission("wlkits.cmd.warplist")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (Warp.getWarps().size == 0) Util.send(sender, Util.getModuleMsg("Warp", "no-warps"))
        else Util.send(sender, Warp.getWarps().keys.toString())
    }
}