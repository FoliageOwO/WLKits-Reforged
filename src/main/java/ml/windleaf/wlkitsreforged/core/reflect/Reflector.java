package ml.windleaf.wlkitsreforged.core.reflect;

import ml.windleaf.wlkitsreforged.core.LoadType;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * The reflector for multiple versions
 *
 * @author WindLeaf_qwq
 */
public interface Reflector extends Listener {
    /**
     * Gets the NMS version
     *
     * @return the NMS version of server
     */
    String getNMS();

    /**
     * Sends message to Console
     *
     * <p>
     * ColorChar in some versions are not supported.
     * </p>
     *
     * <b>
     * Version 1.18+ uses {@link ChatColor} instead of char {@linkplain ChatColor#COLOR_CHAR} (§).
     * </b>
     *
     * @param message the message that send to console
     */
    void sendConsole(String message);

    /**
     * Loads all plugin which be marked as load on {@link LoadType#ON_LOAD_WORLD}
     *
     * <p>
     * Listening to {@link WorldLoadEvent}.
     * </p>
     */
    void loadWorldPlugin(WorldLoadEvent e);
}
