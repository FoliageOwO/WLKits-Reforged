package ml.windleaf.wlkitsreforged.modules.categories.manage.httpapi.handlers.macro

import com.sun.net.httpserver.HttpExchange
import ml.windleaf.wlkitsreforged.core.saving.JsonData
import ml.windleaf.wlkitsreforged.modules.categories.manage.HttpApi
import ml.windleaf.wlkitsreforged.modules.categories.manage.Macro
import ml.windleaf.wlkitsreforged.modules.categories.manage.httpapi.ApiError
import ml.windleaf.wlkitsreforged.modules.categories.manage.httpapi.Handler

class MacroListHandler : Handler() {
    init {
        HttpApi.server.createContext("/api/macro/list", this)
    }

    override fun handle(exchange: HttpExchange) {
        this.exchange = exchange
        val params = getRequestParams()
        if (needToken(params)) {
            if (Macro.enabled) {
                response(content = JsonData.toJson(Macro.manager.getMacros().map {
                    "[${it.getMacroInfo()?.path}]" +
                            "<${it::class.java.simpleName}" +
                            "(${it.getMacroInfo()?.description})>" +
                            "{${it.getMacroInfo()?.args?.joinToString(", ")}}"
                }))
            } else response(403, JsonData.toJson(
                hashMapOf("error" to ApiError.FEATURE_NOT_AVAILABLE, "feature" to "macro")
            ))
        }
    }
}