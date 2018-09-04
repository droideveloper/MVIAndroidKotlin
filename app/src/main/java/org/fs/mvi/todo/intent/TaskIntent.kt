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
package org.fs.mvi.todo.intent

import io.reactivex.Observable
import org.fs.architecture.mvi.common.*
import org.fs.mvi.todo.model.TaskModel
import org.fs.mvi.todo.model.db.TasksDao
import org.fs.mvi.todo.model.entity.Task
import org.fs.mvi.todo.util.async

class TaskRefreshIntent(private val tasksDao: TasksDao): ObservableIntent<TaskModel>() {

  override fun invoke(): Observable<Reducer<TaskModel>> = Observable.create { emitter ->
    emitter.onNext { o -> o.copy(state = PROCESS(ProcessType.REFRESH)) }
    emitter.setDisposable(tasksDao.all(0L)
      .async()
      .subscribe(
      { tasks ->
        emitter.onNext { o -> o.copy(data = tasks, state = IDLE) }
      },
      { error ->
        emitter.onNext { o -> o.copy(state = ERROR(error)) }
        emitter.onNext { o -> o.copy(state = IDLE) }
      }))
  }
}

class TaskPaginationIntent(private val updateAt: Long, private val tasksDao: TasksDao): ObservableIntent<TaskModel>() {

  override fun invoke(): Observable<Reducer<TaskModel>> = Observable.create { emitter ->
    emitter.onNext { o -> o.copy(state = PROCESS(ProcessType.LOAD_MORE)) }
    emitter.setDisposable(tasksDao.all(updateAt)
      .async()
      .subscribe(
      { tasks ->
        emitter.onNext { o -> o.copy(data = tasks, state = IDLE) }
      },
      { error ->
        emitter.onNext { o -> o.copy(state = ERROR(error)) }
        emitter.onNext { o -> o.copy(state = IDLE) }
      }))
  }
}

class TaskCheckedIntent(private val task: Task): ObservableIntent<TaskModel>() {
  override fun invoke(): Observable<Reducer<TaskModel>> = Observable.create { emitter ->
    emitter.setDisposable(Observable.just(task)
      .async()
      .subscribe(
      { task ->
        emitter.onNext { o -> o.copy(data = listOf(task), state = PROCESS(ProcessType.UPDATE)) }
        emitter.onNext { o -> o.copy(data = emptyList(), state = IDLE) }
      },
      { error ->
        emitter.onNext { o -> o.copy(state = ERROR(error)) }
        emitter.onNext { o -> o.copy(state = IDLE) }
      }))
  }
}