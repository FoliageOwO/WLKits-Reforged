package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.modules.Module
import kotlin.reflect.KClass

/**
 * The CommandInfo annotation is used to register a command executor
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandInfo(
    /**
     * The command name
     */
    val cmd: String,

    /**
     * The command aliases
     */
    val aliases: Array<String> = [],

    /**
     * The description of command
     */
    val description: String,

    /**
     * The module which the command belongs to
     */
    val belongTo: KClass<out Module>
)