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
package org.fs.mvi.core

import android.support.v7.widget.RecyclerView
import org.fs.mvi.common.PropertyChangedListener
import org.fs.mvi.util.ObservableList

abstract class AbstractRecyclerViewAdapter<D, VH>(private val dataSet: ObservableList<D>): RecyclerView.Adapter<VH>(), PropertyChangedListener where VH: AbstractRecyclerViewHolder<D> {

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    super.onAttachedToRecyclerView(recyclerView)
    dataSet.register(this)
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    dataSet.unregister(this)
    super.onDetachedFromRecyclerView(recyclerView)
  }

  override fun notifyItemsRemoved(index: Int, size: Int) {
    if (size == 1) {
      notifyItemRemoved(index)
    } else {
      notifyItemRangeRemoved(index, size)
    }
  }

  override fun notifyItemsInserted(index: Int, size: Int) {
    if (size == 1) {
      notifyItemInserted(index)
    } else {
      notifyItemRangeInserted(index, size)
    }
  }

  override fun notifyItemsChanged(index: Int, size: Int) {
    if (size == 1) {
      notifyItemChanged(index)
    } else {
      notifyItemRangeChanged(index, size)
    }
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.bind(dataSet[position])
  }

  override fun onViewRecycled(holder: VH) {
    holder.unbind()
    super.onViewRecycled(holder)
  }

  override fun getItemId(position: Int): Long = position.toLong()
  override fun getItemViewType(position: Int): Int = 0
  override fun getItemCount(): Int = dataSet.size
}