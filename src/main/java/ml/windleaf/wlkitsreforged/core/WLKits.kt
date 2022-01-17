package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class WLKits : JavaPlugin() {
    companion object {
        const val name = "WLKits"
        const val version = "0.2"
        val pluginManager = PluginManager()
        val prefixPath = Util.getPath() + "plugins" + File.separator + "WLKitsReforged" + File.separator
        lateinit var instance: WLKits
        val message: FileConfiguration = YamlConfiguration()

        fun saveResource(name: String) {
            if (!File(prefixPath + name).exists()) instance.saveResource(name, false)
        }

        fun log(s: String) = println(Util.translateColorCode(Util.withPrefix() + s))
        fun reload() {
            instance.reloadConfig()
            message.load(File(prefixPath + "message.yml"))
        }
    }

    override fun onEnable() {
        val startTime = System.currentTimeMillis()
        instance = this
        log("正在加载 &aWLKits-Reforged &f[v$version]&r...")

        Companion.saveResource("config.yml")
        Companion.saveResource("message.yml")
        Companion.saveResource("warps.yml")
        reload()

        pluginManager.loadPlugins()
        log("&f已加载子插件, 共 ${PluginManager.pluginList.size} 个...")

        val endTime = System.currentTimeMillis()
        log("加载完毕, 用时 &e${endTime - startTime}ms&r!")
    }

    override fun onDisable() = pluginManager.unloadPlugins()
}
