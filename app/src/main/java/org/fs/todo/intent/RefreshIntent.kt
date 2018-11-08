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
import org.fs.todo.model.entity.Display
import org.fs.todo.model.entity.Entry
import org.fs.todo.model.entity.EntryState
import org.fs.todo.repo.EntryRepository
import org.fs.todo.util.C
import java.util.concurrent.TimeUnit

class RefreshIntent(private val display: Display, private val entryRepository: EntryRepository): ObservableIntent<EntryModel>() {

  override fun invoke(): Observable<Reducer<EntryModel>> {
    val dataSource = when(display) {
      Display.ALL -> entryRepository.all()
      Display.ACTIVE -> entryRepository.loadByState(EntryState.ACTIVE)
      Display.COMPLETED -> entryRepository.loadByState(EntryState.CLOSED)
    }
    return dataSource.delay(500L, TimeUnit.MILLISECONDS)
      .subscribeOn(Schedulers.io())
      .map(this::bySuccess)
      .onErrorResumeNext(this::byFailure)
      .startWith(byInitial())
  }

  private fun byInitial(): Reducer<EntryModel> = { model -> model.copy(state = Operation(C.REFRESH), data = emptyList()) }

  private fun bySuccess(data: List<Entry>): Reducer<EntryModel> = { model -> model.copy(state = Idle, data = data) }

  private fun byFailure(error: Throwable): Observable<Reducer<EntryModel>> = Observable.just(
      { model -> model.copy(state = Failure(error)) },
      { model -> model.copy(state = Idle) })
}