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
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.PublishRelay
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.fs.architecture.mvi.common.Event
import org.fs.architecture.mvi.common.Model
import org.fs.architecture.mvi.common.ViewModel
import org.fs.architecture.mvi.util.plusAssign
import javax.inject.Inject

abstract class AbstractFragment<T, VM>: Fragment(), HasSupportFragmentInjector where VM: ViewModel<T>, T: Model<*> {

  protected val disposeBag by lazy { CompositeDisposable() }
  private val viewEvents by lazy { PublishRelay.create<Event>() }
  abstract val layoutRes: Int

  @Inject lateinit var viewModel: VM
  @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

  override fun onCreateView(factory: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = factory.inflate(layoutRes, container, false)

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onActivityCreated(savedInstanceState)
    setUp(savedInstanceState ?: arguments)
  }

  override fun onStart() {
    super.onStart()
    attach()
  }

  override fun onStop() {
    detach()
    super.onStop()
  }

  open fun finish() = Unit
  open fun dismiss() = Unit
  open fun isAvailable(): Boolean = isAdded && activity != null

  open fun stringResource(stringRes: Int): String = getString(stringRes)
  open fun context(): Context? = context
  open fun supportFragmentManager(): FragmentManager = childFragmentManager


  open fun attach() {
    viewModel.attach()
  }

  open fun detach() {
    disposeBag.clear()
    viewModel.detach()
  }

  abstract fun setUp(state: Bundle?)

  open fun viewEvents(): Observable<Event> = viewEvents.hide()

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

  public fun accept(event: Event) = viewEvents.accept(event)
}