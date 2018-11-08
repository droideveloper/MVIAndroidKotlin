/*
 * MVI App Android Kotlin Copyright (C) 2018 Fatih, Open Source.
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

package org.fs.todo.intent

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.fs.architecture.mvi.common.*
import org.fs.todo.model.EntryModel
import org.fs.todo.model.entity.Entry
import org.fs.todo.model.entity.EntryState
import org.fs.todo.repo.EntryRepository
import org.fs.todo.util.C
import java.util.*
import java.util.concurrent.TimeUnit

class UpdateEntryIntent(private val entry: Entry, private val entryRepository: EntryRepository): ObservableIntent<EntryModel>() {

  override fun invoke(): Observable<Reducer<EntryModel>> {
    val state = if (entry.state == EntryState.ACTIVE) EntryState.CLOSED else EntryState.ACTIVE
    val entry = entry.copy(state = state, updatedAt = Date())
    return entryRepository.update(entry)
        .andThen(Observable.just(entry))
        .delay(500L, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .concatMap(this::bySuccess)
        .onErrorResumeNext(this::byFailure)
        .startWith(byInitial())
  }

  private fun byInitial(): Reducer<EntryModel> = { model -> model.copy(state = Idle, data = emptyList()) }

  private fun bySuccess(entry: Entry): Observable<Reducer<EntryModel>> = Observable.just(
      { model -> model.copy(state = Operation(C.UPDATE), data = listOf(entry)) },
      { model -> model.copy(state = Idle, data = emptyList()) })

  private fun byFailure(error: Throwable): Observable<Reducer<EntryModel>> = Observable.just(
      { model -> model.copy(state = Failure(error)) },
      { model -> model.copy(state = Idle) })
}