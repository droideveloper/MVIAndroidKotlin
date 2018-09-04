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
package org.fs.mvi.todo.view

import io.reactivex.Observable
import org.fs.architecture.mvi.common.Event
import org.fs.architecture.mvi.core.AbstractFragment
import org.fs.mvi.todo.R
import org.fs.mvi.todo.model.TaskModel
import org.fs.mvi.todo.vm.TasksFragmentViewModel

class TasksFragment : AbstractFragment<TasksFragmentViewModel>(), TasksFragmentView {

  override val layoutRes: Int get() = R.layout.abc_search_view

  override fun attach() {

  }

  override fun detach() {

  }

  override fun render(e: TaskModel) {

  }

  override fun viewEvent(): Observable<Event> = super.viewEvents()
}