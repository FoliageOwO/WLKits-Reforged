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
import org.bukkit.event.entity.PlayerDeathEvent

class BackDeath : Module, Listener, CommandExecutor {
    private var enabled = false
    override fun getName() = "BackDeath"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    private var tpLogs = HashMap<Player, Location>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(name)
    }

    override fun unload() = Unit
    override fun registers() {
        Util.registerEvent(this)
        Util.registerCommand("backdeath", this)
    }

    @EventHandler
    fun event(e: PlayerDeathEvent) {
        if (enabled) tpLogs[e.entity] = e.entity.location
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (enabled) {
            if (Util.mustPlayer(sender) && Util.needPermission(sender, "backdeath", PermissionType.COMMAND)) {
                sender as Player
                val location = tpLogs[sender]
                if (location == null) Util.send(sender, Util.getPluginMsg(name, "fail")) else {
                    sender.teleport(location)
                    Util.send(sender, Util.getPluginMsg(name, "success"))
                }
            }
        } else Util.disabled(sender)
        return false
    }
}