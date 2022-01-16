package ml.windleaf.wlkitsreforged.plugins.commands.warp

import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.plugins.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class WarplistCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Util.needPermission(sender, "warplist", PermissionType.COMMAND)) {
            if (Warp.warpManager.getWarps().size == 0) Util.send(sender, Util.getPluginMsg("Warp", "no-warps"))
            else Util.send(sender, Warp.warpManager.getWarps().keys.toString())
        }
        return true
    }
}