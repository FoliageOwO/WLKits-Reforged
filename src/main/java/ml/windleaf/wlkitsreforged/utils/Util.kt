package ml.windleaf.wlkitsreforged.utils

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.PlayerType
import ml.windleaf.wlkitsreforged.core.enums.Versions
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.module.ModuleManager
import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.core.reflect.versions.V1_16_R3
import ml.windleaf.wlkitsreforged.internal.PlayerInType
import ml.windleaf.wlkitsreforged.modules.enums.PlayerAction
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil.Test
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.lang.reflect.Modifier
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

/**
 * The util object, which includes some useful functions
 */
object Util {
    /**
     * Colors a string
     *
     * @param s the string to color
     * @return the colored string
     */
    fun translateColorCode(s: String?) = s?.replace("&", "§")

    /**
     * Gets the message prefix of the module
     *
     * @return the prefix
     */
    fun withPrefix() = "&b${WLKits.name} >> &r" /* "&b${WLKits.name} » &r" */


    /**
     * A shortcut to get the config of module from plugin config.yml
     *
     * @param moduleName the name of the module
     * @param root the key of the config
     * @return the config
     */
    fun getModuleConfig(moduleName: String, root: String) = WLKits.instance.config.get("modules.${moduleName.lowercase()}.$root")

    /**
     * A shortcut to get the message of module from plugin message.yml
     *
     * @param moduleName the name of the module
     * @param root the key of the message
     * @return the message as string
     */
    fun getModuleMsg(moduleName: String, root: String) = getModuleMsgAs(moduleName, root) as String

    /**
     * A shortcut to get the message of module from plugin message.yml as custom class
     *
     * @param moduleName the name of the module
     * @param root the key of the message
     * @return any object
     */
    fun getModuleMsgAs(moduleName: String, root: String) = WLKits.message["${moduleName.lowercase()}.$root"]

    /**
     * Sends a message to command sender
     *
     * @param p the command sender
     * @param s the message to send
     */
    fun send(p: CommandSender, s: String?) = p.sendMessage(translateColorCode(withPrefix() + s)!!)

    /**
     * Gets the enabled state of module
     *
     * @param moduleName the name of the module
     * @return the enabled state
     */
    fun isEnabled(moduleName: String) = getModuleConfig(moduleName, "enabled") as Boolean

    /**
     * Sends a message to player to tell that the module is disabled
     *
     * @param p the player to tell
     */
    fun disabled(p: CommandSender) = send(p, getModuleMsg("main", "disabled"))

    /**
     * A shortcut to get the uuid string of player
     *
     * @param p the player to get
     * @return the uuid string, null if the player is null
     */
    fun getUUID(p: Player?) = p?.uniqueId?.toString()

    /**
     * Sends a message to player to tell that the command arguments are invalid
     *
     * @param p the player to tell
     */
    fun invalidArgs(p: CommandSender) = send(p, getModuleMsg("main", "invalid-args"))

    /**
     * A shortcut to checks if the command sender has permission
     *
     * @param s the command sender
     * @param permission the [Permission] instance
     * @return true if the command sender has permission
     */
    fun hasPermission(s: CommandSender, permission: Permission) = s.hasPermission(permission.permissionString)

    /**
     * A shortcut to generate a random uuid string without char `-`
     *
     * @return the random uuid string
     */
    fun generateRandomToken() = UUID.randomUUID().toString().replace("-", "")

    /**
     * Sends the message to all online players
     *
     * @param string the message to send
     */
    fun broadcastPlayers(string: String?) = Bukkit.getOnlinePlayers().forEach { send(it, string) }

    /**
     * Sends the help message of module to command sender
     *
     * @param s the command sender
     * @param pairs the help message pairs
     */
    fun sendHelp(s: CommandSender, vararg pairs: Pair<String, String>) = pairs.forEach { send(s, "&6${it.first} &f- &a${it.second}".replace("|", "&2|&6")) }

    /**
     * Sends the messages to command sender
     *
     * @param p the command sender
     * @param s the messages to send
     * @see Util#send(CommandSender, String)
     */
    fun send(p: CommandSender, vararg s: String?) = s.forEach { send(p, it) }

    /**
     * Gets the module by module name
     *
     * @param name the module name
     * @return the module, null if not found
     */
    fun getModuleByName(name: String): Module? = ModuleManager.moduleInstances.firstOrNull { it.getName() == name }

    /**
     * Gets the world by world name
     *
     * @param name the world name
     * @return the world, null if not found
     */
    fun getWorldByName(name: String): World? = Bukkit.getWorlds().firstOrNull { it.toString() == name || it.name == name }

    /**
     * Parses the boolean value
     *
     * @param boolean the boolean value
     * @return string
     */
    fun parseBooleanColor(boolean: Boolean) = if (boolean) WLKits.TRUE else WLKits.FALSE

    /**
     * A shortcut to register event
     *
     * @param listener the listener
     */
    fun registerEvent(listener: Listener) {
        WLKits.debug("register listener: $listener")
        WLKits.instance.server.pluginManager.registerEvents(listener, WLKits.instance)
    }

    /**
     * Checks if command sender is player, else send message to command sender
     *
     * @param p the command sender
     * @return true if command sender is player
     */
    fun mustPlayer(p: CommandSender): Boolean {
        return if (p is Player && p !is ConsoleCommandSender) true
        else {
            send(p, getModuleMsg("main", "must-player"))
            false
        }
    }

    /**
     * Formats the string with the key and value of pairs
     *
     * @param s the string
     * @param pairs the pairs
     */
    fun insert(s: String?, vararg pairs: Pair<String, String>): String? {
        var result = s
        pairs.forEach { result = result?.replace("{${it.first}}", it.second) }
        return result
    }

    /**
     * Checks if the command sender has permission, else send message to command sender
     *
     * @param s the command sender
     * @param permission the [Permission] instance
     * @return true if command sender has permission
     * @see Permission
     */
    fun needPermission(s: CommandSender, permission: Permission): Boolean {
        if (hasPermission(s, permission)) return true
        else send(s, getModuleMsg("main", "no-permission"))
        return false
    }

    /**
     * Gets the reflector which is suitable with NMS version
     *
     * @return the reflector
     * @see Reflector
     */
    fun getReflector(): Reflector {
        val versions = Bukkit.getBukkitVersion().split(".")
        val major = versions[0]
        val minor = versions[1]
        var nmsVersion = "v$major@$minor@R".replace('@', '_')

        for (i in 1..9) {
            val versionTest = nmsVersion + i
            try {
                Class.forName("org.bukkit.craftbukkit.$versionTest.inventory.CraftItemStack")
                nmsVersion += i
                break
            } catch (ignored: ClassNotFoundException) { }
        }

        val result: Reflector = try {
            Versions.valueOf(nmsVersion.uppercase()).reflector
        } catch (e: IllegalArgumentException) { V1_16_R3() }

        WLKits.debug(ref = result, "NMS: $nmsVersion -> $result")
        return result
    }

    /**
     * A shortcut to call method and catch exception automatically, gets the result
     *
     * @param exception the exception class to catch, defaults to Exception
     * @param function the lambda function to call
     * @param onException the lambda function to call when caught exception, defaults to empty function
     * @return the result of the function, null if caught exception
     */
    fun <T> catch(
        exception: Class<out Exception> = Exception::class.java,
        function: () -> T?,
        onException: (e: Exception) -> Unit = {}
    ): T? {
        try {
            return function.invoke()
        } catch (e: Exception) {
            if (exception.isInstance(e)) onException.invoke(e)
            else e.printStackTrace()
        }
        return null
    }

    /**
     * A shortcut to call method and catch exception automatically, gets the result
     *
     * @param function the lambda function to call
     * @return the result of the function, null if caught exception
     * @see Util#catch(Class, () -> T?, (e: Exception) -> Unit)
     */
    fun <T> catch(function: () -> T?) = catch<T>(Exception::class.java, function) { it.printStackTrace() }

    /**
     * Removes the color from the string
     *
     * @param s the string to remove
     * @return the string without color code
     */
    fun removeColor(s: String?): String? {
        var st = translateColorCode(s)
        ChatColor.values().forEach { st = st?.replace("§" + it.char, "") }
        return st
    }

    /**
     * Gets all classes which super a class
     *
     * @param packagePath the package path
     * @return the list of classes
     */
    inline fun <reified T> getClassesWithSuperclass(packagePath: String): ArrayList<Class<out T>> {
        val list = arrayListOf<Class<out T>>()
        val cls = T::class.java
        val resolver = ResolverUtil()
        resolver.classLoader = cls.classLoader
        resolver.findInPackage(object : Test {
            override fun matches(type: Class<*>?) = true
            override fun matches(resource: URI?) = true
            override fun doesMatchClass() = true
            override fun doesMatchResource() = true
        }, packagePath)

        WLKits.debug("Finding $cls")
        WLKits.debug("Found ${resolver.classes.size} classes")
        resolver.classes.forEach {
            it.declaredMethods.find { d -> Modifier.isNative(d.modifiers) }?.let { m ->
                val k = m.declaringClass.typeName + "." + m.name
                throw UnsatisfiedLinkError("Native method $k in ${it.name}")
            }

            WLKits.debug("Class: $it, ${cls.isAssignableFrom(it)}, ${!it.isInterface}, ${!Modifier.isAbstract(it.modifiers)}")
            if (cls.isAssignableFrom(it)
                && !it.isInterface
                && !Modifier.isAbstract(it.modifiers)) list.add(it as Class<out T>)
        }

        return list
    }

    /**
     * Formats the timestamp to string with pattern `yyyy-MM-dd HH:mm:ss`
     *
     * @param long the timestamp
     * @return the date string
     */
    fun getDate(long: Long): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(long))!!

    /**
     * Gets the PlayerInType instance by uuid or name
     *
     * @param uuidOrName uuid or name
     * @return the PlayerInType instance, null if player not found
     */
    fun getPlayerBy(uuidOrName: String): PlayerInType? {
        var playerType: PlayerType = PlayerType.OFFLINE
        var type: PlayerInType.TypeEnum = PlayerInType.TypeEnum.NAME
        var temp: OfflinePlayer? = Bukkit.getPlayer(uuidOrName)

        catch(IllegalArgumentException::class.java,
            {
                if (temp == null) {
                    temp = Bukkit.getPlayer(UUID.fromString(uuidOrName))
                    type = PlayerInType.TypeEnum.UUID
                }
                playerType = PlayerType.ONLINE
            },
            {
                temp = null
                type = PlayerInType.TypeEnum.NAME
            })
        if (temp == null) {
            Bukkit.getOfflinePlayers().forEach {
                playerType = PlayerType.OFFLINE
                type = if (it.name == uuidOrName) {
                    temp = it
                    PlayerInType.TypeEnum.NAME
                } else if (it.uniqueId.toString() == uuidOrName) {
                    temp = it
                    PlayerInType.TypeEnum.UUID
                } else PlayerInType.TypeEnum.NAME
            }
            if (temp == null) return null
        }
        return PlayerInType(type, temp!!, playerType , uuidOrName)
    }

    /**
     * Checks if two collection are equal
     *
     * @param c1 the first collection
     * @param c2 the second collection
     * @return true if equal
     */
    fun collectionsEquals(c1: Collection<*>, c2: Collection<*>): Boolean {
        if (c1.size != c2.size) return false
        c1.forEach { if (!c2.contains(it)) return false }
        return true
    }

    /**
     * Gets the player action by string
     *
     * @param string the action string
     * @return the player action, [PlayerAction.NULL] if not found
     */
    fun getPlayerActionByString(string: String): PlayerAction {
        PlayerAction.values().forEach { if (it.string == string.lowercase()) return it }
        return PlayerAction.NULL
    }

    /**
     * Checks if the args are valid
     *
     * @param args the args map
     * @param needArgs must-have args
     * @return true if valid, throw exception if not
     * @throws IllegalArgumentException if args is invalid
     */
    @Throws(IllegalArgumentException::class)
    fun rightArgs(args: Map<*, *>?, vararg needArgs: String): Boolean {
        return if (args == null) {
            throw IllegalArgumentException("wrong parameters")
        } else {
            needArgs.forEach {
                if (!args.containsKey(it)) {
                    throw IllegalArgumentException("missing parameter `$it`")
                }
            }
            true
        }
    }
}