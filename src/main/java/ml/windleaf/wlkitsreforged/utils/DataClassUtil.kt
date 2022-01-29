package ml.windleaf.wlkitsreforged.utils

import ml.windleaf.wlkitsreforged.core.WLKits
import ml.windleaf.wlkitsreforged.data.DataField
import java.lang.reflect.Field

/**
 * The internal class util object
 */
object DataClassUtil {
    /**
     * Gets the fields which annotated [DataField] of the internal class
     *
     * @param cls the internal class
     * @return the all internal fields
     * @see DataField
     */
    fun getDataFields(cls: Class<*>): ArrayList<Field> {
        val fields = cls.declaredFields
        val result = ArrayList<Field>()
        fields.forEach {
            if (it.isAnnotationPresent(DataField::class.java)) {
                it.isAccessible = true
                result.add(it)
            }
        }
        return result
    }

    /**
     * Gets the fields objects
     *
     * @param fields the fields
     * @param instance the instance of the class
     * @return the objects
     */
    fun getFieldsObjects(fields: Collection<Field>, instance: Any): ArrayList<*> {
        val result = ArrayList<Any>(fields.size)
        fields.forEach {
            // val instance = it.type.newInstance()
            WLKits.debug("The instance of field ${it.name} is $instance")
            result.add(getFieldObject(it, instance))
        }
        WLKits.debug("Result got fields: $result")
        return result
    }

    /**
     * A shortcut to get field object
     *
     * @param field the field
     * @param instance the instnace of class
     * @return the object
     */
    fun getFieldObject(field: Field, instance: Any) = field.get(instance)
}