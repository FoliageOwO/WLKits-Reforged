package ml.windleaf.wlkitsreforged.core;

import ml.windleaf.wlkitsreforged.core.enums.LoadType;

/**
 * The module interface
 *
 * @author WindLeaf_qwq
 */
public interface Module {
    /**
     * Gets the name of module
     *
     * @return the module name
     */
    String getName();

    /**
     * Gets if this module is enabled
     *
     * @return true if this module is enabled
     */
    Boolean getEnabled();

    /**
     * Sets enabled state of module
     *
     * @param target the enabled state
     */
    void setEnabled(Boolean target);

    /**
     * Gets the load type of module
     *
     * @return the load type of module
     * @see LoadType
     */
    LoadType getType();

    /**
     * Loads the module
     */
    void load();

    /**
     * Unloads the module
     */
    void unload();

    /**
     * Registers all events the module need
     */
    void registers();
}
