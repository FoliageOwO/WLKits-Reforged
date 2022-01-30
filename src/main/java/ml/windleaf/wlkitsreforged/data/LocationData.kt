package ml.windleaf.wlkitsreforged.data

import ml.windleaf.wlkitsreforged.internal.file.JsonData
import org.bukkit.Location

/**
 * The location internal, saves the location
 */
@Suppress("UNUSED")
data class LocationData(val l: Location,
                        override val T: Class<*> = Location::class.java) : Data {
    /**
     * `x` coordinate of the location
     */
    @DataField
    val x = l.x

    /**
     * `y` coordinate of the location
     */
    @DataField
    val y = l.y

    /**
     * `z` coordinate of the location
     */
    @DataField
    val z = l.z

    /**
     * `yaw` of the location, measured in degrees.
     */
    @DataField
    val yaw = l.yaw

    /**
     * `pitch` of the location, measured in degrees.
     */
    @DataField
    val pitch = l.pitch

    /**
     * the name of `world` of the location
     */
    @DataField
    val world = l.world?.name

    override fun toString() = JsonData.toJson(this)
}