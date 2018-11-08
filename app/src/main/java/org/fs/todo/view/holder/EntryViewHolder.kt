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

package org.fs.todo.view.holder

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_entry_item.view.*
import org.fs.architecture.mvi.common.BusManager
import org.fs.architecture.mvi.core.AbstractRecyclerViewHolder
import org.fs.architecture.mvi.util.inflate
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.util.checkChanges
import org.fs.todo.R
import org.fs.todo.event.UpdateEntryEvent
import org.fs.todo.model.entity.Entry
import org.fs.todo.model.entity.EntryState

class EntryViewHolder(view: View): AbstractRecyclerViewHolder<Entry>(view) {

  private val disposeBag by lazy { CompositeDisposable() }

  constructor(parent: ViewGroup): this(parent.inflate(R.layout.view_entry_item))

  override fun bind(value: Entry) {
    val state = value.state == EntryState.CLOSED
    val text = SpannableString(value.description)
    if (state) {
      text.setSpan(StrikethroughSpan(), 0, value.description.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    itemView.viewTextEntry.text = text
    itemView.viewCheckBox.isChecked = state
    itemView.alpha = if (state) 0.5f else 1f

    disposeBag += bindEntryUpdateEvent(value).subscribe(BusManager.Companion::send)
  }

  override fun unbind() = disposeBag.clear()

  private fun bindEntryUpdateEvent(entry: Entry): Observable<UpdateEntryEvent> = itemView.viewCheckBox.checkChanges()
    .map { UpdateEntryEvent(entry) }

}