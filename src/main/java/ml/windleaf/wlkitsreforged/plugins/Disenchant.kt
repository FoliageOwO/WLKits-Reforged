package ml.windleaf.wlkitsreforged.plugins

import ml.windleaf.wlkitsreforged.core.Plugin
import ml.windleaf.wlkitsreforged.core.WLKits
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
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta

class Disenchant : Plugin, Listener {
    override var name = "Disenchant"
    private var menu = Bukkit.createInventory(null, 9, Util.translateColorCode(Util.getPluginMsg(name, "menu-display-name"))!!)
    private var disenchantBook: ItemStack? = null
    private var confirm: ItemStack? = null
    private var cancel: ItemStack? = null
    private var enabled = Util.isEnabled(name)

    override fun load() {
        registerRecipe()
        Util.registerEvent(this)
    }

    override fun unload() {
    }

    private fun loadInventory(player: Player) {
        menu.setItem(3, confirm)
        menu.setItem(4, player.inventory.itemInOffHand)
        menu.setItem(5, cancel)
    }

    private fun disenchant(player: Player, offhand: ItemStack) {
        val book = ItemStack(Material.ENCHANTED_BOOK)
        val meta = book.itemMeta as EnchantmentStorageMeta?
        if (meta == null) Util.send(player, Util.getPluginMsg(name, "fail"))
        else {
            val enchants = offhand.enchantments
            for ((key, value) in enchants) meta.addStoredEnchant(key!!, value!!, true)
            val lore = ArrayList<String>()
            val i = HashMap<String, String>()
            i["playerName"] = player.name
            lore.add(Util.insert(Util.translateColorCode(Util.getPluginMsg(name, "lore")), i)!!)
            meta.lore = lore
            book.itemMeta = meta
            player.inventory.setItemInMainHand(book)
            val z = HashMap<String, String>()
            z["item"] = player.inventory.itemInOffHand.type.name
            Util.send(player, Util.insert(Util.getPluginMsg(name, "success")!!, z))
            player.inventory.setItemInOffHand(null)
            player.updateInventory()
        }
    }

    private fun registerRecipe() {
        // 确认按钮 (confirm)
        confirm = ItemStack(Material.ENCHANTED_BOOK, 1)
        val confirmMeta: ItemMeta = confirm!!.itemMeta!!
        confirmMeta.setDisplayName(Util.translateColorCode(Util.getPluginMsg("main", "confirm")))
        val confirmLore = java.util.ArrayList<String>()
        confirmLore.add(Util.translateColorCode(Util.getPluginMsg(name, "click-to-confirm"))!!)
        confirmMeta.lore = confirmLore
        confirm?.itemMeta = confirmMeta

        // 取消按钮 (cancel)
        cancel = ItemStack(Material.BARRIER, 1)
        val cancelMeta: ItemMeta = cancel?.itemMeta!!
        cancelMeta.setDisplayName(Util.translateColorCode(Util.getPluginMsg("main", "cancel")))
        val cancelLore = java.util.ArrayList<String>()
        cancelLore.add(Util.translateColorCode(Util.getPluginMsg(name, "click-to-cancel"))!!)
        cancelMeta.lore = cancelLore
        cancel?.itemMeta = cancelMeta

        // 附魔书
        disenchantBook = ItemStack(Material.ENCHANTED_BOOK)
        val bookMeta: ItemMeta = disenchantBook?.itemMeta!!
        bookMeta.setDisplayName(Util.translateColorCode(Util.getPluginMsg(name, "book-display-name")))
        val bookLore = java.util.ArrayList<String>()
        bookLore.add(Util.translateColorCode(Util.getPluginMsg(name, "book-lore"))!!)
        bookMeta.lore = bookLore
        disenchantBook?.itemMeta = bookMeta

        // 附魔书配方
        val disenchantBookRecipe =
            ShapedRecipe(NamespacedKey(WLKits.instance, "disenchantmentbook"), disenchantBook!!)
        disenchantBookRecipe.shape("###", "#@#", "###")
        disenchantBookRecipe.setIngredient('#', Material.GLOWSTONE_DUST)
        disenchantBookRecipe.setIngredient('@', Material.BOOK)
        Bukkit.addRecipe(disenchantBookRecipe)
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        val player = e.player
        if (enabled) {
            val mainhand = e.item
            if (e.hand == EquipmentSlot.HAND) {
                if (mainhand != null) {
                    if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {
                        val offhand = player.inventory.itemInOffHand
                        if (!((mainhand == disenchantBook) and (offhand == disenchantBook))) {
                            if (mainhand == disenchantBook) {
                                if (mainhand.type.isAir || offhand.type.isAir) Util.send(player, Util.getPluginMsg(name, "cannot-be-empty"))
                                else {
                                    if (!(offhand.type == Material.ENCHANTED_BOOK || mainhand.type != Material.ENCHANTED_BOOK)) {
                                        if (offhand == disenchantBook) Util.send(player, Util.getPluginMsg(name, "offhand-cannot-be-book"))
                                        else {
                                            if (offhand.enchantments.isEmpty() || !offhand.hasItemMeta()) Util.send(player, Util.getPluginMsg(name, "no-enchantment"))
                                            else {
                                                loadInventory(player)
                                                player.openInventory(menu)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else Util.disabled(player)
    }

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        if (enabled) {
            val inventory = e.inventory
            if (inventory == menu) {
                val player = e.whoClicked as Player
                when (e.currentItem) {
                    confirm -> {
                        e.isCancelled = true
                        player.closeInventory()
                        disenchant(player, player.inventory.itemInOffHand)
                        player.inventory.remove(confirm!!)
                        player.updateInventory()
                    }
                    cancel -> {
                        e.isCancelled = true
                        player.closeInventory()
                        player.inventory.remove(cancel!!)
                        player.updateInventory()
                    }
                }
            }
        }
    }
}