package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinInfo : Plugin, Listener {
    override val name = "JoinInfo"
    override val enabled = Util.isEnabled(name)
    override val type = LoadType.ON_STARTUP

    override fun load() {
        Util.registerEvent(this)
    }

    override fun unload() {
    }

    @EventHandler
    fun onPlayerJoinEvent(e: PlayerJoinEvent) {
        if (enabled) {
            val player = e.player
            e.joinMessage = ""
            val pn = "playerName" to player.name
            val uuid = "uuid" to Util.getUUID(player)
            val ip = "ip" to player.address.toString()
            WLKits.log(Util.insert(Util.getPluginMsg(name, "console-join"), pn, uuid, ip)!!)
            Util.broadcastPlayers(Util.insert(Util.getPluginMsg(name, "join"), pn, uuid, ip))
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
        if (enabled) {
            val player = e.player
            e.quitMessage = ""
            val pn = "playerName" to player.name
            val uuid = "uuid" to Util.getUUID(player)
            val ip = "ip" to player.address.toString()
            WLKits.log(Util.insert(Util.getPluginMsg(name, "console-quit"), pn, uuid, ip)!!)
            Util.broadcastPlayers(Util.insert(Util.getPluginMsg(name, "quit"), pn, uuid, ip))
        }
    }
}