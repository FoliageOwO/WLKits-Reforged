package ml.windleaf.wlkitsreforged.core.module.commanding

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.module.ModuleManager
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.CommandSender
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent
import org.bukkit.event.server.TabCompleteEvent

/**
 * The command progressing class of the commands
 */
class Commanding : Listener {
    /**
     * The map of the commands which has been registered
     */
    private val runningCommands = hashMapOf<CommandInfo, ModuleCommand>()

    /**
     * The map of the tab completers which has been registered
     */
    val runningTabCompleter = hashMapOf<CommandInfo, ModuleTabCompleter>()

    /**
     * The loaded state of [Commanding] instance
     */
    private var loaded = false

    /**
     * Initialize the progressing instance
     */
    fun init() {
        // only load once
        if (!loaded) {
            val i = CommandInfo::class.java
            // register all commands
            ModuleManager.commandInstances.forEach {
                val info = it::class.java.getAnnotation(i)
                runningCommands[info] = it
                WLKits.debug("commanding: ${info.cmd}[${info.aliases.joinToString(", ")}] => $it")
            }
            // register all tab completers
            ModuleManager.tabCompleterInstances.forEach {
                val info = it::class.java.getAnnotation(i)
                runningTabCompleter[info] = it
                WLKits.debug("tabcompleter: ${info.cmd}[${info.aliases.joinToString(", ")}] => $it")
            }
            // register this
            Util.registerEvent(this)
            loaded = true
        }
    }

    /**
     * Listening to [PlayerCommandPreprocessEvent] for players
     */
    @EventHandler
    fun onPCPEvent(e: PlayerCommandPreprocessEvent) =
        processCommand(e.message.removePrefix("/").split(" "), e.player, e)

    /**
     * Listening to [ServerCommandEvent] for console
     */
    @EventHandler
    fun onSCEvent(e: ServerCommandEvent) = processCommand(e.command.split(" "), e.sender, e)

    /**
     * Listening to [TabCompleteEvent] for tab completer
     */
    @EventHandler
    fun onTabCompleter(e: TabCompleteEvent) {
        WLKits.debug("onTabCompleter: [${e.completions.joinToString(", ")}] -> ${e.buffer}")
        val list = e.buffer.removePrefix("/").split(" ")
        val cmd = list[0]
        val args = list.drop(1)
        val info = runningCommands.keys.find { it.cmd == cmd }
        if (info != null) {
            val enabled = ModuleManager.getModuleByKClass(info.belongTo).getEnabled()
            WLKits.debug("tab completer: cmd $cmd belongs to ${info.belongTo::class.java} is $enabled")
            if (enabled) {
                e.completions = runningTabCompleter[info]?.onTabComplete(e.sender, args.toTypedArray()) ?: emptyList()
            } else e.isCancelled = true
        }
    }

    /**
     * Processes a command
     */
    private fun processCommand(list: List<String>, sender: CommandSender, e: Cancellable) {
        Util.catch(Exception::class.java,
            {
                val command = list[0]
                val args = list.drop(1).toTypedArray()
                runningCommands.forEach {
                    if (it.key.cmd == command || it.key.aliases.contains(command)) {
                        e.isCancelled = true
                        val info = it.key
                        WLKits.debug("progressing command: ${info.cmd} -> $sender")
                        if (ModuleManager.getModuleByKClass(info.belongTo).getEnabled()) {
                            val cmd = runningCommands[info]!!
                            cmd.onCommand(sender, args)
                        } else Util.disabled(sender)
                    }
                }
            }, {
                it.printStackTrace()
                Util.send(sender, Util.getModuleMsg("main", "error"))
            }
        )
    }
}