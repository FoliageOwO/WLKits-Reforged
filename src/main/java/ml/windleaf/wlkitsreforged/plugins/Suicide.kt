package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class Suicide : Plugin, Listener, CommandExecutor {
    override val name = "Suicide"
    override val enabled = Util.isEnabled(name)
    override val type = LoadType.ON_STARTUP
    private var suicideList = ArrayList<Player>()

    override fun load() {
        Util.registerEvent(this)
        Util.registerCommand("suicide", this)
    }

    override fun unload() {
    }

    @EventHandler
    fun event(e: PlayerDeathEvent) {
        if (enabled && Util.hasPermission(e.entity, "suicide", PermissionType.ACTION)) {
            val player = e.entity
            if (player in suicideList) {
                if (!(Util.getPluginConfig(name, "use-vanilla") as Boolean)) {
                    e.deathMessage = ""
                    Util.broadcastPlayers(Util.insert(Util.getPluginMsg(name, "msg"), "playerName" to player.name))
                }
                suicideList.remove(player)
            }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (enabled && Util.mustPlayer(sender) && Util.needPermission(sender, "suicide", PermissionType.COMMAND)) {
            sender as Player
            suicideList.add(sender)
            sender.health = 0.0
        } else Util.disabled(sender)
        return true
    }
}