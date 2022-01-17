package ml.windleaf.wlkitsreforged.core

interface Plugin {
    val name: String
    val enabled: Boolean
    fun load()
    fun unload()
}