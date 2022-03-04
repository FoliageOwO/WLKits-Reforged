package ml.windleaf.wlkitsreforged.modules.categories.player

import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.modules.ModuleInfo
import ml.windleaf.wlkitsreforged.core.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.modules.Module
import ml.windleaf.wlkitsreforged.modules.categories.Category
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

@CommandInfo(cmd = "backdeath", description = "Back to last death location", aliases = ["backd", "bd"], belongTo = BackDeath::class)
@ModuleInfo(category = Category.PLAYER, description = "Back to last death location", type = LoadType.ON_STARTUP)
class BackDeath : Module, Listener, ModuleCommand {
    private var enabled = false
    override fun getEnabled() = enabled
    private var tpLogs = HashMap<Player, Location>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
    }

    @EventHandler
    fun event(e: PlayerDeathEvent) {
        if (enabled) tpLogs[e.entity] = e.entity.location
    }

    @MustPlayer
    @Permission("wlkits.cmd.backdeath")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        sender as Player
        val location = tpLogs[sender]
        if (location == null) Util.send(sender, Util.getModuleMsg(getName(), "fail")) else {
            sender.teleport(location)
            Util.send(sender, Util.getModuleMsg(getName(), "success"))
        }
    }
}