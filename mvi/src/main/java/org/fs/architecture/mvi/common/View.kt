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
package org.fs.architecture.mvi.common

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentManager
import io.reactivex.Observable

interface View {
  fun startActivity(intent: Intent?)
  fun startActivityForResult(intent: Intent?, requestCode: Int)

  fun finish()
  fun dismiss()
  fun getSupportFragmentManager(): FragmentManager

  fun getStringResource(stringRes: Int): String?
  fun isAvailable(): Boolean
  fun getContext(): Context?
  fun viewEvent(): Observable<Event>
}