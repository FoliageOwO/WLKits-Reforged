package ml.windleaf.wlkitsreforged.modules.categories.manage.macro.macros.server

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.data.Data
import ml.windleaf.wlkitsreforged.data.ServerData
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.categories.manage.macro.MacroInfo

@MacroInfo(path = "server.info", description = "Gets server information")
class ServerInfoMacro : MacroEntire<Data> {
    override fun execute() = ServerData(WLKits.instance.server)
}