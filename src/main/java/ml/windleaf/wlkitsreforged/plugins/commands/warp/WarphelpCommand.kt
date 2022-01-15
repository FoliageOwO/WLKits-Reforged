package ml.windleaf.wlkitsreforged.plugins.commands.warp

import ml.windleaf.wlkitsreforged.plugins.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class WarphelpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Warp.enabled) {
            val helps: MutableMap<String, String> = HashMap()
            helps["/warphelp"] = "查看此帮助"
            helps["/setwarp [name]"] = "设置地标点"
            helps["/delwarp [name]"] = "删除地标点"
            helps["/warp [name]"] = "传送至地标点"
            helps["/warplist"] = "查看所有可用的地标点"
            Util.sendHelp(sender, helps)
        } else Util.disabled(sender)
        return true
    }
}