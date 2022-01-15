package ml.windleaf.wlkitsreforged.core

interface Plugin {
    var name: String
    fun load()
    fun unload()
}