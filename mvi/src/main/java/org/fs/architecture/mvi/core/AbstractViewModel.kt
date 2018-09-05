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
import io.reactivex.disposables.CompositeDisposable
import org.fs.architecture.mvi.common.Intent
import org.fs.architecture.mvi.common.View
import org.fs.architecture.mvi.common.ViewModel

abstract class AbstractViewModel<V>: ViewModel where V: View {

  protected val disposeBag by lazy { CompositeDisposable() }
  protected val intents by lazy { PublishRelay.create<Intent>() }

  override fun accept(intent: Intent) = intents.accept(intent)
}