package ml.windleaf.wlkitsreforged.plugins.commands.tpa

import ml.windleaf.wlkitsreforged.plugins.Tpa
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class TpahelpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (Tpa.enabled) {
            val helps: MutableMap<String, String> = HashMap()
            helps["/tpahelp"] = "查看此帮助"
            helps["/tpa [player]"] = "给玩家发送一个传送请求"
            helps["/tpaccept"] = "同意传送请求"
            helps["/tpadeny"] = "拒绝传送请求"
            helps["/tpacancel"] = "取消传送请求"
            Util.sendHelp(sender, helps)
        } else Util.disabled(sender)
        return true
    }
}