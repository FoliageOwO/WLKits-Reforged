package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.modules.categories.Category

/**
 * The ModuleInfo annotation is used to register a module
 *
 *
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModuleInfo(
    /**
     * The category of module
     *
     * @see Category
     */
    val category: Category,

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