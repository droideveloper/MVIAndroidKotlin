/*
 * MVI Kotlin Copyright (C) 2018 Fatih.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.architecture.mvi.common.db

import androidx.room.TypeConverter
import java.util.*

sealed class Converters {

  companion object {

    // Date conversion
    @JvmStatic @TypeConverter
    fun fromTimestamp(value: Long?): Date? = when(value) {
      null -> null
      else -> Date(value)
    }

    @JvmStatic @TypeConverter fun toTimestamp(value: Date?): Long? = when(value) {
      null -> null
      else -> value.time
    }

    // Boolean from Int
    @JvmStatic @TypeConverter fun fromInt(value: Int?): Boolean? = when(value) {
      null -> null
      else -> value == 1
    }

    @JvmStatic @TypeConverter fun toInt(value: Boolean?): Int? = when(value) {
      null -> null
      else -> if (value) 1 else 0
    }

    // Boolean from String
    @JvmStatic @TypeConverter fun fromString(value: String?): Boolean? = when(value) {
      null -> null
      else -> "true".equals(value, true)
    }

    @JvmStatic @TypeConverter fun toString(value: Boolean?): String? = when(value) {
      null -> null
      else -> if (value) "true" else "false"
    }
  }
}