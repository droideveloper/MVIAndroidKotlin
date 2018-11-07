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
package org.fs.architecture.mvi.core

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.fs.architecture.mvi.common.*
import org.fs.architecture.mvi.util.plusAssign
import org.fs.architecture.mvi.util.toIntent
import org.fs.architecture.mvi.util.toReducer

abstract class AbstractViewModel<T, D, V>(protected val view: V): ViewModel<T> where V: View<D>, T: Model<D> {

  protected val disposeBag by lazy { CompositeDisposable() }

  private val intents by lazy { PublishRelay.create<Intent>() }

  private val storage by lazy {
    intents.hide()
        .toReducer<T>()
        .observeOn(AndroidSchedulers.mainThread())
        .scan(initState()) { o, reducer -> reducer.invoke(o) }
        .replay(1)
  }

  protected abstract fun initState(): T
  protected abstract fun toIntent(event: Event): Intent

  override fun attach() {
    disposeBag += storage.connect()

    disposeBag += view.viewEvents()
      .toIntent(this::toIntent)
      .subscribe(this::accept)
  }

  override fun detach() = disposeBag.clear()

  override fun storage(): Observable<T> = storage.hide()

  override fun state(): Observable<SyncState> = storage().map { item -> item.state }

  override fun accept(intent: Intent) = intents.accept(intent)
}