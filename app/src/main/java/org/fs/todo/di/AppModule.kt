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

package org.fs.todo.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.fs.architecture.mvi.common.ForActivity
import org.fs.todo.App
import org.fs.todo.di.module.ActivityModule
import org.fs.todo.repo.EntryRepository
import org.fs.todo.repo.EntryRepositoryImp
import org.fs.todo.view.MainActivity
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Singleton @Binds abstract fun bindApplication(app: App): Application
  @Singleton @Binds abstract fun bindContext(app: Application): Context
  @Singleton @Binds abstract fun bindEntryRepository(repo: EntryRepositoryImp): EntryRepository

  @ForActivity @ContributesAndroidInjector(modules = [ActivityModule::class])
  abstract fun contributeMainActivity(): MainActivity
}