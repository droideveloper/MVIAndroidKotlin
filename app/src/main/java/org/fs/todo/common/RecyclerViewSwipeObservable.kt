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

package org.fs.todo.common

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import org.fs.rx.extensions.util.checkMainThread

class RecyclerViewSwipeObservable(private val view: RecyclerView): Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (observer.checkMainThread()) {
      val listener = Listener(view, observer)
      observer.onSubscribe(listener)
    }
  }

  internal class Listener(view: RecyclerView, private val observer: Observer<in Int>): MainThreadDisposable() {

    private val callback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
      override fun onMove(v: RecyclerView, h1: RecyclerView.ViewHolder, h2: RecyclerView.ViewHolder): Boolean = false

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (!isDisposed) {
          observer.onNext(position)
        }
      }
    }

    private val helper = ItemTouchHelper(callback)

    init {
      helper.attachToRecyclerView(view)
    }

    override fun onDispose() = Unit
  }
}