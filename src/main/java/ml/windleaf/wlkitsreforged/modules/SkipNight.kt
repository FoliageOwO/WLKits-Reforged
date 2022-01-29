package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerBedLeaveEvent

class SkipNight : Module, Listener {
    private var enabled = false
    override fun getName() = "SkipNight"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_STARTUP
    private var onBed = ArrayList<Player>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
    }

    override fun unload() = Unit
    override fun registers() = Util.registerEvent(this)

    @EventHandler
    fun onPlayerBedEnterEvent(e: PlayerBedEnterEvent) {
        if (!enabled) return
        if (!Util.hasPermission(e.player, "skipnight", PermissionType.ACTION)) return
        if (e.bedEnterResult !== PlayerBedEnterEvent.BedEnterResult.OK) return

        val percent = Util.getPluginConfig(getName(), "percent") as Int
        // 百分比小数形式
        val percentDecimals = percent.toDouble() / 100
        if (percent < 0 || percent > 100) {
            WLKits.log("&c错误: 配置文件中 &6plugins.skipnight.percent &c数值小于 0 或大于 100, 请重新进行配置!")
            return
        }

        if (!onBed.contains(e.player)) onBed.add(e.player)

        val online = Bukkit.getOnlinePlayers().size

        val onBedPercent = onBed.size.toDouble() / online.toDouble()
        if (onBedPercent >= percentDecimals) {
            Util.broadcastPlayers(Util.getPluginMsg(getName(), "msg-ok"))
            // 判断天气
            if (e.player.world.isThundering) {
                // 设置为晴天
                e.player.world.setStorm(false)
                e.player.world.isThundering = false
            }
            // 设置世界时间
            e.player.world.time = 0
            return
        }

        val offBed = online - onBed.size
        // assume another N player is entered bed
        var need = 1
        while (need < offBed) {
            val expect = (onBed.size + need).toDouble() / online.toDouble()
            if (expect >= percentDecimals) {
                break
            }
            need++
        }

        Util.broadcastPlayers(
            Util.insert(
                Util.getPluginMsg(getName(), "msg-need"),
                "onBed" to onBed.size.toString(),
                "needPlayers" to need.toString()
            )
        )

    }

    @EventHandler
    fun onPlayerBedLeaveEvent(e: PlayerBedLeaveEvent) {
        if (!enabled) return
        if (!Util.hasPermission(e.player, "skipnight", PermissionType.ACTION)) return
        onBed.remove(e.player)
    }
}