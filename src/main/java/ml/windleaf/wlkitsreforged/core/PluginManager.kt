package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.plugins.*
import ml.windleaf.wlkitsreforged.plugins.httpapi.HttpApi
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.event.server.ServerLoadEvent.LoadType.*
import org.bukkit.event.world.WorldLoadEvent

class PluginManager : Listener {
    companion object {
        val pluginList = arrayListOf(
            AntiCreeper(),
            Back(), BackDeath(),
            Disenchant(),
            Home(), HttpApi(),
            JoinInfo(),
            Mention(),
            PlayerTag(),
            ScheduleNotice(), SkipNight(), Suicide(),
            Tpa(),
            Warp(),
            WLKitsPlugin()
        )
        val loadedPlugins = arrayListOf<Plugin>()
    }

    @EventHandler
    fun onServerLoadEvent(e: ServerLoadEvent) {
        when (e.type) {
            STARTUP -> {
                pluginList.forEach { it.registers() }
                loadStartupPlugins()
                reloadEnabled()
            }
            RELOAD -> reload()
        }
    }

    @EventHandler
    fun onWorldLoadEvent(e: WorldLoadEvent) = loadLoadWorldPlugins()

    private fun loadStartupPlugins() = pluginList.forEach {
        if (it.type == LoadType.ON_STARTUP && it !in loadedPlugins) {
            it.load()
            loadedPlugins.add(it)
        }
    }

    private fun loadLoadWorldPlugins() = pluginList.forEach {
        if (it.type == LoadType.ON_LOAD_WORLD && it !in loadedPlugins) {
            it.load()
            loadedPlugins.add(it)
        }
    }

    fun unloadPlugins() {
        pluginList.forEach { it.unload() }
        loadedPlugins.clear()
    }

    fun reload() {
        WLKits.reload()
        unloadPlugins()
        loadStartupPlugins()
        loadLoadWorldPlugins()
        reloadEnabled()
    }

    private fun reloadEnabled() = pluginList.forEach { it.enabled = Util.isEnabled(it.name) }
}