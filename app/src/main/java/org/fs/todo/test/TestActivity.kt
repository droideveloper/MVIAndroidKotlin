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
package org.fs.todo.test

import android.os.Bundle
import org.fs.architecture.mvi.core.AbstractActivity
import org.fs.todo.R
import org.fs.todo.util.log

class TestActivity : AbstractActivity<TestActivityViewModel>(), TestActivityView {

  override val layoutRes: Int get() = R.layout.view_layout

  override fun setUp(state: Bundle?) {
    log("test activity called setUp")

  }

  override fun attach() {
    log("test activity called attach")

  }

  override fun detach() {
    log("test activity called detach")

  }
} 