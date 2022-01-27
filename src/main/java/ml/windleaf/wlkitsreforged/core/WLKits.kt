package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.stream.Collectors

class WLKits : JavaPlugin() {
    companion object {
        const val name = "WLKits"
        const val version = "0.2.2"
        val prefixPath = Util.getPath() + "plugins" + File.separator + "WLKitsReforged" + File.separator
        lateinit var instance: WLKits
        lateinit var reflector: Reflector
        var debug = false
        val message: FileConfiguration = YamlConfiguration()

        fun saveResource(name: String) {
            if (!File(prefixPath + name).exists()) instance.saveResource(name, false)
        }

        fun log(any: Any) = log(null, any)

        private fun log(ref: Reflector? = null, any: Any) {
            val r = ref ?: reflector
            r.sendConsole("${Util.withPrefix()}$any")
        }

        fun debug(ref: Reflector? = null, vararg any: Any?) {
            val str = any.asList().stream().map { it.toString() }.collect(Collectors.joining(" "))
            val prefix = "&c&l[DEBUG]&r "
            if (debug) log(ref, "$prefix$str")
        }

        fun reload() {
            instance.reloadConfig()
            message.load(File(prefixPath + "message.yml"))
        }
    }

    override fun onEnable() {
        val startTime = System.currentTimeMillis()
        instance = this
        PluginManager()
        debug = Util.getPluginConfig("main", "debug") as Boolean
        reflector = Util.getReflector()
        Util.registerEvent(reflector)
        log("Loading &aWLKits-Reforged &fv$version&r...")

        Companion.saveResource("config.yml")
        Companion.saveResource("message.yml")
        Companion.saveResource("warps.yml")
        reload()

        val endTime = System.currentTimeMillis()
        log("Successfully loaded in &e${endTime - startTime}ms&r!")
    }

    override fun onDisable() = PluginManager.unloadPlugins()
}
