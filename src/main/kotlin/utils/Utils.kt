package utils

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import kotlin.reflect.full.declaredMemberProperties

val gsonUtils = Gson()
inline fun <reified T : Any> String.fromJson(parser: Gson = gsonUtils): T {
    if (this.isEmpty()) throw JsonParseException("Object has null fields")
    val any = parser.fromJson<T>(this, object : TypeToken<T>() {}.type)
    any?.let {
        it.javaClass.declaredFields.forEach { field ->
            readProperty<Any?>(it, field.name) ?: throw JsonParseException("Object has null fields")
        }
    }
    return any
}

fun Any.toJsonString(parser: Gson): String = parser.toJson(this)

fun <R : Any?> readProperty(instance: Any, propertyName: String): R? {
    val clazz = instance.javaClass.kotlin
    @Suppress("UNCHECKED_CAST") return clazz.declaredMemberProperties.first { it.name == propertyName }.get(instance) as R?
}

fun tryToFalse(function: (() -> Boolean)): Boolean {
    return try {
        function.invoke()
    } catch (e: Exception) {
        false
    }
}