package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.plugins.commands.home.DelhomeCommand
import ml.windleaf.wlkitsreforged.plugins.commands.home.HomeCommand
import ml.windleaf.wlkitsreforged.plugins.commands.home.SethomeCommand
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import kotlin.properties.Delegates

class Home : Plugin {
    override val name = "Home"
    override var enabled = false
    override val type = LoadType.ON_STARTUP
    companion object {
        var path: String = WLKits.prefixPath + "homes.data"
        lateinit var homes: HashMap<String, String>
        var enabled by Delegates.notNull<Boolean>()
    }

    override fun load() {
        enabled = Util.isEnabled(name)
        homes = FileUtil.loadHashMap(path) as HashMap<String, String>
        Companion.enabled = enabled
    }

    override fun unload() = Unit
    override fun registers() {
        Util.registerCommand("sethome", SethomeCommand())
        Util.registerCommand("home", HomeCommand())
        Util.registerCommand("delhome", DelhomeCommand())
    }
}