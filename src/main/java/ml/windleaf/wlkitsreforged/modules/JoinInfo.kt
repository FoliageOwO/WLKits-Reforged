package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@ModuleInfo(description = "Adds join and quit messages to the chat", type = LoadType.ON_STARTUP)
class JoinInfo : Module, Listener {
    private var enabled = false
    override fun getEnabled() = enabled

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
    }

    @EventHandler
    fun onPlayerJoinEvent(e: PlayerJoinEvent) {
        if (enabled) {
            val player = e.player
            e.joinMessage = ""
            val pn = "playerName" to player.name
            val uuid = "uuid" to Util.getUUID(player)!!
            val ip = "ip" to player.address.toString()
            WLKits.log(Util.insert(Util.getModuleMsg(getName(), "console-join"), pn, uuid, ip)!!)
            Util.broadcastPlayers(Util.insert(Util.getModuleMsg(getName(), "join"), pn, uuid, ip))
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
        if (enabled) {
            val player = e.player
            e.quitMessage = ""
            val pn = "playerName" to player.name
            val uuid = "uuid" to Util.getUUID(player)!!
            val ip = "ip" to player.address.toString()
            WLKits.log(Util.insert(Util.getModuleMsg(getName(), "console-quit"), pn, uuid, ip)!!)
            Util.broadcastPlayers(Util.insert(Util.getModuleMsg(getName(), "quit"), pn, uuid, ip))
        }
    }
}