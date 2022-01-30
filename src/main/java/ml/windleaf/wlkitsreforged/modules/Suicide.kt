package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.core.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

@CommandInfo(cmd = "suicide", description = "Suicide", belongTo = Suicide::class)
@ModuleInfo(description = "Suicide", type = LoadType.ON_STARTUP)
class Suicide : Module, Listener, ModuleCommand {
    private var enabled = false
    override fun getEnabled() = enabled
    private var suicideList = ArrayList<Player>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
    }

    @EventHandler
    fun event(e: PlayerDeathEvent) {
        if (enabled && Util.hasPermission(e.entity, Permission("wlkits.cmd.suicide"))) {
            val player = e.entity
            if (player in suicideList) {
                if (!(Util.getModuleConfig(getName(), "use-vanilla") as Boolean)) {
                    e.deathMessage = ""
                    Util.broadcastPlayers(Util.insert(Util.getModuleMsg(getName(), "msg"), "playerName" to player.name))
                }
                suicideList.remove(player)
            }
        }
    }

    @MustPlayer
    @Permission("wlkits.cmd.suicide")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        sender as Player
        suicideList.add(sender)
        sender.damage(sender.health)
    }
}