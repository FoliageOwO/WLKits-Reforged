package ml.windleaf.wlkitsreforged.modules.categories.manage.macro

/**
 * The MacroInfo annotation is used to register a macro
 *
 * @param path the path to the macro
 * @param description the description of the macro
 * @param args the arguments of the macro
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MacroInfo(
    val path: String,
    val description: String,
    val args: Array<String> = []
)
