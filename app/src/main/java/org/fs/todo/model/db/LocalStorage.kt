/*
 * MVI App Copyright (C) 2018 Fatih.
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
package org.fs.todo.model.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.fs.architecture.mvi.common.db.Converters
import org.fs.todo.model.entity.Entry
import org.fs.todo.util.C
import org.fs.todo.util.EntryStateConverter

@Database(entities = [Entry::class], version = C.DATABASE_VERSION, exportSchema = false)
@TypeConverters(value = [Converters::class, EntryStateConverter::class])
abstract class LocalStorage: RoomDatabase() {

  abstract fun entryDao(): EntryDao
}