
package com.example.mytask.util
import androidx.room.TypeConverter
import com.example.mytask.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name
    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)
}
