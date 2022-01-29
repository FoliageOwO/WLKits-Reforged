package ml.windleaf.wlkitsreforged.core

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import org.jetbrains.annotations.NotNull

/**
 * The module interface
 *
 * @author WindLeaf_qwq
 */
interface Module {
    /**
     * Gets the name of module
     *
     * @return the module name
     */
    @NotNull fun getName(): String

    /**
     * Gets if this module is enabled
     *
     * @return `true` if this module is enabled
     */
    @NotNull fun getEnabled(): Boolean

    /**
     * Sets enabled state of module
     *
     * @param target the enabled state
     */
    fun setEnabled(@NotNull target: Boolean)

    /**
     * Gets the load type of module
     *
     * @return the load type of module
     * @see LoadType
     */
    @NotNull fun getType(): LoadType

    /**
     * Loads the module
     */
    fun load()

    /**
     * Unloads the module
     */
    fun unload()

    /**
     * Registers all events the module need
     */
    fun registers()
}
