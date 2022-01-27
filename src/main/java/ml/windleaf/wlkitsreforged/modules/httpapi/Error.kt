package ml.windleaf.wlkitsreforged.modules.httpapi

enum class Error {
    WRONG_TOKEN, EXCEPTION_HAPPENED,
    WRONG_PARAMETERS, MISSING_PARAMETER,
    NO_SUCH_PLAYER, PLAYER_NOT_ONLINE,
    UNKNOWN_ACTION,
    COMMAND_EXCEPTION;
}