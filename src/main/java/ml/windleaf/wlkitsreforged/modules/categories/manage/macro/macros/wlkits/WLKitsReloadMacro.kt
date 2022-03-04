package ml.windleaf.wlkitsreforged.modules.categories.manage.macro.macros.wlkits

import ml.windleaf.wlkitsreforged.modules.ModuleManager
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroInfo

@MacroInfo(path = "wlkits.reload",
    description = "Reloads the WLKits plugin. " +
            "When executed this macro, " +
            "it will return a error message that the token is invalid, " +
            "because that token will reset after the macro is executed.")
class WLKitsReloadMacro : MacroEntire<Unit> {
    override fun execute() = ModuleManager.reload()
}