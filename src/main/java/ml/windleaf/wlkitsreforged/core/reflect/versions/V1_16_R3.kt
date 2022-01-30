package ml.windleaf.wlkitsreforged.core.reflect.versions

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.ModuleManager
import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.event.world.WorldLoadEvent

class V1_16_R3 : Reflector {
    override fun getNMS() = "v1_16_R3"

    companion object {
        fun loadStartupPlugin(e: ServerLoadEvent) {
            when (e.type) {
                ServerLoadEvent.LoadType.STARTUP -> {
                    ModuleManager.registerCommands()
                    ModuleManager.loadModules(LoadType.ON_STARTUP)
                    ModuleManager.refreshEnabled()
                }
                ServerLoadEvent.LoadType.RELOAD -> ModuleManager.reload()
            }
        }
    }

    override fun sendConsole(message: String) = Bukkit.getConsoleSender().sendMessage(Util.translateColorCode(message)!!)

    @EventHandler
    override fun loadWorldPlugin(e: WorldLoadEvent) = ModuleManager.loadModules(LoadType.ON_LOAD_WORLD)

    @EventHandler
    fun loadStartupPlugin(e: ServerLoadEvent) = Companion.loadStartupPlugin(e)
}