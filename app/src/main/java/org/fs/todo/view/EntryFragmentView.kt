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
package org.fs.todo.view

import io.reactivex.Observable
import org.fs.architecture.mvi.common.View
import org.fs.todo.model.EntryModel
import org.fs.todo.model.event.LoadMoreEvent
import org.fs.todo.model.event.NewEntryEvent
import org.fs.todo.model.event.RefreshEvent


interface EntryFragmentView: View {
  fun loadMore(): Observable<LoadMoreEvent>
  fun refresh(): Observable<RefreshEvent>
  fun newEntry(): Observable<NewEntryEvent>
}