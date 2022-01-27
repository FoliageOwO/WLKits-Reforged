package ml.windleaf.wlkitsreforged.modules.commands.warp

import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.modules.Warp
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class WarplistCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (Util.needPermission(sender, "warplist", PermissionType.COMMAND)) {
            if (Warp.getWarps().size == 0) Util.send(sender, Util.getPluginMsg("Warp", "no-warps"))
            else Util.send(sender, Warp.getWarps().keys.toString())
        }
        return true
    }
}