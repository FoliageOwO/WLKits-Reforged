package ml.windleaf.wlkitsreforged.modules.categories.manage.macro.macros.server

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroInfo

@MacroInfo(path = "server.stop", description = "Stops the server")
class ServerStopMacro : MacroEntire<Unit> {
    override fun execute() = WLKits.instance.server.shutdown()
}