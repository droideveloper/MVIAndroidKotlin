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

package org.fs.todo.di.module

import dagger.Binds
import dagger.Module
import org.fs.architecture.mvi.common.ForFragment
import org.fs.todo.view.EntryFragment
import org.fs.todo.view.EntryFragmentView

@Module
abstract class FragmentModule {

  @ForFragment @Binds abstract fun bindEntryFragmentView(fragment: EntryFragment): EntryFragmentView
}