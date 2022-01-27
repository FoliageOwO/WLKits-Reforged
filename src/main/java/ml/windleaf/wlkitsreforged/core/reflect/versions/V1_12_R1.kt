package ml.windleaf.wlkitsreforged.core.reflect.versions

import ml.windleaf.wlkitsreforged.core.PluginManager
import ml.windleaf.wlkitsreforged.core.enums.Versions
import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldLoadEvent

class V1_12_R1: Reflector {
    override fun getNMS() = "v1_12_R1"

    override fun sendConsole(message: String) = Versions.V1_16_R3.reflector.sendConsole(message)

    @EventHandler
    override fun loadWorldPlugin(e: WorldLoadEvent) = Versions.V1_16_R3.reflector.loadWorldPlugin(e)

    @EventHandler
    fun loadStartupPlugin(e: WorldLoadEvent) {
        PluginManager.loadStartupPlugins()
        PluginManager.reloadEnabled()
    }
}