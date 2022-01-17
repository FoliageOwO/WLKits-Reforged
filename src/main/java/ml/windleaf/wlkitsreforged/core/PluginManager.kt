package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.plugins.*

class PluginManager {
    companion object {
        var pluginList: ArrayList<Plugin> = ArrayList()
    }

    fun loadPlugins() {
        pluginList.add(AntiCreeper())
        pluginList.add(BackDeath())
        pluginList.add(Disenchant())
        pluginList.add(JoinInfo())
        pluginList.add(Back())
        pluginList.add(Home())
        pluginList.add(Suicide())
        pluginList.add(SkipNight())
        pluginList.add(Tpa())
        pluginList.add(Warp())
        pluginList.add(PlayerTag())
        pluginList.add(ScheduleNotice())
        pluginList.add(WLKitsPlugin())
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