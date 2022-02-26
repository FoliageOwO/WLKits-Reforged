package ml.windleaf.wlkitsreforged.others.saving

import ml.windleaf.wlkitsreforged.utils.FileUtil
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.configuration.file.YamlConfiguration
import kotlin.collections.ArrayList

/**
 * YamlData class saves `.yml` data
 */
class YamlData(private val name: String) : DataFile<YamlConfiguration> {
    private lateinit var yaml: YamlConfiguration

    init { loadDataFromFile() }

    override fun toString() = yaml.saveToString()

    override fun getName() = name
    override fun getType() = DataType.YAML
    override fun saveData() = yaml.save(super.getFile())
    override fun getData() = yaml
    override fun setData(target: YamlConfiguration) {
        yaml = target
    }

    operator fun set(k: String, v: Any?) = yaml.set(k, v)
    operator fun get(k: String) = getAs<Any?>(k)
    fun <T> getAs(k: String): T? = yaml.get(k) as T?
    fun <T> getListAs(k: String): ArrayList<T>? {
        val arl = ArrayList<T>()
        val list = yaml.getList(k) ?: return null
        list.forEach { arl.add(it as T) }
        return arl
    }

    fun contains(x: String) = yaml.get(x) != null
    fun remove(x: String) = yaml.set(x, null)

    override fun loadDataFromFile() {
        val d = YamlConfiguration()
        yaml = Util.catch {
            FileUtil.makeFile(super.getPath())
            d.load(super.getFile())
            d
        }!!
    }
}