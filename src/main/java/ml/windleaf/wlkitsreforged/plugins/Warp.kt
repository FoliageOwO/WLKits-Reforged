package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.plugins.commands.warp.*
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class Warp : Plugin {
    override var name = "Warp"
    companion object {
        private var path: String = WLKits.prefixPath + "warps.yml"
        val warpManager = WarpManager()
        val enabled = Util.isEnabled("Warp")
    }

    override fun load() {
        warpManager.init()
        Util.registerCommand("setwarp", SetwarpCommand())
        Util.registerCommand("warp", WarpCommand())
        Util.registerCommand("delwarp", DelwarpCommand())
        Util.registerCommand("warphelp", WarphelpCommand())
        Util.registerCommand("warplist", WarplistCommand())
    }

    override fun unload() {
    }

    class WarpManager {
        var warps: YamlConfiguration? = null
        var file: File? = null

        fun init() {
            warps = YamlConfiguration()
            try {
                file = File(path)
                if (!file!!.exists()) {
                    file!!.parentFile.mkdirs()
                    FileUtil.makeFile(path)
                }
                warps!!.load(file!!)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InvalidConfigurationException) {
                e.printStackTrace()
            }
        }

        fun getWarps(): HashMap<String, WarpType> {
            val names: Set<Any>? = warps?.getKeys(false)
            val map = HashMap<String, WarpType>()
            if (names != null) {
                for (i in names) {
                    i as String
                    if (warps?.get(i) != null) map[i] = WarpType.valueOf(warps?.getString("$i.type")!!)
                }
            }
            return map
        }
    }

    enum class WarpType(val string: String) {
        PUBLIC("public"), PRIVATE("private")
    }
}