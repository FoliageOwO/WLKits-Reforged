package ml.windleaf.wlkitsreforged.core.reflect.versions

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.ModuleManager
import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import org.bukkit.event.EventHandler
import org.bukkit.event.world.WorldLoadEvent

class V1_12_R1: Reflector {
    override fun getNMS() = "v1_12_R1"

    @EventHandler
    fun loadStartupPlugin(e: WorldLoadEvent) {
        ModuleManager.registerCommands()
        ModuleManager.loadModules(LoadType.ON_STARTUP)
        ModuleManager.loadModules(LoadType.ON_LOAD_WORLD)
        ModuleManager.refreshEnabled()
    }
}