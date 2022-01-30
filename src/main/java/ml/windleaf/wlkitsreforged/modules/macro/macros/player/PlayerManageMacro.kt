package ml.windleaf.wlkitsreforged.modules.macro.macros.player

import ml.windleaf.wlkitsreforged.internal.PlayerInType
import ml.windleaf.wlkitsreforged.core.enums.PlayerType
import ml.windleaf.wlkitsreforged.modules.enums.PlayerAction
import ml.windleaf.wlkitsreforged.modules.enums.PlayerAction.*
import ml.windleaf.wlkitsreforged.modules.macro.Exceptions
import ml.windleaf.wlkitsreforged.modules.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.macro.MacroException
import ml.windleaf.wlkitsreforged.modules.macro.MacroInfo
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("UNCHECKED_CAST")
@MacroInfo(path = "player.manage", description = "Manages a player", args = ["player", "action", "args"])
class PlayerManageMacro : MacroEntire<Unit> {
    private var player: PlayerInType? = null
    private lateinit var action: PlayerAction
    private lateinit var args: Map<String, Any>

    override fun execute() {
        player.let {
            it!!
            val p = (it.getOnlinePlayer() ?: it.getOfflinePlayer())!!
            Util.catch(IllegalArgumentException::class.java,
                {
                    when (action) {
                        NULL -> run()
                        KICK -> if (Util.rightArgs(args, "reason")) run(p, args["reason"]!!)
                        KILL -> run(p)
                        BAN -> if (Util.rightArgs(args, "reason")) run(p, args["reason"]!!)
                        PARDON -> run(p)
                        CLEAR -> run(p)
                        GIVE -> if (Util.rightArgs(args, "item", "amount", "displayName", "lore")) {
                            val item = ItemStack(Material.valueOf((args["item"]!! as String).uppercase()))
                            val amount = (args["amount"]!! as String).toInt()
                            val displayName = args["displayName"]!! as String
                            val lore = args["lore"]!! as List<String>

                            item.amount = amount
                            val meta = item.itemMeta!!
                            meta.setDisplayName(displayName)
                            meta.lore = lore
                            item.itemMeta = meta
                            run(p, item)
                        }
                        TP_LOCATION -> if (Util.rightArgs(args, "world", "x", "y", "z", "yaw", "pitch")) {
                            val location = Location(
                                Util.getWorldByName(args["world"]!! as String),
                                (args["x"]!! as String).toDouble(),
                                (args["y"]!! as String).toDouble(),
                                (args["z"]!! as String).toDouble(),
                                (args["yaw"]!! as String).toFloat(),
                                (args["pitch"]!! as String).toFloat()
                            )
                            run(p, location)
                        }
                        TP_PLAYER -> if (Util.rightArgs(args, "target")) {
                            val target = Util.getPlayerBy(args["target"]!! as String)
                            if (target == null || target.playerType == PlayerType.OFFLINE) {
                                throw Exceptions.PlayerNotFoundException()
                            }
                            run(p, target.player as Player)
                        }
                        MSG -> if (Util.rightArgs(args, "message")) run(p, args["message"]!! as String)
                    }
                },
                { e ->
                    throw MacroException("error happened when running action `${action.string}`: ${e.message}", e)
                }
            )
        }
    }

    override fun parse(map: Map<String, Any>) {
        val value = map["player"] as String
        action = Util.getPlayerActionByString(map["action"] as String)
        player = Util.getPlayerBy(value) ?: throw Exceptions.PlayerNotFoundException("player `$value` not found")
        args = map["args"] as Map<String, Any>
    }

    private fun run(vararg args: Any) = action.run(args as Array<Any>)
}