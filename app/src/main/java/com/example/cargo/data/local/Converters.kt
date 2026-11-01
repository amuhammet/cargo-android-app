package com.example.cargo.data.local

import androidx.room.TypeConverter
import com.example.cargo.data.model.UserType

class Converters {
    @TypeConverter
    fun fromUserType(value: UserType) = value.name

    @TypeConverter
    fun toUserType(value: String) = UserType.valueOf(value)
}
