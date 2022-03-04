package ml.windleaf.wlkitsreforged.modules.categories.server

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.modules.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.ModuleInfo
import ml.windleaf.wlkitsreforged.modules.categories.Category
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@ModuleInfo(category = Category.SERVER, description = "Adds join and quit messages to the chat", type = LoadType.ON_STARTUP)
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
    fun onPlayerJoinEvent(e: PlayerJoinEvent) = this.send(e, "Join")

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) = this.send(e, "Quit")

    private fun send(e: PlayerEvent, type: String) {
        if (enabled) {
            val player = e.player
            e.javaClass.getMethod("set${type}Message", String::class.java).invoke(e, "")
            val pn = "playerName" to player.name
            val uuid = "uuid" to Util.getUUID(player)!!
            val ip = "ip" to player.address.toString()
            WLKits.log(Util.insert(Util.getModuleMsg(getName(), "console-${type.lowercase()}"), pn, uuid, ip)!!)
            Util.broadcastPlayers(Util.insert(Util.getModuleMsg(getName(), type.lowercase()), pn, uuid, ip))
        }
    }
}