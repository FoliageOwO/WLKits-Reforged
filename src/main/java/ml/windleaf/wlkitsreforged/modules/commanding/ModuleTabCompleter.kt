package ml.windleaf.wlkitsreforged.modules.commanding

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.jetbrains.annotations.NotNull

/**
 * The ModuleTabCompleter interface
 */
interface ModuleTabCompleter {
    /**
     * On tab complete event
     */
    fun onTabComplete(@NotNull sender: CommandSender, @NotNull args: Array<String>): List<String>
}