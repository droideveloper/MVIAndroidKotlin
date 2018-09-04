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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksDaoImp @Inject constructor(private val taskDao: TaskDao): TasksDao {

  override fun all(updateAt: Long, take: Int): Flowable<List<Task>> = taskDao.all(updateAt, take)

  override fun taskById(taskId: String): Maybe<Task> = taskDao.taskById(taskId)


  override fun create(task: Task): Completable = Completable.fromAction { taskDao.create(task) }

  override fun update(task: Task): Completable = Completable.fromAction { taskDao.update(task) }

  override fun delete(task: Task): Completable = Completable.fromAction { taskDao.delete(task) }
}