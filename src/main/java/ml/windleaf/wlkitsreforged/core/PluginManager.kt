package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.modules.*
import ml.windleaf.wlkitsreforged.modules.HttpApi
import ml.windleaf.wlkitsreforged.utils.Util

class PluginManager {
    companion object {
        val pluginList = arrayListOf(
            AntiCreeper(),
            Back(), BackDeath(),
            Disenchant(),
            Home(), HttpApi(),
            JoinInfo(),
            Macro(), Mention(),
            PlayerTag(),
            ScheduleNotice(), SkipNight(), Suicide(),
            Tpa(),
            Warp(),
            WLKitsPlugin()
        )
        private val loadedPlugins = arrayListOf<Module>()

        fun loadStartupPlugins(registers: Boolean = true) = pluginList.forEach {
            if (it.getType() == LoadType.ON_STARTUP && it !in loadedPlugins) {
                reloadEnabled()
                if (it.getEnabled()) {
                    if (registers) it.registers()
                    it.load()
                    loadedPlugins.add(it)
                }
            }
        }

        fun loadLoadWorldPlugins(registers: Boolean = true) = pluginList.forEach {
            if (it.getType() == LoadType.ON_LOAD_WORLD && it !in loadedPlugins) {
                reloadEnabled()
                if (it.getEnabled()) {
                    if (registers) it.registers()
                    it.load()
                    loadedPlugins.add(it)
                }
            }
        }

        fun reloadEnabled() = pluginList.forEach { it.setEnabled(Util.isEnabled(it.getName())) }

        fun reload() {
            WLKits.reload()
            unloadPlugins()
            loadStartupPlugins(false)
            loadLoadWorldPlugins(false)
            reloadEnabled()
        }

        fun unloadPlugins() {
            pluginList.forEach { it.unload() }
            loadedPlugins.clear()
        }
    }
}