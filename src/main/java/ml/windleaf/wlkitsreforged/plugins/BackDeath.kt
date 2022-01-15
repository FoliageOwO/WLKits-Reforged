package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class BackDeath : Plugin, Listener, CommandExecutor {
    override var name = "BackDeath"
    private var tpLogs = HashMap<Player, Location>()
    private var enabled = Util.isEnabled(name)

    override fun load() {
        Util.registerEvent(this)
        Util.registerCommand("backdeath", this)
    }

    override fun unload() {
    }

    @EventHandler
    fun event(e: PlayerDeathEvent) {
        if (enabled) tpLogs[e.entity] = e.entity.location
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (enabled) {
            if (Util.mustPlayer(sender)) {
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