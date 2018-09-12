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

import android.arch.lifecycle.*
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.PublishRelay
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.fs.architecture.mvi.common.Event
import org.fs.architecture.mvi.common.ViewModel
import javax.inject.Inject

abstract class AbstractFragment<VM>: Fragment(), LifecycleOwner, LifecycleObserver where VM: ViewModel {

  protected val disposeBag by lazy { CompositeDisposable() }
  protected val viewEvents by lazy { PublishRelay.create<Event>() }
  protected abstract val layoutRes: Int

  private val lifecycle by lazy { LifecycleRegistry(this) }

  @Inject lateinit var viewModel: VM

  override fun onCreateView(factory: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = factory.inflate(layoutRes, container, false)

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onActivityCreated(savedInstanceState)
    setUp(savedInstanceState ?: arguments)
    lifecycle.addObserver(this)
    lifecycle.addObserver(viewModel)
  }

  override fun getLifecycle(): Lifecycle = lifecycle

  override fun onDestroy() {
    lifecycle.removeObserver(this)
    lifecycle.removeObserver(viewModel)
    super.onDestroy()
  }

  open fun finish() = Unit
  open fun dismiss() = Unit
  open fun isAvailable(): Boolean = isAdded && activity != null

  open fun stringResource(stringRes: Int): String = getString(stringRes)
  open fun context(): Context? = context
  open fun supportFragmentManager(): FragmentManager = childFragmentManager

  @OnLifecycleEvent(Lifecycle.Event.ON_START) abstract fun attach()
  @OnLifecycleEvent(Lifecycle.Event.ON_STOP) abstract fun detach()
  abstract fun setUp(state: Bundle?)

  open fun viewEvents(): Observable<Event> = viewEvents.hide()
}