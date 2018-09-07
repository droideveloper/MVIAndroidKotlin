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
package org.fs.todo.common

import android.view.KeyEvent
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import org.fs.rx.extensions.util.checkMainThread

class TextViewEditorInfoObservable(private val view: TextView): Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (observer.checkMainThread())
  }

  internal class Listener(private val view: TextView, private val observer: Observer<in Int>): MainThreadDisposable(), TextView.OnEditorActionListener {

    override fun onDispose() = view.setOnEditorActionListener(null)

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
      if (!isDisposed) {
        observer.onNext(actionId)
        return false
      }
      return true
    }
  }
}