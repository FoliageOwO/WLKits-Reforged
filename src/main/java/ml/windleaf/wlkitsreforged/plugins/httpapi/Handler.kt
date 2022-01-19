package ml.windleaf.wlkitsreforged.plugins.httpapi

import com.google.gson.Gson
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import ml.windleaf.wlkitsreforged.plugins.httpapi.Error.*
import java.io.BufferedReader
import java.io.InputStreamReader


open class Handler : HttpHandler {
    lateinit var exchange: HttpExchange

    override fun handle(exchange: HttpExchange) {
        this.exchange = exchange
    }

    fun getRequestParams(): HashMap<*, *>? {
        try {
            return if (exchange.requestMethod == "GET") {
                val map = HashMap<String, Any>()
                for (query in exchange.requestURI.query.split("&")) {
                    val s = query.split("=")
                    map[s[0]] = s[1]
                }
                map
            } else {
                val br = BufferedReader(InputStreamReader(exchange.requestBody, "UTF-8"))
                val content = StringBuilder()
                var line: String?
                while (br.readLine().also { line = it } != null) content.append(line?.trim())
                Gson().fromJson(content.toString(), HashMap::class.java)
            }
        } catch (e: Exception) {
            return null
        }
    }

    fun response(code: Int = 200, content: String = "", contentType: String = "text/plain") {
        exchange.responseHeaders.add("Content-Type", contentType)
        exchange.sendResponseHeaders(code, content.length.toLong())
        val out = exchange.responseBody
        out.write(content.toByteArray())
        out.flush()
        out.close()
    }

    fun rightParams(params: HashMap<*, *>?, vararg needParams: String): Boolean {
        return if (params == null) {
            response(400, toJson(hashMapOf("error" to WRONG_PARAMETERS)))
            false
        } else {
            for (p in needParams) {
                if (!params.containsKey(p)) {
                    response(400, toJson(hashMapOf("error" to MISSING_PARAMETER, "parameter" to p)))
                    return false
                }
            }
            true
        }
    }

    fun toJson(any: Any) = Gson().toJson(any, any::class.java)!!

    fun needToken(params: HashMap<*, *>?): Boolean {
        return if (!rightParams(params, "token")) false
        else {
            val token = params!!["token"] as String
            if (HttpApi.token != token) {
                response(400, toJson(hashMapOf("error" to WRONG_TOKEN)))
                false
            } else true
        }
    }
}