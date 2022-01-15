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

        for (plugin in pluginList) {
            WLKits.log("&f加载子插件 &3${plugin.name}&f...")
            plugin.load()
        }
    }

    fun unloadPlugins() {
        for (plugin in pluginList) {
            WLKits.log("&f卸载子插件 &3${plugin.name}&f...")
            plugin.unload()
        }
    }

    fun getPlugins() = pluginList
}