package ml.windleaf.wlkitsreforged.modules.commands.home

import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.Home
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandInfo(cmd = "delhome", description = "Delete home", belongTo = Home::class)
class DelhomeCommand : ModuleCommand {
    @MustPlayer
    @Permission("wlkits.cmd.home")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        val player = sender as Player
        if (Home.homes.contains(Util.getUUID(player)!!)) {
            Home.homes.remove(Util.getUUID(player)!!)
            Home.homes.saveData()
            Util.send(player, Util.getPluginMsg("Home", "delete"))
        } else Util.send(player, Util.getPluginMsg("Home", "no-home"))
    }
}