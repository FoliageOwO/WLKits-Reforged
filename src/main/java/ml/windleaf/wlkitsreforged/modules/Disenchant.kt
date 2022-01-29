package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.enums.PermissionType
import ml.windleaf.wlkitsreforged.core.Module
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.utils.GuiUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.*
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta

class Disenchant : Module, Listener {
    private var enabled = false
    override fun getName() = "Disenchant"
    override fun getEnabled() = enabled
    override fun getType() = LoadType.ON_LOAD_WORLD
    private lateinit var gui: GuiUtil
    private lateinit var disenchantBook: ItemStack
    private lateinit var confirm: ItemStack
    private lateinit var cancel: ItemStack

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
        gui = GuiUtil(Util.getPluginMsg(getName(), "menu-display-name"), 9)
        registerRecipe()
    }

    override fun unload() = Unit
    override fun registers() = Util.registerEvent(this)
    private fun loadInventory(player: Player) = gui.setItems(3 to confirm, 4 to player.inventory.itemInOffHand, 5 to cancel)

    private fun disenchant(player: Player, offhand: ItemStack) {
        val book = ItemStack(Material.ENCHANTED_BOOK)
        val meta = book.itemMeta as EnchantmentStorageMeta?
        if (meta == null) Util.send(player, Util.getPluginMsg(getName(), "fail"))
        else {
            val enchants = offhand.enchantments
            for ((key, value) in enchants) meta.addStoredEnchant(key!!, value!!, true)
            meta.lore = listOf(Util.insert(Util.translateColorCode(Util.getPluginMsg(getName(), "lore")), "playerName" to player.name))
            book.itemMeta = meta
            player.inventory.setItemInMainHand(book)
            Util.send(player, Util.insert(Util.getPluginMsg(getName(), "success"), "item" to player.inventory.itemInOffHand.type.name))
            // 抹除武器附魔
            offhand.itemMeta?.enchants?.forEach { offhand.removeEnchantment(it.key) }
            player.updateInventory()
        }
    }

    private fun registerRecipe() {
        confirm = ItemStack(Material.ENCHANTED_BOOK, 1)
        val confirmMeta: ItemMeta = confirm.itemMeta!!
        confirmMeta.setDisplayName(Util.translateColorCode(Util.getPluginMsg("main", "confirm")))
        val confirmLore = java.util.ArrayList<String>()
        confirmLore.add(Util.translateColorCode(Util.getPluginMsg(getName(), "click-to-confirm"))!!)
        confirmMeta.lore = confirmLore
        confirm.itemMeta = confirmMeta

        cancel = ItemStack(Material.BARRIER, 1)
        val cancelMeta: ItemMeta = cancel.itemMeta!!
        cancelMeta.setDisplayName(Util.translateColorCode(Util.getPluginMsg("main", "cancel")))
        val cancelLore = java.util.ArrayList<String>()
        cancelLore.add(Util.translateColorCode(Util.getPluginMsg(getName(), "click-to-cancel"))!!)
        cancelMeta.lore = cancelLore
        cancel.itemMeta = cancelMeta

        disenchantBook = ItemStack(Material.ENCHANTED_BOOK)
        val bookMeta: ItemMeta = disenchantBook.itemMeta!!
        bookMeta.setDisplayName(Util.translateColorCode(Util.getPluginMsg(getName(), "book-display-name")))
        val bookLore = java.util.ArrayList<String>()
        bookLore.add(Util.translateColorCode(Util.getPluginMsg(getName(), "book-lore"))!!)
        bookMeta.lore = bookLore
        disenchantBook.itemMeta = bookMeta

        try {
            val disenchantBookRecipe =
                ShapedRecipe(NamespacedKey(WLKits.instance, "disenchantmentbook"), disenchantBook)
            disenchantBookRecipe.shape("###", "#@#", "###")
            disenchantBookRecipe.setIngredient('#', Material.GLOWSTONE_DUST)
            disenchantBookRecipe.setIngredient('@', Material.BOOK)
            Bukkit.addRecipe(disenchantBookRecipe)
        } catch (ignored: IllegalStateException) { }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        val player = e.player
        if (enabled) {
            if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {
                // 若主手持有物品非转移附魔书则终止
                if (player.inventory.itemInMainHand != disenchantBook) return
                // 判断物品是否有附魔
                if (player.inventory.itemInOffHand.enchantments.isNotEmpty()) {
                    // 打开 GUI 确认操作
                    loadInventory(player)
                    gui.openGui(player)
                } else Util.send(player, Util.translateColorCode(Util.getPluginMsg(getName(), "no-enchantment"))!!)
            }
        }
    }

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        if (enabled && Util.needPermission(e.whoClicked, "disenchant", PermissionType.ACTION)) {
            val inventory = e.inventory
            if (gui.equals(inventory)) {
                val player = e.whoClicked as Player
                e.isCancelled = true
                when (e.currentItem) {
                    confirm -> {
                        player.closeInventory()
                        disenchant(player, player.inventory.itemInOffHand)
                        player.inventory.remove(confirm)
                        player.updateInventory()
                    }
                    cancel -> {
                        player.closeInventory()
                        player.inventory.remove(cancel)
                        player.updateInventory()
                    }
                }
            }
        }
    }
}