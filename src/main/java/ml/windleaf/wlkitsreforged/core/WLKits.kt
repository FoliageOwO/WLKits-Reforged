package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.modules.ModuleManager
import ml.windleaf.wlkitsreforged.modules.commanding.Commanding
import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.core.saving.YamlData
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.plugin.java.JavaPlugin
import java.util.stream.Collectors

/**
 * The WLKits main class
 */
class WLKits : JavaPlugin() {
    companion object {
        /**
         * The name of WLKits
         */
        const val name = "WLKits"

        /**
         * The version of WLKits
         */
        const val version = "0.2.2"

        /**
         * The instance of WLKits main class, will be initialized in [onEnable]
         */
        lateinit var instance: WLKits

        /**
         * The instance of [Reflector], get by [Util.getReflector]
         *
         * @see Reflector
         */
        lateinit var reflector: Reflector

        /**
         * The instance of [YamlData], saving `message.yml` in resources folder
         */
        lateinit var message: YamlData

        /**
         * The boolean of whether the WLKits is running in debug mode,
         * in the debug mode, the plugin will print out all the debug information
         */
        var debug = false

        /**
         * The instance of [Commanding], which used to process commands and tab completers
         */
        val commanding = Commanding()

        /**
         * TRUE for status
         */
        lateinit var TRUE: String

        /**
         * FALSE for status
         */
        lateinit var FALSE: String

        /**
         * Prints out the message without [Reflector]
         */
        fun log(any: Any) = log(null, any)

        /**
         * Prints out the message with [Reflector]
         */
        private fun log(ref: Reflector? = null, any: Any) {
            val r = ref ?: reflector
            r.sendConsole("${Util.withPrefix()}$any")
        }

        /**
         * Prints out the debug message without [Reflector]
         */
        fun debug(vararg any: Any?) = debug(null, *any)

        /**
         * Prints out the debug message with [Reflector]
         */
        fun debug(ref: Reflector?, vararg any: Any?) {
            val str = any.asList().stream().map { it.toString() }.collect(Collectors.joining(" "))
            val prefix = "&c&l[DEBUG]&r "
            if (debug) log(ref, "$prefix$str")
        }

        /**
         * Reloads WLKits, reloads commands, reload modules, reload all configs
         */
        fun reload() {
            instance.reloadConfig()
            message.loadDataFromFile()
            debug = Util.getModuleConfig("main", "debug") as Boolean
        }
    }

    override fun onEnable() {
        // Loads the reflector
        val startTime = System.currentTimeMillis()
        instance = this
        reflector = Util.getReflector()
        debug = Util.getModuleConfig("main", "debug") as Boolean

        // Debugs the data folder
        debug("Data folder: ${FileUtil.path}")

        // Starts to save all resource files
        FileUtil.saveResource("config.yml")
        FileUtil.saveResource("message.yml")
        FileUtil.saveResource("warps.json")
        FileUtil.saveResource("macro_authorized.json")
        message = YamlData("message")
        reload()

        // Loads status
        TRUE = Util.getModuleMsg("main", "true")
        FALSE = Util.getModuleMsg("main", "false")

        // Loads all modules
        ModuleManager.init()
        debug("All modules: ${ModuleManager.moduleInstances}")
        debug("All commands: ${ModuleManager.commandInstances}")

        // Registers the reflector
        Util.registerEvent(reflector)

        // Outputs
        log("Loading &aWLKits-Reforged &fv$version&r...")
        val endTime = System.currentTimeMillis()
        log("Successfully loaded in &e${endTime - startTime}ms&r!")
    }

    override fun onDisable() = ModuleManager.unloadModules()
}
