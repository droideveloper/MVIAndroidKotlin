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
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
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
import org.fs.architecture.mvi.common.ViewModel
import javax.inject.Inject

abstract class AbstractDialogFragment<VM>: DialogFragment(), LifecycleOwner, LifecycleObserver, HasSupportFragmentInjector where VM: ViewModel {

  protected val disposeBag by lazy { CompositeDisposable() }
  protected val viewEvents by lazy { PublishRelay.create<Event>() }
  abstract val layoutRes: Int

  private val lifecycle by lazy { LifecycleRegistry(this) }

  @Inject lateinit var viewModel: VM
  @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>


  override fun onCreateView(factory: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = factory.inflate(layoutRes, container, false)

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onActivityCreated(savedInstanceState)
    setUp(savedInstanceState ?: arguments)
    lifecycle.addObserver(this)
  }

  override fun show(manager: FragmentManager?, tag: String?) {
    if (manager != null) {
      show(manager.beginTransaction(), tag)
    }
  }

  override fun show(transaction: FragmentTransaction?, tag: String?): Int {
    if (transaction != null) {
      return transaction.add(this, tag)
          .commit()
    }
    return -1
  }

  override fun onDestroy() {
    lifecycle.removeObserver(this)
    super.onDestroy()
  }

  override fun getLifecycle(): Lifecycle = lifecycle

  open fun finish() = Unit
  open fun isAvailable(): Boolean = isAdded && activity != null

  open fun stringResource(stringRes: Int): String = getString(stringRes)
  open fun context(): Context? = context
  open fun supportFragmentManager(): FragmentManager = childFragmentManager

  @OnLifecycleEvent(Lifecycle.Event.ON_START) open fun attach() {
    viewModel.attach()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP) open fun detach() {
    viewModel.detach()
  }

  abstract fun setUp(state: Bundle?)

  open fun viewEvents(): Observable<Event> = viewEvents.hide()

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector
}