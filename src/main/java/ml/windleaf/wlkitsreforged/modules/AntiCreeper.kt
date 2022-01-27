package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.entity.Creeper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import kotlin.properties.Delegates

class AntiCreeper : Module, Listener {
    private var enabled = false
    override fun getName() = "AntiCreeper"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    private var notice by Delegates.notNull<Boolean>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(name)
        notice = Util.getPluginConfig(name, "notice") as Boolean
    }

    override fun unload() = Unit
    override fun registers() = Util.registerEvent(this)

    @EventHandler
    fun event(e: EntityExplodeEvent) {
        if (enabled && e.entity is Creeper) {
            e.isCancelled = true
            if (notice) {
                val loc = e.location
                WLKits.log("&aCreeper exploded at &3X:${loc.blockX}&f, &aY:${loc.blockY}&f, &cZ:${loc.blockZ}&f.")
            }
        }
    }
}