package ml.windleaf.wlkitsreforged.modules.categories.manage.macro

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import kotlin.jvm.Throws

/**
 * The Macro entire class.
 *
 * `T` generic type is the type of the result of macro
 */
interface MacroEntire<T> {
    /**
     * Executes the macro
     *
     * @return the result as `T`, return null if there is no result
     */
    @Throws(Exception::class)
    @Nullable fun execute(): T?

    /**
     * Parses an args map to instance
     *
     * @param map args map
     * @throws MacroException throws when there is an error when parsing macro
     */
    @Throws(MacroException::class)
    fun parse(@NotNull map: Map<String, Any>) = Unit

    /**
     * Gets the MacroInfo of this macro instance
     *
     * @return the MacroInfo instance
     * @see MacroInfo
     */
    @Nullable fun getMacroInfo(): MacroInfo? = javaClass.getAnnotation(MacroInfo::class.java)
}