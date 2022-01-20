package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.scheduler.BukkitRunnable

class ScheduleNotice : Plugin {
    override val name = "ScheduleNotice"
    override var enabled = false
    override val type = LoadType.ON_LOAD_WORLD
    private lateinit var runnable: BukkitRunnable

    override fun load() {
        enabled = Util.isEnabled(name)
        if (enabled) {
            val interval = Util.getPluginConfig(name, "interval") as Int
            val lines = Util.getPluginMsgAs(name, "notice-lines") as List<*>
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