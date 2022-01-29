package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.internal.JsonData
import ml.windleaf.wlkitsreforged.modules.commands.home.DelhomeCommand
import ml.windleaf.wlkitsreforged.modules.commands.home.HomeCommand
import ml.windleaf.wlkitsreforged.modules.commands.home.SethomeCommand
import ml.windleaf.wlkitsreforged.utils.Util
import kotlin.properties.Delegates

class Home : Module {
    private var enabled = false
    override fun getName() = "Home"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    companion object {
        val homes = JsonData("homes")
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
        Util.registerCommand("sethome", SethomeCommand())
        Util.registerCommand("home", HomeCommand())
        Util.registerCommand("delhome", DelhomeCommand())
    }
}