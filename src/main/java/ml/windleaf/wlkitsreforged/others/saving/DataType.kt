package ml.windleaf.wlkitsreforged.others.saving

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