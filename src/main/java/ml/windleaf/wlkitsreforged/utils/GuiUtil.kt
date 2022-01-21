package ml.windleaf.wlkitsreforged.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class GuiUtil(private val title: String, private val size: Int) {
    val name = Util.translateColorCode(this.title)!!
    private val gui = Bukkit.createInventory(null, this.size, this.name)

    fun setItems(vararg pairs: Pair<Int, ItemStack>) = pairs.forEach { gui.setItem(it.first, it.second) }
    fun openGui(p: Player) = p.openInventory(this.gui)

    override fun equals(other: Any?): Boolean {
        return if (other is Inventory?) this.gui == other
        else super.equals(other)
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + size
        result = 31 * result + name.hashCode()
        result = 31 * result + gui.hashCode()
        return result
    }
}