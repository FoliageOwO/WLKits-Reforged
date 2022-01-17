package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.scheduler.BukkitRunnable

class ScheduleNotice : Plugin {
    override val name = "ScheduleNotice"
    override val enabled = Util.isEnabled(name)
    private lateinit var runnable: BukkitRunnable

    override fun load() {
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
}