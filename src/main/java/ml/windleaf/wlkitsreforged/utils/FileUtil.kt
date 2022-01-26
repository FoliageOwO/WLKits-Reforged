package ml.windleaf.wlkitsreforged.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSONReader
import com.alibaba.fastjson.JSONWriter
import java.io.*

class FileUtil {
    companion object {
        fun loadHashMapJSON(path: String): HashMap<*, *> {
            try {
                return if (File(path).exists()) {
                    val os = JSONReader(InputStreamReader(FileInputStream(path), "UTF-8"))
                    os.readObject(HashMap::class.java)
                } else {
                    makeFile(path)
                    saveHashMapJSON(HashMap<Any, Any>(), path)
                    loadHashMapJSON(path)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return HashMap<Any, Any>()
        }

        fun saveHashMapJSON(map: HashMap<*, *>, path: String) {
            try {
                if (File(path).exists()) {
                    val os = OutputStreamWriter(FileOutputStream(path), "UTF-8")
                    os.write(JSON.toJSONString(map))
                    os.flush()
                    os.close()
                } else {
                    val file = File(path)
                    file.parentFile.mkdirs()
                    file.createNewFile()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun loadHashMap(path: String): HashMap<*, *> {
            try {
                return if (File(path).exists()) {
                    val os = ObjectInputStream(FileInputStream(path))
                    os.readObject() as HashMap<*, *>
                } else {
                    makeFile(path)
                    saveHashMap(HashMap<Any, Any>(), path)
                    loadHashMap(path)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return HashMap<Any, Any>()
        }

        fun makeFile(path: String) {
            val file = File(path)
            if (!file.exists()) {
                try {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun saveHashMap(map: HashMap<*, *>, path: String) {
            try {
                if (File(path).exists()) {
                    val os = ObjectOutputStream(FileOutputStream(path))
                    os.writeObject(map)
                    os.flush()
                    os.close()
                } else {
                    val file = File(path)
                    file.parentFile.mkdirs()
                    file.createNewFile()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}