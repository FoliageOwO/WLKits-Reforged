package ml.windleaf.wlkitsreforged.modules.categories.player.tpa.commands

import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.modules.annotations.Permission
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.categories.player.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender

@CommandInfo(cmd = "tpahelp", description = "Show the help", belongTo = Tpa::class)
class TpahelpCommand : ModuleCommand {
    @Permission("wlkits.cmd.tpa")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        Util.sendHelp(sender,
        "/tpahelp" to "查看此帮助",
            "/tpa [player]" to "给玩家发送一个传送请求",
            "/tpaccept" to "同意传送请求",
            "/tpadeny" to "拒绝传送请求",
            "/tpacancel" to "取消传送请求")
    }
}