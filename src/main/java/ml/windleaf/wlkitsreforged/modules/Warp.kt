package ml.windleaf.wlkitsreforged.modules

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.internal.file.JsonData
import ml.windleaf.wlkitsreforged.modules.enums.WarpType
import ml.windleaf.wlkitsreforged.utils.Util
import kotlin.properties.Delegates

@ModuleInfo(description = "Allows players to set warps that other players can teleport to", type = LoadType.ON_STARTUP)
class Warp : Module {
    private var enabled = false
    override fun getEnabled() = enabled
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
        enabled = Util.isEnabled(getName())
        Companion.enabled = enabled
        list = JSON.toJSON(warps.getAs<ArrayList<*>>("list")!!) as JSONArray
        publics = JSON.toJSON(warps.getAs<ArrayList<*>>("public")!!) as JSONArray
        privates = JSON.toJSON(warps.getAs<ArrayList<*>>("private")!!) as JSONArray
    }
}