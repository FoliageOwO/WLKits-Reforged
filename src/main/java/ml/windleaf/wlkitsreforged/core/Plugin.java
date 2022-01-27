package ml.windleaf.wlkitsreforged.core;

/**
 * The plugin interface
 *
 * @author WindLeaf_qwq
 */
public interface Plugin {
    /**
     * Gets the name of plugin
     *
     * @return the plugin name
     */
    String getName();

    /**
     * Gets if this plugin is enabled
     *
     * @return true if this plugin is enabled
     */
    Boolean getEnabled();

    /**
     * Sets enabled state of plugin
     *
     * @param target the enabled state
     */
    void setEnabled(Boolean target);

    /**
     * Gets the load type of plugin
     *
     * @return the load type of plugin
     * @see LoadType
     */
    LoadType getType();

    /**
     * Loads the plugin
     */
    void load();

    /**
     * Unloads the plugin
     */
    void unload();

    /**
     * Registers all events the plugin need
     */
    void registers();
}
