package ml.windleaf.wlkitsreforged.core.annotations

/**
 * Marks a method as requiring a player to run the command
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MustPlayer()
