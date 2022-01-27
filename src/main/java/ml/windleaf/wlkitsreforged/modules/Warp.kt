package ml.windleaf.wlkitsreforged.modules

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.data.JsonData
import ml.windleaf.wlkitsreforged.modules.commands.warp.*
import ml.windleaf.wlkitsreforged.modules.enums.WarpType
import ml.windleaf.wlkitsreforged.utils.Util
import kotlin.properties.Delegates

class Warp : Module {
    private var enabled = false
    override fun getName() = "Warp"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    companion object {
        val warps = JsonData("warps")
        var enabled by Delegates.notNull<Boolean>()
        lateinit var list: JSONArray
        lateinit var publics: JSONArray
        lateinit var privates: JSONArray

        fun existsWarp(uuid: String, warpName: String, warpType: WarpType): Boolean {
            val nameList = arrayListOf<String>()
            list.forEach { nameList.add(it as String) }
            when (warpType) {
                WarpType.PRIVATE -> {
                    privates.forEach {
                        it as JSONObject
                        if ("$uuid|$warpName" in nameList && it["owner"] == uuid && it["name"] == warpName) {
                            return true
                        }
                    }
                }
                WarpType.PUBLIC -> {
                    publics.forEach {
                        it as JSONObject
                        if (warpName in nameList && it["name"] == warpName) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun update() {
            warps["list"] = list
            warps["public"] = publics
            warps["private"] = privates
            warps.saveData()
        }

        fun getWarpByName(name: String, type: WarpType): JSONObject? {
            val l = if (type == WarpType.PRIVATE) privates else publics
            l.forEach {
                it as JSONObject
                if (it["name"] == name) return it
            }
            return null
        }

        fun getWarps(): HashMap<String, WarpType> {
            /*val names = warps.getListAs<String>("list")
            val map = HashMap<String, WarpType>()
            if (names != null) {
                for (name in names) {
                    when ('|' in name.toCharArray()) {
                        true -> if (warps.getAs<String>("private.$name") != null) map[name] = WarpType.PRIVATE
                        false -> if (warps.getAs<String>("public.$name") != null) map[name] = WarpType.PUBLIC
                    }
                }
            }
            return map*/
            val map = HashMap<String, WarpType>()
            list.forEach {
                it as String
                when ('|' in it.toCharArray()) {
                    true -> map[it] = WarpType.PRIVATE
                    false -> map[it] = WarpType.PUBLIC
                }
            }
            return map
        }
    }

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(name)
        Companion.enabled = enabled
        list = warps.getAs("list")!!
        publics = warps.getAs("public")!!
        privates = warps.getAs("private")!!
    }

    override fun unload() = Unit
    override fun registers() {
        Util.registerCommand("setwarp", SetwarpCommand())
        Util.registerCommand("warp", WarpCommand())
        Util.registerCommand("delwarp", DelwarpCommand())
        Util.registerCommand("warphelp", WarphelpCommand())
        Util.registerCommand("warplist", WarplistCommand())
    }
}