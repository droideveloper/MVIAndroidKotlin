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
package org.fs.todo.vm

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.fs.architecture.mvi.common.*
import org.fs.architecture.mvi.core.AbstractViewModel
import org.fs.architecture.mvi.util.plusAssign
import org.fs.architecture.mvi.util.toReducer
import org.fs.todo.model.EntryModel
import org.fs.todo.view.EntryFragmentView
import javax.inject.Inject

@ForFragment
class EntryFragmentViewModelImp @Inject constructor(view: EntryFragmentView) : AbstractViewModel<EntryFragmentView>(view), EntryFragmentViewModel {

  companion object {
    private val INIT_STATE = EntryModel(data = emptyList(), state = IDLE)
  }

  private val store = intents.toReducer<EntryModel>()
    .observeOn(AndroidSchedulers.mainThread())
    .scan(INIT_STATE) { o, reducer -> reducer.invoke(o) }
    .replay(1)
    .apply { disposeBag += connect() }

  override fun state(): Observable<SyncState> = store.map(EntryModel::state)
  override fun model(): Observable<EntryModel> = store.hide()

  override fun attach() {
    if (view.isAvailable()) {
      // TODO implement here
    }
  }

  override fun detach() {
    // TODO implement here
  }
} 