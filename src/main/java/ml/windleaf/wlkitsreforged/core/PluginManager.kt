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
            Mention(),
            PlayerTag(),
            ScheduleNotice(), SkipNight(), Suicide(),
            Tpa(),
            Warp(),
            WLKitsPlugin()
        )
        private val loadedPlugins = arrayListOf<Module>()

        fun loadStartupPlugins(registers: Boolean = true) = pluginList.forEach {
            if (it.type == LoadType.ON_STARTUP && it !in loadedPlugins) {
                if (registers) it.registers()
                it.load()
                loadedPlugins.add(it)
            }
        }

        fun loadLoadWorldPlugins(registers: Boolean = true) = pluginList.forEach {
            if (it.type == LoadType.ON_LOAD_WORLD && it !in loadedPlugins) {
                if (registers) it.registers()
                it.load()
                loadedPlugins.add(it)
            }
        }

        fun reloadEnabled() = pluginList.forEach { it.enabled = Util.isEnabled(it.name) }

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