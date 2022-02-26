package ml.windleaf.wlkitsreforged.others.saving

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.data.Data
import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import java.io.*

/**
 * JsonData class saves `.json` data
 */
class JsonData(private val name: String) : DataFile<HashMap<String, Any?>> {
    private lateinit var json: HashMap<String, Any?>

    init { loadDataFromFile() }

    companion object {
        inline fun <reified T> parse(str: String): T {
            val cls = T::class.java
            var result: T = JSON.parseObject("{}", cls)
            Util.catch(JSONException::class.java,
                {
                    result = JSON.parseObject(str, cls)
                }, {
                    result = JSON.parseObject(if (cls.isArray) "[]" else "{}", cls)
                }
            )
            return result
        }

        fun toJson(obj: Any): String {
            WLKits.debug("Converting object ${obj::class.java}")
            val json = if (obj is Data) obj.toJsonString() else JSON.toJSONString(obj)
            WLKits.debug("Result json: $json")
            return json
        }
    }

    override fun toString(): String = toJson(this)

    override fun getName() = name
    override fun getType() = DataType.JSON
    override fun saveData() = FileUtil.saveData(toString(), super.getPath())
    override fun getData() = json
    override fun setData(target: HashMap<String, Any?>) {
        json = target
    }

    operator fun set(k: String, v: Any?) = json.set(k, v)
    operator fun get(k: String) = getAs<Any?>(k)
    fun <T> getAs(k: String): T? = json[k] as T?
    fun <T> getListAs(k: String): ArrayList<T>? {
        val arl = ArrayList<T>()
        val list = json[k] as List<T>? ?: return null
        list.forEach { arl.add(it) }
        return arl
    }

    fun contains(x: String) = json.containsKey(x)
    fun remove(x: String) = json.remove(x)

    override fun loadDataFromFile() {
        json = parse(readJsonFile())
        WLKits.debug("Loaded JSON data: $json")
    }

    private fun readJsonFile(): String {
        val path = super.getPath()
        return if (File(path).exists()) {
            var jsonStr: String
            Util.catch {
                val jsonFile = File(path)
                val fileReader = FileReader(jsonFile)
                val reader = InputStreamReader(FileInputStream(jsonFile), "UTF-8")
                var ch: Int
                val sb = StringBuffer()
                while (reader.read().also { ch = it } != -1) { sb.append(ch.toChar()) }
                fileReader.close()
                reader.close()
                jsonStr = sb.toString()
                jsonStr
            }!!
        } else {
            val emptyContent = "{}"
            FileUtil.makeFile(path)
            FileUtil.saveData(emptyContent, path)
            emptyContent
        }
    }
}