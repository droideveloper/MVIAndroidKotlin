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

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.fs.mvi.todo.model.entity.Task

interface TasksDao {

  fun all(updateAt: Long, take: Int = 35): Flowable<List<Task>>

  fun taskById(taskId: String): Maybe<Task>

  fun create(task: Task): Completable

  fun update(task: Task): Completable

  fun delete(task: Task): Completable
}