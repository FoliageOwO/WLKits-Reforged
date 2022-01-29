package ml.windleaf.wlkitsreforged.modules.macro.macros.server

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.data.Data
import ml.windleaf.wlkitsreforged.data.ServerData
import ml.windleaf.wlkitsreforged.modules.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.macro.MacroInfo

@MacroInfo(path = "server.info", description = "Gets server information")
class ServerInfoMacro : MacroEntire<Data> {
    override fun execute() = ServerData(WLKits.instance.server)
}