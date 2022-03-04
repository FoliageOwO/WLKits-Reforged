package ml.windleaf.wlkitsreforged.modules.categories.chat

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.modules.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.modules.ModuleInfo
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.modules.categories.Category
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import kotlin.properties.Delegates

@ModuleInfo(category = Category.CHAT, description = "Mentions a player when they are mentioned in chat.", type = LoadType.ON_STARTUP)
class Mention : Module, Listener {
    private var enabled = false
    override fun getEnabled() = enabled
    private lateinit var atPlayer: String
    private lateinit var atAll: String
    private lateinit var atPlayerDisplay: String
    private lateinit var atAllDisplay: String
    private var atAllNeedOp by Delegates.notNull<Boolean>()
    private var soundNotice by Delegates.notNull<Boolean>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        atPlayer = Util.translateColorCode(Util.getModuleConfig(getName(), "at-player") as String)!!
        atAll = Util.translateColorCode(Util.getModuleConfig(getName(), "at-all") as String)!!
        atPlayerDisplay = Util.translateColorCode(Util.getModuleConfig(getName(), "at-player-display") as String)!!
        atAllDisplay = Util.translateColorCode(Util.getModuleConfig(getName(), "at-all-display") as String)!!
        atAllNeedOp = Util.getModuleConfig(getName(), "at-all-need-op") as Boolean
        soundNotice = Util.getModuleConfig(getName(), "sound-notice") as Boolean
    }

    @EventHandler
    fun event(e: AsyncPlayerChatEvent) {
        if (enabled) {
            e.isCancelled = true
            Bukkit.getScheduler().scheduleSyncDelayedTask(WLKits.instance) {
                val s = e.player
                var msg = e.message
                var msgCopy = msg.plus("")
                val pref = "<${s.displayName}> "
                val end = Util.translateColorCode("&r")
                if (Util.hasPermission(s, Permission("wlkits.action.mention", PermissionType.ACTION))) {
                    Bukkit.getOnlinePlayers().forEach {
                        if (atAll != "" && msg.contains(atAll) && s.isOp == atAllNeedOp) {
                            it.sendMessage(pref + msg.replace(atAll, "$atAllDisplay$end"))
                            if (soundNotice) playSound(it)
                        } else {
                            val z = Util.insert(atPlayer, "playerName" to it.name)!!
                            Bukkit.getOnlinePlayers().forEach { p ->
                                val r = Util.insert(atPlayer, "playerName" to p.name)!!
                                val display = Util.insert(atPlayerDisplay, "playerName" to p.name)!!
                                msg = msg.replace(r, "$display$end")
                            }
                            if (msgCopy.contains(z)) {
                                playSound(it)
                            }
                            it.sendMessage(pref + msg)
                            /*if (msg.contains(r)) {
                                it.sendMessage(pref + msg.replace(r, "$styleTo$r$end"))

                            } else it.sendMessage(pref + msg)*/
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