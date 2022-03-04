package ml.windleaf.wlkitsreforged.modules.categories.manage.macro

/**
 * All the exceptions that can be thrown by the macro module
 */
class Exceptions {
    /**
     * Throws when the macro is not found
     */
    class MacroNotFoundException(override val message: String = "macro not found")
        : MacroException(message)

    /**
     * Throws when the executor is not authorized
     */
    class NoPermissionException(override val message: String = "you do not have permission to do that")
        : MacroException(message)

    /**
     * Throws when the argument of macro is invalid
     */
    class ArgumentException(override val message: String = "the argument of macro is invalid")
        : MacroException(message)

    /**
     * Throws when the player is not found
     */
    class PlayerNotFoundException(override val message: String = "player not found")
        : MacroException(message)
}
