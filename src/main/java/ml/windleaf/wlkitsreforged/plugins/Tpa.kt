package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.plugins.commands.tpa.*
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.entity.Player
import kotlin.properties.Delegates

class Tpa : Plugin {
    override val name = "Tpa"
    override var enabled = false
    override val type = LoadType.ON_STARTUP
    companion object {
        var tpaLogs = HashMap<Player, Player>()
        var enabled by Delegates.notNull<Boolean>()
    }

    override fun load() {
        enabled = Util.isEnabled(name)
        Companion.enabled = enabled
    }

    override fun unload() = Unit
    override fun registers() {
        Util.registerCommand("tpa", TpaCommand())
        Util.registerCommand("tpaccept", TpacceptCommand())
        Util.registerCommand("tpadeny", TpadenyCommand())
        Util.registerCommand("tpacancel", TpacancelCommand())
        Util.registerCommand("tpahelp", TpahelpCommand())
    }
}