package ml.windleaf.wlkitsreforged.others.saving

import ml.windleaf.wlkitsreforged.utils.FileUtil
import org.jetbrains.annotations.NotNull

import java.io.File

/**
 * A class that represents a data file
 *
 * @author WindLeaf_qwq
 */
interface DataFile<T> {
    /**
     * Gets the name of file without suffix
     *
     * @return name
     */
    @NotNull fun getName(): String

    /**
     * Gets the path of data file
     *
     * @return path of file
     */
    @NotNull fun getPath() = FileUtil.buildModuleDataPath(getName(), getType())

    /**
     * Gets the file of data
     *
     * @return file
     */
    @NotNull fun getFile() = File(getPath())

    /**
     * Gets the type of data
     *
     * @return the type of data
     * @see DataType
     */
    @NotNull fun getType(): DataType

    /**
     * Gets the data
     *
     * @return data
     */
    @NotNull fun getData(): T

    /**
     * Saves the data
     */
    fun saveData()

    /**
     * Sets the data to target data
     *
     * @param target data
     */
    fun setData(@NotNull target: T)

    /**
     * Loads data from file
     */
    fun loadDataFromFile()
}