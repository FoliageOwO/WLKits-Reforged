package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinInfo : Plugin, Listener {
    override var name = "JoinInfo"
    var enabled = Util.isEnabled(name)

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
            WLKits.log(Util.getPluginMsg(name, "console-join")!!)
            val i = HashMap<String, String>()
            i["playerName"] = player.name
            Util.broadcastPlayers(Util.insert(Util.getPluginMsg(name, "join"), i))
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
        if (enabled) {
            val player = e.player
            e.quitMessage = ""
            WLKits.log(Util.getPluginMsg(name, "console-quit")!!)
            val i = HashMap<String, String>()
            i["playerName"] = player.name
            Util.broadcastPlayers(Util.insert(Util.getPluginMsg(name, "quit"), i))
        }
    }
}