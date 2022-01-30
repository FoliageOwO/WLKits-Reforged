package ml.windleaf.wlkitsreforged.modules.commands.warp

import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

@CommandInfo(cmd = "warplist", description = "Show the list of warps", belongTo = Warp::class)
class WarplistCommand : ModuleCommand {
    @Permission("wlkits.cmd.warplist")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (Warp.getWarps().size == 0) Util.send(sender, Util.getPluginMsg("Warp", "no-warps"))
        else Util.send(sender, Warp.getWarps().keys.toString())
    }
}