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
package org.fs.todo.model.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.fs.todo.util.C
import java.util.*

@Entity(tableName = C.ENTRY_TABLE_NAME)
@Parcelize data class Entry @Ignore constructor(
    @field: PrimaryKey var entryId: String = UUID.randomUUID().toString(),
    var description: String,
    var state: EntryState = EntryState.ACTIVE,
    var createdAt: Date,
    var updatedAt: Date): Parcelable {

  constructor(): this(description = "", createdAt = Date(), updatedAt = Date())

  companion object {
    val EMPTY = Entry()
  }
}