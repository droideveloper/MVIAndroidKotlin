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
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxrelay2.PublishRelay
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.fs.architecture.mvi.common.Event
import org.fs.architecture.mvi.common.ViewModel
import javax.inject.Inject

abstract class AbstractActivity<VM>: AppCompatActivity(), LifecycleOwner, LifecycleObserver, HasSupportFragmentInjector where VM: ViewModel {

  protected val disposeBag by lazy { CompositeDisposable() }
  protected val viewEvents by lazy { PublishRelay.create<Event>() }

  private val lifecycle by lazy { LifecycleRegistry(this) }


  abstract val layoutRes: Int

  @Inject lateinit var viewModel: VM
  @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(layoutRes)
    setUp(savedInstanceState ?: intent.extras)
    lifecycle.addObserver(this)
  }

  override fun onDestroy() {
    lifecycle.removeObserver(this)
    super.onDestroy()
  }

  override fun getLifecycle(): Lifecycle = lifecycle

  open fun dismiss() = Unit
  open fun isAvailable(): Boolean = !isFinishing

  open fun stringResource(stringRes: Int): String = getString(stringRes)
  open fun context(): Context? = this
  open fun supportFragmentManager(): FragmentManager = supportFragmentManager

  open fun viewEvents(): Observable<Event> = viewEvents.hide()

  abstract fun setUp(state: Bundle?)

  @OnLifecycleEvent(Lifecycle.Event.ON_START) open fun attach() {
    viewModel.attach()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP) open fun detach() {
    viewModel.detach()
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector
}