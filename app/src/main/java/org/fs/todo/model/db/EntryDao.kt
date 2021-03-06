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

import androidx.room.*
import io.reactivex.Single
import org.fs.todo.model.entity.Entry

@Dao interface EntryDao {

  @Query("SELECT * FROM entries") fun load(): Single<List<Entry>>

  @Query("SELECT * FROM entries WHERE state == :state") fun loadByState(state: Int): Single<List<Entry>>

  @Insert(onConflict = OnConflictStrategy.REPLACE) fun create(entry: Entry)
  @Update fun update(entry: Entry)
  @Delete fun delete(entry: Entry)
}