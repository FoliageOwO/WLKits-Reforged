package ml.windleaf.wlkitsreforged.data;

import ml.windleaf.wlkitsreforged.utils.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * A class that represents a data file
 *
 * @author WindLeaf_qwq
 */
public interface DataFile<T> {
    @Nullable Object data = null;

    /**
     * Gets the name of file without suffix
     *
     * @return name
     */
    @NotNull String getName();

    /**
     * Gets the path of data file
     *
     * @return path of file
     */
    @NotNull default String getPath() {
        return FileUtil.Companion.buildModuleDataPath(getName(), getType());
    }

    /**
     * Gets the file of data
     *
     * @return file
     */
    @NotNull default File getFile() {
        return new File(getPath());
    }

    /**
     * Gets the type of data
     *
     * @return the type of data
     * @see DataType
     */
    @NotNull DataType getType();

    /**
     * Gets the data
     *
     * @return data
     */
    @NotNull T getData();

    /**
     * Saves the data
     */
    void saveData();

    /**
     * Sets the data to target data
     *
     * @param target data
     */
    void setData(T target);

    /**
     * Loads data from file
     */
    void loadDataFromFile();
}