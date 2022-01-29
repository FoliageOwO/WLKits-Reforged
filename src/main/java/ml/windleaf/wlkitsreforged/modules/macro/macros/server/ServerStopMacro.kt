package ml.windleaf.wlkitsreforged.modules.macro.macros.server

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.macro.MacroInfo

@MacroInfo(path = "server.stop", description = "Stops the server")
class ServerStopMacro : MacroEntire<Unit> {
    override fun execute() = WLKits.instance.server.shutdown()
}