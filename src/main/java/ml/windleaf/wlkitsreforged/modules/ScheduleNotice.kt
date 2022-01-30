package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.scheduler.BukkitRunnable

@ModuleInfo(description = "Broadcasts all players notice messages in schedule", type = LoadType.ON_LOAD_WORLD)
class ScheduleNotice : Module {
    private var enabled = false
    override fun getEnabled() = enabled
    private lateinit var runnable: BukkitRunnable

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        if (enabled) {
            val interval = Util.getModuleConfig(getName(), "interval") as Int
            val lines = Util.getModuleMsgAs(getName(), "notice-lines") as List<*>
            val ticks = (interval * 20).toLong()
            runnable = object : BukkitRunnable() {
                override fun run() {
                    for (line in lines) Util.broadcastPlayers(line as String)
                }
            }
            runnable.runTaskTimer(WLKits.instance, ticks, ticks)
        }
    }

    override fun unload() {
        if (enabled) runnable.cancel()
    }
}