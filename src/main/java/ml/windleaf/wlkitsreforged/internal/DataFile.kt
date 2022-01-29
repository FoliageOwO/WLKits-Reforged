package ml.windleaf.wlkitsreforged.internal;

import ml.windleaf.wlkitsreforged.utils.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A class that represents a internal file
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
     * Gets the path of internal file
     *
     * @return path of file
     */
    @NotNull fun getPath() = FileUtil.buildModuleDataPath(getName(), getType())

    /**
     * Gets the file of internal
     *
     * @return file
     */
    @NotNull fun getFile() = File(getPath())

    /**
     * Gets the type of internal
     *
     * @return the type of internal
     * @see DataType
     */
    @NotNull fun getType(): DataType

    /**
     * Gets the internal
     *
     * @return internal
     */
    @NotNull fun getData(): T

    /**
     * Saves the internal
     */
    fun saveData()

    /**
     * Sets the internal to target internal
     *
     * @param target internal
     */
    fun setData(@NotNull target: T)

    /**
     * Loads internal from file
     */
    fun loadDataFromFile()
}