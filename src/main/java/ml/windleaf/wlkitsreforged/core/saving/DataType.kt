package ml.windleaf.wlkitsreforged.core.saving

/**
 * The data type enum
 */
enum class DataType(val suffix: String) {
    /**
     * `yaml` saving type
     */
    YAML(".yml"),

    /**
     * `json` saving type
     */
    JSON(".json");
}