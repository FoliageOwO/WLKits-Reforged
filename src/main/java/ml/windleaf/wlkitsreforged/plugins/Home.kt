package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.plugins.commands.home.DelhomeCommand
import ml.windleaf.wlkitsreforged.plugins.commands.home.HomeCommand
import ml.windleaf.wlkitsreforged.plugins.commands.home.SethomeCommand
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util

class Home : Plugin {
    override val name = "Home"
    override val enabled = Util.isEnabled(name)
    companion object {
        var path: String = WLKits.prefixPath + "homes.data"
        var homes = FileUtil.loadHashMap(path) as HashMap<String, String>
        val enabled = Util.isEnabled("Home")
    }

    override fun load() {
        Util.registerCommand("sethome", SethomeCommand())
        Util.registerCommand("home", HomeCommand())
        Util.registerCommand("delhome", DelhomeCommand())
    }

    override fun unload() {
    }
}