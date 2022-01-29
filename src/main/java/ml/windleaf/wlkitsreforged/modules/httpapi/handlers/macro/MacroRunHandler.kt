package ml.windleaf.wlkitsreforged.modules.httpapi.handlers.macro

import com.google.gson.internal.LinkedTreeMap
import com.sun.net.httpserver.HttpExchange
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.data.Data
import ml.windleaf.wlkitsreforged.internal.JsonData
import ml.windleaf.wlkitsreforged.modules.HttpApi
import ml.windleaf.wlkitsreforged.modules.Macro
import ml.windleaf.wlkitsreforged.modules.enums.ApiError.*
import ml.windleaf.wlkitsreforged.modules.httpapi.Handler
import ml.windleaf.wlkitsreforged.utils.Util

class MacroRunHandler : Handler() {
    init {
        HttpApi.server.createContext("/api/macro/run", this)
    }

    override fun handle(exchange: HttpExchange) {
        this.exchange = exchange
        val params = getRequestParams()
        if (needToken(params) && rightParams(params, "macro", "params")) {
            if (Macro.enabled) {
                Util.catch(Exception::class.java, {
                    val s = params?.get("macro")!! as String
                    val p = params["params"]!!

                    val macroParams = when (p) {
                        is LinkedTreeMap<*, *> -> (p as LinkedTreeMap<String, Any>).toMap()
                        is String -> JsonData.parse<Map<String, Any>>(p)
                        else -> {
                            response(400, JsonData.toJson(
                                hashMapOf("error" to FEATURE_NOT_AVAILABLE, "message" to "macro `$s` not found")
                            ))
                            null
                        }
                    }

                    if (macroParams != null) {
                        val macro = Macro.manager.getMacroFromString(s)
                        WLKits.debug("Running macro $macro with params $macroParams(${macroParams::class.java})")
                        if (macro != null && Util.rightArgs(macroParams, *macro.getMacroInfo()?.args!!)) {
                            macro.parse(macroParams)
                            var result = Macro.manager.executeMacro(macro)

                            result = when (result) {
                                is Data -> result.toJsonString()
                                is String -> Util.removeColor(result)
                                else -> result
                            }

                            response(content = JsonData.toJson(
                                hashMapOf("error" to "", "message" to "success", "result" to result)
                            ))
                        } else response(400, JsonData.toJson(
                            hashMapOf("error" to FEATURE_NOT_AVAILABLE, "message" to "macro `$s` not found")
                        ))
                    }
                }, {
                    response(500, JsonData.toJson(
                        hashMapOf("error" to EXCEPTION_HAPPENED,
                            "exception" to it::class.java.simpleName,
                            "message" to it.message,
                            "cause" to it.cause?.message
                        )
                    ))
                    it.printStackTrace()
                })
            } else response(403, JsonData.toJson(
                hashMapOf("error" to FEATURE_NOT_AVAILABLE, "feature" to "macro")
            ))
        }
    }
}