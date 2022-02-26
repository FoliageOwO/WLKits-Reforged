package ml.windleaf.wlkitsreforged.modules.macro.macros.player

import ml.windleaf.wlkitsreforged.others.PlayerInType
import ml.windleaf.wlkitsreforged.core.enums.PlayerType
import ml.windleaf.wlkitsreforged.data.OfflinePlayerData
import ml.windleaf.wlkitsreforged.data.OnlinePlayerData
import ml.windleaf.wlkitsreforged.data.Data
import ml.windleaf.wlkitsreforged.modules.macro.Exceptions
import ml.windleaf.wlkitsreforged.modules.macro.MacroEntire
import ml.windleaf.wlkitsreforged.modules.macro.MacroInfo
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

@MacroInfo(path = "player.info", description = "Gets the details of the player", args = ["player"])
class PlayerInfoMacro : MacroEntire<Data> {
    private var player: PlayerInType? = null

    override fun execute(): Data {
        return player.let {
            it!!
            val p = it.getOnlinePlayer() ?: it.getOfflinePlayer()
            when (it.playerType) {
                PlayerType.ONLINE -> OnlinePlayerData(p as Player)
                PlayerType.OFFLINE -> OfflinePlayerData(p as OfflinePlayer)
            }
        }
    }

    override fun parse(map: Map<String, Any>) {
        val value = map["player"]
        player = Util.getPlayerBy(value as String) ?: throw Exceptions.PlayerNotFoundException("player `$value` not found")
    }
}