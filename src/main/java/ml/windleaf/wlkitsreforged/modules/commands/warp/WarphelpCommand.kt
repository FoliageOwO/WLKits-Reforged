package ml.windleaf.wlkitsreforged.modules.commands.warp

import ml.windleaf.wlkitsreforged.modules.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class WarphelpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Warp.enabled) {
            Util.sendHelp(sender,
            "/warphelp" to "查看此帮助",
                "/setwarp [private/public] [name]" to "设置地标点",
                "/delwarp [name]" to "删除地标点",
                "/warp [name]" to "传送至地标点",
                "/warplist" to "查看所有可用的地标点")
        } else Util.disabled(sender)
        return true
    }
}