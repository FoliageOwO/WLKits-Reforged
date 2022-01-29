package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.scheduler.BukkitRunnable

class ScheduleNotice : Module {
    private var enabled = false
    override fun getName() = "ScheduleNotice"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_LOAD_WORLD
    private lateinit var runnable: BukkitRunnable

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        if (enabled) {
            val interval = Util.getPluginConfig(getName(), "interval") as Int
            val lines = Util.getPluginMsgAs(getName(), "notice-lines") as List<*>
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

    override fun registers() = Unit
}