package ml.windleaf.wlkitsreforged.modules.categories.player.warp.commands

import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.categories.player.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender

@CommandInfo(cmd = "warphelp", description = "Show the help", belongTo = Warp::class)
class WarphelpCommand : ModuleCommand {
    @Permission("wlkits.cmd.warp")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        Util.sendHelp(sender,
        "/warphelp" to "查看此帮助",
            "/setwarp [private/public] [name]" to "设置地标点",
            "/delwarp [name]" to "删除地标点",
            "/warp [name]" to "传送至地标点",
            "/warplist" to "查看所有可用的地标点")
    }
}