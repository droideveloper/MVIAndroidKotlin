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

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_entry_fragment.*
import kotlinx.android.synthetic.main.view_entry_item.*
import org.fs.architecture.mvi.core.AbstractFragment
import org.fs.architecture.mvi.util.ObservableList
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.v4.util.refreshes
import org.fs.rx.extensions.v7.util.loadMore
import org.fs.todo.R
import org.fs.todo.common.TextViewEditorInfoObservable
import org.fs.todo.model.Entry
import org.fs.todo.model.event.LoadMoreEvent
import org.fs.todo.model.event.NewEntryEvent
import org.fs.todo.model.event.RefreshEvent
import org.fs.todo.repo.EntryRepository
import org.fs.todo.vm.EntryFragmentViewModel
import javax.inject.Inject

class EntryFragment: AbstractFragment<EntryFragmentViewModel>(), EntryFragmentView {

  companion object {
    private const val BUNDLE_ARGS_DATA_SET = "bundle.args.data.set"
    const val BUNDLE_ARGS_TYPE = "bundle.args.type"
  }

  override val layoutRes: Int get() = R.layout.view_entry_fragment

  private val colorPrimaryDark by lazy { ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, context?.theme) }
  private val colorPrimary by lazy { ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme) }
  private val colorAccent by lazy { ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme) }

  @Inject lateinit var dataSet: ObservableList<Entry>
  @Inject lateinit var entryRepository: EntryRepository

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    if (dataSet.isNotEmpty()) {
      outState.putParcelableArrayList(BUNDLE_ARGS_DATA_SET, dataSet)
    }
  }

  override fun setUp(state: Bundle?) {
    // read state if exists
    state?.apply {
      if (containsKey(BUNDLE_ARGS_DATA_SET)) {
        val restoredDataSet: ArrayList<Entry> = getParcelableArrayList(BUNDLE_ARGS_DATA_SET)
        if (restoredDataSet.isNotEmpty()) {
          dataSet.addAll(restoredDataSet)
        }
      }
    }
    // set up layout
    viewSwipeRefreshLayout.apply {
      setColorSchemeColors(colorAccent, colorPrimary, colorPrimaryDark)
    }
    viewRecycler.apply {
      setHasFixedSize(true)
      isDrawingCacheEnabled = true
      drawingCacheQuality = View.DRAWING_CACHE_QUALITY_AUTO
      setItemViewCacheSize(10)
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      // TODO add adapter
    }
  }

  override fun loadMore(): Observable<LoadMoreEvent> = viewRecycler.loadMore()
    .filter { loadMore -> loadMore && dataSet.isNotEmpty() }
    .map { _ -> LoadMoreEvent(dataSet.last()) }

  override fun refresh(): Observable<RefreshEvent> = viewSwipeRefreshLayout.refreshes()
    .map { _ -> RefreshEvent() }

  override fun newEntry(): Observable<NewEntryEvent> = TextViewEditorInfoObservable(viewEditTextEntry)
    .filter { actionId -> actionId == EditorInfo.IME_ACTION_DONE }
    .map { _ -> NewEntryEvent(viewEditTextEntry.text.toString()) }

  override fun attach() {
    // TODO change this
    // disposeBag += loadMore().subscribe(viewModel::accept)
  }

  override fun detach() = disposeBag.clear()

}