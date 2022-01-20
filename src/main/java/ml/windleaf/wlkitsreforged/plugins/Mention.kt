package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.LoadType
import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class Mention : Plugin, Listener {
    override val name = "Mention"
    override val enabled = Util.isEnabled(name)
    override val type = LoadType.ON_STARTUP
    private val prefix = Util.getPluginConfig(name, "prefix") as String
    private val styleTo = Util.translateColorCode(Util.getPluginConfig(name, "to-style") as String)
    private val styleOther = Util.translateColorCode(Util.getPluginConfig(name, "other-style") as String)
    private val styleAll = Util.translateColorCode(Util.getPluginConfig(name, "all-style") as String)
    private val allMsg = Util.insert(Util.getPluginConfig(name, "all-msg") as String, "prefix" to prefix)!!
    private val soundNotice = Util.getPluginConfig(name, "sound-notice") as Boolean

    override fun load() {
        Util.registerEvent(this)
    }

    override fun unload() {
    }

    @EventHandler
    fun event(e: AsyncPlayerChatEvent) {
        if (enabled) {
            e.isCancelled = true
            Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                val s = e.player
                var msg = e.message
                val pref = "<${s.displayName}> "
                val end = Util.translateColorCode("&r")
                Bukkit.getOnlinePlayers().forEach {
                    if (Util.getPluginConfig(name, "all") as Boolean
                        && msg.contains(allMsg)
                        && s.isOp == Util.getPluginConfig(name, "all-op") as Boolean) {
                        it.sendMessage(pref + msg.replace(allMsg, "$styleAll$allMsg$end"))
                        playSound(it)
                    } else {
                        val r = "$prefix${it.name}"
                        for (p in otherPlayers(it)) {
                            val x = "$prefix${p.name}"
                            msg = msg.replace(x, "$styleOther$x$end")
                        }
                        if (msg.contains(r)) {
                            it.sendMessage(pref + msg.replace(r, "$styleTo$r$end"))
                            playSound(it)
                        }
                        else it.sendMessage(pref + msg)
                    }
                }
            }
        }
    }

    private fun otherPlayers(without: Player): ArrayList<Player> {
        val result = arrayListOf<Player>()
        Bukkit.getOnlinePlayers().forEach { if (it != without) result.add(it) }
        return result
    }

    private fun playSound(p: Player) {
        if (soundNotice) p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
    }
}