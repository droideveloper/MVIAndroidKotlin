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
package org.fs.architecture.mvi.util

import android.os.Build
import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.fs.architecture.mvi.common.*
import java.io.File

fun Observable<Event>.toIntent(block: (Event) -> Intent): Observable<Intent> = map(block)
fun <T> Observable<Intent>.toReducer(): Observable<Reducer<T>> = concatMap { source ->
   if (source is ReducerIntent<*>) {
     val intent = source as? ReducerIntent<T>
     if (intent != null) {
       return@concatMap Observable.just(intent)
     }
   } else if (source is ObservableIntent<*>) {
     val intent = source as? ObservableIntent<T>
     if (intent != null) {
       return@concatMap intent()
     }
   }
   return@concatMap Observable.empty<Reducer<T>>()
}

operator fun CompositeDisposable.plusAssign(d: Disposable) { add(d) }

// String
val String.Companion.EMPTY get() = ""
val String.Companion.WHITE_SPACE get() = " "
val String.Companion.NEW_LINE get() = "\n"
val String.Companion.INDENT get() = "\t"

// Objects
fun <T: Any> T?.isNullOrEmpty(): Boolean {
  if (this != null) {
    if (this is String) {
      return TextUtils.isEmpty(this)
    }
    if (this is Collection<*>) {
      return this.isEmpty()
    }
    if (this is File) {
      return !this.exists()
    }
  }
  return true
}
fun <T: Any> T?.isNotNullOrEmpty(): Boolean = !isNullOrEmpty()

// api level check
fun isApiAvailable(requiredSdkVersion: Int): Boolean = Build.VERSION.SDK_INT >= requiredSdkVersion

// check not null thing
fun <T: Any> T?.checkNotNull(errorString: String = "$this is null") = when {
  isNullOrEmpty() -> throw RuntimeException(errorString)
  else -> Unit
}

fun Boolean.throwIfConditionFails(errorString: String = "$this failed since it won't meet true") = when {
  !this -> throw RuntimeException(errorString)
  else -> Unit
}

// layout inflater better access for usage and others
fun ViewGroup.layoutInflaterFactory(): LayoutInflater = LayoutInflater.from(context)
fun ViewGroup.inflate(@LayoutRes layoutId: Int, attached: Boolean = false): View = layoutInflaterFactory().inflate(layoutId, this, attached)
