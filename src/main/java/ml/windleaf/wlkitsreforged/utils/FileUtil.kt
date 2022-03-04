package ml.windleaf.wlkitsreforged.utils

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.core.saving.DataType
import java.io.*

/**
 * The file util object
 */
object FileUtil {
    val path = WLKits.instance.dataFolder.absolutePath + File.separator

    fun makeFile(path: String) = Util.catch {
        val file = File(path)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
    }

    fun saveData(content: String, path: String) {
        Util.catch {
            if (File(path).exists()) {
                val os = OutputStreamWriter(FileOutputStream(path), "UTF-8")
                os.write(content)
                os.flush()
                os.close()
            } else {
                val file = File(path)
                file.parentFile.mkdirs()
                file.createNewFile()
                saveData(content, path)
            }
        }
    }

    fun saveResource(name: String) {
        if (!File("$path$name").exists()) WLKits.instance.saveResource(name, false)
    }

    fun buildModuleDataPath(name: String, type: DataType) = "$path$name${type.suffix}"
}