/*
 * MVI App Copyright (C) 2018 Fatih.
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
import org.fs.architecture.mvi.common.ForFragment
import org.fs.architecture.mvi.common.Idle
import org.fs.architecture.mvi.common.Intent
import org.fs.architecture.mvi.core.AbstractViewModel
import org.fs.todo.event.CreateEntryEvent
import org.fs.todo.event.DeleteEntryEvent
import org.fs.todo.event.RefreshEvent
import org.fs.todo.event.UpdateEntryEvent
import org.fs.todo.intent.*
import org.fs.todo.model.entity.Entry
import org.fs.todo.model.EntryModel
import org.fs.todo.repo.EntryRepository
import org.fs.todo.view.EntryFragmentView
import javax.inject.Inject

@ForFragment
class EntryFragmentViewModel @Inject constructor(view: EntryFragmentView,
    private val entryRepository: EntryRepository) : AbstractViewModel<EntryModel, EntryFragmentView>(view) {

  override fun initState(): EntryModel = EntryModel(state = Idle, data = emptyList())

  override fun toIntent(event: Event): Intent = when(event) {
    is RefreshEvent -> RefreshIntent(event.display, entryRepository)
    is CreateEntryEvent -> CreateEntryIntent(event.description, entryRepository)
    is UpdateEntryEvent -> UpdateEntryIntent(event.entry, entryRepository)
    is DeleteEntryEvent -> DeleteEntryIntent(event.entry, entryRepository)
    else -> NothingIntent<EntryModel>()
  }
}