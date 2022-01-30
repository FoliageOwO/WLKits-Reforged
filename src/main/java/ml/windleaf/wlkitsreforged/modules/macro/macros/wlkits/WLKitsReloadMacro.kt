package ml.windleaf.wlkitsreforged.modules.macro.macros.wlkits

import ml.windleaf.wlkitsreforged.core.module.ModuleManager
import ml.windleaf.wlkitsreforged.modules.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.macro.MacroInfo

@MacroInfo(path = "wlkits.reload",
    description = "Reloads the WLKits plugin. " +
            "When executed this macro, " +
            "it will return a error message that the token is invalid, " +
            "because that token will reset after the macro is executed.")
class WLKitsReloadMacro : MacroEntire<Unit> {
    override fun execute() = ModuleManager.reload()
}