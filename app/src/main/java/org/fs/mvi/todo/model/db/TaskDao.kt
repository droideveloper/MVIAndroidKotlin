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
package org.fs.mvi.todo.model.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.fs.mvi.todo.model.entity.Task

@Dao
interface TaskDao {
  // all
  @Query("SELECT * FROM tasks WHERE updatedAt < :updateAt ORDER BY updatedAt DESC LIMIT :take") fun all(updateAt: Long, take: Int): Flowable<List<Task>>
  // by id
  @Query("SELECT * FROM tasks WHERE taskId = :taskId") fun taskById(taskId: String): Maybe<Task>
  // insert
  @Insert(onConflict = OnConflictStrategy.REPLACE) fun create(task: Task)
  // update
  @Update(onConflict = OnConflictStrategy .REPLACE) fun update(task: Task)
  // delete
  @Delete fun delete(task: Task)
}