package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.PermissionType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerBedLeaveEvent

class SkipNight : Plugin, Listener {
    override val name = "SkipNight"
    override var enabled = false
    override val type = LoadType.ON_STARTUP
    private var onBed = ArrayList<Player>()

    override fun load() {
        enabled = Util.isEnabled(name)
    }

    override fun unload() = Unit
    override fun registers() = Util.registerEvent(this)

    @EventHandler
    fun onPlayerBedEnterEvent(e: PlayerBedEnterEvent) {
        if (enabled
            && (e.player.world.time >= 12010 || e.player.world.isThundering)
            && Util.hasPermission(e.player, "skipnight", PermissionType.ACTION)
        ) {
            val percent = Util.getPluginConfig(name, "percent") as Int
            if (percent < 0 || percent > 100) {
                WLKits.log("&c错误: 配置文件中 &6plugins.skipnight.percent &c数值小于 0 或大于 100, 请重新进行配置!")
                return
            }

            if (!onBed.contains(e.player)) onBed.add(e.player)

            val online = Bukkit.getOnlinePlayers().size

            val onBedPercent = onBed.size / online

            if (onBedPercent >= (percent / 100)) {
                Util.broadcastPlayers(Util.getPluginMsg(name, "msg-ok"))
                // 设置世界时间
                e.player.world.time = 100
                return
            }

            val offBed = online - onBed.size
            // assume another N player is entered bed
            var need = 1

            while (need < offBed) {
                val expect = (onBed.size + need) / online
                if (expect >= (percent / 100)) {
                    break
                }
                need++
            }

            Util.broadcastPlayers(
                Util.insert(
                    Util.getPluginMsg(name, "msg-need"),
                    "onBed" to onBed.size.toString(),
                    "needPlayers" to need.toString()
                )
            )

        }
    }

    @EventHandler
    fun onPlayerBedLeaveEvent(e: PlayerBedLeaveEvent) {
        if (enabled && Util.hasPermission(e.player, "skipnight", PermissionType.ACTION)) onBed.remove(e.player)
    }
}