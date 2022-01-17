package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.plugins.*

class PluginManager {
    companion object {
        var pluginList: ArrayList<Plugin> = arrayListOf(
            AntiCreeper(),
            Back(), BackDeath(),
            Disenchant(),
            Home(),
            JoinInfo(),
            PlayerTag(),
            ScheduleNotice(), SkipNight(), Suicide(),
            Tpa(),
            Warp(),
            WLKitsPlugin()
        )
    }

    fun loadPlugins() {
        for (plugin in pluginList) plugin.load()
    }

    fun unloadPlugins() {
        for (plugin in pluginList) plugin.unload()
        pluginList.clear()
    }

    fun reload() {
        unloadPlugins()
        loadPlugins()
    }
}