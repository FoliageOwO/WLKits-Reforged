package ml.windleaf.wlkitsreforged.data

import com.alibaba.fastjson.JSON
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.DataClassUtil.getDataFields
import ml.windleaf.wlkitsreforged.utils.DataClassUtil.getFieldObject
import org.jetbrains.annotations.NotNull

/**
 * The Data base interface
 */
interface Data {
    /**
     * The type `T` class of the data
     */
    val T: Class<*>

    /**
     * Gets the string
     */
    @NotNull override fun toString(): String

    /**
     * Parses instance to json string
     *
     * @return the json string
     */
    @NotNull fun toJsonString(): String {
        val map = hashMapOf<String, Any>()
        val fields = getDataFields(this::class.java)
        fields.forEach { map[it.name] = getFieldObject(it, this) }
        return JSON.toJSONString(map)
    }
}