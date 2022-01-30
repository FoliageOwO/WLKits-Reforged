package ml.windleaf.wlkitsreforged.core.annotations

import ml.windleaf.wlkitsreforged.core.enums.LoadType

/**
 * The ModuleInfo annotation is used to register a module
 *
 *
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModuleInfo(
    /**
     * The description of module
     */
    val description: String,

    /**
     * The load type of module
     *
     * @see LoadType
     */
    val type: LoadType
)