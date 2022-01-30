package ml.windleaf.wlkitsreforged.core.reflect

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.enums.Versions
import org.bukkit.ChatColor
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.jetbrains.annotations.NotNull

/**
 * The reflector for multiple versions
 *
 * @author WindLeaf_qwq
 */
interface Reflector : Listener {
    /**
     * Gets the NMS version
     *
     * @return the NMS version of server
     */
    @NotNull fun getNMS(): String

    /**
     * Sends message to Console
     *
     * **ColorChar in some versions are not supported.**
     *
     * Version 1.18+ uses [ChatColor] instead of char [ChatColor.COLOR_CHAR] (§).
     *
     * @param message the message that send to console
     */
    fun sendConsole(@NotNull message: String) {
        Versions.V1_16_R3.reflector.sendConsole(message)
    }

    /**
     * Loads all plugin which be marked as load on [LoadType.ON_LOAD_WORLD]
     *
     * *Listening to [WorldLoadEvent].*
     */
    fun loadWorldPlugin(@NotNull e: WorldLoadEvent) {
        Versions.V1_16_R3.reflector.loadWorldPlugin(e)
    }
}
