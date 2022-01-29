package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.modules.commands.tpa.*
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.entity.Player
import kotlin.properties.Delegates

class Tpa : Module {
    private var enabled = false
    override fun getName() = "Tpa"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    companion object {
        var tpaLogs = HashMap<Player, Player>()
        var enabled by Delegates.notNull<Boolean>()
    }

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
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