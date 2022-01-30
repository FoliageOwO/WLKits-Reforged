package ml.windleaf.wlkitsreforged.core.reflect.versions

import ml.windleaf.wlkitsreforged.core.reflect.Reflector
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.server.ServerLoadEvent

class V1_18_R1 : Reflector {
    override fun getNMS() = "v1_18_R1"

    override fun sendConsole(message: String) {
        var msg = Util.translateColorCode(message)!!
        ChatColor.values().forEach { msg = msg.replace("§${it.char}", it.toString()) }
        Bukkit.getConsoleSender().sendMessage(msg)
    }

    @EventHandler
    fun loadStartupModule(e: ServerLoadEvent) = V1_16_R3.loadStartupModule(e)
}