package ml.windleaf.wlkitsreforged.modules.macro

/**
 * The base macro exception
 */
open class MacroException(message: String, cause: Exception? = null) : Exception(message, cause) {
}