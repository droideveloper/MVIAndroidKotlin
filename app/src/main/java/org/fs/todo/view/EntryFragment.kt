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
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.view_entry_fragment.*
import org.fs.architecture.mvi.common.*
import org.fs.architecture.mvi.core.AbstractFragment
import org.fs.architecture.mvi.util.ObservableList
import org.fs.architecture.mvi.util.plusAssign
import org.fs.rx.extensions.v4.util.refreshes
import org.fs.todo.R
import org.fs.todo.common.RecyclerViewSwipeObservable
import org.fs.todo.event.DeleteEntryEvent
import org.fs.todo.event.RefreshEvent
import org.fs.todo.model.entity.Entry
import org.fs.todo.model.EntryModel
import org.fs.todo.model.entity.Display
import org.fs.todo.util.C
import org.fs.todo.util.C.Companion.CREATE
import org.fs.todo.util.C.Companion.DELETE
import org.fs.todo.util.C.Companion.REFRESH
import org.fs.todo.util.C.Companion.UPDATE
import org.fs.todo.util.bind
import org.fs.todo.util.log
import org.fs.todo.view.adapter.EntryAdapter
import org.fs.todo.vm.EntryFragmentViewModel
import javax.inject.Inject

class EntryFragment: AbstractFragment<EntryModel, EntryFragmentViewModel>(), EntryFragmentView {

  companion object {
    private const val BUNDLE_ARGS_DATA_SET = "bundle.args.data.set"

    @JvmStatic fun newInstance(display: Display): EntryFragment = EntryFragment().apply {
      this.display = display
    }
  }

  override val layoutRes: Int get() = R.layout.view_entry_fragment

  private val colorPrimaryDark by lazy { ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, context?.theme) }
  private val colorPrimary by lazy { ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme) }
  private val colorAccent by lazy { ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme) }
  private val drawable by lazy { ResourcesCompat.getDrawable(resources, R.drawable.ic_list_seperator, context?.theme) }

  @Inject lateinit var dataSet: ObservableList<Entry>
  @Inject lateinit var entryAdapter: EntryAdapter

  private var display = Display.ALL

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
        val restoredDataSet: ArrayList<Entry>? = getParcelableArrayList(BUNDLE_ARGS_DATA_SET)
        if (restoredDataSet?.isNotEmpty() == true) {
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
      setItemViewCacheSize(10)
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      adapter = entryAdapter
      drawable?.let { d ->
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(d)
        addItemDecoration(divider)
      }
    }
  }

  override fun attach() {
    super.attach()

    disposeBag += viewSwipeRefreshLayout.refreshes()
      .filter { it }
      .doOnNext { dataSet.clear() }
      .map { RefreshEvent(display) }
      .subscribe(this::accept)

    disposeBag += viewModel.state()
      .map { if (it is Operation) return@map it.type == REFRESH else return@map false }
      .subscribe(viewSwipeRefreshLayout::bind)

    disposeBag += RecyclerViewSwipeObservable(viewRecycler)
      .map { index -> dataSet[index] }
      .map { entry -> DeleteEntryEvent(entry) }
      .subscribe(this::accept)

    disposeBag += BusManager.add(Consumer { evt -> accept(evt).also {
        log(evt.toString())
      }
    })

    disposeBag += viewModel.storage()
      .subscribe(::render)

    checkIfInitialLoadNeeded()
  }

  override fun render(model: EntryModel) {
    when (model.state) {
      is Operation -> when (model.state.type) {
        CREATE -> {
          if (display == Display.ALL || display == Display.ACTIVE) {
            dataSet.addAll(model.data)
          }
        }
        UPDATE -> {
          val entry = model.data.firstOrNull() ?: Entry.EMPTY
          if (display == Display.ALL) {
            if (entry != Entry.EMPTY) {
              val position = dataSet.indexOfFirst { e -> e.entryId == entry.entryId }
              if (position != -1) {
                dataSet[position] = entry
              }
            }
          } else if (display == Display.ACTIVE || display == Display.COMPLETED) {
            if (entry != Entry.EMPTY) {
              val position = dataSet.indexOfFirst { e -> e.entryId == entry.entryId }
              if (position != -1) {
                dataSet.removeAt(position)
              }
            }
          }
        }
        DELETE -> {
          val entry = model.data.firstOrNull() ?: Entry.EMPTY
          if (entry != Entry.EMPTY) {
            val position = dataSet.indexOfFirst { e -> e.entryId == entry.entryId }
            if (position != -1) {
              dataSet.removeAt(position)
            }
          }
        }
      }
      is Idle -> {
        val data = model.data
        if (data.isNotEmpty()) {
          dataSet.addAll(data)
        }
      }
      is Failure -> log(model.state.error)
    }
  }

  private fun checkIfInitialLoadNeeded() {
    if (dataSet.isEmpty()) {
      accept(RefreshEvent(display))
    }
  }
}