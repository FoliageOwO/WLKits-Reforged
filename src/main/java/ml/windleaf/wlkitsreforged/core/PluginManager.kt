package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.plugins.*
import ml.windleaf.wlkitsreforged.plugins.httpapi.HttpApi
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.event.world.WorldLoadEvent

class PluginManager : Listener {
    companion object {
        lateinit var pluginList: ArrayList<Plugin>
    }

    @EventHandler
    fun onEnable(e: PluginEnableEvent) {
        if (e.plugin == WLKits.instance) {
            pluginList = arrayListOf(
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
            loadStartupPlugins()
        }
    }

    @EventHandler
    fun onDisable(e: PluginDisableEvent) {
        if (e.plugin == WLKits.instance) unloadPlugins()
    }

    @EventHandler
    fun onLoadWorld(e: WorldLoadEvent) = loadLoadWorldPlugins()

    private fun loadStartupPlugins() = pluginList.forEach { if (it.type == LoadType.ON_STARTUP) it.load() }
    private fun loadLoadWorldPlugins() = pluginList.forEach { if (it.type == LoadType.ON_LOAD_WORLD) it.load() }
    private fun unloadPlugins() {
        pluginList.forEach { it.unload() }
        pluginList.clear()
    }

    fun reload() {
        unloadPlugins()
        loadStartupPlugins()
        loadLoadWorldPlugins()
    }
}