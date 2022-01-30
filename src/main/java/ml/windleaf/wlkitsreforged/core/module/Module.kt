package ml.windleaf.wlkitsreforged.core.module

import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

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
    @NotNull fun getName(): String = this.javaClass.simpleName

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
    @NotNull fun getType() = this.getModuleInfo()?.type!!

    /**
     * Loads the module
     */
    fun load() = Unit

    /**
     * Unloads the module
     */
    fun unload() = Unit

    /**
     * Gets the ModuleInfo of this module instance
     *
     * @return the ModuleInfo instance
     * @see ModuleInfo
     */
    @Nullable fun getModuleInfo(): ModuleInfo? = javaClass.getAnnotation(ModuleInfo::class.java)
}
