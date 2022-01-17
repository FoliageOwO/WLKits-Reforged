package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class AntiCreeper : Plugin, Listener {
    override val name = "AntiCreeper"
    override val enabled = Util.isEnabled(name)
    override val type = LoadType.ON_STARTUP
    private var notice = Util.getPluginConfig(name, "notice") as Boolean

    override fun load() {
        if (enabled) Util.registerEvent(this)
    }

    override fun unload() {
    }

    @EventHandler
    fun event(e: EntityExplodeEvent) {
        if (enabled && e.entity is Creeper) {
            e.isCancelled = true
            if (notice) {
                val loc = e.location
                WLKits.log("&a成功阻止 &fCreeper &a爆炸破坏地形! 爆炸坐标: &3X:${loc.blockX}&f, &aY:${loc.blockY}&f, &cZ:${loc.blockZ}&f.")
            }
        }
    }
}