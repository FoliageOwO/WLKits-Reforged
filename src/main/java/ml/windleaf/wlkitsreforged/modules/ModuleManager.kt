package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.modules.commanding.Commanding
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleTabCompleter
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.event.Listener
import kotlin.reflect.KClass

/**
 * The ModuleManager object, used to manage modules
 */
object ModuleManager {
    /**
     * The package for searching modules, commands, and tab completers
     */
    private const val PACKAGE = "ml.windleaf.wlkitsreforged.modules"

    /**
     * The list of module classes [Module]
     *
     * @see Util.getClassesWithSuperclass
     */
    val modules = Util.getClassesWithSuperclass<Module>(PACKAGE)

    /**
     * The list of command classes [ModuleCommand]
     *
     * @see Util.getClassesWithSuperclass
     */
    val commands = Util.getClassesWithSuperclass<ModuleCommand>(PACKAGE)

    /**
     * The list of tab completer classes [ModuleTabCompleter]
     *
     * @see Util.getClassesWithSuperclass
     */
    private val tabCompleters = Util.getClassesWithSuperclass<ModuleTabCompleter>(PACKAGE)

    /**
     * The list of all module instances
     */
    val moduleInstances = arrayListOf<Module>()

    /**
     * The list of all command instances
     */
    val commandInstances = arrayListOf<ModuleCommand>()

    /**
     * The list of all tab completer instances
     */
    val tabCompleterInstances = arrayListOf<ModuleTabCompleter>()

    /**
     * The list of all loaded modules instances
     */
    private val loadedModules = arrayListOf<Module>()

    /**
     * The list of all loaded command instances
     */
    private val loadedCommands = arrayListOf<ModuleCommand>()

    /**
     * The list of all modules which has an error when loading it
     */
    val errorModules = arrayListOf<Module>()

    /**
     * Initialize all modules instances
     */
    fun init() {
        modules.forEach { moduleInstances.add(it.newInstance()) }
        commands.forEach { commandInstances.add(it.newInstance()) }
        tabCompleters.forEach { tabCompleterInstances.add(it.newInstance()) }
    }

    /**
     * Loads all modules by load type
     *
     * @param type the load type of module
     * @param registers if the module need to register events and commands, defaults to true
     *
     * @see LoadType
     */
    fun loadModules(type: LoadType, registers: Boolean = true) {
        moduleInstances.forEach {
            if (it.getType() == type && it !in loadedModules) {
                Util.catch(Exception::class.java,
                    {
                        refreshEnabled()
                        WLKits.debug("loading module: ${it.getName()} [enabled=${it.getEnabled()},isListener=${it is Listener}]")
                        if (it.getEnabled()) {
                            if (it is Listener && registers) Util.registerEvent(it)
                            it.load()
                            loadedModules.add(it)
                        }
                    },
                    { e ->
                        WLKits.log("&cFailed to load module ${it.getName()}")
                        it.setEnabled(false)
                        errorModules.add(it)
                        e.printStackTrace()
                    }
                )
            }
        }
    }

    /**
     * Registers all commands into [Commanding] and tab completers
     *
     * @see Commanding
     */
    fun registerCommands() {
        commandInstances.forEach {
            if (it !in loadedCommands) {
                val cmdCls = it::class.java
                val cls = CommandInfo::class.java
                val info = cmdCls.getAnnotation(cls)
                WLKits.debug("the command info of command $it is $info")
                loadedCommands.add(it)
            }
        }
        WLKits.commanding.init()
    }

    /**
     * Refreshes the enabled states of all modules
     */
    fun refreshEnabled() = moduleInstances.forEach { it.setEnabled(Util.isEnabled(it.getName())) }

    /**
     * Reloads all modules and refresh all configs
     */
    fun reload() {
        WLKits.reload()
        unloadModules()
        registerCommands()
        loadModules(LoadType.ON_STARTUP, false)
        loadModules(LoadType.ON_LOAD_WORLD, false)
        refreshEnabled()
    }

    /**
     * Unloads all modules
     */
    fun unloadModules() {
        loadedModules.forEach(Module::unload)
        loadedModules.clear()
    }

    /**
     * Gets the module instance by [KClass] of module
     *
     * @param cls the [KClass] of module
     * @see KClass
     */
    fun getModuleByKClass(cls: KClass<out Module>) = moduleInstances.first { it::class == cls }
}