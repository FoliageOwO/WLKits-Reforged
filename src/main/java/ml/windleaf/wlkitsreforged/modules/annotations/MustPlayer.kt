package ml.windleaf.wlkitsreforged.modules.annotations

/**
 * Marks a method as requiring a player to run the command
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MustPlayer()
