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
package org.fs.architecture.mvi.util

import org.fs.architecture.mvi.common.PropertyChangedListener
import java.util.*

open class ObservableList<T>: ArrayList<T>() {

  private val listeners = ArrayList<PropertyChangedListener>()

  fun register(callback: PropertyChangedListener) {
    val alreadyAdded = listeners.indexOf(callback) != -1
    if (!alreadyAdded) {
      listeners.add(callback)
    }
  }

  fun unregister(callback: PropertyChangedListener) {
    val alreadyAdded = listeners.indexOf(callback) != -1
    if (alreadyAdded) {
      listeners.remove(callback)
    }
  }

  override fun add(element: T): Boolean {
    val success = super.add(element)
    if (success) {
      val index = indexOf(element)
      if (listeners.isNotEmpty()) {
        listeners.forEach{ listener ->
          listener.notifyItemsInserted(index, 1)
        }
      }
    }
    return success
  }

  override fun add(index: Int, element: T) {
    super.add(index, element)
    if (listeners.isNotEmpty()) {
      listeners.forEach { listener ->
        listener.notifyItemsInserted(index, 1)
      }
    }
  }

  override fun addAll(elements: Collection<T>): Boolean {
    val success = super.addAll(elements)
    if (success) {
      if (listeners.isNotEmpty()) {
        val index = size - elements.size
        val size = elements.size
        listeners.forEach { listener ->
          listener.notifyItemsInserted(index, size)
        }
      }
    }
    return success
  }

  override fun addAll(index: Int, elements: Collection<T>): Boolean {
    val success = super.addAll(index, elements)
    if (success) {
      val size = elements.size
      if (listeners.isNotEmpty()) {
        listeners.forEach { listener ->
          listener.notifyItemsInserted(index, size)
        }
      }
    }
    return success
  }

  override fun remove(element: T): Boolean {
    val index = indexOf(element)
    val success = super.remove(element)
    if (success) {
      if (listeners.isNotEmpty()) {
        listeners.forEach { listener ->
          listener.notifyItemsRemoved(index, 1)
        }
      }
    }
    return success
  }

  override fun removeAll(elements: Collection<T>): Boolean {
    val index = Collections.indexOfSubList(this, ArrayList(elements))
    val size = elements.size
    val success = super.removeAll(elements)
    if (listeners.isNotEmpty()) {
      listeners.forEach { listener ->
        listener.notifyItemsRemoved(index, size)
      }
    }
    return success
  }

  override fun removeAt(index: Int): T {
    val item = super.removeAt(index)
    if (listeners.isNotEmpty()) {
      listeners.forEach { listener ->
        listener.notifyItemsRemoved(index, 1)
      }
    }
    return item
  }

  override fun removeRange(fromIndex: Int, toIndex: Int) {
    super.removeRange(fromIndex, toIndex)
    if (listeners.isNotEmpty()) {
      listeners.forEach { listener ->
        listener.notifyItemsRemoved(fromIndex, toIndex - fromIndex)
      }
    }
  }

  override fun clear() {
    val size = size
    super.clear()
    if (listeners.isNotEmpty()) {
      listeners.forEach { listener ->
        listener.notifyItemsRemoved(0, size)
      }
    }
  }

  override fun set(index: Int, element: T): T {
    val item =  super.set(index, element)
    if (listeners.isNotEmpty()) {
      listeners.forEach { listener ->
        listener.notifyItemsChanged(index, 1)
      }
    }
    return item
  }
}