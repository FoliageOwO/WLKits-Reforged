package ml.windleaf.wlkitsreforged.plugins.commands.tpa

import ml.windleaf.wlkitsreforged.plugins.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class TpahelpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Tpa.enabled) {
            Util.sendHelp(sender,
            "/tpahelp" to "查看此帮助",
                "/tpa [player]" to "给玩家发送一个传送请求",
                "/tpaccept" to "同意传送请求",
                "/tpadeny" to "拒绝传送请求",
                "/tpacancel" to "取消传送请求")
        } else Util.disabled(sender)
        return true
    }
}