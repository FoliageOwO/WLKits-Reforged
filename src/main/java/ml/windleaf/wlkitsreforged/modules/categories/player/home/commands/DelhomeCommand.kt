package ml.windleaf.wlkitsreforged.modules.categories.player.home.commands

import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.modules.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.modules.annotations.Permission
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.modules.categories.player.Home
import ml.windleaf.wlkitsreforged.utils.Util
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
            Util.send(player, Util.getModuleMsg("Home", "delete"))
        } else Util.send(player, Util.getModuleMsg("Home", "no-home"))
    }
}