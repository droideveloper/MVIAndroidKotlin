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

package org.fs.todo.view

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.view_main_activity.*
import org.fs.architecture.mvi.common.BusManager
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.core.AbstractActivity
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.design.util.selects
import org.fs.todo.R
import org.fs.todo.common.TextViewEditorInfoObservable
import org.fs.todo.event.CreateEntryEvent
import org.fs.todo.event.TabSelectedEvent
import org.fs.todo.model.DisplayModel
import org.fs.todo.model.entity.Display
import org.fs.todo.vm.MainActivityViewModel

class MainActivity: AbstractActivity<DisplayModel, MainActivityViewModel>(), MainActivityView {

  override val layoutRes: Int get() = R.layout.view_main_activity

  private val dataSet by lazy { listOf(Display.ALL, Display.ACTIVE, Display.COMPLETED) }

  private var display: Display? = null

  override fun setUp(state: Bundle?) {
    setUpTabLayout()
  }

  override fun attach() {
    super.attach()

    disposeBag += TextViewEditorInfoObservable(viewEditTextEntry)
      .filter { it == EditorInfo.IME_ACTION_DONE }
      .map { viewEditTextEntry.text }
      .filter { text -> !TextUtils.isEmpty(text) }
      .map { text -> CreateEntryEvent(text.toString()) }
      .doOnNext { viewEditTextEntry.text = null }
      .subscribe(BusManager.Companion::send)

    disposeBag += viewTabLayout.selects()
      .map { dataSet[it.position] }
      .map { TabSelectedEvent(it) }
      .subscribe(this::accept)

    checkIfInitialLoadNeeded()
  }

  override fun render(model: DisplayModel) {
    if (model.state is Idle) {
      if (display != model.data) {
        display = model.data
        val fragment = EntryFragment.newInstance(model.data)
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewContentFrame, fragment)
            .commit()
      }
    }
  }

  private fun checkIfInitialLoadNeeded() {
    val fragment = supportFragmentManager.findFragmentById(R.id.viewContentFrame)
    if (fragment == null) {
      accept(TabSelectedEvent(Display.ALL))
    }
  }

  private fun setUpTabLayout() {
    dataSet.forEach { display ->
      viewTabLayout.newTab().apply {
        val textView = TextView(this@MainActivity)
        textView.gravity = Gravity.CENTER
        customView = textView
        textView.text = display.title
        viewTabLayout.addTab(this)
      }
    }
  }
}