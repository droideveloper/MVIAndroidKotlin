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

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.jakewharton.rxrelay3.PublishRelay
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.fs.architecture.mvi.common.Event
import org.fs.architecture.mvi.common.Model
import org.fs.architecture.mvi.common.ViewModel
import javax.inject.Inject

abstract class AbstractActivity<T, VM>: AppCompatActivity(), HasAndroidInjector where VM: ViewModel<T>, T: Model<*> {

  protected val disposeBag by lazy { CompositeDisposable() }
  private val viewEvents by lazy { PublishRelay.create<Event>() }

  abstract val layoutRes: Int

  @Inject lateinit var viewModel: VM
  @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Any>

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(layoutRes)
    setUp(savedInstanceState ?: intent.extras)
  }

  open fun dismiss() = Unit
  open fun isAvailable(): Boolean = !isFinishing

  open fun stringResource(stringRes: Int): String = getString(stringRes)
  open fun context(): Context? = this
  open fun supportFragmentManager(): FragmentManager = supportFragmentManager

  open fun viewEvents(): Observable<Event> = viewEvents.hide()

  abstract fun setUp(state: Bundle?)

  override fun onStart() = super.onStart().also {
    attach()
  }

  override fun onStop() = detach().also {
    super.onStop()
  }

  open fun attach() {
    viewModel.attach()
  }

  open fun detach() {
    disposeBag.clear()
    viewModel.detach()
  }

  override fun androidInjector(): AndroidInjector<Any> = supportFragmentInjector

  public fun accept(event: Event) = viewEvents.accept(event)
}