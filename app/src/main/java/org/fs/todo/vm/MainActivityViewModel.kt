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

package org.fs.todo.vm

import org.fs.architecture.mvi.common.Event
import org.fs.architecture.mvi.common.ForActivity
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.common.Intent
import org.fs.architecture.mvi.core.AbstractViewModel
import org.fs.todo.event.TabSelectedEvent
import org.fs.todo.intent.NothingIntent
import org.fs.todo.intent.TabSelectIntent
import org.fs.todo.model.DisplayModel
import org.fs.todo.model.entity.Display
import org.fs.todo.view.MainActivityView
import javax.inject.Inject

@ForActivity
class MainActivityViewModel @Inject constructor(view: MainActivityView): AbstractViewModel<DisplayModel, Display, MainActivityView>(view) {

  override fun initState(): DisplayModel = DisplayModel(state = Idle, data = Display.ALL)

  override fun toIntent(event: Event): Intent = when(event) {
    is TabSelectedEvent -> TabSelectIntent(event.display)
    else -> NothingIntent<DisplayModel>()
  }
}