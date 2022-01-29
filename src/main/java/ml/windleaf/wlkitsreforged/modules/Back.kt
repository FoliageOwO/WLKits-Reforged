package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class Back : Module, Listener, CommandExecutor {
    private var enabled = false
    override fun getName() = "Back"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    private var tpLogs = HashMap<Player, Location>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
    }

    override fun unload() = Unit
    override fun registers() {
        Util.registerEvent(this)
        Util.registerCommand("back", this)
    }

    @EventHandler
    fun onPlayerTeleportEvent(e: PlayerTeleportEvent) {
        if (enabled) tpLogs[e.player] = e.from
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "back", PermissionType.COMMAND)) {
                sender as Player
                if (tpLogs.containsKey(sender)) {
                    sender.teleport(tpLogs[sender]!!)
                    Util.send(sender, Util.getPluginMsg(getName(), "success"))
                } else Util.send(sender, Util.getPluginMsg(getName(), "fail"))
            }
        } else Util.disabled(sender)
        return false
    }
}