package ml.windleaf.wlkitsreforged.modules.categories.chat

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.modules.CommandInfo
import ml.windleaf.wlkitsreforged.modules.Module
import ml.windleaf.wlkitsreforged.modules.ModuleInfo
import ml.windleaf.wlkitsreforged.modules.annotations.MustPlayer
import ml.windleaf.wlkitsreforged.modules.annotations.Permission
import ml.windleaf.wlkitsreforged.modules.categories.Category
import ml.windleaf.wlkitsreforged.modules.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.utils.ChatComponent
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.properties.Delegates

@ModuleInfo(category = Category.CHAT, description = "Shows your item on the chat", type = LoadType.ON_STARTUP)
@CommandInfo(cmd = "show", description = "Shows your item on the chat", belongTo = ItemShow::class)
class ItemShow : Module, ModuleCommand {
    private var enabled = false
    override fun getEnabled() = enabled
    private lateinit var showMsg: String
    private lateinit var itemDisplay: String
    private var showAllInfo by Delegates.notNull<Boolean>()

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        showMsg = Util.translateColorCode(Util.getModuleMsg(getName(), "show-msg"))!!
        itemDisplay = Util.translateColorCode(Util.getModuleMsg(getName(), "item-display"))!!
        showAllInfo = Util.getModuleConfig(getName(), "show-all-info") as Boolean
    }

    @MustPlayer
    @Permission("wlkits.cmd.show")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        sender as Player
        val item: ItemStack = sender.inventory.itemInMainHand
        val displayName = if (item.itemMeta != null) item.itemMeta!!.displayName else ""
        val pref = "<${sender.displayName}> "
        val itemDisplayContent = Util.translateColorCode(Util.insert(itemDisplay, "displayName" to displayName))!!

        val cc = ChatComponent.builder()
            .append(ChatComponent.Text(Util.translateColorCode(pref)!!))
            .append(ChatComponent.Text(Util.translateColorCode(Util.insert(showMsg, "playerName" to sender.displayName))!!))
            .append(ChatComponent.Text(itemDisplayContent))
        Bukkit.getOnlinePlayers().forEach { cc.send(it) }
    }
}