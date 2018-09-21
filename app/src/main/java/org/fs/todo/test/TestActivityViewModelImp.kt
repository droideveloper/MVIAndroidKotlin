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

import org.fs.architecture.mvi.common.ForActivity
import org.fs.architecture.mvi.core.AbstractViewModel
import org.fs.todo.util.log
import javax.inject.Inject

@ForActivity
class TestActivityViewModelImp @Inject constructor(view: TestActivityView): AbstractViewModel<TestActivityView>(view), TestActivityViewModel  {

  override fun attach() {
    log("test view model called attach")
  }

  override fun detach() {
    log("test view model called deattach")
  }
}