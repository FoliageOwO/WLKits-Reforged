package ml.windleaf.wlkitsreforged.modules.categories.manage.macro

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.saving.JsonData
import ml.windleaf.wlkitsreforged.data.Data
import ml.windleaf.wlkitsreforged.modules.categories.manage.Macro
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.Exceptions.*
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.jetbrains.annotations.Nullable
import kotlin.jvm.Throws

/**
 * The Macro module manager
 */
class MacroManager {
    /**
     * Gets macro entire from string
     *
     * @param macro macro path
     * @return the macro entire, null if not found
     */
    fun getMacroFromString(macro: String): MacroEntire<*>? {
        val macros = getMacros()
        macros.forEach {
            if (it.getMacroInfo()?.path == macro) {
                WLKits.debug("Got macro from string: $macro => $it")
                return it
            }
        }
        return null
    }

    /**
     * Parses macro command args to macro string, then gets macro from string
     *
     * Examples:
     *  - /macro run server.stop -> <ServerStopMacro>
     *  - /macro run player.kill {"player":"Steve"} -> <PlayerManageMacro(name=Steve)>
     *  - /macro run player.chat {"player":"Steve","message":"Hello"} -> <ChatMacro(name=Steve,message=Hello)>
     *
     * @param args the args of macro command
     * @return the macro entire
     * @throws MacroNotFoundException if macro not found
     * @throws ArgumentException if the argument is invalid
     */
    @Throws(MacroNotFoundException::class, ArgumentException::class)
    fun parseMacroCommand(args: Array<out String>): MacroEntire<*> {
        val macroPath = args[1]
        val macro = getMacroFromString(macroPath) ?: throw MacroNotFoundException()
        // drop first two args: run [macro] [params] -> [params]
        val map = JsonData.parse<Map<String, Any>?>(args.drop(2).joinToString(" ")) ?: hashMapOf()
        WLKits.debug("Parsing map: ${args.drop(2).joinToString(" ")} => $map")
        val macroArgs = macro.getMacroInfo()?.args?.asList() ?: listOf()
        WLKits.debug("Macro args: ${macroArgs.joinToString(", ")}")
        return if (Util.collectionsEquals(macroArgs, map.keys)) {
            macro.parse(map)
            macro
        } else {
            val needArguments = arrayListOf<String>()
            macroArgs.forEach { if (!map.containsKey(it)) needArguments.add(it) }
            WLKits.debug("Need args: ${needArguments.joinToString(", ")}")
            throw ArgumentException("argument(s) `${needArguments.joinToString(", ")}` not found")
        }
    }

    /**
     * Checks if the command sender has been authorized
     *
     * @param sender command sender
     * @return true if the command sender has been authorized,
     *  constant to true if the command sender is console,
     *  constant to false if the command sender is not player and console
     */
    fun isAuthorized(sender: CommandSender) = if (sender is Player) {
        Macro.authedList.contains(Util.getUUID(sender)!!)
    } else sender is ConsoleCommandSender

    /**
     * Gets all registered macros
     *
     * @return all macros
     */
    fun getMacros(): ArrayList<MacroEntire<*>> {
        return Util.getClassesWithSuperclass<MacroEntire<*>>("ml.windleaf.wlkitsreforged.modules.categories.manage.macro")
                .map { it.newInstance() }
                .toCollection(ArrayList())
    }

    /**
     * Executes the macro in safety
     *
     * @param macro the macro to execute
     * @return the result as T
     * @throws MacroException if the macro has an exception
     */
    @Throws(MacroException::class)
    @Nullable
    fun <T> executeMacro(macro: MacroEntire<T>): T? {
        return Util.catch(Exception::class.java,
            { macro.execute() },
            { throw MacroException("an exception occurred while running macro: ${it.message}", it) }
        )
    }

    /**
     * Executes the macro in safety (only for command executing)
     *
     * @param macro the macro to execute
     * @param sender the sender
     * @return the result as String
     * @throws NoPermissionException if the sender is not authorized
     * @throws MacroException if the macro has an exception
     */
    @Throws(NoPermissionException::class, MacroException::class)
    @Nullable
    fun executeMacro(macro: MacroEntire<*>, sender: CommandSender): String {
        return if (!Macro.manager.isAuthorized(sender)) {
            throw NoPermissionException("you are not authorized")
        } else {
            val result = executeMacro(macro)
            if (result is Data) {
                val jsonString = result.toJsonString()
                jsonString
            } else result.toString()
        }
    }
}