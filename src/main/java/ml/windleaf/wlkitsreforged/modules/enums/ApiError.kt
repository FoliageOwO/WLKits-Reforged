package ml.windleaf.wlkitsreforged.modules.enums

/**
 * Enum for the different API errors
 */
enum class ApiError {
    /**
     * The token of client is wrong
     */
    WRONG_TOKEN,

    /**
     * There is an error happened
     */
    EXCEPTION_HAPPENED,

    /**
     * The params client send is wrong
     */
    WRONG_PARAMETERS,

    /**
     * The params client send missing a param
     */
    MISSING_PARAMETER,

    /**
     * The feature is not available or not enabled
     */
    FEATURE_NOT_AVAILABLE;
}