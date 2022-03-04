package ml.windleaf.wlkitsreforged.modules.annotations

import ml.windleaf.wlkitsreforged.core.enums.PermissionType

/**
 * The Permission annotation is used to mark a permission
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission(
    /**
     * The string of permission
     */
    val permissionString: String,

    /**
     * The type of permission, defaults to [PermissionType.COMMAND]
     *
     * @see PermissionType
     */
    val type: PermissionType = PermissionType.COMMAND
)
