package ml.windleaf.wlkitsreforged.modules.commanding

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.NotNull

/**
 * The ModuleCommand interface
 */
interface ModuleCommand {
    /**
     * On command execute event
     */
    fun onCommand(@NotNull sender: CommandSender, @NotNull args: Array<String>)
}