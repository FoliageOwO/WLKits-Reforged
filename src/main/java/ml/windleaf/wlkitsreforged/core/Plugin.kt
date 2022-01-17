package ml.windleaf.wlkitsreforged.core

interface Plugin {
    val name: String
    val enabled: Boolean
    val type: LoadType

    fun load()
    fun unload()
}