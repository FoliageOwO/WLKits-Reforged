package ml.windleaf.wlkitsreforged.modules.categories.manage.macro.macros.wlkits

import ml.windleaf.wlkitsreforged.modules.categories.manage.WLKitsCommands
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroInfo

@MacroInfo(path = "wlkits.info", description = "Gets the info of WLKits plugin")
class WLKitsInfoMacro : MacroEntire<String> {
    override fun execute(): String {
        val sb = StringBuilder()
        WLKitsCommands.getInfo().forEach { sb.append(it).append("\n") }
        return sb.toString().trim()
    }
}