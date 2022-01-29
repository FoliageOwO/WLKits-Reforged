package ml.windleaf.wlkitsreforged.core

import com.google.gson.Gson
import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.internal.YamlData
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
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
        val gson = Gson()

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
            debug = Util.getPluginConfig("main", "debug") as Boolean
        }
    }

    override fun onEnable() {
        val startTime = System.currentTimeMillis()
        instance = this

        reflector = Util.getReflector()
        debug = Util.getPluginConfig("main", "debug") as Boolean
        debug("Data folder: ${FileUtil.path}")

        FileUtil.saveResource("config.yml")
        FileUtil.saveResource("message.yml")
        FileUtil.saveResource("warps.json")
        FileUtil.saveResource("macro_authorized.json")
        message = YamlData("message")
        reload()

        debug("All modules: ${Util.getClassesWithSuperclass<Module>("ml.windleaf.wlkitsreforged.modules")}")
        debug("Offline players: ${Bukkit.getOfflinePlayers().toList().stream().map { "[${it.name}|${it.uniqueId}]" }.collect(Collectors.joining(", "))}")
        PluginManager()

        Util.registerEvent(reflector)
        log("Loading &aWLKits-Reforged &fv$version&r...")

        val endTime = System.currentTimeMillis()
        log("Successfully loaded in &e${endTime - startTime}ms&r!")
    }

    override fun onDisable() = PluginManager.unloadPlugins()
}
