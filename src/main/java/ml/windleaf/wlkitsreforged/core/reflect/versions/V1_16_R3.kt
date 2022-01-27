package ml.windleaf.wlkitsreforged.core.reflect.versions

import ml.windleaf.wlkitsreforged.core.PluginManager
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
                    PluginManager.loadStartupPlugins()
                    PluginManager.reloadEnabled()
                }
                ServerLoadEvent.LoadType.RELOAD -> PluginManager.reload()
            }
        }
    }

    override fun sendConsole(message: String) = Bukkit.getConsoleSender().sendMessage(Util.translateColorCode(message)!!)

    @EventHandler
    override fun loadWorldPlugin(e: WorldLoadEvent) = PluginManager.loadLoadWorldPlugins()

    @EventHandler
    fun loadStartupPlugin(e: ServerLoadEvent) = Companion.loadStartupPlugin(e)
}