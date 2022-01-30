package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import kotlin.properties.Delegates

@ModuleInfo(description = "Mentions a player when they are mentioned in chat.", type = LoadType.ON_STARTUP)
class Mention : Module, Listener {
    private var enabled = false
    override fun getEnabled() = enabled
    private lateinit var prefix: String
    private lateinit var styleTo: String
    private lateinit var styleOther: String
    private lateinit var styleAll: String
    private lateinit var allMsg: String
    private var soundNotice by Delegates.notNull<Boolean>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        prefix = Util.getModuleConfig(getName(), "prefix") as String
        styleTo = Util.translateColorCode(Util.getModuleConfig(getName(), "to-style") as String)!!
        styleOther = Util.translateColorCode(Util.getModuleConfig(getName(), "other-style") as String)!!
        styleAll = Util.translateColorCode(Util.getModuleConfig(getName(), "all-style") as String)!!
        allMsg = Util.insert(Util.getModuleConfig(getName(), "all-msg") as String, "prefix" to prefix)!!
        soundNotice = Util.getModuleConfig(getName(), "sound-notice") as Boolean
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
                if (Util.hasPermission(s, Permission("wlkits.action.mention", PermissionType.ACTION))) {
                    Bukkit.getOnlinePlayers().forEach {
                        if (Util.getModuleConfig(getName(), "all") as Boolean
                            && msg.contains(allMsg)
                            && s.isOp == Util.getModuleConfig(getName(), "all-op") as Boolean) {
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
                            } else it.sendMessage(pref + msg)
                        }
                    }
                } else Bukkit.getOnlinePlayers().forEach { it.sendMessage(pref + msg) }
                Bukkit.getConsoleSender().sendMessage(pref + msg)
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