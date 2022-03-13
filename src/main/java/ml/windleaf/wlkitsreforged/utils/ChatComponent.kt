package ml.windleaf.wlkitsreforged.utils

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Content
import org.bukkit.entity.Player

class ChatComponent {
    class ChatComponentBuilder {
        private var textComponents: Array<TextComponent> = arrayOf()

        fun append(text: Text, action: HoverEvent.Action? = null, vararg content: Content) = this.append(text) {
            if (action != null) {
                it.hoverEvent = HoverEvent(action, *content)
            }
        }

        fun append(text: Text, action: ClickEvent.Action? = null, value: String) = this.append(text) {
            if (action != null) {
                it.clickEvent = ClickEvent(action, value)
            }
        }

        private fun append(text: Text, setAction: (message: TextComponent) -> Unit = {}): ChatComponentBuilder {
            val message = TextComponent(text.plain)
            setAction.invoke(message)
            message.color = text.color
            message.isBold = text.bold
            message.isItalic = text.italic
            message.isStrikethrough = text.strikeThrough
            message.isUnderlined = text.underlined
            textComponents = textComponents.plusElement(message)
            return this
        }

        fun send(player: Player) = player.spigot().sendMessage(*this.textComponents)
    }

    data class Text(
        val plain: String,
        val color: ChatColor = ChatColor.WHITE,
        val bold: Boolean = false,
        val italic: Boolean = false,
        val strikeThrough: Boolean = false,
        val underlined: Boolean = false)

    companion object {
        fun builder(): ChatComponentBuilder = ChatComponentBuilder()
    }
}