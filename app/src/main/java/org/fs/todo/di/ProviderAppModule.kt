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

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.fs.todo.model.db.EntryDao
import org.fs.todo.model.db.LocalStorage
import org.fs.todo.util.C
import javax.inject.Singleton

@Module
class ProviderAppModule {

  @Singleton @Provides fun provideLocalStorage(context: Context): LocalStorage = Room.databaseBuilder(context, LocalStorage::class.java, C.DATABASE_NAME).build()
  @Singleton @Provides fun provideEntryDao(storage: LocalStorage): EntryDao = storage.entryDao()
}