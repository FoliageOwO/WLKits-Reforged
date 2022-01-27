package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.data.YamlData
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.plugin.java.JavaPlugin
import java.util.stream.Collectors

class WLKits : JavaPlugin() {
    companion object {
        const val name = "WLKits"
        const val version = "0.2.2"
        lateinit var instance: WLKits
        lateinit var reflector: Reflector
        lateinit var message: YamlData
        var debug = false

        fun log(any: Any) = log(null, any)

        private fun log(ref: Reflector? = null, any: Any) {
            val r = ref ?: reflector
            r.sendConsole("${Util.withPrefix()}$any")
        }

        fun debug(vararg any: Any?) = debug(null, *any)
        fun debug(ref: Reflector?, vararg any: Any?) {
            val str = any.asList().stream().map { it.toString() }.collect(Collectors.joining(" "))
            val prefix = "&c&l[DEBUG]&r "
            if (debug) log(ref, "$prefix$str")
        }

        fun reload() {
            instance.reloadConfig()
            message.loadDataFromFile()
        }
    }

    override fun onEnable() {
        val startTime = System.currentTimeMillis()
        instance = this
        debug("Data folder: ${FileUtil.path}")
        debug = Util.getPluginConfig("main", "debug") as Boolean
        reflector = Util.getReflector()
        message = YamlData("message")
        PluginManager()
        Util.registerEvent(reflector)
        log("Loading &aWLKits-Reforged &fv$version&r...")

        FileUtil.saveResource("config.yml")
        FileUtil.saveResource("message.yml")
        reload()

        val endTime = System.currentTimeMillis()
        log("Successfully loaded in &e${endTime - startTime}ms&r!")
    }

    override fun onDisable() = PluginManager.unloadPlugins()
}
